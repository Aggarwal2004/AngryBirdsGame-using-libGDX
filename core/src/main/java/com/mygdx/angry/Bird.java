package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bird {
    public enum BirdType {
        RED, YELLOW, BLUE
    }

    private final Vector2 position;  // Bird's position
    private final Sprite sprite;     // Sprite to render the bird

    public Bird(float x, float y, float width, float height, BirdType type) {
        // Load the bird texture based on the type
        Texture birdTexture = getTextureForType(type);

        // Create a sprite using the loaded texture
        sprite = new Sprite(birdTexture);

        // Set initial position and dynamic size of the bird sprite
        position = new Vector2(x, y);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);  // Use the provided dynamic size
    }

    // Helper method to return the appropriate texture based on bird type
    private Texture getTextureForType(BirdType type) {
        switch (type) {
            case RED:
                return new Texture("red-bird.png");
            case YELLOW:
                return new Texture("bird-removebg-preview.png");
            case BLUE:
                return new Texture("Blue_Bird.png");
            default:
                throw new IllegalArgumentException("Unknown bird type: " + type);
        }
    }

    // Render the bird using SpriteBatch
    public void render(SpriteBatch batch) {
        sprite.draw(batch);  // Draw the bird sprite
    }

    // Optional: Move the bird by setting a new position
    public void setPosition(float x, float y) {
        position.set(x, y);
        sprite.setPosition(position.x, position.y);  // Sync the sprite's position
    }

    // Dispose the texture to avoid memory leaks
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
