package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Wall extends BaseWall<Wall> {

    public Wall(double width, double height, double positionX, double positionY) {
        super(Bodies.newWall(width, height, positionX, positionY), Renderers.WALL);
    }
}
