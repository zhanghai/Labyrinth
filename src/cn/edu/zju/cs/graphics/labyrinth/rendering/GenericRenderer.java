package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class GenericRenderer {

    private static GenericRenderer sInstance;

    private int mGenericProgram;
    private int mPositionAttribute;
    private int mTextureCoordinateAttribute;
    private int mModelMatrixUniform;
    private int mViewProjectionMatrixUniform;
    private int mTextureMatrixUniform;
    private int mTextureUniform;

    private FloatBuffer mModelMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private FloatBuffer mViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private FloatBuffer mTextureMatrixBuffer = BufferUtils.createFloatBuffer(3 * 3);

    public static GenericRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new GenericRenderer();
        }
        return sInstance;
    }

    private GenericRenderer() throws IOException {
        mGenericProgram = GlUtils.createProgram(makeShaderResource("generic.vs"),
                makeShaderResource("generic.fs"));
        mPositionAttribute = GlUtils.getAttribLocation(mGenericProgram, "aPosition");
        glEnableVertexAttribArray(mPositionAttribute);
        mTextureCoordinateAttribute = GlUtils.getAttribLocation(mGenericProgram,
                "aTextureCoordinate");
        glEnableVertexAttribArray(mTextureCoordinateAttribute);
        mModelMatrixUniform = GlUtils.getUniformLocation(mGenericProgram, "uModelMatrix");
        mViewProjectionMatrixUniform = GlUtils.getUniformLocation(mGenericProgram,
                "uViewProjectionMatrix");
        mTextureMatrixUniform = GlUtils.getUniformLocation(mGenericProgram, "uTextureMatrix");
        mTextureUniform = GlUtils.getUniformLocation(mGenericProgram, "uTexture");
    }

    private static String makeShaderResource(String name) {
        return "cn/edu/zju/cs/graphics/labyrinth/shader/" + name;
    }

    public void render(int vertexArrayBuffer, int positionSize, int elementArrayBuffer,
                       int elementCount, Matrix4f modelMatrix, Matrix4f viewProjectionMatrix,
                       Matrix3f textureMatrix, int texture) {
        glUseProgram(mGenericProgram);
        glBindBuffer(GL_ARRAY_BUFFER, vertexArrayBuffer);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
        final int TEXTURE_COORDINATE_SIZE = 2;
        GlUtils.vertexAttribPointer(mPositionAttribute, positionSize,
                positionSize + TEXTURE_COORDINATE_SIZE, 0);
        GlUtils.vertexAttribPointer(mTextureCoordinateAttribute, TEXTURE_COORDINATE_SIZE,
                positionSize + TEXTURE_COORDINATE_SIZE, positionSize);
        glUniformMatrix4fv(mModelMatrixUniform, false, modelMatrix.get(mModelMatrixBuffer));
        glUniformMatrix4fv(mViewProjectionMatrixUniform, false,
                viewProjectionMatrix.get(mViewProjectionMatrixBuffer));
        glUniformMatrix3fv(mTextureMatrixUniform, false, textureMatrix.get(mTextureMatrixBuffer));
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glUniform1i(mTextureUniform, 0);
        glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }
}
