package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SavedGamesScreen extends AbstractGameScreen {
    private final Texture backgroundTexture;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private ImageButton backButton;
    private ImageButton savedSlotButton;

    public SavedGamesScreen(GameStateManager gsm) {
        super(gsm);

        // Load background texture and SpriteBatch
        backgroundTexture = new Texture("saved_bg.png");
        batch = new SpriteBatch();

        // Use FitViewport to maintain aspect ratio
        viewport = new FitViewport(1000, 600);
        stage = new Stage(viewport, batch);

        // Set up UI elements
        setupUI();
    }

    @Override
    protected void setupUI() {
        // Ensure input events are processed by the stage
        Gdx.input.setInputProcessor(stage);

        // Create the ImageButton for the saved game slot
        Texture savedSlotTexture = new Texture("saved1.png");  // Ensure the image exists in assets
        TextureRegionDrawable savedSlotDrawable = new TextureRegionDrawable(new TextureRegion(savedSlotTexture));
        savedSlotButton = new ImageButton(savedSlotDrawable);

        // Set size and position of the saved slot button
        savedSlotButton.setSize(200, 100);  // Adjust size as needed
        savedSlotButton.setPosition(40, 450);  // Adjust position

        stage.addActor(savedSlotButton);

        // Add click listener to the saved slot button
        savedSlotButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Loading saved game...");
                gsm.setScreen("savedgame");  // Switch to saved game screen
            }
        });

        // Create the back button
        Texture backTexture = new Texture("back.png");  // Ensure the image exists in assets
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(new TextureRegion(backTexture));
        backButton = new ImageButton(backDrawable);

        // Set back button size and position
        backButton.setSize(80, 150);  // Adjust size
        setBackButtonPosition(10, 480);  // Top-left corner

        stage.addActor(backButton);

        // Add click listener to the back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Back button clicked!");
                gsm.setScreen("start");  // Return to StartScreen
            }
        });
    }

    // Helper method to set the back button's position
    public void setBackButtonPosition(float x, float y) {
        backButton.setPosition(x, y);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Draw the background image
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);  // Ensure correct resizing
    }

    @Override
    public void dispose() {
        // Dispose of resources properly
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
