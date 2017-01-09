package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.Hole;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
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
        sBallVertexBufferData
                .put(0f).put(0.45f)
                .put(0.45f).put(0f)
                .put(0f).put(-0.45f)
                .put(0f).put(0.45f)
                .put(-0.45f).put(0f)
                .put(0f).put(-0.45f)
                .flip();
    }
    private static FloatBuffer sBallColorBuffer;
    static {
        sBallColorBuffer = BufferUtils.createFloatBuffer(4);
        sBallColorBuffer
                .put(0.5f).put(0.5f).put(0.5f).put(1f)
                .flip();
    }

    private static int sWallVertexBuffer;
    private static FloatBuffer sWallVertexBufferData;
    static {
        sWallVertexBufferData = BufferUtils.createFloatBuffer(6 * 2);
        sWallVertexBufferData
                .put(0.5f).put(0.5f)
                .put(-0.5f).put(0.5f)
                .put(-0.5f).put(-0.5f)
                .put(0.5f).put(0.5f)
                .put(0.5f).put(-0.5f)
                .put(-0.5f).put(-0.5f)
                .flip();
    }
    private static FloatBuffer sWallColorBuffer;
    static {
        sWallColorBuffer = BufferUtils.createFloatBuffer(4);
        sWallColorBuffer
                .put(1f).put(1f).put(0f).put(1f)
                .flip();
    }

    private static int sHoleVertexBuffer;
    private static FloatBuffer sHoleVertexBufferData;
    static {
        sHoleVertexBufferData = BufferUtils.createFloatBuffer(6 * 2);
        sHoleVertexBufferData
                .put(0f).put(0.5f)
                .put(0.5f).put(0f)
                .put(0f).put(-0.5f)
                .put(0f).put(0.5f)
                .put(-0.5f).put(0f)
                .put(0f).put(-0.5f)
                .flip();
    }
    private static FloatBuffer sHoleColorBuffer;
    static {
        sHoleColorBuffer = BufferUtils.createFloatBuffer(4);
        sHoleColorBuffer
                .put(1f).put(0f).put(0f).put(1f)
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

        sBallVertexBuffer = GlUtils.createVertexArrayBuffer(sBallVertexBufferData, GL_STATIC_DRAW);
        sWallVertexBuffer = GlUtils.createVertexArrayBuffer(sWallVertexBufferData, GL_STATIC_DRAW);
        sHoleVertexBuffer = GlUtils.createVertexArrayBuffer(sHoleVertexBufferData, GL_STATIC_DRAW);
    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public static void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
        glUseProgram(sPrototypeProgram);
        glUniformMatrix4fv(sViewProjectionMatrixUniform, false,
                viewProjectionMatrix.get(sViewProjectionMatrixBuffer));
        glUseProgram(0);
    }

    private static Matrix4f getModelMatrix(Entity<?> entity) {
        return sModelMatrix
                .identity()
                .translate((float) entity.getPositionX(),
                        (float) entity.getPositionY(), 0f)
                .rotateZ((float) entity.getRotation());
    }

    private static FloatBuffer getModelMatrixBuffer(Entity<?> entity) {
        return getModelMatrix(entity).get(sModelMatrixBuffer);
    }

    private static Matrix4f getModelMatrixForWall(Wall wall) {
        return getModelMatrix(wall).scale((float) wall.getWidth(),
                (float) wall.getLength(), 1f);
    }

    private static FloatBuffer getModelMatrixBufferForWall(Wall wall) {
        return getModelMatrixForWall(wall).get(sModelMatrixBuffer);
    }

    private static void renderPrototype(int vertexBuffer, FloatBuffer modelMatrixBuffer,
                                        FloatBuffer colorBuffer) {
        glUseProgram(sPrototypeProgram);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(sPositionAttribute);
        glVertexAttribPointer(sPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
        glUniformMatrix4fv(sModelMatrixUniform, false, modelMatrixBuffer);
        glUniform4fv(sColorUniform, colorBuffer);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(sPositionAttribute);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }

    public static final Renderer<Ball> BALL = new Renderer<Ball>() {
        @Override
        public void render(Ball ball) {
            renderPrototype(sBallVertexBuffer, getModelMatrixBuffer(ball), sBallColorBuffer);
        }
    };

    public static final Renderer<Wall> WALL = new Renderer<Wall>() {
        @Override
        public void render(Wall wall) {
            renderPrototype(sWallVertexBuffer, getModelMatrixBufferForWall(wall), sWallColorBuffer);
        }
    };

    public static final Renderer<Hole> HOLE = new Renderer<Hole>() {
        @Override
        public void render(Hole hole) {
            renderPrototype(sHoleVertexBuffer, getModelMatrixBuffer(hole), sHoleColorBuffer);
        }
    };
}
