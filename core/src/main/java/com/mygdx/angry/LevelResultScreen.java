package com.mygdx.angry;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelResultScreen extends AbstractGameScreen {

    public LevelResultScreen(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void setupUI() {
        // Button to restart the level
        TextButton restartButton = new TextButton("Restart Level", skin);
        restartButton.setPosition(400, 300);
        stage.addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gsm.setScreen("game");  // Restart the game
            }
        });

        // Button to return to the StartScreen
        TextButton menuButton = new TextButton("Back to Menu", skin);
        menuButton.setPosition(400, 200);
        stage.addActor(menuButton);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gsm.setScreen("start");  // Return to StartScreen
            }
        });
    }
}

