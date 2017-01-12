package cn.edu.zju.cs.graphics.labyrinth;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES20.glGetUniformLocation;
import static org.lwjgl.opengles.GLES20.glUniform1i;

/**
 * Created by xuxin on 2017/1/12.
 */
public class Element {
    protected float mWidth;
    protected float mHeight;
    protected float mPosX;
    protected float mPosY;

    protected Matrix4f mModelMatrix = new Matrix4f();
    private int mTexId;
    private  int mProgram;
    private  int mPositionAttribute;
    private  int mModelMatrixUniform;
    private FloatBuffer mModelMatrixBuffer = BufferUtils.createFloatBuffer(16);

    private  int mButtonVertexBuffer;
    public static FloatBuffer mButtonVertexBufferData;
    static {
        mButtonVertexBufferData = BufferUtils.createFloatBuffer(6 * 2);
        mButtonVertexBufferData
                .put(1f).put(1f)
                .put(-1f).put(1f)
                .put(-1f).put(-1f)
                .put(1f).put(1f)
                .put(1f).put(-1f)
                .put(-1f).put(-1f)
                .flip();
    }
    public Element(){}
    public Element(float width, float height, float PosX, float PosY, int program){
        mWidth=width;
        mHeight=height;
        mPosX=PosX;
        mPosY=PosY;
        mButtonVertexBuffer = GlUtils.createVertexArrayBuffer(mButtonVertexBufferData, GL_STATIC_DRAW);
        mProgram=program;
        mPositionAttribute = GlUtils.getAttribLocation(mProgram, "aPosition");
        mModelMatrixUniform = GlUtils.getUniformLocation(mProgram, "uModelMatrix");

    }


    public void ModelTransform()
    {
        mModelMatrix
                .identity()
                .translate(mPosX, mPosY,0f)
                .scale(mWidth,mHeight, 1f);
    }

    public void Render(){
        ModelTransform();
        glUseProgram(mProgram);
        glBindTexture(GL_TEXTURE_2D, mTexId);
        glBindBuffer(GL_ARRAY_BUFFER, mButtonVertexBuffer);
        glEnableVertexAttribArray(mPositionAttribute);
        glVertexAttribPointer(mPositionAttribute, 2, GL_FLOAT, false, 0, 0L);
        glUniformMatrix4fv(mModelMatrixUniform, false, mModelMatrix.get(mModelMatrixBuffer));
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(mPositionAttribute);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glUseProgram(0);
    }

    public void setTexture(String texSource, int tex)throws IOException
    {
        mTexId=GlUtils.createTexture(texSource,8*1024);
        int texLocation = glGetUniformLocation(mProgram, "uTex");
        glUniform1i(texLocation, tex);
    }
}
