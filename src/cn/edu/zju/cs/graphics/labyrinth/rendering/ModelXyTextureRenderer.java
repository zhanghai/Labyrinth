package cn.edu.zju.cs.graphics.labyrinth.rendering;

import java.io.IOException;

public class ModelXyTextureRenderer extends BaseModelSpaceTextureRenderer {

    private static ModelXyTextureRenderer sInstance;

    public static ModelXyTextureRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new ModelXyTextureRenderer();
        }
        return sInstance;
    }

    protected ModelXyTextureRenderer() throws IOException {}

    @Override
    protected String getVertexShaderName() {
        return "model_xy_texture.vs";
    }
}
