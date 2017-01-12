package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.util.IoUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES20;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

import static cn.edu.zju.cs.graphics.labyrinth.DemoUtils.*;

public class EnvironmentTeapotDemo {

    private long mWindow;
    private int mWidth = 1024;
    private int mHeight = 768;
    private int mFrameBufferWidth = 1024;
    private int mFrameBufferHeight = 768;
    private float mFov = 60, mRotationX, mRotationY;

    private int mEnvironmentProgram;
    private int mTeapotProgram;

    private int mInvViewProjUniform;
    private int mViewProjUniform;
    private int mCameraPositionUniform;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMatrix = new Matrix4f();
    private Matrix4f mViewProjMatrix = new Matrix4f();
    private Matrix4f mInvViewProjMatrix = new Matrix4f();
    private Vector3f mCameraPosition = new Vector3f();
    private FloatBuffer mMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private WavefrontMeshLoader.Mesh mMesh;
    private int mNumVertices;
    private long mNormalsOffset;
    private int mTeapotVbo;
    private int mFullscreenVbo;

    private GLFWKeyCallback mKeyCallback;
    private GLFWFramebufferSizeCallback mFramebufferSizeCallback;
    private GLFWWindowSizeCallback mWindowSizeCallback;
    private GLFWCursorPosCallback mCursorPositionCallback;
    private GLFWScrollCallback mScrollCallback;

