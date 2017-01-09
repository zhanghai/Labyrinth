package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Labyrinth {

    private static final Comparator<Entity<?>> ENTITY_COMPARATOR = new Comparator<Entity<?>>() {
        @Override
        public int compare(Entity<?> entity1, Entity<?> entity2) {
            // TODO
            return 0;
        }
    };

    private SortedSet<Entity<?>> mEntities = new TreeSet<>(ENTITY_COMPARATOR);
    private World mWorld;
    {
        mWorld = new World();
        mWorld.setGravity(new Vector2(World.ZERO_GRAVITY));
        Settings settings = mWorld.getSettings();
        // Gravity can change from zero.
        settings.setAutoSleepingEnabled(false);
        // TODO: Always restitution?
        settings.setRestitutionVelocity(0);
    }
    private double mWorldTimeSeconds;

    public void addEntity(Entity<?> entity) {
        mEntities.add(entity);
        mWorld.addBody(entity.getBody());
    }

    public void removeEntity(Entity<?> entity) {
        mWorld.removeBody(entity.getBody());
        mEntities.remove(entity);
    }

    public SortedSet<Entity<?>> getEntities() {
        return Collections.unmodifiableSortedSet(mEntities);
    }

    public Vector2 getGravity() {
        return mWorld.getGravity();
    }

    public void update() {
        double currentTimeSeconds = System.currentTimeMillis() / 1000d;
        if (mWorldTimeSeconds == 0) {
            mWorldTimeSeconds = currentTimeSeconds;
            return;
        }
        mWorld.update(currentTimeSeconds - mWorldTimeSeconds, Integer.MAX_VALUE);
        mWorldTimeSeconds = currentTimeSeconds;
    }

    public void render() {
        for (Entity<?> entity : mEntities) {
            entity.render();
        }
    }

    public World getWorld() {
        return mWorld;
    }
}
