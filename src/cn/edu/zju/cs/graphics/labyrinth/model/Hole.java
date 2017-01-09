package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Hole extends BaseHole<Hole> {

    public Hole(double positionX, double positionY) {
        super(Bodies.newHole(positionX, positionY), Renderers.HOLE);
    }
}
