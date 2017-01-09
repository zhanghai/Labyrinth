package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.rendering.PrototypeRenderers;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.geometry.Vector2;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengles.GLES;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LabyrinthPrototypeApplication implements Labyrinth.Listener {

    private static final float LABYRINTH_WIDTH = 18;
    private static final float LABYRINTH_LENGTH = 12;

    private long mWindow;
    private int mWidth = 720;
    private int mHeight = 480;
    private int mFrameBufferWidth = 720;
    private int mFrameBufferHeight = 480;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMatrix = new Matrix4f();
    private Matrix4f mViewProjectionMatrix = new Matrix4f();

    private Labyrinth mLabyrinth;

    private GLFWFramebufferSizeCallback mFramebufferSizeCallback;
    private GLFWWindowSizeCallback mWindowSizeCallback;
    private GLFWKeyCallback mKeyCallback;

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

        System.out.println("Change gravity with W/A/S/D, reset gravity with 0");
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
                    case GLFW_KEY_0:
                        mLabyrinth.setRotationX(0).setRotationY(0);
                        break;
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
        PrototypeRenderers.initialize();

        mLabyrinth = new Labyrinth()
                .addEntity(new Hole(1.5, LABYRINTH_LENGTH - 1.5))
                .addEntity(new Hole(LABYRINTH_WIDTH / 4d + 1d, 1.5))
                .addEntity(new Hole(LABYRINTH_WIDTH / 2d - 1d, LABYRINTH_LENGTH - 1.5))
                .addEntity(new Hole(LABYRINTH_WIDTH / 2d - 0.5, 3.5))
                .addEntity(new Hole(LABYRINTH_WIDTH / 2d + 0.5, 3.5))
                .addEntity(new Hole(LABYRINTH_WIDTH / 2d + 1d, LABYRINTH_LENGTH - 1.5))
                .addEntity(new Hole(LABYRINTH_WIDTH * 3d / 4d - 1, 1.5))
                .addEntity(new Hole(LABYRINTH_WIDTH - 1.5, LABYRINTH_LENGTH - 1.5))
                .addEntity(new FinishHole(LABYRINTH_WIDTH - 2.5, 1.5))
                .addEntity(new Wall(LABYRINTH_WIDTH, 1d, LABYRINTH_WIDTH / 2d, 0.5))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH, LABYRINTH_WIDTH - 0.5,
                        LABYRINTH_LENGTH / 2d))
                .addEntity(new Wall(LABYRINTH_WIDTH, 1d, LABYRINTH_WIDTH / 2d,
                        LABYRINTH_LENGTH - 0.5))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH, 0.5, LABYRINTH_LENGTH / 2d))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH * 3d / 4d, LABYRINTH_WIDTH / 4d,
                        LABYRINTH_LENGTH * 3d / 8d))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH / 2d, LABYRINTH_WIDTH / 2d,
                        LABYRINTH_LENGTH * 3d / 4d))
                .addEntity(new Wall(1d, LABYRINTH_LENGTH * 3d / 4d, LABYRINTH_WIDTH * 3d / 4d,
                        LABYRINTH_LENGTH * 3d / 8d))
                .addEntity(new Magnet(LABYRINTH_WIDTH / 2d, 2d))
                .addEntity(new Ball(2.5, 2.5))
                .setListener(this);
    }

    @Override
    public void onBallFallingTowardsMagnet(Ball ball, Magnet magnet) {
        // TODO
    }

    @Override
    public void onBallFallingTowardsHole(Ball ball, BaseHole hole) {
        Vector2 distance = new Vector2(hole.getPositionX(), hole.getPositionY())
                .subtract(ball.getPositionX(), ball.getPositionY());
        ball.applyForce(Vector2.create(1d / distance.getMagnitude(), distance.getDirection()));
        Vector2 velocity = ball.getVelocity();
        Vector2 tangentVelocity = new Vector2(velocity).subtract(distance.multiply(
                distance.dot(velocity) / distance.getMagnitudeSquared()));
        ball.applyForce(Vector2.create(10d * tangentVelocity.getMagnitude(),
                tangentVelocity.getDirection() + Math.PI));
    }

    @Override
    public void onBallFallenIntoHole(Ball ball, BaseHole hole) {
        ball.stopMovement();
        if (hole instanceof Hole) {
            // TODO: Die.
            ball.setPositionX(2.5).setPositionY(2.5);
            mLabyrinth.setRotationX(0).setRotationY(0);
        } else if (hole instanceof FinishHole) {
            // TODO: Victory.
            glfwSetWindowShouldClose(mWindow, true);
        } else {
            throw new IllegalStateException("Unknown type of hole: " + hole);
        }
    }

    @Override
    public void onBallHitEntity(Ball ball, Entity<?> entity, ContactPoint point) {
        // TODO: Audio.
    }

    @Override
    public void onBallRolled(Ball ball, Vector2 movement) {
        // TODO: Audio.
    }

    private void update() {

        mViewMatrix.identity();
        mProjectionMatrix.setOrtho2D(0, LABYRINTH_WIDTH, 0, LABYRINTH_LENGTH);
        mProjectionMatrix.mul(mViewMatrix, mViewProjectionMatrix);
        PrototypeRenderers.setViewProjectionMatrix(mViewProjectionMatrix);

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
            glfwDestroyWindow(mWindow);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            glfwTerminate();
        }
    }
}
