package cn.edu.zju.cs.graphics.labyrinth.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengles.GLES20;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.stb.STBImage.*;

public class GlUtils {

    private GlUtils() {}

    /**
     * @deprecated Use {@link #createBuffer(int, float[])} for size check.
     */
    public static FloatBuffer createBuffer(float[] data) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(data.length)
                .put(data)
                .flip();
    }

    public static FloatBuffer createBuffer(int size, float[] data) {
        if (data.length != size) {
            throw new IllegalArgumentException();
        }
        return createBuffer(data);
    }

    /**
     * @deprecated Use {@link #createBuffer(int, int[])} for size check.
     */
    public static IntBuffer createBuffer(int[] data) {
        return (IntBuffer) BufferUtils.createIntBuffer(data.length)
                .put(data)
                .flip();
    }

    public static IntBuffer createBuffer(int size, int[] data) {
        if (data.length != size) {
            throw new IllegalArgumentException();
        }
        return createBuffer(data);
    }

    public static int createProgram(String vertexShaderResource, String fragmentShaderResource)
            throws IOException {
        int program = glCreateProgram();
        int vertexShader = createShader(vertexShaderResource, GL_VERTEX_SHADER);
        glAttachShader(program, vertexShader);
        int fragmentShader = createShader(fragmentShaderResource, GL_FRAGMENT_SHADER);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programInfoLog = glGetProgramInfoLog(program);
        if (programInfoLog.trim().length() > 0) {
            System.err.println(programInfoLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
        return program;
    }

    private static int createShader(String resource, int type) throws IOException {
        int shader = glCreateShader(type);
        ByteBuffer source = IoUtils.getResourceAsByteBuffer(resource, 1024);
        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);
        strings.put(0, source);
        lengths.put(0, source.remaining());
        glShaderSource(shader, strings, lengths);
        glCompileShader(shader);
        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        String shaderLog = glGetShaderInfoLog(shader);
        if (shaderLog.trim().length() > 0) {
            System.err.println(shaderLog);
        }
        if (compiled == 0) {
            throw new AssertionError("Could not compile shader");
        }
        return shader;
    }

    public static int createTexture(String resource) throws IOException {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        ByteBuffer imageBuffer;
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer componentCountBuffer = BufferUtils.createIntBuffer(1);
        imageBuffer = IoUtils.getResourceAsByteBuffer(resource, 16 * 1024);
        if (!stbi_info_from_memory(imageBuffer, widthBuffer, heightBuffer, componentCountBuffer)) {
            throw new IOException("Failed to read image information: " + stbi_failure_reason());
        }
        ByteBuffer pixelBuffer = stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer,
                componentCountBuffer, 4);
        if (pixelBuffer == null) {
            throw new IOException("Failed to load image: " + stbi_failure_reason());
        }
        try {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthBuffer.get(0), heightBuffer.get(0), 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
        } finally {
            stbi_image_free(pixelBuffer);
        }
        glBindTexture(GL_TEXTURE_2D, 0);
        return texture;
    }

    public static int createVertexArrayBuffer(FloatBuffer data, int usage) {
        return updateVertexArrayBuffer(glGenBuffers(), data, usage);
    }

    public static int updateVertexArrayBuffer(int buffer, FloatBuffer data, int usage) {
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, data, usage);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer;
    }

    public static int createVertexArrayIndexBuffer(IntBuffer data, int usage) {
        return updateVertexArrayIndexBuffer(glGenBuffers(), data, usage);
    }

    public static int updateVertexArrayIndexBuffer(int buffer, IntBuffer data, int usage) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, usage);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        return buffer;
    }

    public static int getUniformLocation(int program, CharSequence name) {
        int location = GLES20.glGetUniformLocation(program, name);
        if (location < 0) {
            throw new AssertionError("Uniform not found: " + name);
        }
        return location;
    }

    public static int getAttribLocation(int program, CharSequence name) {
        int location = GLES20.glGetAttribLocation(program, name);
        if (location < 0) {
            throw new AssertionError("Uniform not found: " + name);
        }
        return location;
    }

    public static void vertexAttribPointer(int index, int size, int stride, int offset) {
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES,
                offset * Float.BYTES);
    }
}
