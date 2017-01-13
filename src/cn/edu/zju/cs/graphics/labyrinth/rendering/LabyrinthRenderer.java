package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import org.joml.Matrix4f;

import java.io.IOException;

public class LabyrinthRenderer {

    private static LabyrinthRenderer sInstance;

    private FloorRenderer mFloorRenderer;
    private WallRenderer mWallRenderer;
    private BallRenderer mBallRenderer;
    private HoleRenderer mHoleRenderer;
    private FinishHoleRenderer mFinishHoleRenderer;
    private MagnetRenderer mMagnetRenderer;

    public static LabyrinthRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new LabyrinthRenderer();
        }
        return sInstance;
    }

    private LabyrinthRenderer() throws IOException {
        mFloorRenderer = FloorRenderer.getInstance();
        mWallRenderer = WallRenderer.getInstance();
        mBallRenderer = BallRenderer.getInstance();
        mHoleRenderer = HoleRenderer.getInstance();
        mFinishHoleRenderer = FinishHoleRenderer.getInstance();
        mMagnetRenderer = MagnetRenderer.getInstance();
    }

    public void render(Labyrinth labyrinth, Matrix4f viewProjectionMatrix, Matrix4f lightMatrix,
                       int shadowMap) {
        mFloorRenderer.render(viewProjectionMatrix, lightMatrix, shadowMap);
        for (Entity entity : labyrinth.getEntities()) {
            if (entity instanceof Wall) {
                mWallRenderer.render((Wall) entity, viewProjectionMatrix);
            } else if (entity instanceof Ball) {
                mBallRenderer.render((Ball) entity, viewProjectionMatrix);
            } else if (entity instanceof Hole) {
                mHoleRenderer.render((Hole) entity, viewProjectionMatrix);
            } else if (entity instanceof FinishHole) {
                mFinishHoleRenderer.render((FinishHole) entity, viewProjectionMatrix);
            } else if (entity instanceof Magnet) {
                mMagnetRenderer.render((Magnet) entity, viewProjectionMatrix);
            }
        }
    }
}
