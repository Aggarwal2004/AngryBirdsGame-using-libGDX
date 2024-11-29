// Java
package com.mygdx.angry;

import com.badlogic.gdx.utils.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Class to represent the game's state for serialization
public class GameState implements Serializable {
    public int score; // Player's score
    public int level; // Current level
    public int remainingBirds; // Number of birds remaining
    public int currentLevel; // Add this line
    public List<PigState> pigs = new ArrayList<>(); // State of pigs
    public List<BlockState> blocks = new ArrayList<>(); // State of blocks (wood, glass)
    public List<BirdState> birds = new ArrayList<>(); // Store states for all active birds

    // Inner class to represent the state of a pig
    public static class PigState implements Serializable {
        public float x; // X position
        public float y; // Y position
        public int health; // Health of the pig
        public float width; // Width of the pig
        public float height; // Height of the pig

        public PigState(float x, float y, int health, float width, float height) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.width = width;
            this.height = height;
        }

        public PigState() {
        }
    }

    // Inner class to represent the state of a block
    public static class BlockState implements Serializable {
        public float x; // X position
        public float y; // Y position
        public String type; // Type of block (e.g., "WOODEN", "STEEL")
        public int health; // Health of the block
        public float width; // Width of the block
        public float height; // Height of the block

        public BlockState(float x, float y, String type, int health, float width, float height) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.health = health;
            this.width = width;
            this.height = height;
        }

        public BlockState() {
        }
    }


    // Inner class to represent the state of a bird
    public static class BirdState implements Serializable {
        public String type; // Type of bird (e.g., "RED", "YELLOW")
        public float x; // X position
        public float y; // Y position
        public boolean launched; // Launched status

        public BirdState(String type, float x, float y, boolean launched) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.launched = launched;
        }

        public BirdState() {
        }
    }

    // Method to serialize the GameState object to JSON
    public String serialize() {
        Json json = new Json(); // Create a new Json object
        return json.toJson(this); // Convert the current GameState to JSON string
    }

    // Method to deserialize a JSON string back into a GameState object
    public static GameState deserialize(String jsonString) {
        Json json = new Json(); // Create a new Json object
        return json.fromJson(GameState.class, jsonString); // Convert JSON string back to GameState
    }
}
