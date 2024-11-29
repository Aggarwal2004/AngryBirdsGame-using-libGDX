package com.mygdx.angry;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private GameStateManager gsm;

    @Override
    public void create() {
        gsm = new GameStateManager(this);

        // Register all screens
        gsm.addScreen("start", new StartScreen(gsm));
        gsm.addScreen("levvels", new LevelsScreen(gsm)); // Ensure LevelsScreen is registered
        gsm.addScreen("level1", new GameScreen(gsm));     // Ensure GameScreen is registered
        gsm.addScreen("level2", new GameScreen2(gsm));
        gsm.addScreen("level3", new GameScreen3(gsm));
        gsm.addScreen("pause", new PauseScreen(gsm));
        gsm.addScreen("result", new LevelResultScreen(gsm));
        gsm.addScreen("saved", new SavedGamesScreen(gsm));
        gsm.addScreen("savedgame", new SavedScreen1(gsm));
        gsm.addScreen("win", new WinningScreen(gsm));
        gsm.addScreen("lose", new LosingScreen(gsm));

        // Set the initial screen to StartScreen
        gsm.setScreen("start");
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);  // Ensure resize propagates to all screens
    }

    @Override
    public void dispose() {
//        gsm.dispose();  // Ensure all screens are cleaned up properly
    }
}
