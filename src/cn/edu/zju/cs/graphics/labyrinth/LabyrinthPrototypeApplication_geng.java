package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.rendering.PrototypeRenders;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengles.GLES;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LabyrinthPrototypeApplication {

    private static final float LABYRINTH_WIDTH = 30;
    private static final float LABYRINTH_LENGTH = 20;

    private long mWindow;
    private int mWidth = 640;
    private int mHeight = 480;
    private int mFrameBufferWidth = 640;
    private int mFrameBufferHeight = 480;
    private float mFov = 60, mRotationX, mRotationY;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMatrix = new Matrix4f();
    private Matrix4f mViewProjectionMatrix = new Matrix4f();
    private Vector3f mCameraPosition = new Vector3f();

    private Labyrinth mLabyrinth;

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
                    case GLFW_KEY_ESCAPE:
                        glfwSetWindowShouldClose(window, true);
                        break;
                    case GLFW_KEY_LEFT:
                    case GLFW_KEY_A:
                        mLabyrinth.addRotationX(-1);
                        break;
                    case GLFW_KEY_RIGHT:
                    case GLFW_KEY_F:
                        mLabyrinth.addRotationX(1);
                        break;
                    case GLFW_KEY_DOWN:
                    case GLFW_KEY_D:
                        mLabyrinth.addRotationY(-1);
                        break;
                    case GLFW_KEY_UP:
                    case GLFW_KEY_W:
                        mLabyrinth.addRotationY(1);
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

        glClearColor(1f, 1f, 1f, 1f);
        PrototypeRenders.initialize();

        mLabyrinth = new Labyrinth()

                .addEntity(new Wall(LABYRINTH_WIDTH, 1d, LABYRINTH_WIDTH / 2d, 0.5))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH, LABYRINTH_WIDTH - 0.5,
                        LABYRINTH_LENGTH / 2d))
                .addEntity(new Wall(LABYRINTH_WIDTH, 1d, LABYRINTH_WIDTH / 2d,
                        LABYRINTH_LENGTH - 0.5))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH, 0.5, LABYRINTH_LENGTH / 2d))
                .addEntity(new Hole(LABYRINTH_WIDTH - 2.5, LABYRINTH_LENGTH - 2.5))
                .addEntity(new Ball(0, 0));
    }

    private void update() {

        mViewMatrix.identity();
        mProjectionMatrix.setOrtho2D(0, LABYRINTH_WIDTH, 0, LABYRINTH_LENGTH);
        mProjectionMatrix.mul(mViewMatrix, mViewProjectionMatrix);
        PrototypeRenders.setViewProjectionMatrix(mViewProjectionMatrix);

        mLabyrinth.update();
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);

        mLabyrinth.render();
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

}
