package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class Bodies {

    private static final float BALL_RADIUS = 1;
    private static final float HOLE_RADIUS = BALL_RADIUS;

    private Bodies() {}

    public static Body newBall() {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(BALL_RADIUS));
        fixture.setRestitution(0.5);
        return new Body()
                .addFixture(fixture)
                .setMass(MassType.NORMAL);
    }

    public static Body newHole() {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(HOLE_RADIUS));
        fixture.setSensor(true);
        return new Body()
                .addFixture(fixture);
    }

    public static Body newWall(double width, double height) {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, height));
        fixture.setRestitution(0.5);
        return new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
        // TODO: Avoid collision with a ignore-same-type filter?
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
}
