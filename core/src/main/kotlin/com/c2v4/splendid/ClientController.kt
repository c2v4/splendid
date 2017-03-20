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
import com.c2v4.splendid.component.reservedcard.ReservedCardsModel
import com.c2v4.splendid.component.reservedcard.ReservedCardsView
import com.c2v4.splendid.component.resourcetable.ResourceTableController
import com.c2v4.splendid.component.resourcetable.ResourceTableModel
import com.c2v4.splendid.component.resourcetable.ResourceTableView
import com.c2v4.splendid.core.model.Player
import com.c2v4.splendid.entity.BoardView
import com.c2v4.splendid.network.message.game.CardDeal
import com.c2v4.splendid.network.message.game.CoinsTaken
import com.c2v4.splendid.network.message.game.InitialCoins
import com.c2v4.splendid.network.message.game.NobleDeal
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
        model = CommonModel.empty(cardTableModel,
                resourceModel,
                playerTableModel,
                reservedCardsModel)

        val cardTableView = CardTableView(skin, cardTableModel)
        val cardTableController = CardTableController(cardTableView,
                cardTableModel,
                reservedCardsModel,
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
                ReservedCardsView(reservedCardsModel, skin),
                model)
        boardActor.onButtonClick { inputEvent, button ->
            println("GO")
            if (model.getPlayerTurn() && model.getActionCorrect()) {
                model.setPlayerTurn(false)
                model.setActionCorrect(false)
                when (model.playerEvent) {
                    PlayerEvent.GET_COINS -> {
                        client.sendTCP(resourceController.getEvent())
                    }
                    PlayerEvent.BUY ->{
//                        client.sendTCP(cardTableController.getBuyEvent())
                    }
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

    fun setPlayerTurn(playerTurn: Boolean) {
        model.setPlayerTurn(playerTurn)
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


}