package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Ball;
import cn.edu.zju.cs.graphics.labyrinth.model.Entity;
import cn.edu.zju.cs.graphics.labyrinth.model.Wall;
import org.dyn4j.geometry.Rectangle;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.stb.STBImage.*;

import java.io.IOException;
import java.nio.*;

import static cn.edu.zju.cs.graphics.labyrinth.DemoUtils.*;

import static org.lwjgl.opengles.GLES20.*;

public class PrototypeRenders {

    private static int sPrototypeProgram1, sPrototypeProgram2;
    private static int sPositionAttribute, sPositionAttribute2;
    private static Matrix4f sModelMatrix = new Matrix4f();
    private static int sModelMatrixUniform, sModelMatrixUniform2;
    private static FloatBuffer sModelMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sViewProjectionMatrixUniform;
    private static FloatBuffer sViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(16);
    private static int sColorUniform;
    private static int sTex1, sTex2, sTex;
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
                .put(0f).put(0.5f)
                .put(0.5f).put(0f)
                .put(0f).put(-0.5f)
                .put(0f).put(0.5f)
                .put(-0.5f).put(0f)
                .put(0f).put(-0.5f)
                .flip();
    }
    private static FloatBuffer sBallColorBuffer;
    static {
        sBallColorBuffer = BufferUtils.createFloatBuffer(4);
        sBallColorBuffer
                .put(0.5f).put(0.5f).put(0.5f).put(1f)
                .flip();
    }

    //wall
