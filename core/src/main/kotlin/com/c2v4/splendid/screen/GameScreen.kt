package com.c2v4.splendid.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.FitViewport

class GameScreen : Screen {

    private var stage: Stage? = null
    private var skin: Skin? = null

    override fun show() {
        stage = Stage(FitViewport(640f, 480f))
        skin = Skin(Gdx.files.internal("ui/skin.json"))



        val window = Window("Example screen", skin!!, "border")
        window.defaults().pad(10f)
        window.add("This is a simple Scene2D view.").row()
        val button = TextButton("Click me!", skin!!)
        button.pad(4f)
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                button.setText("Clicked.")
            }
        })
        window.add(button)
        window.pack()
        window.setPosition(stage!!.width / 2f - window.width / 2f,
                stage!!.height / 2f - window.height / 2f)
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)))
        stage!!.addActor(window)

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