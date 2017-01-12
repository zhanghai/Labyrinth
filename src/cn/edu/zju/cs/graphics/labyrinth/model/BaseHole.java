package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderer;
import org.dyn4j.dynamics.Body;

public class BaseHole<EntityType extends BaseHole<EntityType>> extends Entity<EntityType> {

    public static final double RADIUS = 13d;
    public static final double SENSOR_RADIUS = RADIUS - Ball.RADIUS;

    public BaseHole(Body body, Renderer<EntityType> renderer) {
        super(body, renderer);
    }
}
