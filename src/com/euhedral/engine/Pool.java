package com.euhedral.engine;

import java.awt.*;
import java.util.LinkedList;

public class Pool {
    private LinkedList<Entity> entities;
    private int reusable;

    public Pool() {
        entities = new LinkedList<>();
        reusable = 0;
    }

    public void update() {
        for (Entity entity: entities) {
            if (entity.isActive())
                entity.update();
        }
    }

    public void render(Graphics g) {
        for (Entity entity: entities) {
            if (entity.isActive())
                entity.render(g);
        }
    }

    public void addEntity() {
        if (reusable > 0) {

            Entity entity = findInList();
            if (entity != null) {
                entity.resurrect();
                reusable--;
            }
        } else {
            /*
            * Entity entity = new Entity();
            * list.add(entity);
            * */
        }
    }

    public int getPoolSize() {
        return reusable;
    }

    public void increase() {
        reusable++;
    }

    public void decrease() {
        reusable--;
    }

    public void add(Entity entity) {
        entities.add(entity);
    }

    public void destroy(Entity entity) {
        entity.disable();
    }

    public LinkedList<Entity> getEntities() {
        return entities;
    }

    public void clear() {
        reusable = 0;
        entities.clear();
    }

    public Entity findInList() throws NullPointerException {
        for (Entity e: entities) {
            if (!e.isActive())
                return e;
        }
        return null;
    }

    public void addAll(LinkedList<Entity> entities) {
        this.entities.addAll(entities);
    }

    public void checkIfBelowScreen(Entity entity, int levelHeight) {
        int offset = 200;
        if (entity.getY() > levelHeight + offset ) {
            entity.disable();
            reusable++;
        }
    }
}
