package cn.edu.zju.cs.graphics.labyrinth;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengles.GLES;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static cn.edu.zju.cs.graphics.labyrinth.DemoUtils.ioResourceToByteBuffer;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LabyrinthPrototypeApplication {

    private long mWindow;
    private int mWidth = 1024;
    private int mHeight = 768;
    private int mFrameBufferWidth = 1024;
    private int mFrameBufferHeight = 768;
    private float mFov = 60, mRotationX, mRotationY;

    private int mPrototypeProgram;
    private int mPositionAttribute;
    private int mViewProjectionUniform;
    private int mColorUniform;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMatrix = new Matrix4f();
    private Matrix4f mViewProjectionMatrix = new Matrix4f();
    private Vector3f mCameraPosition = new Vector3f();
    private FloatBuffer mMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private int mFullscreenVbo;

    private GLFWFramebufferSizeCallback mFramebufferSizeCallback;
    private GLFWWindowSizeCallback mWindowSizeCallback;
    private GLFWKeyCallback mKeyCallback;
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
        mWindow = glfwCreateWindow(mWidth, mHeight, "Labyrinth Prototype", NULL, NULL);
        if (mWindow == NULL) {
            throw new AssertionError("Failed to create the GLFW window");
        }

        System.out.println("Play with W/A/S/D");
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
                switch (key) {
                    case GLFW_KEY_W:
                        // TODO
                        break;
                    case GLFW_KEY_ESCAPE:
                        glfwSetWindowShouldClose(window, true);
                        break;
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

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(mWindow, (videoMode.width() - mWidth) / 2,
                (videoMode.height() - mHeight) / 2);
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
        createFullScreenQuad();
        createPrototypeProgram();
    }

    private void createFullScreenQuad() {
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4 * 2 * 6);
        byteBuffer.asFloatBuffer()
                .put(-1f).put(-1f)
                .put(1f).put(-1f)
                .put(1f).put(1f)
                .put(1f).put(1f)
                .put(-1f).put(1f)
                .put(-1f).put(-1f);
        mFullscreenVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, mFullscreenVbo);
        glBufferData(GL_ARRAY_BUFFER, byteBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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

    private static int createProgram(String vertexShaderResource, String fragmentShaderResource)
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

    private void createPrototypeProgram() throws IOException {
        mPrototypeProgram = createProgram("cn/edu/zju/cs/graphics/labyrinth/shader/prototype.vs",
                "cn/edu/zju/cs/graphics/labyrinth/shader/prototype.fs");
        mPositionAttribute = glGetAttribLocation(mPrototypeProgram, "aPosition");
        mViewProjectionUniform = glGetUniformLocation(mPrototypeProgram, "uViewProjection");
        mColorUniform = glGetUniformLocation(mPrototypeProgram, "uColor");
    }

    private void update() {
        mProjectionMatrix.setPerspective((float) Math.toRadians(mFov), (float) mWidth / mHeight,
                0.01f, 100f);
        mViewMatrix.translation(0, 0, -10f)
                .rotateX(mRotationX)
                .rotateY(mRotationY);
        mViewMatrix.originAffine(mCameraPosition);
        mProjectionMatrix.mulPerspectiveAffine(mViewMatrix, mViewProjectionMatrix);
        glUseProgram(mPrototypeProgram);
        glUniformMatrix4fv(mViewProjectionUniform, false, mViewProjectionMatrix.get(mMatrixBuffer));
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_DEPTH_TEST);
        glUseProgram(mPrototypeProgram);
        glBindBuffer(GL_ARRAY_BUFFER, mFullscreenVbo);
        glEnableVertexAttribArray(mPositionAttribute);
        glVertexAttribPointer(mPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(mPositionAttribute);
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

            mFramebufferSizeCallback.free();
            mWindowSizeCallback.free();
            mKeyCallback.free();
            mCursorPositionCallback.free();
            mScrollCallback.free();
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
