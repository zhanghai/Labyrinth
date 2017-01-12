package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;

public class BallRenderer implements Renderer<Ball> {

    private static final float TEXTURE_SCALE = 120f / 102f;

    private static BallRenderer sInstance;

    private RectangleRenderer mRectangleRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private Matrix3f mTextureMatrix = new Matrix3f();
    private int mTexture;

    public static BallRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new BallRenderer();
        }
        return sInstance;
    }

    private BallRenderer() throws IOException {
        mRectangleRenderer = RectangleRenderer.getInstance();
        mTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("ball.png"));
    }

    public void render(Ball ball, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) ball.getPositionX(), (float) ball.getPositionY(),
                        2 * GlUtils.BIAS)
                .scale(TEXTURE_SCALE, TEXTURE_SCALE, 1f)
                .scale(2f * (float) Ball.RADIUS, 2f * (float) Ball.RADIUS, 1f);
        mRectangleRenderer.render(mModelMatrix, viewProjectionMatrix, mTextureMatrix, mTexture);
    }
}
