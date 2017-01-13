package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
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

    private MagnetRenderer() throws IOException {
        mRenderer = ModelRenderer.getInstance();
        mModel = GlUtils.loadModel("magnet.obj");
    }

    public void render(Magnet magnet, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) magnet.getPositionX(), (float) magnet.getPositionY(), 0)
                .scale(10, 10, 10)
                .rotateXYZ((float) Math.toRadians(90), (float) Math.toRadians(60), 0);
        mRenderer.render(mModel, mModelMatrix, viewProjectionMatrix);
    }
}
