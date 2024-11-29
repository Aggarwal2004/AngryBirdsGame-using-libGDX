package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen extends AbstractGameScreen {
    private final Texture backgroundTexture;
    private final SpriteBatch batch;
    private final Skin skin;
    private final Viewport viewport;
    private ImageButton exitButton;

    // Popup elements
    private Stage popupStage;
    private boolean isPopupActive = false;

    public StartScreen(GameStateManager gsm) {
        super(gsm);

        // Initialize background texture and SpriteBatch
        backgroundTexture = new Texture("start.png");
        batch = new SpriteBatch();

        // Use FitViewport to maintain aspect ratio
        viewport = new FitViewport(1000, 600);
        stage = new Stage(viewport, batch);

        // Load the skin for buttons
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Set up the UI elements
        setupUI();
    }

    @Override
    protected void setupUI() {
        Gdx.input.setInputProcessor(stage);  // Process input via the stage

        // New Game button
        Texture newGameTexture = new Texture("play.png");
        ImageButton newGameButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(newGameTexture)));
        newGameButton.setSize(270, 170);
        newGameButton.setPosition((viewport.getWorldWidth() - newGameButton.getWidth()) / 2, 20);

        stage.addActor(newGameButton);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                gsm.setScreen("levvels");
            }
        });

        // Resume Game button
        Texture resumeTexture = new Texture("resume.png");
        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        resumeButton.setSize(200, 100);
        resumeButton.setPosition(250, 20);
        stage.addActor(resumeButton);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {

                gsm.setScreen("saved");
            }
        });

        // Exit button
        Texture exitTexture = new Texture("exit.png");
        exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(exitTexture)));
        exitButton.setSize(150, 80);
        setExitButtonPosition(850, 500);

        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showCustomExitPopup();  // Show custom exit popup
            }
        });
    }

    private void showCustomExitPopup() {
        // Initialize popup stage
        popupStage = new Stage(new FitViewport(800, 480), batch);

        // Popup background image
        Texture popupTexture = new Texture("popup_background.png");
        Image popupBackground = new Image(new TextureRegionDrawable(new TextureRegion(popupTexture)));
        popupBackground.setSize(350, 220);
        popupBackground.setPosition(
            (popupStage.getWidth() - popupBackground.getWidth()) / 2,
            (popupStage.getHeight() - popupBackground.getHeight()) / 2
        );
        popupStage.addActor(popupBackground);

        // "Yes" button
        Texture yesTexture = new Texture("yes_button.png");
        ImageButton yesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(yesTexture)));
        yesButton.setSize(100, 50);
        yesButton.setPosition(
            267,
            118
        );
        popupStage.addActor(yesButton);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit game
            }
        });

        // "No" button
        Texture noTexture = new Texture("no_button.png");
        ImageButton noButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(noTexture)));
        noButton.setSize(100, 50);
        noButton.setPosition(
            445,
            118
        );
        popupStage.addActor(noButton);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isPopupActive = false;  // Close popup
                Gdx.input.setInputProcessor(stage);  // Reset input processor to main stage
            }
        });

        // Set input processor to popup stage and mark popup as active
        Gdx.input.setInputProcessor(popupStage);
        isPopupActive = true;
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background image
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Update and draw the appropriate stage
        if (isPopupActive) {
            popupStage.act(delta);
            popupStage.draw();
        } else {
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void setExitButtonPosition(float x, float y) {
        exitButton.setPosition(x, y);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        if (popupStage != null) {
            popupStage.dispose();
        }
    }
}
