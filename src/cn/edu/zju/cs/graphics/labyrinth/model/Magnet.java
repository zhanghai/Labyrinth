package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Magnet extends Entity<Magnet> {

    public Magnet(double positionX, double positionY) {
        super(Bodies.newMagnet(positionX, positionY), Renderers.MAGNET);
    }
}
