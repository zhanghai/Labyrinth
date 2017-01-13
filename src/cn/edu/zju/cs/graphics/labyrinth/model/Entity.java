package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.Body;

public abstract class Entity {

    private Body mBody;

    public static Entity ofBody(Body body) {
        return (Entity) body.getUserData();
    }

    public Entity(Body body) {
        mBody = body;
        mBody.setUserData(this);
    }

    Body getBody() {
        return mBody;
    }

    public double getPositionX() {
        return mBody.getTransform().getTranslationX();
    }

    public Entity setPositionX(double positionX) {
        mBody.getTransform().setTranslationX(positionX);
        return this;
    }

    public double getPositionY() {
        return mBody.getTransform().getTranslationY();
    }

    public Entity setPositionY(double positionY) {
        mBody.getTransform().setTranslationY(positionY);
        return this;
    }

    public double getRotation() {
        return mBody.getTransform().getRotation();
    }

    public Entity setRotation(double rotation) {
        mBody.getTransform().setRotation(rotation);
        return this;
    }
}
