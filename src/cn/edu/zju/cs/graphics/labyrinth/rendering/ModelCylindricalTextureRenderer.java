package cn.edu.zju.cs.graphics.labyrinth.rendering;

import java.io.IOException;

public class ModelCylindricalTextureRenderer extends BaseModelSpaceTextureRenderer {

    private static ModelCylindricalTextureRenderer sInstance;

    public static ModelCylindricalTextureRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelCylindricalTextureRenderer();
        }
        return sInstance;
    }

    private ModelCylindricalTextureRenderer() throws IOException {}

    @Override
    protected String getVertexShaderName() {
        return "model_cylindrical_texture.vs";
    }
}
