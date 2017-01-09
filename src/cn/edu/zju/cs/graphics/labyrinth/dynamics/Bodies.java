package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class Bodies {

    private static final double BALL_RADIUS = 0.5;
    private static final double HOLE_RADIUS = BALL_RADIUS;

    private Bodies() {}

    public static Body newBall(double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(BALL_RADIUS));
        fixture.setRestitution(0.5);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.NORMAL);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newHole() {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(HOLE_RADIUS));
        fixture.setSensor(true);
        return new Body()
                .addFixture(fixture);
    }

    public static Body newWall(double width, double height, double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, height));
        fixture.setRestitution(0.5);
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

    public static Body newFinishHole() {
        return newHole();
    }

    private static Body setBodyPosition(Body body, double x, double y) {
        body.getTransform().setTranslation(x, y);
        return body;
    }
}
