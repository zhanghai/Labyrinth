package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;

public class Hole extends BaseHole {

    public Hole(double positionX, double positionY) {
        super(Bodies.newHole(positionX, positionY));
    }
}
