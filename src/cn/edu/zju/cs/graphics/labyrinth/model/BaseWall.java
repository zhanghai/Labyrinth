package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.Body;

public class BaseWall extends Entity {

    public static final double THICKNESS_DEFAULT = 20d;
    public static final double THICKNESS_THIN = 15d;
    public static final double HEIGHT = Labyrinth.HEIGHT;
    public static final double RESTITUTION = Ball.RESTITUTION;

    public BaseWall(Body body) {
        super(body);
    }
}
