package cn.edu.zju.cs.graphics.labyrinth.dynamics;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseWall;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

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
        /*Circle circleShape = Geometry.createCircle(Magnet.CIRCLE_RADIUS);
        circleShape.translate(0, Magnet.CIRCLE_Y);
        BodyFixture circleFixture = new BodyFixture(circleShape);
        circleFixture.setRestitution(Magnet.RESTITUTION);
        Rectangle leftRectangleShape = Geometry.createRectangle(Magnet.RECTANGLE_WIDTH,
                Magnet.RECTANGLE_LENGTH);
        leftRectangleShape.translate(Magnet.RECTANGLE_WIDTH / 2 - Magnet.CIRCLE_RADIUS,
                        Magnet.RECTANGLE_LENGTH / 2 + Magnet.CIRCLE_Y);
        leftRectangleShape.rotate(Math.toRadians(7.5), 0, Magnet.CIRCLE_Y);
        BodyFixture leftRectangleFixture = new BodyFixture(leftRectangleShape);
        leftRectangleFixture.setRestitution(Magnet.RESTITUTION);
        Rectangle rightRectangleShape = Geometry.createRectangle(Magnet.RECTANGLE_WIDTH,
                Magnet.RECTANGLE_LENGTH);
        rightRectangleShape.translate(Magnet.RECTANGLE_WIDTH / 2 - Magnet.CIRCLE_RADIUS,
                Magnet.RECTANGLE_LENGTH / 2 + Magnet.CIRCLE_Y);
        rightRectangleShape.rotate(Math.toRadians(7.5), 0, Magnet.CIRCLE_Y);
        BodyFixture rightRectangleFixture = new BodyFixture(rightRectangleShape);
        rightRectangleFixture.setRestitution(Magnet.RESTITUTION);
        Body body = new Body()
                .addFixture(circleFixture)
                .addFixture(leftRectangleFixture)
                .addFixture(rightRectangleFixture)
                .setMass(MassType.INFINITE);
                */
        BodyFixture fixture = new BodyFixture(Geometry.createRectangle(50d, 56d));
        fixture.setRestitution(Magnet.RESTITUTION);
        Body body = new Body()
                .addFixture(fixture)
                .setMass(MassType.INFINITE);
        return setBodyPosition(body, positionX, positionY);
    }

    private static Body setBodyPosition(Body body, double x, double y) {
        body.getTransform().setTranslation(x, y);
        return body;
    }
}
