package cn.edu.zju.cs.graphics.labyrinth.util;

import cn.edu.zju.cs.graphics.labyrinth.model.Entity;

public class MathUtils {

    private MathUtils() {}

    public static double direction(double fromX, double fromY, double toX, double toY) {
        return Math.atan2(toY - fromY, toX - fromX);
    }

    public static double direction(Entity from, Entity to) {
        return direction(from.getPositionX(), from.getPositionY(), to.getPositionX(),
                to.getPositionY());
    }

    public static double distanceSquared(double fromX, double fromY, double toX, double toY) {
        return square(toX - fromX) + square(toY - fromY);
    }

    public static double distanceSquared(Entity from, Entity to) {
        return distanceSquared(from.getPositionX(), from.getPositionY(), to.getPositionX(),
                to.getPositionY());
    }

    public static double distance(double fromX, double fromY, double toX, double toY) {
        return Math.sqrt(distanceSquared(fromX, fromY, toX, toY));
    }

    public static double distance(Entity from, Entity to) {
        return distance(from.getPositionX(), from.getPositionY(), to.getPositionX(),
                to.getPositionY());
    }

    public static double square(double base) {
        return base * base;
    }
}
