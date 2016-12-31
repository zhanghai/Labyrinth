package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;

public interface Renderers {

    // TODO
    Renderer<Ball> BALL = null;

    // TODO
    Renderer<Hole> HOLE = null;

    // TODO
    Renderer<Wall> WALL = null;

    // TODO
    Renderer<FinishHole> FINISH_HOLE = null;
}
