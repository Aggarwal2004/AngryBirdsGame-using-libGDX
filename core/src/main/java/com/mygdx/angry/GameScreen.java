package com.mygdx.angry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;


public class GameScreen implements Screen {
    private final GameStateManager gsm;
    private final SpriteBatch batch;
    private final Bird bird;
    private final ArrayList<Pig> pigs;
    private final ArrayList<Block> blocks;
    private final ArrayList<Bird> birdQueue;  // Birds in queue

    private final Texture backgroundTexture;
    private final Catapult catapult;

    private final Stage stage;
    private final Skin skin;
    private ImageButton  pauseButton;
    private ImageButton menuButton;
    private boolean isPopupActive = false;
    private Table popupTable;

    // Popup elements
    private Image popupBackground;
    private ImageButton resumePopupButton, levelsPopupButton;

    public GameScreen(GameStateManager gsm) {
        this.gsm = gsm;
        batch = new SpriteBatch();

        // Initialize birds with different sizes and types
        birdQueue = new ArrayList<>();

        birdQueue.add(new Bird(30, 200, 60, 60, Bird.BirdType.YELLOW));
        birdQueue.add(new Bird(60, 200, 50, 50, Bird.BirdType.RED));
        birdQueue.add(new Bird(5, 200, 40, 40, Bird.BirdType.BLUE));
        birdQueue.add(new Bird(125, 236, 20, 20, Bird.BirdType.BLUE));

        // Set up the first bird in the catapult
        bird = birdQueue.removeFirst();  // Launch the first bird


        // Initialize pigs (at various heights)
        // Initialize pigs (at various heights and sizes)
        pigs = new ArrayList<>();
        pigs.add(new Pig(422, 247, 17, 17));  // Small pig
        pigs.add(new Pig(455, 200, 30, 30));  // Medium pig
        pigs.add(new Pig(405, 282, 50, 50));  // Large pig


        // Initialize blocks with different materials
        blocks = new ArrayList<>();

        blocks.add(new Block(385, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(425, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(500, 225, 50, 20, Block.BlockType.HORI));
        blocks.add(new Block(400, 200, 30, 30, Block.BlockType.WOODEN));  // Wooden block base


        blocks.add(new Block(495, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(535, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(390, 225, 50, 20, Block.BlockType.HORI));
        blocks.add(new Block(510, 200, 30, 30, Block.BlockType.WOODEN));   // Wooden block top


        blocks.add(new Block(440, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(480, 190, 20, 50, Block.BlockType.VERT));
        blocks.add(new Block(445, 225, 50, 20, Block.BlockType.HORI));
           // Steel block

        blocks.add(new Block(410, 235, 40, 40, Block.BlockType.ICE));      // Ice block
        blocks.add(new Block(450, 235, 40, 40, Block.BlockType.ICETRI));      // Ice block
        blocks.add(new Block(490, 235, 40, 40, Block.BlockType.STEEL));

        blocks.add(new Block(385, 270, 180, 20, Block.BlockType.HORI));
        blocks.add(new Block(455, 282, 20, 60, Block.BlockType.LONG));
        blocks.add(new Block(475, 282, 50, 20, Block.BlockType.LONG));




        // Load background and catapult textures
        backgroundTexture = new Texture("background.png");

        catapult = new Catapult(100, 190, 100, 120);

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Texture pauseTexture = new Texture("pause.png");
        pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
        pauseButton.setSize(60, 60);
        pauseButton.setPosition(565, Gdx.graphics.getHeight() - 90+30);
        stage.addActor(pauseButton);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showPopup();
            }
        });

        // Menu button on the popup
        Texture menuTexture = new Texture("menu.png");
        menuButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuTexture)));
        menuButton.setSize(50, 50);
        menuButton.setPosition(15, Gdx.graphics.getHeight() - 90+30);
        stage.addActor(menuButton);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                showTablePopup();  // Show Win/Lose/Restart table
            }
        });

        isPopupActive = true;

    }



    private void showPopup() {
        // Popup background
        Texture popupTexture = new Texture("popup_background1.png");
        popupBackground = new Image(new TextureRegionDrawable(new TextureRegion(popupTexture)));
        popupBackground.setSize(650, 500);
        popupBackground.setPosition(0, 0);
        stage.addActor(popupBackground);

        // Resume button
        Texture resumeTexture = new Texture("resume.png");
        resumePopupButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(resumeTexture)));
        resumePopupButton.setSize(120, 120);
        resumePopupButton.setPosition(130, 190);
        stage.addActor(resumePopupButton);

        resumePopupButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hidePopup();  // Resume game
            }
        });

        // Levels button
        Texture levelsTexture = new Texture("exit.png");
        levelsPopupButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(levelsTexture)));
        levelsPopupButton.setSize(90, 90);
        levelsPopupButton.setPosition(380, 200);
        stage.addActor(levelsPopupButton);

        levelsPopupButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                hidePopup();
                gsm.setScreen("levels");
            }
        });
    }

    private void showTablePopup() {
        popupTable = new Table();
        popupTable.setFillParent(true);  // Center the table
        popupTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("popup_background1.png"))));

        // Create buttons: Win, Lose, Restart
        TextButton winButton = new TextButton("Win", skin);
        TextButton loseButton = new TextButton("Lose", skin);
        Texture restartTexture = new Texture("restart_button.png");
        ImageButton restartButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(restartTexture)));

        // Set size properly within the table
        popupTable.add(winButton).size(100, 55).pad(10);  // Adjust size as needed
        popupTable.row();
        popupTable.add(loseButton).size(100, 55).pad(10);  // Adjust size as needed
        popupTable.row();
        popupTable.add(restartButton).size(150, 75).pad(10);  // Adjust size here

        // Add listeners for buttons
        winButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Win button clicked!");
                hideTablePopup();
                gsm.setScreen("win");
            }
        });

        loseButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Lose button clicked!");
                hideTablePopup();
                gsm.setScreen("lose");
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                hideTablePopup();
                gsm.setScreen("game");
            }
        });

        // Add the table to the stage
        stage.addActor(popupTable);
    }


    private void hideTablePopup() {
        popupTable.remove();
        isPopupActive = false;
    }

    private void hidePopup() {
        popupBackground.remove();
        resumePopupButton.remove();
        levelsPopupButton.remove();
        menuButton.remove();
        isPopupActive = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        catapult.render(batch);

        for (Bird queuedBird : birdQueue) {
            queuedBird.render(batch);
        }
        bird.render(batch);

        for (Block block : blocks) {
            block.render(batch);
        }
        for (Pig pig : pigs) {
            pig.render(batch);
        }

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        batch.dispose();
        backgroundTexture.dispose();
        catapult.dispose();
        bird.dispose();

        for (Bird queuedBird : birdQueue) {
            queuedBird.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        for (Block block : blocks) {
            block.dispose();
        }

        stage.dispose();
        skin.dispose();
    }
}
