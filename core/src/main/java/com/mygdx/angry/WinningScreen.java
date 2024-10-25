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
    private Image resultImage;

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
        setupResultImage();

        setupUI();
        Gdx.input.setInputProcessor(stage);  // Ensure input is handled by the stage
    }

    private void setupResultImage() {
        // Load the result image (e.g., "you_lost.png")
        Texture resultTexture = new Texture("result.png");
        resultImage = new Image(new TextureRegionDrawable(resultTexture));

        // Set size and position for the result image
        resultImage.setSize(350, 300);  // Adjust size if needed
        resultImage.setPosition(120,100 );

        // Add the result image to the stage
        stage.addActor(resultImage);
    }

    private void setupUI() {
        // Restart button
        Texture restartTexture = new Texture("restart_button.png");
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        restartButton.setSize(100, 55);  // Adjust size if needed
        restartButton.setPosition(130,110);  // Adjust as necessary
        stage.addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                gsm.setScreen("game");  // Restart the game
            }
        });

        // Levels button
        Texture levelsTexture = new Texture("back.png");
        levelsButton = new ImageButton(new TextureRegionDrawable(levelsTexture));
        levelsButton.setSize(150, 75);  // Adjust size if needed
        levelsButton.setPosition(180, 100);  // Adjust as necessary
        stage.addActor(levelsButton);

        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Levels button clicked!");
                gsm.setScreen("levels");  // Return to levels screen
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
