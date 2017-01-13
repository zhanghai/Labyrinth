package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix4f;

import java.io.IOException;

public abstract class BaseHoleRenderer<HoleType extends BaseHole> {

    private TextureRectangleRenderer mRenderer;
    private Matrix4f mModelMatrix = new Matrix4f();
    private int mTexture;

    public BaseHoleRenderer() throws IOException {
        mRenderer = TextureRectangleRenderer.getInstance();
        mTexture = GlUtils.createTexture(ResourceUtils.makeTextureResource(
                getTextureResourceName()));
    }

    protected abstract String getTextureResourceName();

    protected abstract float getTextureScale();

    public void render(HoleType hole, Matrix4f viewProjectionMatrix) {
        float textureScale = getTextureScale();
        mModelMatrix
                .identity()
                .translate((float) hole.getPositionX(), (float) hole.getPositionY(),
                        // For ball shadow at GlUtils.BIAS.
                        2 * GlUtils.BIAS)
                .scale(textureScale, textureScale, 1f)
                .scale(2f * (float) BaseHole.RADIUS, 2f * (float) BaseHole.RADIUS, 1f);
        mRenderer.render(mModelMatrix, viewProjectionMatrix, mTexture);
    }
}
