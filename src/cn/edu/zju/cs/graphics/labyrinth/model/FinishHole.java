package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;

public class FinishHole extends BaseHole {

    public FinishHole(double positionX, double positionY) {
        super(Bodies.newFinishHole(positionX, positionY));
    }
}
