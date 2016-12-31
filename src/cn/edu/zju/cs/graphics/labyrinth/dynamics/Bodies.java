package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Rectangle;

public class Bodies {

    private static final float BALL_RADIUS = 1;

    private static final float HOLE_RADIUS = 1;

    private Bodies() {}

    public static Body newBall() {
        return new Body()
                .addFixture(new BodyFixture(new Circle(BALL_RADIUS)));
    }

    public static Body newHole() {
        BodyFixture fixture = new BodyFixture(new Circle(HOLE_RADIUS));
        fixture.setSensor(true);
        return new Body()
                .addFixture(fixture);
    }

    public static Body newWall(double width, double height) {
        return new Body()
                .addFixture(new BodyFixture(new Rectangle(width, height)));
    }

    public static Body newFinishHole() {
        return newHole();
    }
}
