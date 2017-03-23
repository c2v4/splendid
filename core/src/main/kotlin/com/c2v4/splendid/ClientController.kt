package com.c2v4.splendid

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.PlayerEvent
import com.c2v4.splendid.component.cardtable.CardTableController
import com.c2v4.splendid.component.cardtable.CardTableModel
import com.c2v4.splendid.component.cardtable.CardTableView
import com.c2v4.splendid.component.playerstable.PlayerTableModel
import com.c2v4.splendid.component.playerstable.PlayerTableView
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsController
import com.c2v4.splendid.component.reservedcard.ReservedCardsModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsView
import com.c2v4.splendid.component.resourcetable.ResourceTableController
import com.c2v4.splendid.component.resourcetable.ResourceTableModel
import com.c2v4.splendid.component.resourcetable.ResourceTableView
import com.c2v4.splendid.core.model.Noble.Companion.POINTS_FOR_NOBLE
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.util.merge
import com.c2v4.splendid.core.util.subtract
import com.c2v4.splendid.entity.BoardView
import com.c2v4.splendid.network.message.game.*
import com.c2v4.splendid.screen.TestScreen
import com.esotericsoftware.kryonet.Client

class ClientController(val name: String, val skin: Skin, val splendidGame: SplendidGame) {

    var model: CommonModel = CommonModel.empty()
    private var client: Client = object : Client() {
        override fun sendUDP(p0: Any?): Int {
            throw IllegalStateException()
        }

        override fun sendTCP(p0: Any?): Int {
            throw IllegalStateException()
        }
    }

    var gameStarted = false
    fun startGame(players: Set<String>) {
        if (gameStarted) throw IllegalStateException()
        else {
            createGame(players)
        }
    }

    private fun createGame(players: Set<String>) {
        gameStarted = true
        val cardTableModel = CardTableModel.empty()
        val resourceModel = ResourceTableModel.empty()
        val playerStateModel = PlayerStateModel.empty()
        val enemies = mutableListOf<PlayerStateModel>()
        players.filter { it != name }.forEach {
            enemies.add(PlayerStateModel.emptyEnemy(it))
        }
        val playerTableModel = PlayerTableModel(name, playerStateModel, enemies)

        val reservedCardsModel = ReservedCardsModel.empty()
        val reservedCardsView = ReservedCardsView(reservedCardsModel, skin)

        model = CommonModel.empty(cardTableModel,
                resourceModel,
                playerTableModel,
                reservedCardsModel)

        val reservedCardsController = ReservedCardsController(reservedCardsView,
                reservedCardsModel,
                playerStateModel,
                model)

        val cardTableView = CardTableView(skin, cardTableModel)
        val cardTableController = CardTableController(cardTableView,
                cardTableModel,
                reservedCardsModel,
                resourceModel,
                playerStateModel,
                model)

        val playerTableView = PlayerTableView(playerTableModel, skin)
        val resourceView = ResourceTableView(skin, resourceModel)
        val resourceController = ResourceTableController(resourceView,
                playerTableView.playerStateView,
                playerStateModel,
                resourceModel,
                model)


        val boardActor = BoardView(skin,
                cardTableView,
                resourceView,
                playerTableView,
                reservedCardsView,
                model)
        boardActor.onButtonClick { inputEvent, button ->
            if (model.getPlayerTurn() && model.getActionCorrect()) {
                model.setPlayerTurn(false)
                model.setActionCorrect(false)
                when (model.playerEvent) {
                    PlayerEvent.GET_COINS -> {
                        client.sendTCP(resourceController.getEvent())
                    }
                    PlayerEvent.RESERVE -> {
                        client.sendTCP(cardTableController.getReserveEvent(resourceController.getEvent().returned))
                    }
                    PlayerEvent.BUY -> {
                        client.sendTCP(cardTableController.getBuyEvent())
                    }
                    PlayerEvent.BUY_RESERVED_CARD -> {
                        client.sendTCP(reservedCardsController.getEvent())
                    }
                    PlayerEvent.NONE -> throw IllegalStateException()
                }
            } else {
                throw IllegalStateException()
            }
        }
        val screen = splendidGame.screen as TestScreen
        screen.addMainActor(boardActor)
    }

