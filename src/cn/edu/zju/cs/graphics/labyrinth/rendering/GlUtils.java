package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengles.GLES20;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static cn.edu.zju.cs.graphics.labyrinth.DemoUtils.ioResourceToByteBuffer;
import static org.lwjgl.opengles.GLES20.*;

public class GlUtils {

    private GlUtils() {}

    public static int createVertexArrayBuffer(FloatBuffer data, int usage) {
        return updateVertexArrayBuffer(glGenBuffers(), data, usage);
    }

    public static int updateVertexArrayBuffer(int buffer, FloatBuffer data, int usage) {
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, data, usage);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer;
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
        ByteBuffer source = ioResourceToByteBuffer(resource, 1024);
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
}
