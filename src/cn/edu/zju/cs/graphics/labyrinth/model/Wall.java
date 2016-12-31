package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Wall extends Entity<Wall> {

    public Wall(double width, double height) {
        super(Bodies.newWall(width, height), Renderers.WALL);
    }
}
