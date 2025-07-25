package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WinningScreen implements Screen {
    private final GameStateManager gsm;
    private final SpriteBatch batch;
    private final Stage stage;
    private final Texture winBackgroundTexture;
    private ImageButton restartButton, levelsButton;
    private int currentLevel;

    public WinningScreen(GameStateManager gsm) {
        this.gsm = gsm;
        batch = new SpriteBatch();
        stage = new Stage();

        // Load background texture for the winning screen
        winBackgroundTexture = new Texture("win_background.png");
        Image backgroundImage = new Image(new TextureRegionDrawable(winBackgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add the background image first (so buttons appear on top)
        stage.addActor(backgroundImage);
        currentLevel = gsm.getCurrentLevel();

        setupUI();
        Gdx.input.setInputProcessor(stage);  // Ensure input is handled by the stage
    }

    private void setupUI() {
        // Restart button
        Texture restartTexture = new Texture("restart_button.png");
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        restartButton.setSize(150, 85);  // Adjust size if needed
        restartButton.setPosition(850,250);  // Adjust as necessary
        stage.addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                gsm.setScreen("level" + currentLevel);  // Restart the game
            }
        });

        // Levels button
        Texture levelsTexture = new Texture("back.png");
        levelsButton = new ImageButton(new TextureRegionDrawable(levelsTexture));
        levelsButton.setSize(210, 105);  // Adjust size if needed
        levelsButton.setPosition(400, 240);  // Adjust as necessary
        stage.addActor(levelsButton);

        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Levels button clicked!");
                gsm.setScreen("levvels");  // Return to levels screen
            }
        });
        Texture nextLevelTexture = new Texture("next_level_button.png");
        ImageButton nextLevelButton = new ImageButton(new TextureRegionDrawable(nextLevelTexture));
        nextLevelButton.setSize(170, 105);  // Adjust size if needed
        nextLevelButton.setPosition(1320, 730);  // Position at the top of the screen
        stage.addActor(nextLevelButton);

        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Next level button clicked!");
                gsm.setScreen("level" + (currentLevel + 1));  // Go to the next level
            }
        });
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw UI elements
        batch.begin();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);  // Reset input to stage when shown
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        winBackgroundTexture.dispose();
        stage.dispose();
    }
}

//the thing is that when I click on the restart button of the winning sxcren after winning level 2 then it directs to level 1 instead of level 2.fix this without adding any more arguments to the required functions
