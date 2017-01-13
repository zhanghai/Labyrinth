package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public class Ball extends Entity {

    // Smaller to fall in the hole easier; holes have the diameter of 1.
    public static final double RADIUS = 12d;
    public static final double DENSITY = 0.5 / (Math.PI * RADIUS * RADIUS);
    public static final double RESTITUTION = 0.5;
    public static final double LINEAR_DAMPING = 0.5;

    public Ball(double positionX, double positionY) {
        super(Bodies.newBall(positionX, positionY));
    }

    public double getMass() {
        return getBody().getMass().getMass();
    }

    public Vector2 getVelocity() {
        return getBody().getLinearVelocity();
    }

    public void setForce(Vector2 force) {
        Body body = getBody();
        body.clearForce();
        body.applyForce(force);
    }

    public void stopMovement() {
        Body body = getBody();
        body.setLinearVelocity(0, 0);
        body.clearAccumulatedForce();
        body.clearAccumulatedTorque();
    }
}
