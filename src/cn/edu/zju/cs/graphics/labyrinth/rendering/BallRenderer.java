package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix4f;

import java.io.IOException;

public class BallRenderer {

    private static final float TEXTURE_SCALE = 120f / 102f;

    private static BallRenderer sInstance;

    private TextureRectangleRenderer mRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private int mBallTexture;
    private int mShadowTexture;

    public static BallRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new BallRenderer();
        }
        return sInstance;
    }

    private BallRenderer() throws IOException {
        mRenderer = TextureRectangleRenderer.getInstance();
        mBallTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("ball.png"));
        mShadowTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource(
                "ball-shadow.png"));
    }

    public void render(Ball ball, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) ball.getPositionX(), (float) ball.getPositionY(),
                        // For hole shadow at GlUtils.BIAS.
                        2 * GlUtils.BIAS)
                .scale(TEXTURE_SCALE, TEXTURE_SCALE, 1f)
                .scale(2f * (float) Ball.RADIUS, 2f * (float) Ball.RADIUS, 1f);
        mRenderer.render(mModelMatrix, viewProjectionMatrix, mShadowTexture);
        mModelMatrix
                .translate(0, 0, (float) Ball.RADIUS);
        mRenderer.render(mModelMatrix, viewProjectionMatrix, mBallTexture);
    }
}
