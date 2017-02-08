package com.c2v4.splendid;

import com.badlogic.gdx.Game;
import com.c2v4.splendid.screen.TestScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SplendidGame extends Game {

    @Override
    public void create() {
        setScreen(new TestScreen());
    }
}