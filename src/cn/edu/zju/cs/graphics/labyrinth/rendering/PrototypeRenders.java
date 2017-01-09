package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class PrototypeRenders {

    private static int sPrototypeProgram;
    private static int sPositionAttribute;
    private static int sModelMatrixUniform;
    private static FloatBuffer sModelMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sViewProjectionMatrixUniform;
    private static FloatBuffer sViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sColorUniform;

    private static int sBallVertexBuffer;


    // new circle-based ball
    private static int sCircleBallVertexBufferIndex;
    private static FloatBuffer sCircleBallVertexBuffer;
    private final static int sPoints = 100;

    private static float[] genCircle2d(float rad, int points, float x, float y) {
        float[] verts = new float[points * 2 + 2];
        boolean first = true;
        float fx = 0;
        float fy = 0;
        int c = 0;
        for (int i = 0; i < points; ++i) {
            float fi = 2 * (float)Math.PI * i / points;
            float xa = rad * (float)(Math.sin(fi + Math.PI)) + x;
            float ya = rad * (float)(Math.cos(fi + Math.PI)) + y;
            if (first) {
                first = false;
                fx = xa;
                fy = ya;
            }
            verts[c] = xa;
            verts[c + 1] = ya;
            c += 2;
        }
        verts[c] = fx;
        verts[c + 1] = fy;
        return verts;
    }
    private static FloatBuffer sBallVertexBufferData;
    static {
        sBallVertexBufferData = BufferUtils.createFloatBuffer(6 * 2);
        sBallVertexBufferData
                .put(0f).put(1f)
                .put(1f).put(0f)
                .put(0f).put(-1f)
                .put(0f).put(1f)
                .put(-1f).put(0f)
                .put(0f).put(-1f)
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
        //sViewProjectionMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram,
        //        "uViewProjectionMatrix");
        sColorUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uColor");

        sBallVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, sBallVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, sBallVertexBufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //new circle-based ball
        sCircleBallVertexBufferIndex = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,sCircleBallVertexBufferIndex);
        sCircleBallVertexBuffer = BufferUtils.createFloatBuffer(sPoints * 4);
        sCircleBallVertexBuffer.put(genCircle2d(1,sPoints,0,0));
        sCircleBallVertexBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER,sCircleBallVertexBuffer,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public static void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
        glUseProgram(sPrototypeProgram);
        //glUniformMatrix4fv(sViewProjectionMatrixUniform, false,
        //        viewProjectionMatrix.get(sViewProjectionMatrixBuffer));
        glUseProgram(0);
    }

    public static final Renderer<Ball> BALL = new Renderer<Ball>() {
        @Override
        public void render(Ball ball) {
            glUseProgram(sPrototypeProgram);
            glBindBuffer(GL_ARRAY_BUFFER, sCircleBallVertexBufferIndex);
            glEnableVertexAttribArray(sPositionAttribute);
            glVertexAttribPointer(sPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
            // TODO
            glUniformMatrix4fv(sModelMatrixUniform, false, new Matrix4f().get(sModelMatrixBuffer));
            glUniform4fv(sColorUniform, sBallColorBuffer);
            glDrawArrays(GL_TRIANGLE_FAN, 0, sPoints);
            glDisableVertexAttribArray(sPositionAttribute);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);
        }
    };


}
