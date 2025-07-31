package com.euhedral.engine;

import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;

import java.awt.*;
import java.util.LinkedList;

public class Pool {
    protected LinkedList<Entity> entities;
    private int reusable;
    private float camMarker;

    Entity entity;

    public Pool() {
        entities = new LinkedList<>();
        reusable = 0;
    }

    public void update() {
        camMarker = GameController.getCamera().getMarker();
            for (Entity entity : entities) {
                entity.update();
            }
    }

    public void render(Graphics g) {
        for (Entity entity: entities) {
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

    public void increase(Entity entity) {
        entity.disable();
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
        for (Entity entity: entities) {
            entity.clear(); // todo: instead of clearing, destroy themr
//            increase(entity);
        }
//        entities.clear();
    }

    public Entity findInList() throws NullPointerException {
        for (Entity e: entities) {
            if (e.isInactive())
                return e;
        }
        return null;
    }

    public void addAll(LinkedList<Entity> entities) {
        this.entities.addAll(entities);
    }

    public void disableIfOutsideBounds(int levelHeight) {
        for (Entity entity: entities) {
            if (entity.isActive())
                disableIfOutsideBounds(entity, levelHeight);
        }
    }

    public void disableIfOutsideBounds(Entity entity, int levelHeight) {

        if (entity.canDisable()) {
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
        entity = findInList();
        entity.resurrect(x, y, id);
        decrease();
    }

    public void printPool(String name) {
        System.out.println(name + " pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }
}
