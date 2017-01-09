package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

public class Ball extends Entity<Ball> {

    public Ball(double positionX, double positionY) {
        super(Bodies.newBall(positionX, positionY), Renderers.BALL);
    }

    public Vector2 getVelocity() {
        return getBody().getLinearVelocity();
    }

    public void applyForce(Vector2 force) {
        getBody().applyForce(force);
    }

    public void stopMovement() {
        Body body = getBody();
        body.setLinearVelocity(0, 0);
        body.clearAccumulatedForce();
        body.clearAccumulatedTorque();
    }
}
