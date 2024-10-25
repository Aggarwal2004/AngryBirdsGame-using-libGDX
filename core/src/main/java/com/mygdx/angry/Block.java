package com.mygdx.angry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Block {
    // Enum to represent different types of blocks
    public enum BlockType {
        WOODEN, STEEL, ICE, HORI, VERT, ICETRI,LONG,SMALL
    }

    private final Vector2 position;  // Position of the block
    private final float width, height;  // Dimensions of the block
    private final Sprite sprite;  // Sprite for rendering the block

    public Block(float x, float y, float width, float height, BlockType type) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;

        // Load the appropriate texture based on block type
        Texture blockTexture = loadTexture(type);
        sprite = new Sprite(blockTexture);

        // Set the size and position of the sprite
        sprite.setSize(width, height);
        sprite.setPosition(position.x, position.y);
    }

    // Load texture based on the type of block
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
                return new Texture("smallsteel.png");
            default:
                return new Texture("TNT.png");  // Default block texture
        }
    }

    // Render the block using SpriteBatch
    public void render(SpriteBatch batch) {
        sprite.draw(batch);  // Draw the block sprite
    }

    // Update block's position (optional)
    public void setPosition(float x, float y) {
        position.set(x, y);
        sprite.setPosition(position.x, position.y);  // Sync sprite's position
    }

    // Dispose the texture to avoid memory leaks
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
