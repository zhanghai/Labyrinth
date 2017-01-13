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
    private int mTexture;

    public static BallRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new BallRenderer();
        }
        return sInstance;
    }

    private BallRenderer() throws IOException {
        mRenderer = TextureRectangleRenderer.getInstance();
        mTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("ball.png"));
    }

    public void render(Ball ball, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) ball.getPositionX(), (float) ball.getPositionY(),
                        (float) Ball.RADIUS)
                .scale(TEXTURE_SCALE, TEXTURE_SCALE, 1f)
                .scale(2f * (float) Ball.RADIUS, 2f * (float) Ball.RADIUS, 1f);
        mRenderer.render(mModelMatrix, viewProjectionMatrix, mTexture);
    }
}
