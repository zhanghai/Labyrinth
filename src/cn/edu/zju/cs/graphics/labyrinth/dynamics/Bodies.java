package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class Bodies {

    // Smaller to fall in the hole easier; holes have the diameter of 1.
    private static final double BALL_RADIUS = 0.45;
    private static final double BALL_RESTITUTION = 0.2;
    private static final double WALL_RESTITUTION = BALL_RESTITUTION;
    // The ball has radius.
    public static final double HOLE_RADIUS = 0.05;

    private Bodies() {}

    public static Body newBall(double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(BALL_RADIUS));
        fixture.setRestitution(BALL_RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.NORMAL);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newWall(double width, double length, double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, length));
        fixture.setRestitution(WALL_RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
        // TODO: Avoid collision with a ignore-same-type filter?
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newConvexWall(double radius) {
        BodyFixture fixture = new BodyFixture(Geometry.createSlice(radius, Math.toRadians(90)));
        fixture.setRestitution(0.5);
        return new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
    }

    public static Body newHole(double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(HOLE_RADIUS));
        fixture.setSensor(true);
        Body body = new Body()
                .addFixture(fixture);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newFinishHole(double positionX, double positionY) {
        return newHole(positionX, positionY);
    }

    private static Body setBodyPosition(Body body, double x, double y) {
        body.getTransform().setTranslation(x, y);
        return body;
    }
}
