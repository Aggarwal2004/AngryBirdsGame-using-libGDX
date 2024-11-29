package com.mygdx.angry;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.*;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;


public class GameScreen2 implements Screen , Serializable{
    private transient World world;
    private transient Bird currentBird;
    private transient GameStateManager gsm;
    private transient SpriteBatch batch;
    private Catapult catapult;
    private ArrayList<Pig> pigs;
    private ArrayList<Block> blocks;
    private LinkedList<Bird> birdQueue;
    private final ArrayList<Bird> launchedBirds;// Birds in queue

    private transient PhysicsBodyDestructor bodyDestructor;

    private transient Stage stage;
    private transient Skin skin;
    private transient ImageButton  pauseButton;
    private transient ImageButton menuButton;
    private boolean isPopupActive = false;
    private final transient Viewport viewport;
    private Table popupTable;
    private boolean isDragging;
    private Vector2 slingOrigin;
    private Vector2 dragPosition;
    private final Vector2 initialPosition;
    private Vector2 releaseVelocity = new Vector2();
    private transient ShapeRenderer shapeRenderer; // To render trajectory
    private transient OrthographicCamera camera;
    private static final float LAUNCH_SPEED_MULTIPLIER = 20.0f; // Adjust this to control launch speed
    public static final float PIXELS_TO_METERS = 100.0f; // Maximum stretch distance
    private static final float SLING_X = 100f; // X position of slingshot
    private static final float SLING_Y = 200f; // Y position of slingshot
    private static final float MAX_STRETCH_DISTANCE = 60.0f;  // Maximum stretch in pixels
    private Vector2 birdPosition;
    private Vector2 birdVelocity;
    private static final float DRAG_AREA_X = 0f; // X position of the confined area
    private static final float DRAG_AREA_Y = 150f; // Y position of the confined area
    private static final float DRAG_AREA_WIDTH = 200f; // Width of the confined area
    private static final float DRAG_AREA_HEIGHT = 200f;


    // Additional fields for stretch mechanics
    private float currentStretchDistance;
    private Vector2 frontBandPos;
    private Vector2 backBandPos;
    private int score = 0;
    private transient BitmapFont scoreFont;
    private static final int PIG_HIT_SCORE = 1000;
    private static final int BLOCK_HIT_SCORE = 100;
    private static final int PIG_DESTROY_SCORE = 1000;
    private static final int BLOCK_DESTROY_SCORE = 250;
    private static final int SCORE_THRESHOLD = 500;
    private float screenShakeTime = 0;

    private float screenShakeIntensity = 0;

    private static final float SHAKE_DURATION = 0.5f;

    private static final float SHAKE_INTENSITY = 5.0f;
    private int currentLevel;

    private transient Texture backgroundTexture;
    private transient Texture pauseTexture;
    private transient Texture menuTexture;



    // Popup elements
    private transient Image popupBackground;
    private transient ImageButton resumePopupButton, levelsPopupButton;
    private static boolean show=true;

    public GameScreen2(GameStateManager gsm) {

        this.gsm = gsm;

        this.camera = new OrthographicCamera(800, 480);

        camera.setToOrtho(false);

        camera.zoom = 0.7f;  // Zoom out (increase number to zoom out more)

        camera.position.set(540, 340, 0);  // Shifted more to left


        camera.update();
        batch = new SpriteBatch();
        launchedBirds = new ArrayList<>();
        this.viewport = new FitViewport(1000, 600);
        world = new World(new Vector2(0, -9.8f), true);
        this.bodyDestructor = new PhysicsBodyDestructor(world);
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(1.5f);
        currentLevel = gsm.getCurrentLevel();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Object userDataA = fixtureA.getBody().getUserData();
                Object userDataB = fixtureB.getBody().getUserData();

                // Bird colliding with Pig
                if (userDataA instanceof Bird && userDataB instanceof Pig) {
                    handleBirdPigCollision((Bird) userDataA, (Pig) userDataB);
                } else if (userDataA instanceof Pig && userDataB instanceof Bird) {
                    handleBirdPigCollision((Bird) userDataB, (Pig) userDataA);
                }

                // Bird colliding with Block
                if (userDataA instanceof Bird && userDataB instanceof Block) {
                    handleBirdBlockCollision((Bird) userDataA, (Block) userDataB);
                } else if (userDataA instanceof Block && userDataB instanceof Bird) {
                    handleBirdBlockCollision((Bird) userDataB, (Block) userDataA);
                }

                // Block colliding with Pig
                if (userDataA instanceof Block && userDataB instanceof Pig) {
                    handleBlockPigCollision((Block) userDataA, (Pig) userDataB);
                } else if (userDataA instanceof Pig && userDataB instanceof Block) {
                    handleBlockPigCollision((Block) userDataB, (Pig) userDataA);
                }
            }



            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // Calculate collision force
                float maxImpulse = 0f;
                for (float imp : impulse.getNormalImpulses()) {
                    maxImpulse = Math.max(maxImpulse, imp);
                }
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                Object userDataA = fixtureA.getBody().getUserData();
                Object userDataB = fixtureB.getBody().getUserData();

