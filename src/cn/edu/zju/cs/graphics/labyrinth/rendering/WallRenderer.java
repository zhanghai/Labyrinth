package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix4f;

import java.io.IOException;

public class WallRenderer implements Renderer<Wall> {

    private static WallRenderer sInstance;

    private ModelSpaceTextureCubeRenderer mRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private int mTopTexture;
    private int mSideTexture;

    public static WallRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new WallRenderer();
        }
        return sInstance;
    }

    private WallRenderer() throws IOException {
        mRenderer = ModelSpaceTextureCubeRenderer.getInstance();
        mTopTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("wall-top.jpg"));
        mSideTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource("wall-side.jpg"));
    }

    public void render(Wall wall, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate((float) wall.getPositionX(), (float) wall.getPositionY(),
                        (float) Wall.HEIGHT / 2f)
                .scale((float) wall.getWidth(), (float) wall.getLength(), (float) Wall.HEIGHT);
        mRenderer.render(mModelMatrix, viewProjectionMatrix, mTopTexture, (float) Labyrinth.SIZE,
                (float) Labyrinth.SIZE, mSideTexture, (float) Wall.HEIGHT);
    }
}
