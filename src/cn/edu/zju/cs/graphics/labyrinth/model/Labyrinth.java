package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Labyrinth {

    private static final double ROTATION_MAX = 45;
    private static final double GRAVITY = 10;

    private List<Entity<?>> mEntities = new ArrayList<>();
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

    private double mRotationX;
    private double mRotationY;

    public Labyrinth addEntity(Entity<?> entity) {
        mEntities.add(entity);
        mWorld.addBody(entity.getBody());
        return this;
    }

    public Labyrinth removeEntity(Entity<?> entity) {
        mWorld.removeBody(entity.getBody());
        mEntities.remove(entity);
        return this;
    }

    public List<Entity<?>> getEntities() {
        return Collections.unmodifiableList(mEntities);
    }

    public double getRotationX() {
        return mRotationX;
    }

    public void setRotationX(double rotationX) {
        if (rotationX > ROTATION_MAX) {
            return;
        }
        mRotationX = rotationX;
        updateGravity();
    }

    public void addRotationX(double amount) {
        setRotationX(mRotationX + amount);
    }

    public double getRotationY() {
        return mRotationY;
    }

    public void setRotationY(double rotationY) {
        if (rotationY > ROTATION_MAX) {
            return;
        }
        mRotationY = rotationY;
        updateGravity();
    }

    public void addRotationY(double amount) {
        setRotationY(mRotationY + amount);
    }

    private void updateGravity() {
        mWorld.getGravity().set(GRAVITY * Math.sin(Math.toRadians(mRotationX)),
                GRAVITY * Math.sin(Math.toRadians(mRotationY)));
        System.out.println(String.format("RotationX: %f, RotationY: %f, Gravity: %s", mRotationX,
                mRotationY, mWorld.getGravity()));
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
