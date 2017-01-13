package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class ModelXyTextureShadowRenderer extends ModelXyTextureRenderer {

    private static ModelXyTextureShadowRenderer sInstance;

    private int mLightMatrixUniform;
    private int mShadowMapUniform;

    private FloatBuffer mLightMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private int mShadowMap;

    public static ModelXyTextureShadowRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelXyTextureShadowRenderer();
        }
        return sInstance;
    }

    private ModelXyTextureShadowRenderer() throws IOException {
        int program = getProgram();
        mLightMatrixUniform = GlUtils.getUniformLocation(program, "uLightMatrix");
        mShadowMapUniform = GlUtils.getUniformLocation(program, "uShadowMap");
    }

    @Override
    protected String getVertexShaderName() {
        return "model_xy_texture_shadow.vs";
    }

    @Override
    protected String getFragmentShaderName() {
        return "shadow.fs";
    }

    public void render(int vertexArrayBuffer, int positionSize, int elementArrayBuffer,
                       int elementCount, Matrix4f modelMatrix, Matrix4f viewProjectionMatrix,
                       int texture, float textureWidth, float textureLength, Matrix4f lightMatrix,
                       int shadowMap) {
        lightMatrix.get(mLightMatrixBuffer);
        mShadowMap = shadowMap;
        super.render(vertexArrayBuffer, positionSize, elementArrayBuffer, elementCount, modelMatrix,
                viewProjectionMatrix, texture, textureWidth, textureLength);
    }

    @Override
    public final void render(int vertexArrayBuffer, int positionSize, int elementArrayBuffer,
                             int elementCount, Matrix4f modelMatrix, Matrix4f viewProjectionMatrix,
                             int texture, float textureWidth, float textureLength) {
        throw new UnsupportedOperationException("Use render() with shadow map instead");
    }

    @Override
    protected void onDrawElements() {
        super.onDrawElements();

        glUniformMatrix4fv(mLightMatrixUniform, false, mLightMatrixBuffer);
        GlUtils.uniformTexture(mShadowMapUniform, GL_TEXTURE1, mShadowMap);
    }
}
