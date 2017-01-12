package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class GenericRenderer {

    private static GenericRenderer sInstance;

    private int mProgram;
    private int mPositionAttribute;
    private int mTextureCoordinateAttribute;
    private int mModelMatrixUniform;
    private int mViewProjectionMatrixUniform;
    private int mTextureUniform;

    private FloatBuffer mModelMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private FloatBuffer mViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

    public static GenericRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new GenericRenderer();
        }
        return sInstance;
    }

    private GenericRenderer() throws IOException {
        mProgram = GlUtils.createProgram(ResourceUtils.makeShaderResource("generic.vs"),
                ResourceUtils.makeShaderResource("generic.fs"));
        mPositionAttribute = GlUtils.getAttribLocation(mProgram, "aPosition");
        glEnableVertexAttribArray(mPositionAttribute);
        mTextureCoordinateAttribute = GlUtils.getAttribLocation(mProgram,
                "aTextureCoordinate");
        glEnableVertexAttribArray(mTextureCoordinateAttribute);
        mModelMatrixUniform = GlUtils.getUniformLocation(mProgram, "uModelMatrix");
        mViewProjectionMatrixUniform = GlUtils.getUniformLocation(mProgram,
                "uViewProjectionMatrix");
        mTextureUniform = GlUtils.getUniformLocation(mProgram, "uTexture");
    }

    public void render(int vertexArrayBuffer, int positionSize, int elementArrayBuffer,
                       int elementCount, Matrix4f modelMatrix, Matrix4f viewProjectionMatrix,
                       int texture) {
        glUseProgram(mProgram);
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
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        glUniform1i(mTextureUniform, 0);
        glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }
}
