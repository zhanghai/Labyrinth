package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.ConvexWall;
import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;

public interface Renderers {

    Renderer<Ball> BALL = PrototypeRenderers.BALL;

    Renderer<Wall> WALL = PrototypeRenderers.WALL;

    // TODO
    Renderer<ConvexWall> CONVEX_WALL = null;

    Renderer<Hole> HOLE = PrototypeRenderers.HOLE;

    Renderer<FinishHole> FINISH_HOLE = PrototypeRenderers.FINISH_HOLE;

    Renderer<Magnet> MAGNET = PrototypeRenderers.MAGNET;
}
