package com.c2v4.splendid

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.c2v4.splendid.manager.FontManager
import com.c2v4.splendid.network.NetworkAdapter
import com.c2v4.splendid.network.message.login.JoinLobby
import com.c2v4.splendid.network.message.login.SimpleLogIn
import com.c2v4.splendid.screen.TestScreen
import java.util.*

class SplendidGame : Game() {

    override fun create() {
        setScreen(TestScreen())
        val skin = initializeSkin()
        val name = UUID.randomUUID().toString().substring(0..10)
        val networkAdapter = NetworkAdapter(ClientListener(ClientController(name, skin, this)),"splendid.hopto.org")
        networkAdapter.send(SimpleLogIn(name))
        networkAdapter.send(JoinLobby(1))
    }

    private fun initializeSkin(): Skin {
        val skin = Skin(Gdx.files.internal("ui/skin.json"))
        val generator = FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 48
        val uiFont = generator.generateFont(parameter)
        parameter.size = 72
        val playerNameFont = generator.generateFont(parameter)
        generator.dispose()
        skin.add(FontManager.UI_FONT, uiFont)
        skin.add(FontManager.PLAYER_NAME_FONT, playerNameFont)
        return skin
    }

    companion object {
        val WIDTH = 1366
        val HEIGHT = 768
    }
}