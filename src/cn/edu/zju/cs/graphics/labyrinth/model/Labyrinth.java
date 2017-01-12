package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.util.MathUtils;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;
import org.joml.Math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Labyrinth {

    public static final double WIDTH = 480d;
    public static final double LENGTH = 320d;
    public static final double SIZE = Math.max(WIDTH, LENGTH);
    private static final double ROTATION_MAX_DEGREES = 30;
    private static final double GRAVITY = World.EARTH_GRAVITY.getMagnitude() * (24d / 0.005) / 100;

    private List<Entity<?>> mEntities = new ArrayList<>();
    private Listener mListener;
    private World mWorld;
    {
        mWorld = new World();
        mWorld.setGravity(new Vector2(World.ZERO_GRAVITY));
        Settings settings = mWorld.getSettings();
        // Gravity can change from zero.
        settings.setAutoSleepingEnabled(false);
        // TODO: Always restitution?
        settings.setRestitutionVelocity(0);
        mWorld.addListener(new ContactAdapter() {
            @Override
            public void sensed(ContactPoint point) {
                Entity<?> entity1 = Entity.ofBody(point.getBody1());
                Entity<?> entity2 = Entity.ofBody(point.getBody2());
                Ball ball;
                Entity<?> sensor;
                if (entity1 instanceof Ball) {
                    ball = (Ball) entity1;
                    sensor = entity2;
                } else {
                    ball = (Ball) entity2;
                    sensor = entity1;
                }
                if (sensor instanceof BaseHole<?>) {
                    BaseHole<?> hole = (BaseHole<?>) sensor;
                    if (MathUtils.distance(ball, hole) < BaseHole.RADIUS - Ball.RADIUS) {
                        mListener.onBallFallenIntoHole(ball, hole);
                    } else {
                        mListener.onBallFallingTowardsHole(ball, hole);
                    }
                } else if (sensor instanceof Magnet) {
                    // TODO
                }
            }
            @Override
            public boolean begin(ContactPoint point) {
                Entity<?> entity1 = Entity.ofBody(point.getBody1());
                Entity<?> entity2 = Entity.ofBody(point.getBody2());
                Ball ball;
                Entity<?> other;
                if (entity1 instanceof Ball) {
                    ball = (Ball) entity1;
                    other = entity2;
                } else {
                    ball = (Ball) entity2;
                    other = entity1;
                }
                mListener.onBallHitEntity(ball, other, point);
                return true;
            }
        });
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

    public Labyrinth setRotationX(double rotationX) {
        if (Math.abs(rotationX) > ROTATION_MAX_DEGREES) {
            return this;
        }
        mRotationX = rotationX;
        updateGravity();
        return this;
    }

    public Labyrinth addRotationX(double amount) {
        setRotationX(mRotationX + amount);
        return this;
    }

    public double getRotationY() {
        return mRotationY;
    }

    public Labyrinth setRotationY(double rotationY) {
        if (Math.abs(rotationY) > ROTATION_MAX_DEGREES) {
            return this;
        }
        mRotationY = rotationY;
        updateGravity();
        return this;
    }

    public Labyrinth addRotationY(double amount) {
        setRotationY(mRotationY + amount);
        return this;
    }

    private void updateGravity() {
        mWorld.getGravity().set(GRAVITY * Math.sin(Math.toRadians(mRotationX)),
                GRAVITY * Math.sin(Math.toRadians(mRotationY)));
        System.out.println(String.format("RotationX: %f, RotationY: %f, Gravity: %s", mRotationX,
                mRotationY, mWorld.getGravity()));
    }

    public double getGravity() {
        return GRAVITY;
    }

    public Labyrinth setListener(final Listener listener) {
        mListener = listener;
        return this;
    }

    public double getStepFrequency() {
        return mWorld.getSettings().getStepFrequency();
    }

    public void update() {

        double currentTimeSeconds = System.currentTimeMillis() / 1000d;
        if (mWorldTimeSeconds == 0) {
            mWorldTimeSeconds = currentTimeSeconds;
            return;
        }
        mWorld.update(currentTimeSeconds - mWorldTimeSeconds, Integer.MAX_VALUE);
        mWorldTimeSeconds = currentTimeSeconds;

        for (Entity<?> entity : mEntities) {
            if (!(entity instanceof Ball)) {
                continue;
            }
            Ball ball = (Ball) entity;
            Vector2 movement = ball.getBody().getChangeInPosition();
            if (!movement.isZero()) {
                mListener.onBallRolling(ball, movement);
            }
        }
    }

    public void render() {
        for (Entity<?> entity : mEntities) {
            entity.render(null);
        }
    }

    public interface Listener {

        /**
         * Modification of the {@link World} is permitted from this methods.
         * <p>
         * If a body is to be removed, make sure to return false to disable the contact.  Otherwise
         * the contact between the bodies will still be resolved even if the body has been removed.
         * If a body is removed you should check the remaining contacts for that body and return
         * false from the those methods as well.
         * </p>
         */
        void onBallAttractedByMagnet(Ball ball, Magnet magnet);

        /**
         * Modification of the {@link World} is permitted from this methods.
         * <p>
         * If a body is to be removed, make sure to return false to disable the contact.  Otherwise
         * the contact between the bodies will still be resolved even if the body has been removed.
         * If a body is removed you should check the remaining contacts for that body and return
         * false from the those methods as well.
         * </p>
         */
        void onBallFallingTowardsHole(Ball ball, BaseHole hole);

        /**
         * Modification of the {@link World} is permitted from this methods.
         * <p>
         * If a body is to be removed, make sure to return false to disable the contact.  Otherwise
         * the contact between the bodies will still be resolved even if the body has been removed.
         * If a body is removed you should check the remaining contacts for that body and return
         * false from the those methods as well.
         * </p>
         */
        void onBallFallenIntoHole(Ball ball, BaseHole hole);

        /**
         * Modification of the {@link World} is permitted from this methods.
         * <p>
         * If a body is to be removed, make sure to return false to disable the contact.  Otherwise
         * the contact between the bodies will still be resolved even if the body has been removed.
         * If a body is removed you should check the remaining contacts for that body and return
         * false from the those methods as well.
         * </p>
         */
        void onBallHitEntity(Ball ball, Entity<?> entity, ContactPoint point);

        void onBallRolling(Ball ball, Vector2 movement);
    }
}
