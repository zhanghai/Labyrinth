package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderer;

public class Hole extends BaseHole<Hole> {

    public Hole(double positionX, double positionY, Renderer<Hole> renderer) {
        super(Bodies.newHole(positionX, positionY), renderer);
    }
}
