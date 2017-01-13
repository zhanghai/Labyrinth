package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.GL_STATIC_DRAW;

public class TextureCubeRenderer {

    private final FloatBuffer mVertexArrayBufferData = GlUtils.createBuffer(6 * 4 * (3 + 2),
            new float[] {
                    // Top
                    0.5f, 0.5f, 0.5f, 1f, 1f,
                    -0.5f, 0.5f, 0.5f, 0f, 1f,
                    -0.5f, -0.5f, 0.5f, 0f, 0f,
                    0.5f, -0.5f, 0.5f, 1f, 0f,
                    // Right
                    0.5f, 0.5f, 0.5f, 1f, 1f,
                    0.5f, -0.5f, 0.5f, 0f, 1f,
                    0.5f, -0.5f, -0.5f, 0f, 0f,
                    0.5f, 0.5f, -0.5f, 1f, 0f,
                    // Back
                    -0.5f, 0.5f, 0.5f, 1f, 1f,
                    0.5f, 0.5f, 0.5f, 0f, 1f,
                    0.5f, 0.5f, -0.5f, 0f, 0f,
                    -0.5f, 0.5f, -0.5f, 1f, 0f,
                    // Left
                    -0.5f, -0.5f, 0.5f, 1f, 1f,
                    -0.5f, 0.5f, 0.5f, 0f, 1f,
                    -0.5f, 0.5f, -0.5f, 0f, 0f,
                    -0.5f, -0.5f, -0.5f, 1f, 0f,
                    // Front
                    0.5f, -0.5f, 0.5f, 1f, 1f,
                    -0.5f, -0.5f, 0.5f, 0f, 1f,
                    -0.5f, -0.5f, -0.5f, 0f, 0f,
                    0.5f, -0.5f, -0.5f, 1f, 0f,
                    // Bottom
                    0.5f, -0.5f, -0.5f, 1f, 1f,
                    -0.5f, -0.5f, -0.5f, 0f, 1f,
                    -0.5f, 0.5f, -0.5f, 0f, 0f,
                    0.5f, 0.5f, -0.5f, 1f, 0f,
            });
    private final IntBuffer mElementArrayBufferData = GlUtils.createBuffer(6 * 2 * 3, new int[] {
            // Top
            0, 1, 2,
            0, 2, 3,
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
            // Bottom
            20, 21, 22,
            20, 22, 23,
    });

    private static TextureCubeRenderer sInstance;

    private TextureRenderer mRenderer;
    private int mVertexArrayBuffer;
    private int mElementArrayBuffer;

    public static TextureCubeRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new TextureCubeRenderer();
        }
        return sInstance;
    }

    private TextureCubeRenderer() throws IOException {
        mRenderer = TextureRenderer.getInstance();
        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData,
                GL_STATIC_DRAW);
        mElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(mElementArrayBufferData,
                GL_STATIC_DRAW);
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture) {
        mRenderer.render(mVertexArrayBuffer, 3, mElementArrayBuffer,
                mElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix, texture);
    }
}
