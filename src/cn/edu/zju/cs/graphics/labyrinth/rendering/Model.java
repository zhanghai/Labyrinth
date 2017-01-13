package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class Model {

    public AIScene mScene;
    public List<Mesh> mMeshes;
    public List<Material> mMaterials;

    public Model(AIScene scene) {

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

        public Material(AIMaterial material) {
            material = material;
        }
    }
}