                // Apply damage based on collision force
                if (maxImpulse > 1.0f) {  // Threshold for damage
                    int damage = (int)(maxImpulse * 10);  // Convert impulse to damage

                    if (userDataA instanceof Pig) {
                        ((Pig)userDataA).takeDamage(damage);
                    }
                    if (userDataB instanceof Pig) {
                        ((Pig)userDataB).takeDamage(damage);
                    }
                    if (userDataA instanceof Block) {
                        ((Block)userDataA).takeDamage(damage);
                    }
                    if (userDataB instanceof Block) {
                        ((Block)userDataB).takeDamage(damage);
                    }
                }
            }
        });

        slingOrigin = new Vector2(SLING_X, SLING_Y);
        initialPosition = new Vector2(SLING_X, SLING_Y);
        frontBandPos = new Vector2(slingOrigin.x + 20, slingOrigin.y + 10); // Front anchor point
        backBandPos = new Vector2(slingOrigin.x - 20, slingOrigin.y + 10);  // Back anchor point
        currentStretchDistance = 0;

        shapeRenderer = new ShapeRenderer();




        // ShapeRenderer for trajectory
        shapeRenderer = new ShapeRenderer();
        birdQueue = new LinkedList<>();

// Create birds at the initial position
        Bird blueBird = new Bird(
            initialPosition.x / PIXELS_TO_METERS,
            initialPosition.y / PIXELS_TO_METERS,
            40, 40, Bird.BirdType.BLUE, world
        );
        Bird yellowBird = new Bird(
            initialPosition.x / PIXELS_TO_METERS,
            initialPosition.y / PIXELS_TO_METERS,
            60, 60, Bird.BirdType.YELLOW, world
        );
        Bird blackBird = new Bird(  // Added black bird

            initialPosition.x / PIXELS_TO_METERS,

            initialPosition.y / PIXELS_TO_METERS,

            50, 50, Bird.BirdType.BLACK, world

        );
        Bird redBird = new Bird(
            initialPosition.x / PIXELS_TO_METERS,
            initialPosition.y / PIXELS_TO_METERS,
            50, 50, Bird.BirdType.RED, world
        );

// Ensure all birds start as static bodies
        blueBird.getBody().setType(BodyDef.BodyType.StaticBody);
        yellowBird.getBody().setType(BodyDef.BodyType.StaticBody);
        redBird.getBody().setType(BodyDef.BodyType.StaticBody);
        blackBird.getBody().setType(BodyDef.BodyType.StaticBody);  // Added for black bird

