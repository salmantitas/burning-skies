package com.euhedral.engine;

import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;

import java.awt.*;
import java.util.LinkedList;

public class Pool {
    private LinkedList<Entity> entities;
    private int reusable;
    private float camMarker;

    public Pool() {
        entities = new LinkedList<>();
        reusable = 0;
    }

    public void update() {
        camMarker = GameController.getCamera().getMarker();
            for (Entity entity : entities) {
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

    public void addEntity(int x, int y, EntityID id) {
        if (reusable > 0) {
            spawnFromPool(x,y,id);
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

    public void checkIfBelowScreen(int levelHeight) {
        for (Entity entity: entities) {
            if (entity.isActive())
                checkIfBelowScreen(entity, levelHeight);
        }
    }

    public void checkIfBelowScreen(Entity entity, int levelHeight) {
        int offset = 200;
        if (entity.getY() > levelHeight + offset ) {
            entity.disable();
            reusable++;
        }
    }

    public void checkIfAboveScreen() {
        for (Entity entity: entities) {
            if (entity.isActive())
                checkIfAboveScreen(entity);
        }
    }

    public void checkIfAboveScreen(Entity entity) {
        if (entity.getY() < camMarker + Utility.percHeight(30)) {
            entity.disable();
            increase();
        }
    }

    public void spawnFromPool(int x, int y, EntityID id) {
        Entity entity = findInList();
        entity.resurrect(x, y, id);
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, EntityID id, float angle) {
        Entity entity = findInList();
        entity.resurrect(x, y, id);
        MobileEntity mob = (MobileEntity) entity;
        mob.setAngle(angle);
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }
}
