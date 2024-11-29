// Java
package com.mygdx.angry;

public class PigJ {
    private int health;
    private boolean isDestroyed;

    public PigJ(int initialHealth) {
        this.health = initialHealth;
        this.isDestroyed = false;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void takeDamage(int damage) {
        if (!isDestroyed) {
            health -= damage;
            if (health <= 0) {
                die();
            }
        }
    }

    private void die() {
        isDestroyed = true;
    }
}
