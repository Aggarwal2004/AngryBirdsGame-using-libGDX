package com.mygdx.angry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import java.util.HashMap;

public class GameStateManager {
    private final Game game;
    private final HashMap<String, Screen> screens = new HashMap<>();

    public GameStateManager(Game game) {
        this.game = game;
    }

    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public void setScreen(String name) {
        game.setScreen(screens.get(name));
    }

    public void dispose() {
        for (Screen screen : screens.values()) {
            screen.dispose();
        }
    }
}