    fun setInitialCoins(received: InitialCoins) {
        model.resourceModel.setResourceAvailable(received.coins)
    }

    fun cardDeal(received: CardDeal) {
        model.cardTableModel.changeCard(received.tier, received.position, received.card)
    }

    fun nobleDeal(received: NobleDeal) {
        model.cardTableModel.changeNoble(received.position, received.noble)
    }

    fun setPlayerTurn(received: PlayerTurn) {
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.playerName)
        model.setPlayerTurn(received.playerName == name)
        modelForPlayer.setPlayerTurn(true)
        model.playerTableModel.getModelsForOtherPlayers(received.playerName).forEach {
            it.setPlayerTurn(false)
        }
    }


    fun setClient(client: Client) {
        this.client = client
    }

    fun coinsTaken(received: CoinsTaken) {
        model.resourceModel.setResourceAvailable(received.boardsCoinsAvailable)
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.player)
        received.balance.forEach {
            modelForPlayer.setWalletAmount(it.key,
                    modelForPlayer.wallet.getOrDefault(it.key, 0) + it.value)
        }
    }

    fun cardReserved(received: CardReserved) {
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.playerName)
        modelForPlayer.setCardsReservedAmount(modelForPlayer.cardsReserved + 1)
        val amountOfGold = model.resourceModel.resourcesAvailable.getOrDefault(Resource.GOLD, 0)
        if (amountOfGold > 0) {
            val subtracted = model.resourceModel.resourcesAvailable.subtract(mapOf(Resource.GOLD to 1))
            model.resourceModel.setResourceAvailable(subtracted)
            modelForPlayer.setWalletAmount(Resource.GOLD,
                    modelForPlayer.wallet.getOrDefault(Resource.GOLD, 0) + 1)
        }
        if (received.returned != null) {
            val receivedNotNull = received.returned!!
            modelForPlayer.setWalletAmount(receivedNotNull,
                    modelForPlayer.wallet.getOrDefault(receivedNotNull, 0) - 1)
            val added = model.resourceModel.resourcesAvailable.merge(mapOf(receivedNotNull to 1))
            model.resourceModel.setResourceAvailable(added)
        }
        if (received.playerName == name) {
            val card = model.cardTableModel.cards[received.tier][received.position]
            model.reservedCardsModel.setCard(card, received.cardPosition)
        }
        model.cardTableModel.changeCard(received.tier, received.position, null)
    }

    fun cardBought(received: CardBought) {
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.playerName)
        val card = model.cardTableModel.cards[received.tier][received.position]!!
        modelForPlayer.setPointsAmount(modelForPlayer.points + card.points)
        modelForPlayer.setCardResourcesAmount(card.resource,
                modelForPlayer.cardResources.getOrDefault(card.resource, 0) + 1)
        received.toPay.forEach {
            modelForPlayer.setWalletAmount(it.key,
                    modelForPlayer.wallet.getOrDefault(it.key, 0) - it.value)
        }
        model.resourceModel.setResourceAvailable(model.resourceModel.resourcesAvailable.merge(
                received.toPay))
        model.cardTableModel.changeCard(received.tier, received.position, null)
    }

    fun reservedCardBought(received: ReservedCardBought) {
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.playerName)
        val card = model.reservedCardsModel.cards[received.position]!!
        modelForPlayer.setCardsReservedAmount(modelForPlayer.cardsReserved - 1)
        modelForPlayer.setPointsAmount(modelForPlayer.points + card.points)
        modelForPlayer.setCardResourcesAmount(card.resource,
                modelForPlayer.cardResources.getOrDefault(card.resource, 0) + 1)
        received.toPay.forEach {
            modelForPlayer.setWalletAmount(it.key,
                    modelForPlayer.wallet.getOrDefault(it.key, 0) - it.value)
        }
        model.resourceModel.setResourceAvailable(model.resourceModel.resourcesAvailable.merge(
                received.toPay))
        model.reservedCardsModel.setCard(null,received.position)
    }

    fun nobleTaken(received: NobleTaken) {
        val modelForPlayer = model.playerTableModel.getModelForPlayer(received.playerName)
        modelForPlayer.setPointsAmount(modelForPlayer.points + POINTS_FOR_NOBLE)
        model.cardTableModel.changeNoble(received.position, null)
    }

}