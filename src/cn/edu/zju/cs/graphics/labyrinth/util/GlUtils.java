package cn.edu.zju.cs.graphics.labyrinth.util;

import cn.edu.zju.cs.graphics.labyrinth.rendering.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.system.CustomBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.stb.STBImage.*;

public class GlUtils {

    private GlUtils() {}

    public static final float BIAS = 0.001f;

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
        //noinspection deprecation
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
        //noinspection deprecation
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
            throw new AssertionError("Failed to link program");
        }
        return program;
    }

    private static int createShader(String resource, int type) throws IOException {
        int shader = glCreateShader(type);
        ByteBuffer source = ResourceUtils.getResourceAsByteBuffer(resource, 1024);
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
            throw new AssertionError("Failed to compile shader");
        }
        return shader;
    }

    public static int createTexture(int width, int height, ByteBuffer pixels) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, pixels);
        glBindTexture(GL_TEXTURE_2D, 0);
        return texture;
    }

    public static int createTexture(String resource) throws IOException {
        ByteBuffer imageBuffer;
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer componentCountBuffer = BufferUtils.createIntBuffer(1);
        imageBuffer = ResourceUtils.getResourceAsByteBuffer(resource, 16 * 1024);
        if (!stbi_info_from_memory(imageBuffer, widthBuffer, heightBuffer, componentCountBuffer)) {
            throw new IOException("Failed to read image information: " + stbi_failure_reason());
        }
        ByteBuffer pixelBuffer = stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer,
                componentCountBuffer, 4);
        if (pixelBuffer == null) {
            throw new IOException("Failed to load image: " + stbi_failure_reason());
        }
        try {
            return createTexture(widthBuffer.get(0), heightBuffer.get(0), pixelBuffer);
        } finally {
            stbi_image_free(pixelBuffer);
        }
    }

    public static int createVertexArrayBuffer(FloatBuffer data) {
        return updateVertexArrayBuffer(glGenBuffers(), data);
    }

    public static int updateVertexArrayBuffer(int buffer, FloatBuffer data) {
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer;
    }

    public static int createVertexArrayBuffer(CustomBuffer<?> data, int dataElementSize) {
        return updateVertexArrayBuffer(glGenBuffers(), data, dataElementSize);
    }

    public static int updateVertexArrayBuffer(int buffer, CustomBuffer<?> data,
                                              int dataElementSize) {
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        nglBufferData(GL_ARRAY_BUFFER, dataElementSize * data.remaining(), data.address(),
                GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return buffer;
    }

    public static int createElementArrayBuffer(IntBuffer data) {
        return updateElementArrayBuffer(glGenBuffers(), data);
    }

    public static int updateElementArrayBuffer(int buffer, IntBuffer data) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
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
            throw new AssertionError("Attribute not found: " + name);
        }
        return location;
    }

    public static Model loadModel(String resource) throws IOException {
        //AIPropertyStore properties = aiCreatePropertyStore();
        //aiSetImportPropertyInteger(properties, AI_CONFIG_PP_PTV_NORMALIZE, 1);
        //AIScene scene = aiImportFileExWithProperties(ResourceUtils.getResourceFile(resource),
        //        aiProcess_JoinIdenticalVertices | aiProcess_PreTransformVertices
        //                | aiProcess_Triangulate, null, properties);
        AIScene scene = aiImportFile(ResourceUtils.getResourceFile(resource),
                aiProcess_JoinIdenticalVertices | aiProcess_Triangulate);
        if (scene == null) {
            throw new IllegalStateException(aiGetErrorString());
        }
        return new Model(scene);
    }

    public static void uniformTexture(int uniform, int textureUnit, int textureTarget,
                                      int texture) {
        glActiveTexture(textureUnit);
        glBindTexture(textureTarget, texture);
        glUniform1i(uniform, textureUnit - GL_TEXTURE0);
    }

    public static void uniformTexture(int uniform, int textureUnit, int texture) {
        uniformTexture(uniform, textureUnit, GL_TEXTURE_2D, texture);
    }

    public static void vertexAttribPointer(int index, int size, int stride, int offset) {
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES,
                offset * Float.BYTES);
    }

    public static void vertexAttribPointer(int index, int size) {
        vertexAttribPointer(index, size, 0, 0);
    }
//
//    public static class AiResourceFileProc implements AIFileOpenProcI, AIFileCloseProcI {
//
//        @Override
//        public long invoke(long pFileIO, long fileName, long openMode) {
//            // AIFileOpenProcI
//            return 0;
//        }
//
//        @Override
//        public void invoke(long pFileIO, long pFile) {
//            // AIFileCloseProcI
//        }
//    }
}
