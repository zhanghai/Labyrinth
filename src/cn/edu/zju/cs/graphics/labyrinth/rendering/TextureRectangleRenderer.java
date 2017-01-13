package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class TextureRectangleRenderer {

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

    private static TextureRectangleRenderer sInstance;

    private TextureRenderer mRenderer;
    private int mVertexArrayBuffer;
    private int mElementArrayBuffer;

    public static TextureRectangleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new TextureRectangleRenderer();
        }
        return sInstance;
    }

    private TextureRectangleRenderer() throws IOException {
        mRenderer = TextureRenderer.getInstance();
        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData);
        mElementArrayBuffer = GlUtils.createElementArrayBuffer(mElementArrayBufferData);
    }

    public void render(Matrix4f modelMatrix, Matrix4f viewProjectionMatrix, int texture) {
        mRenderer.render(mVertexArrayBuffer, 2, mElementArrayBuffer,
                mElementArrayBufferData.remaining(), modelMatrix, viewProjectionMatrix, texture);
    }
}
