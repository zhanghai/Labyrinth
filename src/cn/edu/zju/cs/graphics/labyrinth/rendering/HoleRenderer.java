package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.Hole;

import java.io.IOException;

public class HoleRenderer extends BaseHoleRenderer<Hole> {

    private static final float TEXTURE_SCALE = 156f / 148f;

    private static HoleRenderer sInstance;

    public static HoleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new HoleRenderer();
        }
        return sInstance;
    }

    private HoleRenderer() throws IOException {}

    protected String getTextureResourceName() {
        return "hole.png";
    }

    @Override
    protected float getTextureScale() {
        return TEXTURE_SCALE;
    }
}
