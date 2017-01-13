package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.util.GlUtils;
import cn.edu.zju.cs.graphics.labyrinth.util.ResourceUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.*;

public class ModelRenderer {

    private static ModelRenderer sInstance;

    private int mProgram;
    private int mVertexAttribute;
    private int mNormalAttribute;
    //private int mTextureCoordinateAttribute;
    private int mModelMatrixUniform;
    private int mViewProjectionMatrixUniform;
    //private int mTextureUniform;

    private FloatBuffer mModelMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);
    private FloatBuffer mViewProjectionMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

    public static ModelRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelRenderer();
        }
        return sInstance;
    }

    private ModelRenderer() throws IOException {
        mProgram = GlUtils.createProgram(ResourceUtils.makeShaderResource("model.vs"),
                ResourceUtils.makeShaderResource("model.fs"));
        mVertexAttribute = GlUtils.getAttribLocation(mProgram, "aVertex");
        glEnableVertexAttribArray(mVertexAttribute);
        mNormalAttribute = GlUtils.getAttribLocation(mProgram, "aNormal");
        glEnableVertexAttribArray(mNormalAttribute);
        //mTextureCoordinateAttribute = GlUtils.getAttribLocation(mProgram,
        //        "aTextureCoordinate");
        //glEnableVertexAttribArray(mTextureCoordinateAttribute);
        mModelMatrixUniform = GlUtils.getUniformLocation(mProgram, "uModelMatrix");
        mViewProjectionMatrixUniform = GlUtils.getUniformLocation(mProgram,
                "uViewProjectionMatrix");
        //mTextureUniform = GlUtils.getUniformLocation(mProgram, "uTexture");
    }

    public void render(Model model, Matrix4f modelMatrix, Matrix4f viewProjectionMatrix/*,
                       int texture*/) {
        glUseProgram(mProgram);
        for (Model.Mesh mesh : model.mMeshes){
            if (mesh.mVertexArrayBuffer == 0) {
                mesh.mVertexArrayBuffer = GlUtils.createVertexArrayBuffer(mesh.mMesh.mVertices(),
                        AIVector3D.SIZEOF);
            }
            glBindBuffer(GL_ARRAY_BUFFER, mesh.mVertexArrayBuffer);
            GlUtils.vertexAttribPointer(mVertexAttribute, 3);
            if (mesh.mNormalArrayBuffer == 0) {
                mesh.mNormalArrayBuffer = GlUtils.createVertexArrayBuffer(mesh.mMesh.mNormals(),
                        AIVector3D.SIZEOF);
            }
            glBindBuffer(GL_ARRAY_BUFFER, mesh.mNormalArrayBuffer);
            GlUtils.vertexAttribPointer(mNormalAttribute, 3);
            //if (mesh.mTextureCoordinateArrayBuffer == 0) {
            //    mesh.mTextureCoordinateArrayBuffer = GlUtils.createVertexArrayBuffer(
            //            mesh.mMesh.mTextureCoords(0), AIVector3D.SIZEOF);
            //}
            //glBindBuffer(GL_ARRAY_BUFFER, mesh.mTextureCoordinateArrayBuffer);
            //GlUtils.vertexAttribPointer(mTextureCoordinateAttribute,
            //        mesh.mMesh.mNumUVComponents().get(0));
            glUniformMatrix4fv(mModelMatrixUniform, false, modelMatrix.get(mModelMatrixBuffer));
            glUniformMatrix4fv(mViewProjectionMatrixUniform, false,
                    viewProjectionMatrix.get(mViewProjectionMatrixBuffer));
            //GlUtils.uniformTexture(mTextureUniform, GL_TEXTURE0, texture);
            if (mesh.mElementArrayBuffer == 0) {
                int faceCount = mesh.mMesh.mNumFaces();
                int elementCount = faceCount * 3;
                IntBuffer elementArrayBufferData = BufferUtils.createIntBuffer(elementCount);
                AIFace.Buffer facesBuffer = mesh.mMesh.mFaces();
                for (int i = 0; i < faceCount; ++i) {
                    AIFace face = facesBuffer.get(i);
                    if (face.mNumIndices() != 3) {
                        throw new IllegalStateException("AIFace.mNumIndices() != 3");
                    }
                    elementArrayBufferData.put(face.mIndices());
                }
                elementArrayBufferData.flip();
                mesh.mElementArrayBuffer = GlUtils.createElementArrayBuffer(elementArrayBufferData);
                mesh.mElementCount = elementCount;
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.mElementArrayBuffer);
            glDrawElements(GL_TRIANGLES, mesh.mElementCount, GL_UNSIGNED_INT, 0);
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }
}
