package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.GL_STATIC_DRAW;

public class ModelSpaceTextureCubeRenderer {

    private final FloatBuffer mVertexArrayBufferData = GlUtils.createBuffer(6 * 4 * 3,
            new float[] {
                    // Top
                    0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    // Right
                    0.5f, 0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
                    // Back
                    -0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    // Left
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    // Front
                    0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    // Bottom
                    0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,
            });
    private final IntBuffer mXyElementArrayBufferData = GlUtils.createBuffer(2 * 2 * 3, new int[] {
            // Top
            0, 1, 2,
            0, 2, 3,
            // Bottom
            20, 21, 22,
            20, 22, 23,
    });
    private final IntBuffer mCylindricalElementArrayBufferData = GlUtils.createBuffer(4 * 2 * 3,
            new int[] {
                    // Right
                    4, 5, 6,
                    4, 6, 7,
                    // Back
                    8, 9, 10,
                    8, 10, 11,
                    // Left
                    12, 13, 14,
                    12, 14, 15,
                    // Front
                    16, 17, 18,
                    16, 18, 19,
            });

    private static ModelSpaceTextureCubeRenderer sInstance;

    private ModelXyTextureRenderer mXyRenderer;
    private ModelCylindricalTextureRenderer mCylindricalRenderer;

    private int mVertexArrayBuffer;
    private int mXyElementArrayBuffer;
    private int mCylindricalElementArrayBuffer;

    public static ModelSpaceTextureCubeRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelSpaceTextureCubeRenderer();
        }
        return sInstance;
    }

    private ModelSpaceTextureCubeRenderer() throws IOException {

        mXyRenderer = ModelXyTextureRenderer.getInstance();
        mCylindricalRenderer = ModelCylindricalTextureRenderer.getInstance();

        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData,
                GL_STATIC_DRAW);
        mXyElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(mXyElementArrayBufferData,
                GL_STATIC_DRAW);
        mCylindricalElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(
                mCylindricalElementArrayBufferData, GL_STATIC_DRAW);
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture,
                       float textureWidth, float textureLength, float textureHeight) {
        mXyRenderer.render(mVertexArrayBuffer, 3, mXyElementArrayBuffer,
                mXyElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix,
                texture, textureWidth, textureLength);
        float textureDiameter = (float) Math.hypot(textureWidth, textureLength);
        mCylindricalRenderer.render(mVertexArrayBuffer, 3, mCylindricalElementArrayBuffer,
                mCylindricalElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix,
                texture, textureDiameter, textureHeight);
    }
}
