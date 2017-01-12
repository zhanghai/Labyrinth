package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.joml.Matrix4f;

public interface Renderable {

    void render(Matrix4f viewProjectionMatrix);
}
