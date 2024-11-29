// Java
package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Block implements Serializable {
    public enum BlockType {
        WOODEN, STEEL, ICE, HORI, VERT, ICETRI, LONG, SMALL
    }

    private final Vector2 position;
    private final float width, height;
    private transient Sprite sprite;
    private int health;
    private boolean destroyed;
    private transient Body body;
    private transient World world;
    public static final float PIXELS_TO_METERS = 100.0f;
    private transient PhysicsBodyDestructor bodyDestructor;
    private BlockType type;

    public Block(World world, float x, float y, float width, float height, BlockType type, int initialHealth, PhysicsBodyDestructor destructor) {
        this.bodyDestructor = destructor;
        this.world = world;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.health = initialHealth;
        this.destroyed = false;
        this.type = type;

        initializeSprite(type);
        createBody();
    }

    private void initializeSprite(BlockType type) {
        Texture blockTexture = loadTexture(type);
        sprite = new Sprite(blockTexture);
        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
    }


    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Body getBody() {
        return body;
    }
    public BlockType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x / PIXELS_TO_METERS, position.y / PIXELS_TO_METERS);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / (2 * PIXELS_TO_METERS), height / (2 * PIXELS_TO_METERS));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        box.dispose();
    }

    public void reinitialize(World world, PhysicsBodyDestructor bodyDestructor) {
        this.world = world;
        this.bodyDestructor = bodyDestructor;

        createBody();
    }

    public void takeDamage(int damage) {
        if (!destroyed) {
            health -= damage;
            if (health <= 0) {
                destroy();
            }
        }
    }

    private void destroy() {
        destroyed = true;
        if (body != null) {
            bodyDestructor.queueBodyForDestruction(body);
            body = null;
        }
    }

    public Vector2 getPosition() {
        if (body != null) {
            Vector2 bodyPos = body.getPosition();
            return new Vector2(bodyPos.x * PIXELS_TO_METERS, bodyPos.y * PIXELS_TO_METERS);
        }
        return position;
    }

    public void update() {
        if (!destroyed && body != null) {
            Vector2 pos = getPosition();
            sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        }
    }

    private Texture loadTexture(BlockType type) {
        switch (type) {
            case WOODEN:
                return new Texture("TNT.png");
            case STEEL:
                return new Texture("steel.png");
            case ICE:
                return new Texture("ice-block.png");
            case HORI:
                return new Texture("hori.png");
            case VERT:
                return new Texture("vert.png");
            case ICETRI:
                return new Texture("icetri.png");
            case LONG:
                return new Texture("longsteel.png");
            case SMALL:
                return new Texture("small.png");
            default:
                return new Texture("default.png");
        }
    }

    public void render(SpriteBatch batch) {
        if (!destroyed && sprite != null) {
            sprite.draw(batch);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void dispose() {
        if (sprite != null && sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
    }
}
