package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseWall;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

public class Bodies {

    private Bodies() {}

    public static Body newBall(double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(Ball.RADIUS));
        fixture.setDensity(Ball.DENSITY);
        fixture.setRestitution(Ball.RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.NORMAL);
        body.setLinearDamping(Ball.LINEAR_DAMPING);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newWall(double width, double length, double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(width, length));
        fixture.setRestitution(BaseWall.RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
        // TODO: Avoid collision with a ignore-same-type filter?
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newConvexWall(double radius, double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createSlice(radius, Math.toRadians(90)));
        fixture.setRestitution(BaseWall.RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newHole(double positionX, double positionY) {
        BodyFixture fixture = new BodyFixture(Geometry.createCircle(BaseHole.SENSOR_RADIUS));
        fixture.setSensor(true);
        Body body = new Body()
                .addFixture(fixture);
        return setBodyPosition(body, positionX, positionY);
    }

    public static Body newFinishHole(double positionX, double positionY) {
        return newHole(positionX, positionY);
    }

    public static Body newMagnet(double positionX, double positionY) {
        Circle circleShape = Geometry.createCircle(Magnet.MAGNET_CIRCLE_RADIUS);
        circleShape.translate(0, Magnet.MAGNET_CIRCLE_Y);
        BodyFixture circleFixture = new BodyFixture(circleShape);
        circleFixture.setRestitution(Magnet.MAGNET_RESTITUTION);
        Rectangle leftRectangleShape = Geometry.createRectangle(Magnet.MAGNET_RECTANGLE_WIDTH,
                Magnet.MAGNET_RECTANGLE_LENGTH);
        leftRectangleShape.translate(Magnet.MAGNET_RECTANGLE_WIDTH / 2 - Magnet.MAGNET_CIRCLE_RADIUS,
                        Magnet.MAGNET_RECTANGLE_LENGTH / 2 + Magnet.MAGNET_CIRCLE_Y);
        leftRectangleShape.rotate(Math.toRadians(7.5), 0, Magnet.MAGNET_CIRCLE_Y);
        BodyFixture leftRectangleFixture = new BodyFixture(leftRectangleShape);
        leftRectangleFixture.setRestitution(Magnet.MAGNET_RESTITUTION);
        Rectangle rightRectangleShape = Geometry.createRectangle(Magnet.MAGNET_RECTANGLE_WIDTH,
                Magnet.MAGNET_RECTANGLE_LENGTH);
        rightRectangleShape.translate(Magnet.MAGNET_RECTANGLE_WIDTH / 2 - Magnet.MAGNET_CIRCLE_RADIUS,
                Magnet.MAGNET_RECTANGLE_LENGTH / 2 + Magnet.MAGNET_CIRCLE_Y);
        rightRectangleShape.rotate(Math.toRadians(7.5), 0, Magnet.MAGNET_CIRCLE_Y);
        BodyFixture rightRectangleFixture = new BodyFixture(rightRectangleShape);
        rightRectangleFixture.setRestitution(Magnet.MAGNET_RESTITUTION);
        // TODO: Magnetic field.
        Body body = new Body()
                .addFixture(circleFixture)
                .addFixture(leftRectangleFixture)
                .addFixture(rightRectangleFixture)
                .setMass(MassType.INFINITE);
        return setBodyPosition(body, positionX, positionY);
    }

    private static Body setBodyPosition(Body body, double x, double y) {
        body.getTransform().setTranslation(x, y);
        return body;
    }
}
