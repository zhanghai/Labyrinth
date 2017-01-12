package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderer;

public class FinishHole extends BaseHole<FinishHole> {

    public FinishHole(double positionX, double positionY, Renderer<FinishHole> renderer) {
        super(Bodies.newFinishHole(positionX, positionY), renderer);
    }
}
