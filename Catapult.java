package com.mygdx.angry;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Catapult implements Serializable {
    private transient Sprite sprite;  // Sprite to render the catapult
    private final Vector2 anchorPoint; // Fixed position of the catapult
    private Vector2 stretchedPoint; // Drag/stretch point for the rubber band
    private Bird currentBird; // The bird loaded onto the catapult
    private transient Texture rubberBandTexture; // Texture for the rubber band
    private boolean isStretching;
    public static final float PIXELS_TO_METERS = 100.0f; // Tracks if the rubber band is being stretched
    private final float width;
    private final float height;


    public Catapult(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;

        Texture catapultTexture = new Texture("catapult-removebg-preview.png");
        rubberBandTexture = new Texture("rubber_band.png"); // Elastic band texture

        // Create a sprite using the texture and set size/position
        sprite = new Sprite(catapultTexture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);

        // Initialize anchor and stretch points
        anchorPoint = new Vector2(x + width / 2, y + height / 2);
        stretchedPoint = new Vector2(anchorPoint);
        isStretching = false;
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read non-transient fields

        // Reinitialize textures and sprite
        Texture catapultTexture = new Texture("catapult-removebg-preview.png");
        rubberBandTexture = new Texture("rubber_band.png");

        sprite = new Sprite(catapultTexture);
        sprite.setPosition(anchorPoint.x, anchorPoint.y);
        sprite.setSize(width, height); // You'll need to store width and height as non-transient fields
    }

    /**
     * Set the bird on the catapult.
     * The bird will be positioned at the anchor point.
     */
    public void setBird(Bird bird) {
        this.currentBird = bird;
        if (currentBird != null) {
            // Position bird at the anchor point
            currentBird.setPosition(anchorPoint.x, anchorPoint.y);
            currentBird.getBody().setType(BodyDef.BodyType.StaticBody);
        }
    }

    /**
     * Handle the stretching of the rubber band as the bird is dragged.
     *
     * @param dragPosition The current position of the drag.
     */
    public void stretch(Vector2 dragPosition) {
        if (currentBird != null) {
            isStretching = true;

            // Create a temporary vector for the drag position
            Vector2 tempDragPos = new Vector2(dragPosition);

            // Ensure minimum Y position is above ground level
            float minY = 180f; // Ground level height
            if (tempDragPos.y < minY) {
                tempDragPos.y = minY;
            }

            stretchedPoint.set(tempDragPos);

            // Limit stretch distance
            float maxStretchDistance = 150f;
            Vector2 stretch = new Vector2(stretchedPoint).sub(anchorPoint);
            if (stretch.len() > maxStretchDistance) {
                stretch.nor().scl(maxStretchDistance);
                stretchedPoint.set(anchorPoint).add(stretch);
            }

            // Update bird position with ground constraint
            if (currentBird != null) {
                currentBird.setPosition(
                    stretchedPoint.x / PIXELS_TO_METERS,
                    Math.max(stretchedPoint.y, minY) / PIXELS_TO_METERS
                );
            }
        }
    }

    /**
     * Render the catapult, rubber band, and the bird.
     */
    public void render(SpriteBatch batch) {
        // Draw the rubber band behind everything if stretching
        if (isStretching && currentBird != null) {
            // Draw two rubber band lines for slingshot effect
            float bandWidth = 2f;
            rubberBandTexture = new Texture("rubber_band.png");
            batch.draw(rubberBandTexture,
                anchorPoint.x - 20, anchorPoint.y,
                stretchedPoint.x - anchorPoint.x + 20,
                stretchedPoint.y - anchorPoint.y
            );
            batch.draw(rubberBandTexture,
                anchorPoint.x + 20, anchorPoint.y,
                stretchedPoint.x - anchorPoint.x - 20,
                stretchedPoint.y - anchorPoint.y
            );
        }

        // Draw the catapult
        sprite.draw(batch);

        // Draw the bird last so it's always visible
        if (currentBird != null) {
            currentBird.render(batch);
        }
    }

    /**
     * Dispose of the textures to avoid memory leaks.
     */
    public void dispose() {
        if (sprite != null && sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
        if (rubberBandTexture != null) {
            rubberBandTexture.dispose();
        }
    }
}
