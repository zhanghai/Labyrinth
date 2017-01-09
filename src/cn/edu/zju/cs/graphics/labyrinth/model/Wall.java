package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.dynamics.Bodies;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderers;

public class Wall extends BaseWall<Wall> {

    private double mWidth;
    private double mLength;

    public Wall(double width, double length, double positionX, double positionY) {
        super(Bodies.newWall(width, length, positionX, positionY), Renderers.WALL);

        mWidth = width;
        mLength = length;
    }

    public double getWidth() {
        return mWidth;
    }

    public double getLength() {
        return mLength;
    }
}
