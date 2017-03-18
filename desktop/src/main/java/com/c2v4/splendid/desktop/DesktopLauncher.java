package com.c2v4.splendid.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.c2v4.splendid.SplendidGame;

import static com.c2v4.splendid.SplendidGame.HEIGHT;
import static com.c2v4.splendid.SplendidGame.WIDTH;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {

    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new SplendidGame(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Splendid";
        configuration.width = Companion.getWIDTH();
        configuration.height = Companion.getHEIGHT();
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}