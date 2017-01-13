package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.Labyrinth;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import org.joml.Matrix4f;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.glDrawBuffers;
import static org.lwjgl.opengles.GLES30.glReadBuffer;

public class ShadowMapRenderer {

    private final int SHADOW_WIDTH = 1024;
    private final int SHADOW_LENGTH = 1024;

    private static ShadowMapRenderer sInstance;

    private int mShadowMap;
    private int mFrameBuffer;
    private Matrix4f mLightMatrix;

    public static ShadowMapRenderer getInstance() {
        if (sInstance == null) {
            sInstance = new ShadowMapRenderer();
        }
        return sInstance;
    }

    private ShadowMapRenderer() {

        // TODO: Refactor after it works.
        Matrix4f viewMatrix = new Matrix4f()
                .setLookAt(
                        (float) Labyrinth.WIDTH / 2f, (float) Labyrinth.LENGTH / 2f, 1f,
                        (float) Labyrinth.WIDTH / 2f, (float) Labyrinth.LENGTH / 2f, 0f,
                        0f, 1f, 0f
                )
                .translate(0, 0, (float) -Labyrinth.HEIGHT);
        Matrix4f projectionMatrix = new Matrix4f()
                .setOrtho((float) -Labyrinth.WIDTH / 2f, (float) Labyrinth.WIDTH / 2f,
                        (float) -Labyrinth.LENGTH / 2f, (float) Labyrinth.LENGTH / 2f,
                        -2f * (float) Labyrinth.HEIGHT, 2f * (float) Labyrinth.HEIGHT);
        mLightMatrix = new Matrix4f();
        projectionMatrix.mul(viewMatrix, mLightMatrix);

        mShadowMap = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, mShadowMap);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_LENGTH, 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, 0L);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        mFrameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, mShadowMap,
                0);
        glDrawBuffers(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Matrix4f getLightMatrix() {
        return mLightMatrix;
    }

    public int getShadowMap() {
        return mShadowMap;
    }

    public void render(Labyrinth labyrinth) {
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_LENGTH);
        glBindFramebuffer(GL_FRAMEBUFFER, mFrameBuffer);
        glClear(GL_DEPTH_BUFFER_BIT);
        for (Entity<?> entity : labyrinth.getEntities()) {
            if (entity instanceof Wall || entity instanceof Ball) {
                entity.render(mLightMatrix);
            }
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
