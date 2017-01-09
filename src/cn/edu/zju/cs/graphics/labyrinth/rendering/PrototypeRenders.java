package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import org.dyn4j.geometry.Transform;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static cn.edu.zju.cs.graphics.labyrinth.DemoUtils.*;

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
    private static int sTex;
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
        int tex = genTexture();
        sTex = GlUtils.getUniformLocation(sPrototypeProgram,"uTexture");
        glUniform1i(sTex,tex);
        sPositionAttribute = GlUtils.getAttribLocation(sPrototypeProgram, "aPosition");
        sModelMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uModelMatrix");
        //sViewProjectionMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram,
        //        "uViewProjectionMatrix");
        //sColorUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uColor");

        sBallVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, sBallVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, sBallVertexBufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //new circle-based ball
        sCircleBallVertexBufferIndex = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,sCircleBallVertexBufferIndex);
        sCircleBallVertexBuffer = BufferUtils.createFloatBuffer(sPoints * 4);
        sCircleBallVertexBuffer.put(genCircle2d(0.5f,sPoints,0,0));
        sCircleBallVertexBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER,sCircleBallVertexBuffer,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public static void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
        glUseProgram(sPrototypeProgram);
        //glUniformMatrix4fv(sViewProjectionMatrixUniform, false, viewProjectionMatrix.get(sViewProjectionMatrixBuffer));
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
            glBindBuffer(GL_ARRAY_BUFFER, sCircleBallVertexBufferIndex);
            glEnableVertexAttribArray(sPositionAttribute);
            glVertexAttribPointer(sPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
            glUniformMatrix4fv(sModelMatrixUniform, false, getModelMatrixBuffer(ball));
            glUniform4fv(sColorUniform, sBallColorBuffer);
            glDrawArrays(GL_TRIANGLE_FAN, 0, sPoints);
            glDisableVertexAttribArray(sPositionAttribute);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);
        }
    };

    private static int genTexture() throws IOException {
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        ByteBuffer imageBuffer;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image;
        imageBuffer = ioResourceToByteBuffer("cn/edu/zju/cs/graphics/labyrinth/texture/ball.png", 1024 * 8);
        image = stbi_load_from_memory(imageBuffer,w,h,comp,3);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,w.get(0),h.get(0),0,GL_RGB,GL_UNSIGNED_BYTE,image);
        stbi_image_free(image);
        return tex;
    }
}
