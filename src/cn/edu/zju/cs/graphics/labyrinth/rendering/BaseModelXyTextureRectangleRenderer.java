package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class BaseModelXyTextureRectangleRenderer {

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

    private int mVertexArrayBuffer;
    private int mElementArrayBuffer;

    protected BaseModelXyTextureRectangleRenderer() throws IOException {
        mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mVertexArrayBufferData
        );
        mElementArrayBuffer = GlUtils.createElementArrayBuffer(mElementArrayBufferData
        );
    }

    public int getVertexArrayBuffer() {
        return mVertexArrayBuffer;
    }

    public int getPositionSize() {
        return 2;
    }

    public int getElementArrayBuffer() {
        return mElementArrayBuffer;
    }

    public int getElementCount() {
        return mElementArrayBufferData.remaining();
    }
}
