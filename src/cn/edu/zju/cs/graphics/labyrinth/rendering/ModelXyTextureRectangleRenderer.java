package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.joml.Matrix4f;

import java.io.IOException;

public class ModelXyTextureRectangleRenderer extends BaseModelXyTextureRectangleRenderer {

    private static ModelXyTextureRectangleRenderer sInstance;

    private ModelXyTextureRenderer mRenderer;

    public static ModelXyTextureRectangleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelXyTextureRectangleRenderer();
        }
        return sInstance;
    }

    protected ModelXyTextureRectangleRenderer() throws IOException {
        mRenderer = ModelXyTextureRenderer.getInstance();
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture,
                       float textureWidth, float textureHeight) {
        mRenderer.render(getVertexArrayBuffer(), getPositionSize(), getElementArrayBuffer(),
                getElementCount(), modelMatrix, viewProjectionMatrix, texture, textureWidth,
                textureHeight);
    }
}
