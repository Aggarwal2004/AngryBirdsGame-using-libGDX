package com.mygdx.angry;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Pig implements Serializable {
    private final Vector2 position;
    private transient World world;

    private transient Sprite sprite;
    private int health;
    private boolean isDestroyed;
    private transient Body body;
    public static final float PIXELS_TO_METERS = 100.0f;
    private int height;
    private int width;
    private transient PhysicsBodyDestructor bodyDestructor;
    // Flag to indicate if the pig is destroyed

    public void reinitialize(World world, PhysicsBodyDestructor bodyDestructor) {
        this.world = world;
        this.bodyDestructor = bodyDestructor;


        initializeSprite(sprite.getWidth(), sprite.getHeight());
        createBody(sprite.getWidth());
    }
    private void initializeSprite(float width, float height) {
        Texture pigTexture = new Texture("pig 2.png");
        sprite = new Sprite(pigTexture);
        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
    }

    private void createBody(float width) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x / PIXELS_TO_METERS, position.y / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(width / (2 * PIXELS_TO_METERS));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        circle.dispose();

    }

    public int getHealth() {
        return health;
    }

    // Constructor to initialize pig with position, size, and health
    public Pig(World world, float x, float y, float width, float height, int initialHealth, PhysicsBodyDestructor destructor) {
        this.bodyDestructor = destructor;
        this.world = world;
        this.position = new Vector2(x, y);
        this.health = initialHealth;
        this.isDestroyed = false;
        this.width = (int) width;
        this.height = (int) height;
        initializeSprite(width, height);
        createBody(width);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // Method to handle damage
    public void takeDamage(int damage) {
        if (!isDestroyed) {  // Only take damage if the pig is not destroyed
            health -= damage;

            // Check if health has dropped to or below zero
            if (health <= 0) {
                die();
            }
        }
    }

    // Method to handle pig's death
    private void die() {
        isDestroyed = true;
        if (body != null) {
            bodyDestructor.queueBodyForDestruction(body);
            body = null;
        }
    }


    public Vector2 getPosition() {
        if (body != null) {
            Vector2 bodyPos = body.getPosition();
            // Convert Box2D coordinates to screen coordinates
            return new Vector2(
                bodyPos.x * PIXELS_TO_METERS,
                bodyPos.y * PIXELS_TO_METERS
            );
        }
        return position;  // Return stored position if body is null
    }


    public void update() {
        if (!isDestroyed && body != null) {
            // Update position from physics body
            Vector2 pos = getPosition();
            sprite.setPosition(
                pos.x - sprite.getWidth()/2,
                pos.y - sprite.getHeight()/2
            );
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    // Render the pig using SpriteBatch
    public void render(SpriteBatch batch) {
        if (!isDestroyed && sprite != null) {  // Added null check for sprite
            sprite.draw(batch);  // Draw the pig sprite
        }
    }

    // Optional: Set the pig's position dynamically
    public void setPosition(float x, float y) {
        position.set(x, y);
        sprite.setPosition(position.x, position.y);  // Sync the sprite's position
    }

    // Method to check if the pig is destroyed
    public boolean isDestroyed() {
        return isDestroyed;
    }

    // Dispose of the pig's texture to avoid memory leaks
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