// Clear existing queue and add birds
        birdQueue.clear();
        birdQueue.add(blueBird);
        birdQueue.add(yellowBird);
        birdQueue.add(redBird);
        birdQueue.add(blueBird);
        birdQueue.add(redBird);
        birdQueue.add(yellowBird);
        birdQueue.add(redBird);


        catapult = new Catapult(100, 175, 120, 100);// Red bird last

        // Initialize first bird
        if (!birdQueue.isEmpty()) {
            currentBird = birdQueue.getFirst();
            currentBird.setPosition(
                initialPosition.x / PIXELS_TO_METERS,
                initialPosition.y / PIXELS_TO_METERS
            );
            currentBird.getBody().setType(BodyDef.BodyType.StaticBody);
            catapult.setBird(currentBird);
        }
        pigs = new ArrayList<>();
        pigs.add(new Pig(world, 605, 180, 50, 50, 150, bodyDestructor));  // Large pig
        pigs.add(new Pig(world, 680, 180, 30, 30, 150, bodyDestructor));


        // Initialize blocks with different materials
        blocks = new ArrayList<>();

        blocks.add(new Block(world,585, 180, 20, 100, Block.BlockType.VERT,50, bodyDestructor));
        blocks.add(new Block(world,650, 180, 20, 100, Block.BlockType.VERT,50, bodyDestructor));
        blocks.add(new Block(world,715, 180, 20, 100, Block.BlockType.VERT,50, bodyDestructor));
        blocks.add(new Block(world,660, 210, 70, 15, Block.BlockType.HORI,50, bodyDestructor));
        blocks.add(new Block(world,675, 220, 40, 40, Block.BlockType.WOODEN,50, bodyDestructor));
        // Load background and catapult textures
        backgroundTexture = new Texture("background.png");
        Image backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Texture pauseTexture = new Texture("pause.png");
        pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));
        pauseButton.setSize(120, 120);
        pauseButton.setPosition(1300, 700);
        stage.addActor(pauseButton);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPopup();
            }
        });


        // Menu button on the popup
        Texture menuTexture = new Texture("menu.png");
        menuButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuTexture)));
        menuButton.setSize(100, 100);
        menuButton.setPosition(20, 700);
        stage.addActor(menuButton);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showTablePopup();  // Show Win/Lose/Restart table
            }
        });
        slingOrigin = new Vector2(150, 260); // Set the slingshot origin
        releaseVelocity = new Vector2();
        Gdx.input.setInputProcessor(stage);

        isPopupActive = false;
