package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.Matrices;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;

public class WallRenderer implements Renderer<Wall> {

    private static WallRenderer sInstance;

    private CubeRenderer mCubeRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private Matrix3f mTextureMatrix = new Matrix3f();
    private int mTexture;

    public static WallRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new WallRenderer();
        }
        return sInstance;
    }

    private WallRenderer() throws IOException {
        mCubeRenderer = CubeRenderer.getInstance();
        mTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("wall.jpg"));
    }

    public void render(Wall wall, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) wall.getPositionX(), (float) wall.getPositionY(), 0)
                .scale((float) wall.getWidth(), (float) wall.getLength(), (float) Wall.HEIGHT);
        mTextureMatrix = Matrices.IDENTITY_3;
        mCubeRenderer.render(mModelMatrix, viewProjectionMatrix, mTextureMatrix, mTexture);
    }
}
