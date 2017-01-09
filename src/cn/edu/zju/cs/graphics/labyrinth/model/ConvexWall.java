package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class ConvexWall extends BaseWall<ConvexWall> {

    public ConvexWall(double radius, double positionX, double positionY) {
        super(Bodies.newConvexWall(radius, positionX, positionY), Renderers.CONVEX_WALL);
    }
}
