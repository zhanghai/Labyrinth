package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class FinishHole extends BaseHole<FinishHole> {

    public FinishHole(double positionX, double positionY) {
        super(Bodies.newFinishHole(positionX, positionY), Renderers.FINISH_HOLE);
    }
}
