package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.io.Serializable;

public class Bird implements Serializable {


    public enum BirdType {
        RED, YELLOW, BLUE, BLACK  // Added BLACK to bird types
    }
    private final Vector2 position;
    private boolean launched;

    private transient Body body; // Box2D physics body
    private boolean isLaunched = false;
    private transient Sprite sprite;
    private float width, height;
    private boolean isFlying;
    private static final float GRAVITY = -3.0f;
    public static final float PIXELS_TO_METERS = 100.0f;
    private boolean isActive = true;// Sprite to render the bird
    private static final float BLUE_BIRD_SPLIT_ANGLE = 30f; // Angle between split birds
    private boolean hasSplit = false;
    private boolean hasUsedSpecial = false;  // Track if special ability was used
    private static final float YELLOW_SPEED_MULTIPLIER = 2.0f;  // Speed boost factor
    private static final float EXPLOSION_RADIUS = 200f;
    private static final float EXPLOSION_DAMAGE = 150f;

    // World reference for Box2D
    private transient World world;
    private final BirdType type;

    public void reinitialize(World world) {
        // Reinitialize the bird with the world
        this.world = world;
        createBody();
        initializeSprite();
        // Additional reinitialization logic if needed
    }
    private void initializeSprite() {
        sprite = new Sprite(new Texture("bird_" + type.name().toLowerCase() + ".png"));
        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
    }
    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(width / 2 / GameScreen.PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        circle.dispose();
    }

