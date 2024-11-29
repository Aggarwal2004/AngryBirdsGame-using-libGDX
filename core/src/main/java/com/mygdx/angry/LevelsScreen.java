package com.mygdx.angry;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class LevelsScreen extends AbstractGameScreen  implements Screen {
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
        createLevelButton("level1.png", 10, 140, "level1", "game");       // Level 1 leads to "game"
        createLevelButton("level2.png", 300, 140, "level2", "level2");    // Level 2 leads to "level2"
        createLevelButton("level3.png", 610, 140, "level3", "level3");

        // Create an ImageButton for the back button
        Texture backTexture = new Texture("back.png");  // Ensure this image exists in assets
        TextureRegionDrawable backDrawable = new TextureRegionDrawable(new TextureRegion(backTexture));
        backButton = new ImageButton(backDrawable);  // Initialize ImageButton
        backButton.setSize(200, 100);

        // Set initial position (centered horizontally)
        setBackButtonPosition((viewport.getWorldWidth() - backButton.getWidth()) / 2, 30);

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
    // Helper method to create an ImageButton for each level
    private void createLevelButton(String imagePath, float x, float y, String levelName, String targetScreen) {
        Texture levelTexture = new Texture(imagePath);
        TextureRegionDrawable levelDrawable = new TextureRegionDrawable(new TextureRegion(levelTexture));

        ImageButton levelButton = new ImageButton(levelDrawable);
        levelButton.setPosition(x, y);

        levelButton.setSize(400, 400);
        stage.addActor(levelButton);

        levelButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float xx, float yy) {
                System.out.println(levelName + " image clicked!");
                gsm.setScreen(targetScreen);  // Switch to the specified screen
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
//        batch.dispose();
        backgroundTexture.dispose();
    }
}
