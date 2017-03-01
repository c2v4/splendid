package com.c2v4.splendid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import com.c2v4.splendid.SplendidGame
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
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.entity.BoardView
import java.util.*

class TestScreen(val skin: Skin) : Screen {

    private var stage: Stage? = null

    override fun show() {
        stage = Stage(FitViewport(2f * SplendidGame.WIDTH, 2f * SplendidGame.HEIGHT))

        val model = CommonModel.empty()

        val cardTableModel = CardTableModel.empty()
        val cardTableView = CardTableView(skin, cardTableModel)
        CardTableController(cardTableView, cardTableModel)

        val resourceModel = ResourceTableModel.empty()
        val resourceView = ResourceTableView(skin, resourceModel)
        val resourceController = ResourceTableController(resourceView, resourceModel, model)

        val playerStateModel = PlayerStateModel.empty()
        val playerStateModel2 = PlayerStateModel.emptyEnemy("Rysiek")
        val playerResourceModel = PlayerTableModel(playerStateModel, mutableListOf(playerStateModel2))
        val reservedCardsModel = ReservedCardsModel.empty()

        val boardActor = BoardView(skin, cardTableView, resourceView,PlayerTableView(playerResourceModel,skin),
                ReservedCardsView(reservedCardsModel,skin))

        playerStateModel.setWalletAmount(Resource.RED,4)
        playerStateModel.setWalletAmount(Resource.BLACK,3)
        playerStateModel.setCardAmount(Resource.BLACK,2)

        boardActor.setFillParent(true)
        stage!!.addActor(boardActor)
        boardActor.debugAll()
        (0..2).forEach { i ->
            (0..3).forEach {
                cardTableModel.changeCard(i,
                        it,
                        Card(i,
                                i + it,
                                mapOf(Resource.values()[Random().nextInt(
                                        Resource.values().size)] to Random().nextInt(5) + 1,
                                        Resource.values()[Random().nextInt(
                                                Resource.values().size)] to Random().nextInt(5) + 1,
                                        Resource.values()[Random().nextInt(
                                                Resource.values().size)] to Random().nextInt(5) + 1),
                                Resource.values()[i + it]))
            }
        }
        Gdx.input.inputProcessor = stage
    }

    override fun pause() {
    }

    override fun hide() {
    }

    override fun resume() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage!!.act(Gdx.graphics.deltaTime)
        stage!!.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage!!.viewport.update(width, height)
    }

    override fun dispose() {
        stage!!.dispose()
        skin.dispose()
    }
}