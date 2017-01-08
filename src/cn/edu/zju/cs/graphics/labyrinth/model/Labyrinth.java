package cn.edu.zju.cs.graphics.labyrinth.model;

import org.dyn4j.dynamics.World;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Labyrinth {

    private static final Comparator<Entity<?>> ENTITY_COMPARATOR = new Comparator<Entity<?>>() {
        @Override
        public int compare(Entity<?> entity1, Entity<?> entity2) {
            // TODO
            return 0;
        }
    };

    private SortedSet<Entity<?>> mEntities = new TreeSet<>(ENTITY_COMPARATOR);
    private World mWorld;
    {
        mWorld = new World();
        // TODO
        mWorld.getSettings().setRestitutionVelocity(0);
    }
    private boolean mWorldStarted;

    public void addEntity(Entity<?> entity) {
        mEntities.add(entity);
        mWorld.addBody(entity.getBody());
    }

    public void removeEntity(Entity<?> entity) {
        mWorld.removeBody(entity.getBody());
        mEntities.remove(entity);
    }

    public SortedSet<Entity<?>> getEntities() {
        return Collections.unmodifiableSortedSet(mEntities);
    }

    public void update(double elapsedTime) {
        if (!mWorldStarted) {
            mWorld.setAccumulatedTime(elapsedTime);
            mWorldStarted = true;
            return;
        }
        mWorld.update(elapsedTime, Integer.MAX_VALUE);
    }

    public World getWorld() {
        return mWorld;
    }
}
