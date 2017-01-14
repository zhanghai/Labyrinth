package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix4f;

import java.io.IOException;

public class MagnetRenderer {

    private static MagnetRenderer sInstance;

    public static MagnetRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new MagnetRenderer();
        }
        return sInstance;
    }

    private ModelRenderer mRenderer;
    private Model mModel;
    private Matrix4f mModelMatrix = new Matrix4f();

    private TextureRectangleRenderer mTestRenderer;
    private int mTestTexture;

    private MagnetRenderer() throws IOException {
        mRenderer = ModelRenderer.getInstance();
        mModel = GlUtils.loadModel(ResourceUtils.makeModelResource("magnet.obj"));

        mTestRenderer = TextureRectangleRenderer.getInstance();
        mTestTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("wall.jpg"));
    }

    public void render(Magnet magnet, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) magnet.getPositionX(), (float) magnet.getPositionY(), 0f)
                .translate(-1.5f, -4f, 0f)
                .scale(10f * 100f / 102f, 10f * 112f / 114f, 10f)
                .rotateXYZ((float) Math.toRadians(90f), (float) Math.toRadians(60f - 1f), 0f);
        mRenderer.render(mModel, mModelMatrix, viewProjectionMatrix);

        mModelMatrix
                .identity()
                .translate((float) magnet.getPositionX(), (float) magnet.getPositionY(),
                        GlUtils.BIAS)
                .scale(50, 56, 1);
        mTestRenderer.render(mModelMatrix, viewProjectionMatrix, mTestTexture);
    }
}
