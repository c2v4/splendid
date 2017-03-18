package com.c2v4.splendid

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.c2v4.splendid.component.CommonModel
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
import com.c2v4.splendid.entity.BoardView
import com.c2v4.splendid.network.message.game.CardDeal
import com.c2v4.splendid.network.message.game.InitialCoins
import com.c2v4.splendid.network.message.game.NobleDeal
import com.c2v4.splendid.screen.TestScreen

class ClientController(val name: String, val skin: Skin, val splendidGame: SplendidGame) {

    var model: CommonModel = CommonModel.empty()

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
        val playerTableModel = PlayerTableModel(playerStateModel, enemies)
        val reservedCardsModel = ReservedCardsModel.empty()
        model = CommonModel.empty(cardTableModel,
                resourceModel,
                playerTableModel,
                reservedCardsModel)

        val cardTableView = CardTableView(skin, cardTableModel)
        CardTableController(cardTableView, cardTableModel)

        val resourceView = ResourceTableView(skin, resourceModel)
        val resourceController = ResourceTableController(resourceView, resourceModel, model)


        val boardActor = BoardView(skin,
                cardTableView,
                resourceView,
                PlayerTableView(playerTableModel, skin),
                ReservedCardsView(reservedCardsModel, skin))
        val screen = splendidGame.screen as TestScreen
        screen.addMainActor(boardActor)
    }

    fun setInitialCoins(received: InitialCoins) {
        model.resourceModel.setResourceAvailable(received.coins)
    }

    fun cardDeal(received: CardDeal) {
        model.cardTableModel.changeCard(received.tier,received.position,received.card)
    }

    fun nobleDeal(received: NobleDeal) {
        model.cardTableModel.changeNoble(received.position,received.noble)
    }

}