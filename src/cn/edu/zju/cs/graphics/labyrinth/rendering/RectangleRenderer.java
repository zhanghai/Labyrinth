package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class RectangleRenderer {

    private final FloatBuffer mVertexArrayBufferData = GlUtils.createBuffer(4 * (2 + 2),
            new float[] {
                    0.5f, 0.5f, 1f, 1f,
                    -0.5f, 0.5f, 0f, 1f,
                    -0.5f, -0.5f, 0f, 0f,
                    0.5f, -0.5f, 1f, 0f
            });
    private final IntBuffer mElementArrayBufferData = GlUtils.createBuffer(2 * 3, new int[] {
            0, 1, 2,
            0, 2, 3
    });

    private static RectangleRenderer sInstance;

    private GenericRenderer mGenericRenderer;
    private int mVertexArrayBuffer;
    private int mElementArrayBuffer;

    public static RectangleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new RectangleRenderer();
        }
        return sInstance;
    }

    private RectangleRenderer() throws IOException {
        mGenericRenderer = GenericRenderer.getInstance();
        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData,
                GL_STATIC_DRAW);
        mElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(mElementArrayBufferData,
                GL_STATIC_DRAW);
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, Matrix3f textureMatrix,
                       int texture) {
        mGenericRenderer.render(mVertexArrayBuffer, 2, mElementArrayBuffer,
                mElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix,
                textureMatrix, texture);
    }
}
