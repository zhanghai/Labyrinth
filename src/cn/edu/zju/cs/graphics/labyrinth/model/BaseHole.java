package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.Body;

public class BaseHole extends Entity {

    public static final double RADIUS = 13d;
    public static final double SENSOR_RADIUS = RADIUS - Ball.RADIUS;

    public BaseHole(Body body) {
        super(body);
    }
}