    // Constructor with Box2D integration
    public Bird(float x, float y, float width, float height, BirdType type, World world) {
        this.world = world;
        this.position = new Vector2(x, y);
        this.type = type;

        // Set up the sprite for the bird
        sprite = new Sprite(new Texture("bird_" + type.name().toLowerCase() + ".png"));
        sprite.setSize(width, height);
        sprite.setPosition(x, y);
        this.width = width;
        this.height = height;

        // Create a Box2D body for the bird
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Start as static
        bodyDef.position.set(x / PIXELS_TO_METERS, y / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius((width / 2) / PIXELS_TO_METERS); // Convert to meters
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = type == BirdType.YELLOW ? 2.0f : 1.0f;  // Make yellow bird heavier
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Set user data for collision detection
        body.setUserData(this);
        body.setBullet(true);  // Enable continuous collision detection for all birds
    }

    public Bird[] split() {
        // Only blue birds can split, and only once
        if (type != BirdType.BLUE || hasSplit || !isLaunched) {
            return null;
        }

        hasSplit = true;
        Bird[] splitBirds = new Bird[3];

        // Get current velocity and position
        Vector2 currentVelocity = body.getLinearVelocity();
        Vector2 currentPos = body.getPosition();
        float speed = currentVelocity.len();

        // Base angle from current velocity
        float baseAngle = (float) Math.atan2(currentVelocity.y, currentVelocity.x);
        float[] angles = {0, -30, 30}; // Center, left, right angles

        for (int i = 0; i < 3; i++) {
            // Convert the current angle to radians and add to base angle
            float splitAngle = baseAngle + angles[i] * MathUtils.degreesToRadians;

            // Create new bird at the EXACT same position as the original
            splitBirds[i] = new Bird(
                currentPos.x,
                currentPos.y,
                sprite.getWidth() * 0.7f,
                sprite.getHeight() * 0.7f,
                BirdType.BLUE,
                world
            );

            // Calculate new velocity based on the split angle
            Vector2 splitVelocity = new Vector2(
                speed * MathUtils.cos(splitAngle),
                speed * MathUtils.sin(splitAngle)
            );

            // Immediately make the bird dynamic and launch it
            splitBirds[i].getBody().setType(BodyDef.BodyType.DynamicBody);
            splitBirds[i].getBody().setTransform(currentPos, splitAngle);
            splitBirds[i].launch(splitVelocity);
            splitBirds[i].hasSplit = true;
            splitBirds[i].isLaunched = true;
        }

        // Deactivate original bird
        isActive = false;
        body.setActive(false);

        return splitBirds;
    }

    // Method to check if splitting is possible
    public boolean canSplit() {
        return type == BirdType.BLUE && isLaunched && !hasSplit;
    }

    // Launch the bird with a given direction and force
    public void launch(Vector2 velocity) {
        switch (type) {
            case YELLOW:
                launchYellowBird(velocity);
                break;
            case BLACK:
                launchBlackBird(velocity);
                break;
            default:
                launchNormalBird(velocity);
                break;
        }
    }

    private void launchNormalBird(Vector2 velocity) {
        setupBasicPhysics(velocity);
        body.setLinearDamping(0.1f);
        body.setAngularDamping(0.1f);
    }

    private void launchYellowBird(Vector2 velocity) {
        setupBasicPhysics(velocity);
        // No additional physics modifiers for yellow bird to ensure proper trajectory
        body.setLinearDamping(0.1f);  // Same as normal bird
        body.setAngularDamping(0.1f);  // Same as normal bird
    }

    private void launchBlackBird(Vector2 velocity) {
        setupBasicPhysics(velocity);
        // Keep same base physics as other birds for consistent trajectory
        body.setLinearDamping(0.1f);
        body.setAngularDamping(0.1f);
    }

    private void setupBasicPhysics(Vector2 velocity) {
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setGravityScale(1.0f);
        body.setAwake(true);
        body.setBullet(true);
        body.setActive(true);
        body.setFixedRotation(false);

        // Ensure velocity is properly scaled
        Vector2 scaledVelocity = new Vector2(velocity);
        body.setLinearVelocity(scaledVelocity);
        body.setAngularVelocity(0);

        isLaunched = true;
        isFlying = true;
        isActive = true;
        hasUsedSpecial = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void resetVelocity() {
        body.setLinearVelocity(0, 0);
        body.setAngularVelocity(0);
    }

    public void update(float delta) {
        if (body != null) {

            if (isLaunched && !body.isActive()) {
                body.setActive(true);
            }

            Vector2 bodyPos = body.getPosition();
            position.set(
                bodyPos.x * PIXELS_TO_METERS,
                bodyPos.y * PIXELS_TO_METERS
            );

            // Sync sprite position with physics body (centering)
            sprite.setPosition(
                position.x - sprite.getWidth() / 2,
                position.y - sprite.getHeight() / 2
            );

            // Update rotation if flying
            if (isLaunched) {
                Vector2 velocity = body.getLinearVelocity();

                // Update sprite rotation based on velocity
                if (velocity.len2() > 0.1f) {
                    float angle = (float) Math.atan2(velocity.y, velocity.x);
                    sprite.setRotation(angle * MathUtils.radiansToDegrees);
                }
            }
        }
    }



    // Render the bird using SpriteBatch
    public void render(SpriteBatch batch) {
        if (body != null) {
            // Always update sprite position based on physics body
            Vector2 pos = body.getPosition();
            sprite.setPosition(
                pos.x * PIXELS_TO_METERS - sprite.getWidth()/2,
                pos.y * PIXELS_TO_METERS - sprite.getHeight()/2
            );

            // Update rotation based on velocity when flying
            if (isFlying) {
                Vector2 velocity = body.getLinearVelocity();
                if (velocity.len2() > 0.1f) {
                    float angle = (float) Math.atan2(velocity.y, velocity.x);
                    sprite.setRotation(angle * MathUtils.radiansToDegrees);
                }
            }

            // Always draw the sprite
            sprite.draw(batch);
        }
    }

    public BirdType getType() {
        return type;
    }

    // Optional: Move the bird by setting a new position
    public void setPosition(float x, float y) {
        // x and y are now in meters (Box2D coordinates)
        position.set(
            x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            y * PIXELS_TO_METERS - sprite.getHeight() / 2
        );

        sprite.setPosition(position.x, position.y);

        if (body != null) {
            body.setTransform(new Vector2(x, y), body.getAngle());
        }
    }

    public void setPosition(Vector2 position) {
        // x and y are now in meters (Box2D coordinates)
        this.position.set(
            position.x * PIXELS_TO_METERS - sprite.getWidth() / 2,
            position.y * PIXELS_TO_METERS - sprite.getHeight() / 2
        );

        sprite.setPosition(this.position.x, this.position.y);

        if (body != null) {
            body.setTransform(position, body.getAngle());
        }
    }

    // Dispose the texture to avoid memory leaks
    public void dispose() {
        if (sprite != null && sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
    }

    // Getters for the bird's body
    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bird other = (Bird) obj;
        return this.body == other.body;
    }

    public void draw(SpriteBatch batch, Texture texture) {
        if (!launched || position.y > 0) { // Prevent drawing if it goes below ground
            batch.draw(texture, position.x, position.y);
        }
    }

    public boolean canBoost() {
        return type == BirdType.YELLOW && isLaunched && !hasUsedSpecial;
    }

    public void boost() {
        if (!canBoost()) return;

        Vector2 currentVelocity = body.getLinearVelocity();
        float currentSpeed = currentVelocity.len();

        // Keep direction but increase speed
        Vector2 newVelocity = new Vector2(currentVelocity).nor().scl(currentSpeed * YELLOW_SPEED_MULTIPLIER);
        body.setLinearVelocity(newVelocity);

        hasUsedSpecial = true;
    }

    public boolean canExplode() {
        return type == BirdType.BLACK && isLaunched && !hasUsedSpecial;
    }

    public void explode() {
        if (!canExplode()) return;
        hasUsedSpecial = true;
        isActive = false;
        if (body != null) {
            body.setActive(false);
        }
    }

    public float getExplosionRadius() {
        return EXPLOSION_RADIUS;
    }

    public float getExplosionDamage() {
        return EXPLOSION_DAMAGE;
    }
}
