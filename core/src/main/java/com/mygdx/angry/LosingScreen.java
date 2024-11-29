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
    private int currentLevel;

    public LosingScreen(GameStateManager gsm) {
        this.gsm = gsm;
        batch = new SpriteBatch();
        stage = new Stage();
        currentLevel = gsm.getCurrentLevel();

        // Load background texture for the losing screen
        loseBackgroundTexture = new Texture("lose_background.png");  // Correct background file name
        Image backgroundImage = new Image(new TextureRegionDrawable(loseBackgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);


        setupUI();

        // Ensure input events are processed by the stage
        Gdx.input.setInputProcessor(stage);
    }



    private void setupUI() {

        Texture levelsTexture = new Texture("back.png");
        levelsButton = new ImageButton(new TextureRegionDrawable(levelsTexture));
        levelsButton.setSize(180, 230);
        levelsButton.setPosition(905,355);
        stage.addActor(levelsButton);


        // Restart button
        Texture restartTexture = new Texture("restart_button.png");
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture));
        restartButton.setSize(90, 100);  // Adjust size if needed
        restartButton.setPosition(435,420);  // Adjust as necessary
        stage.addActor(restartButton);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                gsm.setScreen("level" + currentLevel);  // Restart the game
            }
        });

        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Levels button clicked!");
                gsm.setScreen("levvels");  // Return to levels screen
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
