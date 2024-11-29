package com.mygdx.angry;

public class BlockJ {
    private int health;
    private boolean destroyed;

    public BlockJ(int initialHealth) {
        this.health = initialHealth;
        this.destroyed = false;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return destroyed;
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
    }
}
