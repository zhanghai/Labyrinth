package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.BaseHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.rendering.FloorRenderer;
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

public class LabyrinthApplication implements Labyrinth.Listener {

    private static final double KEY_ROTATE_DEGREES = 3;

    private long mWindow;
    private int mWidth = 720;
    private int mHeight = 480;
    private int mFrameBufferWidth = 720;
    private int mFrameBufferHeight = 480;

    private Matrix4f mProjectionMatrix = new Matrix4f();
    private Matrix4f mViewMatrix = new Matrix4f();
    private Matrix4f mViewProjectionMatrix = new Matrix4f();

    private FloorRenderer mFloorRenderer;
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
                        mLabyrinth.addRotationX(-KEY_ROTATE_DEGREES);
                        break;
                    case GLFW_KEY_RIGHT:
                    case GLFW_KEY_F:
                        mLabyrinth.addRotationX(KEY_ROTATE_DEGREES);
                        break;
                    case GLFW_KEY_DOWN:
                    case GLFW_KEY_D:
                        mLabyrinth.addRotationY(-KEY_ROTATE_DEGREES);
                        break;
                    case GLFW_KEY_UP:
                    case GLFW_KEY_W:
                        mLabyrinth.addRotationY(KEY_ROTATE_DEGREES);
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
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        PrototypeRenderers.initialize();

        System.out.println("GL_VERSION: " + glGetString(GL_VERSION));
        System.out.println("GL_MAX_TEXTURE_SIZE: " + glGetInteger(GL_MAX_TEXTURE_SIZE));

        mFloorRenderer = FloorRenderer.getInstance();
        mLabyrinth = new Labyrinth()
                .addEntity(new Hole(Wall.DEFAULT_THICKNESS + Hole.RADIUS,
                        Labyrinth.LENGTH - (Wall.DEFAULT_THICKNESS + Hole.RADIUS)))
                .addEntity(new FinishHole(Labyrinth.WIDTH - (Wall.DEFAULT_THICKNESS + Hole.RADIUS),
                        Wall.DEFAULT_THICKNESS + Hole.RADIUS))
                .addEntity(new Wall(Labyrinth.WIDTH, Wall.DEFAULT_THICKNESS, Labyrinth.WIDTH / 2d,
                        Wall.DEFAULT_THICKNESS / 2))
                .addEntity(new Wall(Wall.DEFAULT_THICKNESS, Labyrinth.LENGTH,
                        Labyrinth.WIDTH - Wall.DEFAULT_THICKNESS / 2, Labyrinth.LENGTH / 2d))
                .addEntity(new Wall(Labyrinth.WIDTH, Wall.DEFAULT_THICKNESS, Labyrinth.WIDTH / 2d,
                        Labyrinth.LENGTH - Wall.DEFAULT_THICKNESS / 2))
                .addEntity(new Wall(Wall.DEFAULT_THICKNESS, Labyrinth.LENGTH,
                        Wall.DEFAULT_THICKNESS / 2, Labyrinth.LENGTH / 2d))
                //.addEntity(new Magnet(WIDTH / 2d, Wall.DEFAULT_THICKNESS))
                .addEntity(new Ball(Wall.DEFAULT_THICKNESS + Ball.RADIUS, Wall.DEFAULT_THICKNESS
                        + Ball.RADIUS))
                .setListener(this);
    }

    @Override
    public void onBallAttractedByMagnet(Ball ball, Magnet magnet) {
        // TODO
    }

    @Override
    public void onBallFallingTowardsHole(Ball ball, BaseHole hole) {
        Vector2 displacement = new Vector2(hole.getPositionX(), hole.getPositionY())
                .subtract(ball.getPositionX(), ball.getPositionY());
        double angle = Math.acos((BaseHole.RADIUS - displacement.getMagnitude()) / Ball.RADIUS);
        Vector2 gravity = Vector2.create(
                10 * ball.getMass() * mLabyrinth.getGravity() * Math.cos(angle) * Math.sin(angle),
                displacement.getDirection());
        ball.setForce(gravity);
        Vector2 velocity = ball.getVelocity();
        velocity
                .setMagnitude(velocity.dot(displacement) / displacement.getMagnitude())
                .setDirection(displacement.getDirection());
    }

    @Override
    public void onBallFallenIntoHole(Ball ball, BaseHole hole) {
        ball.stopMovement();
        if (hole instanceof Hole) {
            // TODO: Die.
            ball
                    .setPositionX(Wall.DEFAULT_THICKNESS + Ball.RADIUS)
                    .setPositionY(Wall.DEFAULT_THICKNESS + Ball.RADIUS);
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
    public void onBallRolling(Ball ball, Vector2 movement) {
        // TODO: Audio.
    }

    private void update() {

        //mViewMatrix.identity();
        //mProjectionMatrix.setOrtho2D(0, WIDTH, 0, LENGTH);
        mViewMatrix.setLookAt(
                Labyrinth.WIDTH / 2, Labyrinth.LENGTH / 2, 1f,
                Labyrinth.WIDTH / 2, Labyrinth.LENGTH / 2, 0f,
                0f, 1f, 0f
        );
        mProjectionMatrix.setOrtho(-Labyrinth.WIDTH / 2f, Labyrinth.WIDTH / 2f,
                -Labyrinth.LENGTH / 2f, Labyrinth.LENGTH / 2f, -1f, 1f);
        mProjectionMatrix.mul(mViewMatrix, mViewProjectionMatrix);
        PrototypeRenderers.setViewProjectionMatrix(mViewProjectionMatrix);

        mLabyrinth.update();
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);

        mFloorRenderer.render(mViewProjectionMatrix);
        //mLabyrinth.render();

        int error = glGetError();
        if (error != GL_NO_ERROR) {
            throw new IllegalStateException("glGetError(): " + error);
        }
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
