package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Magnet extends Entity<Magnet> {

    // Magnet: 50x56
    public static final double MAGNET_CIRCLE_RADIUS = 25d;
    public static final double MAGNET_CIRCLE_Y = -3d;
    public static final double MAGNET_RECTANGLE_WIDTH = 18d;
    public static final double MAGNET_RECTANGLE_LENGTH = 32d;
    public static final double MAGNET_RESTITUTION = BaseWall.RESTITUTION;

    public Magnet(double positionX, double positionY) {
        super(Bodies.newMagnet(positionX, positionY), Renderers.MAGNET);
    }
}
