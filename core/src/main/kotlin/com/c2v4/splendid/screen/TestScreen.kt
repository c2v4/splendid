package com.c2v4.splendid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import com.c2v4.splendid.SplendidGame
import com.c2v4.splendid.controller.BoardController
import com.c2v4.splendid.entity.BoardActor

class TestScreen(val skin: Skin) : Screen {

    private var stage: Stage? = null

    override fun show() {
        stage = Stage(FitViewport(2f*SplendidGame.WIDTH, 2f*SplendidGame.HEIGHT))


        val boardActor = BoardActor(skin)
        boardActor.setFillParent(true)
        stage!!.addActor(boardActor)
        boardActor.setPosition(0f,0f)

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