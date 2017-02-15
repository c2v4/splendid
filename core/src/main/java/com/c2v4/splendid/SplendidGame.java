package com.c2v4.splendid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.c2v4.splendid.manager.FontManager;
import com.c2v4.splendid.screen.TestScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class SplendidGame extends Game {

    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;

    @Override
    public void create() {
        Skin skin = initializeSkin();
        setScreen(new TestScreen(skin));
    }

    private Skin initializeSkin() {
        final Skin skin = new Skin(Gdx.files.internal("ui/skin.json"));
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/comic.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        final BitmapFont uiFont = generator.generateFont(parameter);
        generator.dispose();
        skin.add(FontManager.Companion.getUI_FONT(),uiFont);
        return skin;
    }
}