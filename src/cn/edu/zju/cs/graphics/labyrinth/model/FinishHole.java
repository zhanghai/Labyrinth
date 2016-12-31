package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class FinishHole extends Entity<FinishHole> {

    public FinishHole() {
        super(Bodies.newFinishHole(), Renderers.FINISH_HOLE);
    }
}
