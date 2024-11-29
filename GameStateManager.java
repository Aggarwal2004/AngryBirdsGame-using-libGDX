package com.mygdx.angry;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.io.Serializable;
import java.util.HashMap;

public class GameStateManager implements Serializable {
    private final Game game;
    private final HashMap<String, Screen> screens = new HashMap<>();
    private int currentLevel=1;

    public GameStateManager(Game game) {
        this.game = game;
    }

    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public void setScreen(String screenName) {
        // Dispose of the current screen if it exists
        Screen currentScreen = game.getScreen();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        if (screenName.startsWith("level")) {
            currentLevel = Integer.parseInt(screenName.substring(5));
        }

        // Add debug print to see the actual screen name being passed
        System.out.println("Attempting to set screen: " + screenName);

        Screen newScreen = createScreen(screenName);

        // Optional: Add the newly created screen to the screens map
        screens.put(screenName, newScreen);

        game.setScreen(newScreen);
    }
    public int getCurrentLevel() {
        return currentLevel;
    }
    public void setScreen(GameScreen screen) {
        game.setScreen(screen);
    }
    public void setScreen(GameScreen2 screen) {
        game.setScreen(screen);
    }
    public void setScreen(GameScreen3 screen) {
        game.setScreen(screen);
    }


    private Screen createScreen(String screenName) {
        // Add more comprehensive screen mapping
        switch (screenName) {
            case "game":
                return new GameScreen(this);
            case "win":
                return new WinningScreen(this);
            case "levvels":
                return new LevelsScreen(this);  // Changed to LevelsScreen
            case "start":
                return new StartScreen(this);
            case "level1":
                return new GameScreen(this);  // Assuming level1 goes to GameScreen
            case "level2":
                return new GameScreen2(this);  // You might want to create specific level screens
            // Add other specific level cases as needed
            case "level3":
                return new GameScreen3(this);
            case "saved":
                return new SavedGamesScreen(this);
            case "lose":
                return new LosingScreen(this);

            default:
                // Print out the unrecognized screen name for debugging
                System.err.println("Unrecognized screen name: " + screenName);
                throw new IllegalArgumentException("Unknown screen: " + screenName);
        }
    }

    public Screen getScreen(String screenName) {
        return screens.get(screenName);
    }

    public void dispose() {
        for (Screen screen : screens.values()) {
//            screen.dispose();
        }
        screens.clear();
    }
}
