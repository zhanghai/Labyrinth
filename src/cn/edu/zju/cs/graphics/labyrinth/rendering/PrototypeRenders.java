package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import org.dyn4j.geometry.Transform;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class PrototypeRenders {

    private static int sPrototypeProgram;
    private static int sPositionAttribute;
    private static Matrix4f sModelMatrix = new Matrix4f();
    private static int sModelMatrixUniform;
    private static FloatBuffer sModelMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sViewProjectionMatrixUniform;
    private static FloatBuffer sViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sColorUniform;

    private static int sBallVertexBuffer;
    private static FloatBuffer sBallVertexBufferData;
    static {
        sBallVertexBufferData = BufferUtils.createFloatBuffer(6 * 2);
        float inverseSqrt2 = 1f / (float) Math.sqrt(2);
        sBallVertexBufferData
                .put(0f).put(inverseSqrt2)
                .put(inverseSqrt2).put(0f)
                .put(0f).put(-inverseSqrt2)
                .put(0f).put(inverseSqrt2)
                .put(-inverseSqrt2).put(0f)
                .put(0f).put(-inverseSqrt2)
                .flip();
    }
    private static FloatBuffer sBallColorBuffer;
    static {
        sBallColorBuffer = BufferUtils.createFloatBuffer(4);
        sBallColorBuffer
                .put(0.5f).put(0.5f).put(0.5f).put(1f)
                .flip();
    }

    public static void initialize() throws IOException {

        sPrototypeProgram = GlUtils.createProgram(makeShaderResource("prototype.vs"),
                makeShaderResource("prototype.fs"));
        sPositionAttribute = GlUtils.getAttribLocation(sPrototypeProgram, "aPosition");
        sModelMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uModelMatrix");
        sViewProjectionMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram,
                "uViewProjectionMatrix");
        sColorUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uColor");

        sBallVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, sBallVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, sBallVertexBufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public static void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
        glUseProgram(sPrototypeProgram);
        glUniformMatrix4fv(sViewProjectionMatrixUniform, false, viewProjectionMatrix.get(sViewProjectionMatrixBuffer));
        glUseProgram(0);
    }

    private static FloatBuffer getModelMatrixBuffer(Entity<?> entity) {
        Transform transform = entity.getBody().getTransform();
        sModelMatrix
                .identity()
                .translate((float) transform.getTranslationX(),
                        (float) transform.getTranslationY(), 0)
                .rotateZ((float) transform.getRotation());
        return sModelMatrix.get(sModelMatrixBuffer);
    }

    public static final Renderer<Ball> BALL = new Renderer<Ball>() {
        @Override
        public void render(Ball ball) {
            glUseProgram(sPrototypeProgram);
            glBindBuffer(GL_ARRAY_BUFFER, sBallVertexBuffer);
            glEnableVertexAttribArray(sPositionAttribute);
            glVertexAttribPointer(sPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
            glUniformMatrix4fv(sModelMatrixUniform, false, getModelMatrixBuffer(ball));
            glUniform4fv(sColorUniform, sBallColorBuffer);
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glDisableVertexAttribArray(sPositionAttribute);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);
        }
    };
}
