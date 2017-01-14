package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Magnet;
import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import de.javagl.obj.ObjReader;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import de.javagl.obj.ObjData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengles.GLES20.*;

/**
 * Created by mcdreamy on 1/13/17.
 */

public class MagnetRenderer implements  Renderer<Magnet> {

    private static MagnetRenderer sInstance;
    private AIScene mScene;
    private Matrix4f mModelMatrix = new Matrix4f();
    private Matrix3f mTextureMatrix = new Matrix3f();
    private int mTexture;
    private FloatBuffer mVertexBufferData;
    private IntBuffer mElementArrayBufferData;
    private FloatBuffer mNormalArrayBufferData;

    private int mVertexBuffer;
    private int mElementArrayBuffer;
    private int mNormalArrayBuffer;
    private int mProgram;
    private int mPositionAttribute;
    private int mModelMatrixUniform;
    private int mViewProjectionMatrixUniform;
    private FloatBuffer mModelMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private FloatBuffer mViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

    public static MagnetRenderer getsInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new MagnetRenderer();
        }
        return sInstance;
    }

    private MagnetRenderer() throws IOException {
        InputStream objInputStream = new FileInputStream("res/cn/edu/zju/cs/graphics/labyrinth/obj/mag.obj");
        Obj obj = ObjReader.read(objInputStream);
        Obj triangulated = ObjUtils.convertToRenderable(obj);
        mVertexBufferData = ObjData.getVertices(triangulated);
        mElementArrayBufferData = ObjData.getFaceVertexIndices(triangulated);
        mNormalArrayBufferData = ObjData.getNormals(triangulated);
        mVertexBuffer = GlUtils.createVertexArrayBuffer(mVertexBufferData,GL_STATIC_DRAW);
        mElementArrayBuffer = GlUtils.createVertexArrayIndexBuffer(mElementArrayBufferData,GL_STATIC_DRAW);
        mProgram = GlUtils.createProgram(ResourceUtils.makeShaderResource("cube.vs"),
                ResourceUtils.makeShaderResource("cube.fs"));
    }

    public void render(Magnet magnet, Matrix4f viewProjectionMatrix) {
        mModelMatrix
                .identity()
                .translate(150f, 150f,
                        1f)
                .rotateX((float)Math.toRadians(90))
                .scale(6f,6f,6f)
                ;
        System.out.println(mVertexBufferData.get());
        mPositionAttribute = GlUtils.getAttribLocation(mProgram, "aPosition");
        glEnableVertexAttribArray(mPositionAttribute);
        //mTextureSizeUniform = GlUtils.getUniformLocation(mProgram, "uTextureSize");
        mModelMatrixUniform = GlUtils.getUniformLocation(mProgram, "uModelMatrix");
        mViewProjectionMatrixUniform = GlUtils.getUniformLocation(mProgram,
                "uViewProjectionMatrix");
        //mTextureUniform = GlUtils.getUniformLocation(mProgram, "uTexture");
        glUseProgram(mProgram);
        glBindBuffer(GL_ARRAY_BUFFER, mVertexBuffer);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mElementArrayBuffer);
        GlUtils.vertexAttribPointer(mPositionAttribute, 3);
        //glUniform2f(mTextureSizeUniform, textureWidth, textureLength);
        glUniformMatrix4fv(mModelMatrixUniform, false, mModelMatrix.get(mModelMatrixBuffer));
        glUniformMatrix4fv(mViewProjectionMatrixUniform, false,
                viewProjectionMatrix.get(mViewProjectionMatrixBuffer));
        //glActiveTexture(GL_TEXTURE0);
        //glBindTexture(GL_TEXTURE_2D, texture);
        //glUniform1i(mTextureUniform, 0);
        glDrawElements(GL_TRIANGLES, mElementArrayBufferData.remaining(), GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glUseProgram(0);

    }
}
