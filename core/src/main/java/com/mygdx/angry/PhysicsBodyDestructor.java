package com.mygdx.angry;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.io.Serializable;

public class PhysicsBodyDestructor implements Serializable {
    private final World world;
    private final Array<Body> bodiesToDestroy;

    public PhysicsBodyDestructor(World world) {
        this.world = world;
        this.bodiesToDestroy = new Array<>();
    }

    public void queueBodyForDestruction(Body body) {
        if (!bodiesToDestroy.contains(body, true)) {
            bodiesToDestroy.add(body);
        }
    }

    public void processBodies() {
        for (Body body : bodiesToDestroy) {
            if (body != null) {
                world.destroyBody(body);
            }
        }
        bodiesToDestroy.clear();
    }
}
