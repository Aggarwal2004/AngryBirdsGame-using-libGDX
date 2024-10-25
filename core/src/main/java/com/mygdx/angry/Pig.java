package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Pig {
    private final Vector2 position;  // Position of the pig
    private final Sprite sprite;     // Sprite to render the pig

    public Pig(float x, float y, float width, float height) {
        // Load the pig texture and create a sprite
        Texture pigTexture = new Texture("pig 2.png");  // Ensure 'pig 2.png' is in the assets folder
        sprite = new Sprite(pigTexture);

        // Set initial position and size of the pig sprite
        position = new Vector2(x, y);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);  // Adjust the size dynamically
    }

    // Render the pig using SpriteBatch
    public void render(SpriteBatch batch) {
        sprite.draw(batch);  // Draw the pig sprite
    }

    // Optional: Set the pig's position
    public void setPosition(float x, float y) {
        position.set(x, y);
        sprite.setPosition(position.x, position.y);  // Sync the sprite's position
    }


    // Dispose the texture to avoid memory leaks
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
