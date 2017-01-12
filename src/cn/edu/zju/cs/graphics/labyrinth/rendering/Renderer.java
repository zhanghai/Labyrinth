package cn.edu.zju.cs.graphics.labyrinth.rendering;

import org.joml.Matrix4f;

public interface Renderer<EntityType> {

    void render(EntityType entity, Matrix4f ViewProjectionMatrix);
}