    private void init() throws IOException {

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        // Enable multi-sampling.
        glfwWindowHint(GLFW_SAMPLES, 8);
        mWindow = glfwCreateWindow(mWidth, mHeight,
                "Spherical environment mapping with teapot demo", NULL, NULL);
        if (mWindow == NULL) {
            throw new AssertionError("Failed to create the GLFW window");
        }

        System.out.println("Move the mouse to look around");
        System.out.println("Zoom in/out with mouse wheel");
        glfwSetFramebufferSizeCallback(mWindow, mFramebufferSizeCallback =
                new GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window, int width, int height) {
                        if (width > 0 && height > 0
                                && (mFrameBufferWidth != width || mFrameBufferHeight != height)) {
                            mFrameBufferWidth = width;
                            mFrameBufferHeight = height;
                        }
                    }
                });
        glfwSetWindowSizeCallback(mWindow, mWindowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                if (width > 0 && height > 0 && (mWidth != width || mHeight != height)) {
                    mWidth = width;
                    mHeight = height;
                }
            }
        });
        glfwSetKeyCallback(mWindow, mKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action != GLFW_RELEASE) {
                    return;
                }
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(window, true);
                }
            }
        });
        glfwSetCursorPosCallback(mWindow, mCursorPositionCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                float nx = (float) x / mWidth * 2f - 1f;
                float ny = (float) y / mHeight * 2f - 1f;
                mRotationX = ny * (float) Math.PI * 0.5f;
                mRotationY = nx * (float) Math.PI;
            }
        });
        glfwSetScrollCallback(mWindow, mScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if (yoffset < 0) {
                    mFov *= 1.05f;
                } else {
                    mFov *= 1f / 1.05f;
                }
                if (mFov < 10f) {
                    mFov = 10f;
                } else if (mFov > 120f) {
                    mFov = 120f;
                }
            }
        });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(mWindow, (vidmode.width() - mWidth) / 2, (vidmode.height() - mHeight) / 2);
        glfwMakeContextCurrent(mWindow);
        glfwSwapInterval(0);
        glfwShowWindow(mWindow);
        glfwSetCursorPos(mWindow, mWidth / 2, mHeight / 2);

        IntBuffer framebufferSize = BufferUtils.createIntBuffer(2);
        nglfwGetFramebufferSize(mWindow, memAddress(framebufferSize),
                memAddress(framebufferSize) + 4);
        mFrameBufferWidth = framebufferSize.get(0);
        mFrameBufferHeight = framebufferSize.get(1);

        GLES.createCapabilities();
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_LOADER.set(true);
        //debugProc = glDebugMessageCallback();

        /* Create all needed GL resources */
        loadMesh();
        createTexture();
        createFullScreenQuad();
        createTeapotProgram();
        createEnvironmentProgram();
    }

    private void loadMesh() throws IOException {
        mMesh = new WavefrontMeshLoader()
                .loadMesh("cn/edu/zju/cs/graphics/labyrinth/teapot.obj.zip");
        mNumVertices = mMesh.numVertices;
        long bufferSize = 4 * (3 + 3) * mMesh.numVertices;
        mNormalsOffset = 4L * 3 * mMesh.numVertices;
        mTeapotVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, mTeapotVbo);
        glBufferData(GL_ARRAY_BUFFER, bufferSize, GL_STATIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0L, mMesh.positions);
        glBufferSubData(GL_ARRAY_BUFFER, mNormalsOffset, mMesh.normals);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void createFullScreenQuad() {
        ByteBuffer vertices = BufferUtils.createByteBuffer(4 * 2 * 6);
        FloatBuffer fv = vertices.asFloatBuffer();
        fv.put(-1f).put(-1f);
        fv.put(1f).put(-1f);
        fv.put(1f).put(1f);
        fv.put(1f).put(1f);
        fv.put(-1f).put(1f);
        fv.put(-1f).put(-1f);
        mFullscreenVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, mFullscreenVbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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

    private void createEnvironmentProgram() throws IOException {
        int program = glCreateProgram();
        int vertexShader = createShader("cn/edu/zju/cs/graphics/labyrinth/environment.vs",
                GL_VERTEX_SHADER);
        int fragmentShader = createShader("cn/edu/zju/cs/graphics/labyrinth/environment.fs",
                GL_FRAGMENT_SHADER);
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog.trim().length() > 0) {
            System.err.println(programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
        glUseProgram(program);
        int texLocation = glGetUniformLocation(program, "uTex");
        glUniform1i(texLocation, 0);
        mInvViewProjUniform = glGetUniformLocation(program, "uInvViewProj");
        mEnvironmentProgram = program;
    }

    void createTeapotProgram() throws IOException {
        int program = glCreateProgram();
        int vertexShader = createShader("cn/edu/zju/cs/graphics/labyrinth/teapot.vs",
                GL_VERTEX_SHADER);
        int fragmentShader = createShader("cn/edu/zju/cs/graphics/labyrinth/teapot.fs",
                GL_FRAGMENT_SHADER);
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        int linked = glGetProgrami(program, GL_LINK_STATUS);
        String programLog = glGetProgramInfoLog(program);
        if (programLog.trim().length() > 0) {
            System.err.println(programLog);
        }
        if (linked == 0) {
            throw new AssertionError("Could not link program");
        }
        glUseProgram(program);
        mViewProjUniform = glGetUniformLocation(program, "uViewProj");
        mCameraPositionUniform = glGetUniformLocation(program, "uCameraPosition");
        mTeapotProgram = program;
    }

    private static void createTexture() throws IOException {
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        ByteBuffer imageBuffer;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image;
        imageBuffer = IoUtils.getResourceAsByteBuffer(
                "cn/edu/zju/cs/graphics/labyrinth/environment.jpg", 8 * 1024);
        if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
            throw new IOException("Failed to read image information: " + stbi_failure_reason());
        }
        image = stbi_load_from_memory(imageBuffer, w, h, comp, 3);
        if (image == null) {
            throw new IOException("Failed to load image: " + stbi_failure_reason());
        }
        glTexImage2D(GL_TEXTURE_2D, 0, /*GL_RGB8*/ GL_RGB, w.get(0), h.get(0), 0, GL_RGB,
                GL_UNSIGNED_BYTE, image);
        stbi_image_free(image);
    }

    private void update() {
        mProjectionMatrix.setPerspective((float) Math.toRadians(mFov), (float) mWidth / mHeight,
                0.01f, 100f);
        mViewMatrix.translation(0, 0, -10f)
                .rotateX(mRotationX)
                .rotateY(mRotationY);
        mViewMatrix.originAffine(mCameraPosition);
        mProjectionMatrix.mulPerspectiveAffine(mViewMatrix, mViewProjMatrix)
                .invert(mInvViewProjMatrix);
        glUseProgram(mEnvironmentProgram);
        glUniformMatrix4fv(mInvViewProjUniform, false, mInvViewProjMatrix.get(mMatrixBuffer));
        glUseProgram(mTeapotProgram);
        glUniformMatrix4fv(mViewProjUniform, false, mViewProjMatrix.get(mMatrixBuffer));
        glUniform3f(mCameraPositionUniform, mCameraPosition.x, mCameraPosition.y,
                mCameraPosition.z);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        /* Render environment map */
        glDisable(GL_DEPTH_TEST);
        glUseProgram(mEnvironmentProgram);
        glBindBuffer(GL_ARRAY_BUFFER, mFullscreenVbo);
        int glPositionLocation = glGetAttribLocation(mEnvironmentProgram, "aPosition");
        glEnableVertexAttribArray(glPositionLocation);
        glVertexAttribPointer(glPositionLocation, 2, GL_FLOAT, false, 0, 0L);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(glPositionLocation);

        /* Render teapot */
        glEnable(GL_DEPTH_TEST);
        glUseProgram(mTeapotProgram);
        glBindBuffer(GL_ARRAY_BUFFER, mTeapotProgram);
        glPositionLocation = glGetAttribLocation(mTeapotProgram, "aPosition");
        glEnableVertexAttribArray(glPositionLocation);
        glVertexAttribPointer(glPositionLocation, 3, GL_FLOAT, false, 3 * 4, 0L);
        int glNormalLocation = glGetAttribLocation(mTeapotProgram, "aNormal");
        glVertexAttribPointer(glNormalLocation, /* TODO: ? */3, GL_FLOAT, /* TODO: ? */ true, 3 * 4,
                mNormalsOffset);
        glDrawArrays(GL_TRIANGLES, 0, mNumVertices);
        glDisableVertexAttribArray(glPositionLocation);
        glDisableVertexAttribArray(glNormalLocation);
    }

    private void loop() {
        while (!glfwWindowShouldClose(mWindow)) {
            glfwPollEvents();
            glViewport(0, 0, mFrameBufferWidth, mFrameBufferHeight);
            update();
            render();
            glfwSwapBuffers(mWindow);
        }
    }

    public void run() {
        try {
            init();
            loop();
            mCursorPositionCallback.free();
            mKeyCallback.free();
            mFramebufferSizeCallback.free();
            mWindowSizeCallback.free();
            glfwDestroyWindow(mWindow);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            glfwTerminate();
        }
    }

    private static int glGetUniformLocation(int program, CharSequence name) {
        int location = GLES20.glGetUniformLocation(program, name);
        if (location < 0) {
            throw new AssertionError("Uniform not found: " + name);
        }
        return location;
    }

    private static int glGetAttribLocation(int program, CharSequence name) {
        int location = GLES20.glGetAttribLocation(program, name);
        if (location < 0) {
            throw new AssertionError("Uniform not found: " + name);
        }
        return location;
    }
}
