package cn.edu.zju.cs.graphics.labyrinth.rendering;

import cn.edu.zju.cs.graphics.labyrinth.model.FinishHole;

import java.io.IOException;

public class FinishHoleRenderer extends BaseHoleRenderer<FinishHole> {

    private static final float TEXTURE_SCALE = 170f / 156f;

    private static FinishHoleRenderer sInstance;

    public static FinishHoleRenderer getInstance() throws IOException {
        if (sInstance == null) {
            sInstance = new FinishHoleRenderer();
        }
        return sInstance;
    }

    private FinishHoleRenderer() throws IOException {}

    protected String getTextureResourceName() {
        return "finish-hole.png";
    }

    @Override
    protected float getTextureScale() {
        return TEXTURE_SCALE;
    }
}
