package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;

public abstract class BaseHoleRenderer<HoleType extends BaseHole> implements Renderer<HoleType> {

    private RectangleRenderer mRectangleRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private Matrix3f mTextureMatrix = new Matrix3f();
    private int mTexture;

    public BaseHoleRenderer() throws IOException {
        mRectangleRenderer = RectangleRenderer.getInstance();
        mTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource(
                getTextureResourceName()));
    }

    protected abstract String getTextureResourceName();

    protected abstract float getTextureScale();

    public void render(HoleType hole, Matrix4f viewProjectionMatrix) {
        float textureScale = getTextureScale();
        mModelMatrix
                .identity()
                .translate((float) hole.getPositionX(), (float) hole.getPositionY(), GlUtils.BIAS)
                .scale(textureScale, textureScale, 1f)
                .scale(2f * (float) BaseHole.RADIUS, 2f * (float) BaseHole.RADIUS, 1f);
        mRectangleRenderer.render(mModelMatrix, viewProjectionMatrix, mTextureMatrix, mTexture);
    }
}
