package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengles.GLES;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

/**
 * Created by xuxin on 2017/1/10.
 */
public class StartScene {
    private long mWindow;
    private int mWidth = 1024;
    private int mHeight = 768;
    private int mFrameBufferWidth = 1024;
    private int mFrameBufferHeight = 768;
    private float mFov = 33 ,mRotationX, mRotationY;
    private float mMouseX,mMouseY;

    private int mStartSceneProgram;

    private int mViewProjMatrixUniform;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMarix = new Matrix4f();
    private Matrix4f mViewProjMatrix = new Matrix4f();
    private FloatBuffer mViewProjMatrixBuffer = BufferUtils.createFloatBuffer(16);

    private GLFWKeyCallback mKeyCallback;
    private GLFWFramebufferSizeCallback mFramebufferSizeCallback;
    private GLFWWindowSizeCallback mWindowSizeCallback;
    private GLFWCursorPosCallback mCursorPositionCallback;
    private GLFWMouseButtonCallback mMouseButtonCallback;
    private GLFWScrollCallback mScrollCallback;

    private Element mBackGround;
    private Button mBtn;

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
                "StartScene", NULL, NULL);
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
                // TODO
            }
        });
        glfwSetMouseButtonCallback(mWindow, mMouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) switch(button)
                {
                    case GLFW_MOUSE_BUTTON_LEFT:
                        mBtn.OnMouseDown(mMouseX,mMouseY,mHeight);
                        System.out.println("Mosue left button clicked!");
                        break;
                    case GLFW_MOUSE_BUTTON_MIDDLE:
                        System.out.println("Mosue middle button clicked!");
                        break;
                    case GLFW_MOUSE_BUTTON_RIGHT:
                        System.out.println("Mosue right button clicked!");
                        break;
                    default:
                        return;
                }
                if (action == GLFW_RELEASE) switch(button)
                {
                    case GLFW_MOUSE_BUTTON_LEFT:
                        mBtn.OnMouseUp();
                        break;
                    default:
                        return;

                }
                return;
            }
        });

        glfwSetCursorPosCallback(mWindow, mCursorPositionCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                mMouseX = (float) x / mWidth * 2f - 1f;
                mMouseY = (float) y / mHeight * 2f - 1f;
            }
        });
        glfwSetScrollCallback(mWindow, mScrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
//                if (yoffset < 0) {
//                    mFov *= 1.05f;
//                } else {
//                    mFov *= 1f / 1.05f;
//                }
//                if (mFov < 10f) {
//                    mFov = 10f;
//                } else if (mFov > 120f) {
//                    mFov = 120f;
//                }
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


        mStartSceneProgram = GlUtils.createProgram("cn/edu/zju/cs/graphics/labyrinth/StartScene.vs",
                "cn/edu/zju/cs/graphics/labyrinth/StartScene.fs");
        mViewProjMatrixUniform = GlUtils.getUniformLocation(mStartSceneProgram,
                "uViewProjectionMatrix");
        mBackGround=new Element(4.0f,3f,0f,0f,mStartSceneProgram );
        mBackGround.setTexture("cn/edu/zju/cs/graphics/labyrinth/StartScene.jpg",0);
        mBtn=new Button(2.1f,0.4f,-2f,0f,mStartSceneProgram );
        mBtn.setTexture("cn/edu/zju/cs/graphics/labyrinth/btn_play.png",0);
    }

    private void update() {
        mProjectionMatrix.setPerspective((float) Math.toRadians(mFov), (float) mWidth / mHeight,
                0.01f, 100f);
        mViewMarix.translation(0, 0, -10f)
                .rotateX(mRotationX)
                .rotateY(mRotationY);
        mProjectionMatrix.mul(mViewMarix, mViewProjMatrix);
        glUniformMatrix4fv(mViewProjMatrixUniform, false, mViewProjMatrix.get(mViewProjMatrixBuffer));
    }



    private void loop() {
        while (!glfwWindowShouldClose(mWindow)) {
            glClearColor(0.6f, 0.7f, 0.8f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glViewport(0, 0, mFrameBufferWidth, mFrameBufferHeight);
            update();
            mBackGround.Render();
            mBtn.Render();
            glfwSwapBuffers(mWindow);
            glfwPollEvents();
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

    public static void main(String[] args) {
        new StartScene().run();
    }
}
