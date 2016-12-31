package cn.edu.zju.cs.graphics.labyrinth.model;

import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderable;
import cn.edu.zju.cs.graphics.labyrinth.rendering.Renderer;
import org.dyn4j.dynamics.Body;

public abstract class Entity<EntityType extends Entity<EntityType>> implements Renderable {

    private Body mBody;
    private Renderer<EntityType> mRenderer;

    public Entity(Body body, Renderer<EntityType> renderer) {
        mBody = body;
        mRenderer = renderer;
    }

    public Body getBody() {
        return mBody;
    }

    public Renderer<EntityType> getRenderer() {
        return mRenderer;
    }

    @Override
    public void render() {
        //noinspection unchecked
        mRenderer.render((EntityType) this);
    }
}
