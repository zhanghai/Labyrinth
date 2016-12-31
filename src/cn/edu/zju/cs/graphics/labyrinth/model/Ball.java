package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Ball extends Entity<Ball> {

    public Ball() {
        super(Bodies.newBall(), Renderers.BALL);
    }
}