//        initializeTransientFields();

    }

    private void handleBirdPigCollision(Bird bird, Pig pig) {
        // Apply damage to pig based on bird's velocity
        Vector2 velocity = bird.getBody().getLinearVelocity();
        float speed = velocity.len();
        int damage = (int)(speed * 10);  // Convert speed to damage
        pig.takeDamage(damage);
        score += PIG_HIT_SCORE;

        // Check if pig is destroyed
        if (pig.isDestroyed()) {
            score += PIG_DESTROY_SCORE;
        }
    }

    private void handleBirdBlockCollision(Bird bird, Block block) {
        // Apply damage to block based on bird's velocity
        Vector2 velocity = bird.getBody().getLinearVelocity();
        float speed = velocity.len();
        int damage = (int)(speed * 5);  // Convert speed to damage
        block.takeDamage(damage);

        score += BLOCK_HIT_SCORE;

        // Check if block is destroyed
        if (block.isDestroyed()) {
            score += BLOCK_DESTROY_SCORE;
        }
    }

    private void handleBlockPigCollision(Block block, Pig pig) {
        score += BLOCK_HIT_SCORE;
        // Apply damage to pig based on block's velocity
        Vector2 velocity = block.getBody().getLinearVelocity();
        float speed = velocity.len();
        int damage = (int)(speed * 7);  // Convert speed to damage
        pig.takeDamage(damage);
        if (pig.isDestroyed()) {
            score += PIG_DESTROY_SCORE;
        }
    }


    private void showPopup() {
        // Popup background
        Texture popupTexture = new Texture("popup_background1.png");
        popupBackground = new Image(new TextureRegionDrawable(new TextureRegion(popupTexture)));
        popupBackground.setSize(1920, 1080);
        popupBackground.setPosition(0, 0);
        stage.addActor(popupBackground);

        // Resume button
        Texture resumeTexture = new Texture("resume.png");
        resumePopupButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(resumeTexture)));
        resumePopupButton.setSize(150, 150);
        resumePopupButton.setPosition(400, 200);
        stage.addActor(resumePopupButton);

        resumePopupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hidePopup();  // Resume game
            }
        });

        // Levels button
        Texture levelsTexture = new Texture("exit.png");
        levelsPopupButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(levelsTexture)));
        levelsPopupButton.setSize(120, 120);
        levelsPopupButton.setPosition(900, 200);
        stage.addActor(levelsPopupButton);

        levelsPopupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveState("save2.ser");
                hidePopup();
                gsm.setScreen("levvels");
            }
        });
    }

    private void showTablePopup() {
        popupTable = new Table();
        popupTable.setFillParent(true);  // Center the table
        popupTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("popup_background1.png"))));

        Texture restartTexture = new Texture("restart_button.png");
        ImageButton restartButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(restartTexture)));

        // Set size properly within the table
        popupTable.row();
        popupTable.add(restartButton).size(150, 75).pad(10);  // Adjust size here


        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart button clicked!");
                hideTablePopup();
                gsm.setScreen("level" + currentLevel);
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
    private void handleInput() {
        if (isPopupActive) return;

        // First handle special abilities
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            for (Bird bird : new ArrayList<>(launchedBirds)) {
                if (bird.canExplode()) {

                    handleExplosion(bird);

                    return;

                }
                // Try yellow bird boost first
                if (bird.canBoost()) {
                    bird.boost();
                    return;
                }
                // Then try blue bird split
                else if (bird.canSplit()) {
                    Bird[] splitBirds = bird.split();
                    if (splitBirds != null) {
                        launchedBirds.remove(bird);
                        launchedBirds.addAll(Arrays.asList(splitBirds));
                        return;
                    }
                }
            }
        }

        // Then handle launching
        if (currentBird != null) {
            if (Gdx.input.isTouched()) {
                handleDragging();
            } else if (isDragging) {
                handleLaunch();
            }
        }
    }
    private void handleExplosion(Bird bird) {

        Vector2 explosionCenter = bird.getPosition();



        // Damage all pigs within radius

        for (Pig pig : pigs) {

            Vector2 pigPos = pig.getPosition();

            float distance = pigPos.dst(explosionCenter);

            if (distance <= bird.getExplosionRadius()) {

                float damage = bird.getExplosionDamage() * (1 - distance / bird.getExplosionRadius());

                pig.takeDamage((int)damage);

            }

        }



        // Damage all blocks within radius

        for (Block block : blocks) {

            Vector2 blockPos = block.getPosition();

            float distance = blockPos.dst(explosionCenter);

            if (distance <= bird.getExplosionRadius()) {

                float damage = bird.getExplosionDamage() * (1 - distance / bird.getExplosionRadius());

                block.takeDamage((int)damage);

            }

        }



        // Trigger screen shake

        screenShakeTime = SHAKE_DURATION;

        screenShakeIntensity = SHAKE_INTENSITY;



        // Remove the exploded bird

        bird.explode();

        launchedBirds.remove(bird);

    }



    private void handleLaunch() {
        isDragging = false;

        // Scale launch velocity appropriately
        Vector2 launchVelocity = new Vector2(releaseVelocity).scl(LAUNCH_SPEED_MULTIPLIER);

        // Convert to Box2D coordinates and scale
        Vector2 scaledVelocity = new Vector2(
            launchVelocity.x / PIXELS_TO_METERS,
            launchVelocity.y / PIXELS_TO_METERS
        );

        // Set the bird to dynamic mode and launch
        currentBird.getBody().setType(BodyDef.BodyType.DynamicBody);
        currentBird.launch(scaledVelocity);

        // Add to launched birds list
        launchedBirds.add(currentBird);

        // Setup next bird
        setupNextBird();
    }

    private void setupNextBird() {
        if (!birdQueue.isEmpty()) {
            currentBird = birdQueue.removeFirst();
            currentBird.setPosition(
                initialPosition.x / PIXELS_TO_METERS,
                initialPosition.y / PIXELS_TO_METERS
            );
            currentBird.getBody().setType(BodyDef.BodyType.StaticBody);
            currentBird.resetVelocity();
            catapult.setBird(currentBird);
        } else {
            currentBird = null;
        }
    }

    private void renderTrajectoryPoints(ShapeRenderer shapeRenderer, Vector2 startPos, Vector2 velocity) {
        if (!isDragging || startPos == null ) return;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        float timeStep = 0.1f;
        Vector2 position = new Vector2();
        Vector2 scaledVelocity = new Vector2(velocity).scl(1 / LAUNCH_SPEED_MULTIPLIER);

        for (float t = 0; t < 2.0f; t += timeStep) {
            // Projectile motion equations
            position.x = startPos.x + scaledVelocity.x * t * LAUNCH_SPEED_MULTIPLIER;
            position.y = startPos.y + scaledVelocity.y * t * LAUNCH_SPEED_MULTIPLIER +
                0.5f * world.getGravity().y * t * t;

            // Stop if prediction goes below ground or off-screen
            if (position.y < 0 || position.x > Gdx.graphics.getWidth()*1.3f) break;

            shapeRenderer.circle(position.x, position.y, 2);
        }

        shapeRenderer.end();
    }

    private void handleDragging() {
        if (!isDragging) {
            isDragging = true;
            currentBird.getBody().setType(BodyDef.BodyType.StaticBody);
        }

        // Get touch position
        float touchX = Gdx.input.getX();
        float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
        Vector2 touchPos = new Vector2(touchX, touchY);
        if (touchPos.x < DRAG_AREA_X || touchPos.x > DRAG_AREA_X + DRAG_AREA_WIDTH ||
            touchPos.y < DRAG_AREA_Y || touchPos.y > DRAG_AREA_Y + DRAG_AREA_HEIGHT) {
            isDragging=false;
            return; // Do not allow dragging outside the confined area
        }

        // Calculate drag vector
        Vector2 dragVector = new Vector2(touchPos).sub(slingOrigin);

        // Limit stretch distance
        float dragDistance = dragVector.len();
        if (dragDistance > MAX_STRETCH_DISTANCE) {
            dragVector.nor().scl(MAX_STRETCH_DISTANCE);
        }

        // Update bird position and store launch data
        Vector2 newBirdPos = new Vector2(slingOrigin).add(dragVector);
        currentBird.setPosition(
            newBirdPos.x / PIXELS_TO_METERS,
            newBirdPos.y / PIXELS_TO_METERS
        );

        dragPosition = newBirdPos;
        releaseVelocity.set(-dragVector.x, -dragVector.y);

        // Update catapult visuals
        catapult.stretch(newBirdPos);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        if (screenShakeTime > 0) {
            screenShakeTime -= delta;
            float shakeOffsetX = (float)(Math.random() * 2 - 1) * screenShakeIntensity;
            float shakeOffsetY = (float)(Math.random() * 2 - 1) * screenShakeIntensity;
            camera.position.set(
                Gdx.graphics.getWidth() / 2 + shakeOffsetX,
                Gdx.graphics.getHeight() / 2 + shakeOffsetY,
                0
            );
        } else {
            camera.zoom =0.7f;  // Maintain zoom level
            camera.position.set(540, 340, 0);  // Maintain left shift
        }
        camera.update();

        // Update physics world
        float timeStep = 1 / 60f;
        world.step(timeStep, 6, 2);
        bodyDestructor.processBodies();

        // Handle input BEFORE rendering
        handleInput();

        // Render game elements
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);

        // Render catapult first
        catapult.render(batch);

        // Render launched birds
        for (Bird bird : launchedBirds) {
            if (bird.isActive()) {
                bird.update(delta);
                bird.render(batch);
            }
        }

        // Render only the current bird
        if (currentBird != null) {
            currentBird.update(delta);
            currentBird.render(batch);
        }

        // Render pigs and blocks
        for (Pig pig : pigs) pig.render(batch);
        for (Block block : blocks) block.render(batch);
        scoreFont.draw(batch, "Score: " + score, 820, 580);

        batch.end();

        // Draw trajectory prediction
        if (isDragging && currentBird != null) {
            renderTrajectoryPoints(shapeRenderer, dragPosition, releaseVelocity);
        }

        // Draw UI stage
        stage.act(delta);
        stage.draw();

        // Check win/lose conditions only if the game is not paused
        if (!isPopupActive) {
            if (checkWinCondition()) {
                gsm.setScreen("win");
            } else if (checkLoseCondition()) {
                System.out.println("Entering lose screen");
                gsm.setScreen("lose");
            }
        }
    }

    private boolean checkWinCondition() {
        // Win if score reaches threshold and all pigs are destroyed
        boolean allPigsDestroyed = pigs.stream().allMatch(Pig::isDestroyed);
        return score >= SCORE_THRESHOLD && allPigsDestroyed;
    }

    private boolean checkLoseCondition() {
        // Check if no more birds are available and the current bird is null
        boolean noMoreBirds = birdQueue.isEmpty() && currentBird == null;

        // Check if not all pigs are destroyed
        boolean pigsSurvived = pigs.stream().anyMatch(pig -> !pig.isDestroyed());

        // Check if score is below threshold
        boolean lowScore = score <= SCORE_THRESHOLD;

        return noMoreBirds && (pigsSurvived || lowScore);
    }

    @Override
    public void show() {
        if(show){
            Gdx.input.setInputProcessor(stage);
            if (!birdQueue.isEmpty()) {
                currentBird = birdQueue.removeFirst();
                catapult.setBird(currentBird);
            }
        }

    }

    @Override
    public void resize(int width, int height) {

        // Update viewport to maintain 1000x600 aspect ratio

        float aspectRatio = 800f / 480f;  // Changed from 1000/600

        float w = width;

        float h = w / aspectRatio;

        if (h > height) {

            h = height;

            w = h * aspectRatio;

        }
        if (camera == null) {
            camera = new OrthographicCamera(800, 480);
            camera.setToOrtho(false);
        }

        camera.zoom = 0.7f;  // Maintain zoom level

        camera.position.set(540, 340, 0);  // Maintain left shift

        camera.update();

        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {}

    public void resume() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                if (fixtureA.getBody().getUserData() instanceof Pig && fixtureB.getBody().getUserData() instanceof Bird) {
                    Pig pig = (Pig) fixtureA.getBody().getUserData();
                    pig.takeDamage(50);
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }
    public void saveState(String filePath) {
        try {
            GameState state = new GameState();
            state.currentLevel = currentLevel;
            state.score = score;

            // Save pig states
            state.pigs.clear();
            for (Pig pig : pigs) {
                if (!pig.isDestroyed()) {
                    Vector2 pos = pig.getPosition();
                    state.pigs.add(new GameState.PigState(pos.x, pos.y, pig.getHealth(), pig.getWidth(), pig.getHeight()));
                }
            }

            // Save block states
            state.blocks.clear();
            for (Block block : blocks) {
                if (!block.isDestroyed()) {
                    Vector2 pos = block.getPosition();
                    state.blocks.add(new GameState.BlockState(pos.x, pos.y, block.getType().name(), block.getHealth(), block.getWidth(), block.getHeight()));
                }
            }

            // Save bird states
            state.birds.clear();
            for (Bird bird : birdQueue) {
                Vector2 pos = bird.getPosition();
                state.birds.add(new GameState.BirdState(bird.getType().name(), pos.x, pos.y, false));
            }
            for (Bird bird : launchedBirds) {
                Vector2 pos = bird.getPosition();
                state.birds.add(new GameState.BirdState(bird.getType().name(), pos.x, pos.y, true));
            }

            // Save to file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(state);
            }
        } catch (IOException e) {
            Gdx.app.error("GameScreen", "Error saving game state: " + e.getMessage());
        }
    }

    public static GameScreen2 loadState(String filePath, GameStateManager gsm) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            GameState state = (GameState) ois.readObject();

            GameScreen2 gameScreen = new GameScreen2(gsm);
            gameScreen.currentLevel = state.currentLevel;
            gameScreen.score = state.score;

            // Restore pig positions
            gameScreen.pigs.clear();
            for (GameState.PigState pigState : state.pigs) {
                Pig pig = new Pig(gameScreen.world, pigState.x, pigState.y, pigState.width, pigState.height, pigState.health, gameScreen.bodyDestructor);
                gameScreen.pigs.add(pig);
            }

            // Restore block positions
            gameScreen.blocks.clear();
            for (GameState.BlockState blockState : state.blocks) {
                Block block = new Block(gameScreen.world, blockState.x, blockState.y, blockState.width, blockState.height, Block.BlockType.valueOf(blockState.type), blockState.health, gameScreen.bodyDestructor);
                gameScreen.blocks.add(block);
            }

            // Restore bird queue and launched birds
            gameScreen.birdQueue.clear();
            gameScreen.launchedBirds.clear();
            for (GameState.BirdState birdState : state.birds) {
                Bird.BirdType birdType = Bird.BirdType.valueOf(birdState.type);
                Bird bird = new Bird(birdState.x, birdState.y, 50, 50, birdType, gameScreen.world);
                bird.getBody().setType(BodyDef.BodyType.StaticBody);
                if (birdState.launched) {
                    gameScreen.launchedBirds.add(bird);
                } else {
                    gameScreen.birdQueue.add(bird);
                }
            }

            return gameScreen;
        } catch (IOException | ClassNotFoundException e) {
            Gdx.app.error("GameScreen", "Error loading game state: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        catapult.dispose();
        for (Bird bird : birdQueue) bird.dispose();
        for (Pig pig : pigs) pig.dispose();
        for (Block block : blocks) block.dispose();
        for (Bird bird : launchedBirds) {
            bird.dispose();
        }
        stage.dispose();
        skin.dispose();
    }
}

