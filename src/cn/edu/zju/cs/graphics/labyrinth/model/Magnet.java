package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;

public class Magnet extends Entity {

    public static final double WIDTH = 50d;
    public static final double LENGTH = 56d;
    public static final double CIRCLE_RADIUS = 25d;
    public static final double CIRCLE_Y = 3d;
    public static final double RECTANGLE_WIDTH = 18d;
    public static final double RECTANGLE_LENGTH = 28.5d;
    public static final double RESTITUTION = BaseWall.RESTITUTION;
    public static final double FIELD_RADIUS = 48d;
    public static final double FIELD_SENSOR_RADIUS = FIELD_RADIUS - Ball.RADIUS;
    public static final double FIELD_X_LEFT = -13d;
    public static final double FIELD_X_RIGHT = 13d;
    public static final double FIELD_Y = -20d;

    public Magnet(double positionX, double positionY) {
        super(Bodies.newMagnet(positionX, positionY));
    }
}
