package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Hole extends Entity<Hole> {

    public Hole() {
        super(Bodies.newHole(), Renderers.HOLE);
    }
}
