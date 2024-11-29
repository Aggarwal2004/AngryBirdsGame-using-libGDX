package com.mygdx.angry;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;

/**
 * Base class for all screens to avoid duplicate code.
 * Each screen will extend this class and implement its own UI setup.
 */
public abstract class AbstractGameScreen implements Screen {
    protected GameStateManager gsm;
    protected Stage stage;
    protected Skin skin;
    protected SpriteBatch batch;

    // Constructor to initialize the GameStateManager and set up the Stage
    public AbstractGameScreen(GameStateManager gsm) {
        this.gsm = gsm;
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);  // Set input handling to the stage
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));  // Default skin
    }

    /**
     * This method must be implemented by subclasses to set up their specific UI components.
     */
    protected abstract void setupUI();

    @Override
    public void show() {
        stage.clear();  // Clear the stage whenever the screen is shown
        Gdx.input.setInputProcessor(stage);  // Set the input processor to the stage
        setupUI();  // Call the setupUI method to initialize components
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);  // Update the stage with the delta time
        stage.draw();      // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if(batch != null) {
            batch.dispose();
        }
        stage.dispose();  // Dispose of the stage to free resources
        skin.dispose();   // Dispose of the skin to free resources
    }
}

