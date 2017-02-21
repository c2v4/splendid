package com.c2v4.splendid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import com.c2v4.splendid.SplendidGame
import com.c2v4.splendid.cardtable.CardTableModel
import com.c2v4.splendid.cardtable.CardTableView
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.entity.BoardView
import java.util.*

class TestScreen(val skin: Skin) : Screen {

    private var stage: Stage? = null

    override fun show() {
        stage = Stage(FitViewport(2f * SplendidGame.WIDTH, 2f * SplendidGame.HEIGHT))


        val model = CardTableModel.empty()
        val boardActor = BoardView(skin, CardTableView(skin, model))
        boardActor.setFillParent(true)
        stage!!.addActor(boardActor)
        boardActor.debugAll()
        (0..2).forEach { i ->
            (0..3).forEach {
                model.changeCard(i,
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