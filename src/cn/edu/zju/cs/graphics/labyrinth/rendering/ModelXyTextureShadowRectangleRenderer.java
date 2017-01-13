package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.joml.Matrix4f;

import java.io.IOException;

public class ModelXyTextureShadowRectangleRenderer extends BaseModelXyTextureRectangleRenderer {

    private static ModelXyTextureShadowRectangleRenderer sInstance;

    private ModelXyTextureShadowRenderer mRenderer;

    public static ModelXyTextureShadowRectangleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelXyTextureShadowRectangleRenderer();
        }
        return sInstance;
    }

    protected ModelXyTextureShadowRectangleRenderer() throws IOException {
        mRenderer = ModelXyTextureShadowRenderer.getInstance();
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture,
                       float textureWidth, float textureHeight, Matrix4f lightMatrix,
                       int shadowMap) {
        mRenderer.render(getVertexArrayBuffer(), getPositionSize(), getElementArrayBuffer(),
                getElementCount(), modelMatrix, viewProjectionMatrix, texture, textureWidth,
                textureHeight, lightMatrix, shadowMap);
    }
}
