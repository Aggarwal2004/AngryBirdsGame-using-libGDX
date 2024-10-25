package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LosingScreen implements Screen {
    private final GameStateManager gsm;
    private final SpriteBatch batch;
    private final Stage stage;
    private final Texture loseBackgroundTexture;
    private ImageButton restartButton, levelsButton;
    private Image resultImage;

    public LosingScreen(GameStateManager gsm) {
        this.gsm = gsm;
        batch = new SpriteBatch();
        stage = new Stage();

        // Load background texture for the losing screen
        loseBackgroundTexture = new Texture("win_background.png");  // Correct background file name
        Image backgroundImage = new Image(new TextureRegionDrawable(loseBackgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add the background image first (so buttons are on top)
        stage.addActor(backgroundImage);
        setupResultImage();


        setupUI();

        // Ensure input events are processed by the stage
        Gdx.input.setInputProcessor(stage);
    }

    private void setupResultImage() {
        // Load the result image (e.g., "you_lost.png")
        Texture resultTexture = new Texture("losing.png");
        resultImage = new Image(new TextureRegionDrawable(resultTexture));

        // Set size and position for the result image
        resultImage.setSize(450, 300);  // Adjust size if needed
        resultImage.setPosition(80,100 );

        // Add the result image to the stage
        stage.addActor(resultImage);
    }

    private void setupUI() {

        Texture levelsTexture = new Texture("back.png");
        levelsButton = new ImageButton(new TextureRegionDrawable(levelsTexture));
        levelsButton.setSize(200, 250);
        levelsButton.setPosition(205,35);
        stage.addActor(levelsButton);
        

        // Restart button
        Texture restartTexture = new Texture("restart_button.png");
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        restartButton.setSize(110, 120);  // Adjust size if needed
        restartButton.setPosition(155,95);  // Adjust as necessary
        stage.addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                gsm.setScreen("game");  // Restart the game
            }
        });

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
        // Ensure the input processor is set when the screen is shown
        Gdx.input.setInputProcessor(stage);
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
        loseBackgroundTexture.dispose();
        stage.dispose();
    }
}
