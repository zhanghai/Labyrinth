package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMaterialProperty;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_AMBIENT;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_DIFFUSE;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_SPECULAR;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiReleaseImport;
import static org.lwjgl.assimp.Assimp.aiTextureType_AMBIENT;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

public class Model {

    public AIScene mScene;
    public List<Mesh> mMeshes;
    public List<Material> mMaterials;

    public Model(AIScene scene) {

        mScene = scene;

        int meshCount = scene.mNumMeshes();
        PointerBuffer meshesBuffer = scene.mMeshes();
        mMeshes = new ArrayList<>();
        for (int i = 0; i < meshCount; ++i) {
            mMeshes.add(new Mesh(AIMesh.create(meshesBuffer.get(i))));
        }

        int materialCount = scene.mNumMaterials();
        PointerBuffer materialsBuffer = scene.mMaterials();
        mMaterials = new ArrayList<>();
        for (int i = 0; i < materialCount; ++i) {
            mMaterials.add(new Material(AIMaterial.create(materialsBuffer.get(i))));
        }
    }

    // FIXME
    @Override
    public void finalize() {
        aiReleaseImport(mScene);
    }

    public static class Mesh {

        public AIMesh mMesh;
        public int mVertexArrayBuffer;
        public int mNormalArrayBuffer;
        //public int mTextureCoordinateArrayBuffer;
        public int mElementArrayBuffer;
        public int mElementCount;

        public Mesh(AIMesh mesh) {
            mMesh = mesh;
        }
    }

    public static class Material {

        public AIMaterial mMaterial;
        public AIColor4D mAmbientColor;
        public AIColor4D mDiffuseColor;
        public AIColor4D mSpecularColor;

        public Material(AIMaterial material) {

            mMaterial = material;

            mAmbientColor = AIColor4D.create();
            if (aiGetMaterialColor(mMaterial, AI_MATKEY_COLOR_AMBIENT,
                    aiTextureType_NONE, 0, mAmbientColor) != 0) {
                throw new IllegalStateException("aiGetMaterialColor");
            }
            mDiffuseColor = AIColor4D.create();
            if (aiGetMaterialColor(mMaterial, AI_MATKEY_COLOR_DIFFUSE,
                    aiTextureType_NONE, 0, mDiffuseColor) != 0) {
                throw new IllegalStateException("aiGetMaterialColor");
            }
            mSpecularColor = AIColor4D.create();
            if (aiGetMaterialColor(mMaterial, AI_MATKEY_COLOR_SPECULAR,
                    aiTextureType_NONE, 0, mSpecularColor) != 0) {
                throw new IllegalStateException("aiGetMaterialColor");
            }
        }
    }
}
