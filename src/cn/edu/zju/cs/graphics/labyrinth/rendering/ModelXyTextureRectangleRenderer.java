package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.GL_STATIC_DRAW;

public class ModelXyTextureRectangleRenderer {

    private final FloatBuffer mVertexArrayBufferData = GlUtils.createBuffer(4 * 2,
            new float[] {
                    0.5f, 0.5f,
                    -0.5f, 0.5f,
                    -0.5f, -0.5f,
                    0.5f, -0.5f,
            });
    private final IntBuffer mElementArrayBufferData = GlUtils.createBuffer(2 * 3, new int[] {
            0, 1, 2,
            0, 2, 3
    });

    private static ModelXyTextureRectangleRenderer sInstance;

    private ModelXyTextureRenderer mRenderer;
    private int mVertexArrayBuffer;
    private int mElementArrayBuffer;

    public static ModelXyTextureRectangleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelXyTextureRectangleRenderer();
        }
        return sInstance;
    }

    private ModelXyTextureRectangleRenderer() throws IOException {
        mRenderer = ModelXyTextureRenderer.getInstance();
        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData,
                GL_STATIC_DRAW);
        mElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(mElementArrayBufferData,
                GL_STATIC_DRAW);
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture,
                       float textureWidth, float textureHeight) {
        mRenderer.render(mVertexArrayBuffer, 2, mElementArrayBuffer,
                mElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix,
                texture, textureWidth, textureHeight);
    }
}
