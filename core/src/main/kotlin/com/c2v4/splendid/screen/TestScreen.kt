package com.c2v4.splendid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.entity.CardActor

class TestScreen : Screen {

    private var stage: Stage? = null
    private var skin: Skin? = null

    override fun show() {
        stage = Stage(FitViewport(1024f, 768f))
        skin = Skin(Gdx.files.internal("ui/skin.json"))

        stage!!.addActor(CardActor(Card(1,4,mapOf(Resource.BLACK to 2, Resource.BLUE to 3),Resource.RED), skin!!))


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
        skin!!.dispose()
    }
}