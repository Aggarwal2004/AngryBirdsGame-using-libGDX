package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Catapult {
    private final Sprite sprite;  // Sprite to render the catapult

    public Catapult(float x, float y, float width, float height) {
        // Load catapult texture
        Texture catapultTexture = new Texture("catapult-removebg-preview.png");

        // Create a sprite using the texture and set size/position
        sprite = new Sprite(catapultTexture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }

    // Render the catapult sprite
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // Dispose the texture to avoid memory leaks
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
