package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;

public class ConvexWall extends BaseWall {

    public ConvexWall(double radius, double positionX, double positionY) {
        super(Bodies.newConvexWall(radius, positionX, positionY));
    }
}