//    private static float sWallVertices[] = {
//  0          -1.0f, -1.0f, -1.0f,
//  1          1.0f, -1.0f, -1.0f,
//  2          1.0f,  1.0f, -1.0f,
//  3          -1.0f, 1.0f, -1.0f,
//  4          -1.0f, -1.0f,  1.0f,
//  5          1.0f, -1.0f,  1.0f,
//  6          1.0f,  1.0f,  1.0f,
//  7          -1.0f,  1.0f,  1.0f
//    };
    private static float sWallVertices[] = {
            -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,  1.0f, 1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,  1.0f, 1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,1.0f, -1.0f,  1.0f,1.0f,  1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,1.0f,  1.0f,  1.0f,1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,1.0f,  1.0f,  1.0f,-1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,-1.0f,  1.0f,  1.0f,-1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,-1.0f,  1.0f,  1.0f,-1.0f, -1.0f,  1.0f,
            -1.0f, 1.0f, -1.0f,-1.0f, -1.0f,  1.0f,-1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,-1.0f,  1.0f,  1.0f,1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,1.0f,  1.0f,  1.0f,1.0f, -1.0f,  1.0f,
            -1.0f, 1.0f, -1.0f,-1.0f, -1.0f, -1.0f,1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,1.0f, -1.0f, -1.0f,1.0f,  1.0f, -1.0f
    };

    private static byte sWallIndices[] = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };

    private static ByteBuffer sWallIndexBuffer;
    private static FloatBuffer sWallVertexBufferData;
    private static int sWallVertexBuffer;
    static {
        for (int i = 0; i<sWallVertices.length; ++i)
            sWallVertices[i] /=10;
        sCircleBallVertexBufferIndex = glGenBuffers();
        System.out.println(" " + sCircleBallVertexBufferIndex + sWallVertexBuffer);
        glBindBuffer(GL_ARRAY_BUFFER,sCircleBallVertexBufferIndex);
        sCircleBallVertexBuffer = BufferUtils.createFloatBuffer(sPoints * 4);
        sCircleBallVertexBuffer.put(genCircle2d(0.5f,sPoints,0,0));
        sCircleBallVertexBuffer.position(0);
        glBufferData(GL_ARRAY_BUFFER,sCircleBallVertexBuffer,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        sWallVertexBuffer = glGenBuffers();
        sWallVertexBufferData = BufferUtils.createFloatBuffer(sWallVertices.length * 4);
        sWallVertexBufferData.put(sWallVertices);
        sWallVertexBufferData.position(0);
        glBindBuffer(GL_ARRAY_BUFFER,sWallVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER,sWallVertexBufferData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        sWallIndexBuffer = ByteBuffer.allocate(sWallIndices.length);
        sWallIndexBuffer.put(sWallIndices);
        sWallIndexBuffer.position(0);


    }

    public static void initialize() throws IOException {

        sPrototypeProgram1 = GlUtils.createProgram(makeShaderResource("prototype.vs"),
                makeShaderResource("prototype.fs"));
        sPrototypeProgram2 = GlUtils.createProgram(makeShaderResource("prototype.vs"),
                makeShaderResource("prototype.fs"));
        sTex1 = GlUtils.getUniformLocation(sPrototypeProgram1,"uTexture");
        sTex2 = GlUtils.getUniformLocation(sPrototypeProgram2,"uTexture");
        genTexture(); genTexture2();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,1);

        glUniform1i(sTex1,1);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D,2);
        glUniform1i(sTex2,2);
        sPositionAttribute = GlUtils.getAttribLocation(sPrototypeProgram1, "aPosition");
        sModelMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram1, "uModelMatrix");
        sPositionAttribute2 = GlUtils.getAttribLocation(sPrototypeProgram2, "aPosition");
        sModelMatrixUniform2 = GlUtils.getUniformLocation(sPrototypeProgram2, "uModelMatrix");
        //sViewProjectionMatrixUniform = GlUtils.getUniformLocation(sPrototypeProgram,
        //        "uViewProjectionMatrix");
        //sColorUniform = GlUtils.getUniformLocation(sPrototypeProgram, "uColor");

        sBallVertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, sBallVertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, sBallVertexBufferData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //new circle-based ball



        sBallVertexBuffer = GlUtils.createVertexArrayBuffer(sBallVertexBufferData, GL_STATIC_DRAW);
        //sWallVertexBuffer = GlUtils.createVertexArrayBuffer(sWallVertexBufferData, GL_STATIC_DRAW);
    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public static void setViewProjectionMatrix(Matrix4f viewProjectionMatrix) {
        glUseProgram(sPrototypeProgram1);

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
        Rectangle rectangle = (Rectangle) wall.getBody().getFixture(0).getShape();
        return getModelMatrix(wall).scale((float) rectangle.getWidth(),
                (float) rectangle.getHeight(), 1f);
    }

    private static FloatBuffer getModelMatrixBufferForWall(Wall wall) {
        return getModelMatrixForWall(wall).get(sModelMatrixBuffer);
    }

    private static void renderPrototype(int vertexBuffer, FloatBuffer modelMatrixBuffer, int points,int way) {
        glUseProgram(sPrototypeProgram1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,1);
        glUniform1i(sTex1,0);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glEnableVertexAttribArray(sPositionAttribute);
        glVertexAttribPointer(sPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
        glUniformMatrix4fv(sModelMatrixUniform, false, modelMatrixBuffer);
        glDrawArrays(way, 0, points);
        glDisableVertexAttribArray(sPositionAttribute);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }

    public static final Renderer<Ball> BALL = new Renderer<Ball>() {
        @Override
        public void render(Ball ball) {

            renderPrototype(sCircleBallVertexBufferIndex, getModelMatrixBuffer(ball),sPoints,GL_TRIANGLE_FAN);
        }
    };

    public static final Renderer<Wall> WALL = new Renderer<Wall>() {
        @Override
        public void render(Wall wall) {

//            //renderPrototype(sWallVertexBuffer, getModelMatrixBufferForWall(wall),sPoints,GL_TRIANGLE_FAN);
            glUseProgram(sPrototypeProgram2);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D,2);
            glUniform1i(sTex2,1);
            glBindBuffer(GL_ARRAY_BUFFER,sWallVertexBuffer);
            glEnableVertexAttribArray(sPositionAttribute2);
            glVertexAttribPointer(sPositionAttribute,3,GL_FLOAT,false,0,0L);
            glUniformMatrix4fv(sModelMatrixUniform2,false,sModelMatrix.identity().get(sModelMatrixBuffer));
            glDrawArrays(GL_TRIANGLES,0,12*9);
            //glDrawElements(GL_TRIANGLES,sWallIndexBuffer);
            glDisableVertexAttribArray(sPositionAttribute2);
            glBindBuffer(GL_ARRAY_BUFFER,0);
            glUseProgram(0);
        }
    };

    private static void genTexture() throws IOException {
        int tex = glGenTextures();
        System.out.println(tex);
        glBindTexture(GL_TEXTURE_2D, 1);
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
    }
    private static void genTexture2() throws IOException {
        int tex = glGenTextures();
        System.out.println(tex);
        glBindTexture(GL_TEXTURE_2D, 2);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        ByteBuffer imageBuffer;
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        ByteBuffer image;
        imageBuffer = ioResourceToByteBuffer("cn/edu/zju/cs/graphics/labyrinth/texture/ball_tmp.jpg", 1024 * 8);
        image = stbi_load_from_memory(imageBuffer,w,h,comp,3);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,w.get(0),h.get(0),0,GL_RGB,GL_UNSIGNED_BYTE,image);
        stbi_image_free(image);

    }



}
