package com.mygdx.angry;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseScreen extends AbstractGameScreen {

    public PauseScreen(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void setupUI() {
        // Resume button to return to GameScreen
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.setPosition(400, 300);
        stage.addActor(resumeButton);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gsm.setScreen("game");  // Resume the game
            }
        });

        // Exit button to return to StartScreen
        TextButton exitButton = new TextButton("Exit to Menu", skin);
        exitButton.setPosition(400, 200);
        stage.addActor(exitButton);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gsm.setScreen("start");  // Go back to StartScreen
            }
        });
    }
}

