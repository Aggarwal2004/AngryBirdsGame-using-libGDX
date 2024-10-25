package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelsScreen extends AbstractGameScreen {
    private final Texture backgroundTexture;
    private final SpriteBatch batch;
    private final Skin skin;
    private final Viewport viewport;
    private ImageButton backButton;  // Use ImageButton instead of TextButton

    public LevelsScreen(GameStateManager gsm) {
        super(gsm);

        // Initialize the background texture and SpriteBatch
        backgroundTexture = new Texture("level.png");  // Ensure this image exists in assets
        batch = new SpriteBatch();

        // Use FitViewport to maintain aspect ratio
        viewport = new FitViewport(1000, 600);  // Logical screen size
        stage = new Stage(viewport, batch);  // Use the same viewport for the stage

        // Load the skin (used for other buttons if needed)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Set up the UI elements
        setupUI();
    }

    @Override
    protected void setupUI() {
        // Ensure input is processed by the stage
        Gdx.input.setInputProcessor(stage);

        // Create ImageButtons for levels 1 to 5
        createLevelButton("one.png", 50, 450, "level1");
        createLevelButton("two.png", 180, 450, "level2");
        createLevelButton("three.png", 310, 450, "level3");
        createLevelButton("four.png", 440, 450, "level4");
        createLevelButton("five.png", 570, 450, "level5");
        createLevelButton("six.png", 700, 450, "level6");
        createLevelButton("seven.png", 830, 450, "level7");
//
        createLevelButton("eight.png", 50, 270, "level8");
        createLevelButton("nine.png", 180, 270, "level9");
        createLevelButton("ten 2.png", 310, 270, "level10");
        createLevelButton("lock.png", 440, 270, "lock1");
        createLevelButton("lock.png", 570, 270, "lock2");
        createLevelButton("lock.png", 700, 270, "lock3");
        createLevelButton("lock.png", 830, 270, "lock4");

        // Create an ImageButton for the back button
        Texture backTexture = new Texture("back.png");  // Ensure this image exists in assets
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(new TextureRegion(backTexture));
        backButton = new ImageButton(backDrawable);  // Initialize ImageButton
        backButton.setSize(200, 100);

        // Set initial position (centered horizontally)
        setBackButtonPosition((viewport.getWorldWidth() - backButton.getWidth()) / 2, 50);

        stage.addActor(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Back button clicked!");
                gsm.setScreen("start");  // Return to StartScreen
            }
        });
    }

    // Method to dynamically set the Back button's position
    public void setBackButtonPosition(float x, float y) {
        backButton.setPosition(x, y);
    }

    // Helper method to create an ImageButton for each level
    private void createLevelButton(String imagePath, float x, float y, String levelName) {
        Texture levelTexture = new Texture(imagePath);
        TextureRegionDrawable levelDrawable = new TextureRegionDrawable(new TextureRegion(levelTexture));

        ImageButton levelButton = new ImageButton(levelDrawable);
        levelButton.setPosition(x, y);

        levelButton.setSize(100, 100);
        stage.addActor(levelButton);

        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float xx, float yy) {
                System.out.println(levelName + " image clicked!");
                gsm.setScreen("game");  // Switch to the GameScreen
            }
        });
    }

    @Override
    public void render(float delta) {
        // Clear the screen and prepare for rendering
        Gdx.gl.glClearColor(1, 1, 1, 1);  // Optional: Clear with a white background
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Draw the background image without distortion
        batch.setProjectionMatrix(viewport.getCamera().combined);  // Use the viewport's camera
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Draw the UI elements on top of the background
        stage.act(delta);  // Update stage
        stage.draw();      // Draw stage
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);  // Ensure the viewport is updated correctly
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }
}
