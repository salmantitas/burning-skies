package com.euhedral.engine;

import com.euhedral.game.Entities.Enemy.Enemy;
import com.euhedral.game.Entities.Pickup;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;

import java.awt.*;
import java.util.LinkedList;

public class Pool {
    protected LinkedList<Entity> entities;
    private int reusable;
    private float camMarker;

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
            entity.clear();
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

    public void disableIfBelowScreen(int levelHeight) {
        for (Entity entity: entities) {
            if (entity.isActive())
                disableIfBelowScreen(entity, levelHeight);
        }
    }

    public void disableIfBelowScreen(Entity entity, int levelHeight) {
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

//    public void spawnFromPool(int x, int y, String move, int time) {
//        Entity entity = findInList();
//        entity.resurrect(x, y);
//        Enemy enemy = (Enemy) entity;
//        enemy.setHMove(move);
//        enemy.setMovementDistance(time);
//        decrease();
//    }

    public void spawnFromPool(int x, int y, EntityID id) {
        Entity entity = findInList();
        entity.resurrect(x, y, id);
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, EntityID id, int value) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        Pickup pickup = (Pickup) entity;
        pickup.setValue(value);
        decrease();
    }

    public void spawnFromPool(int x, int y, double angle) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        MobileEntity mob = (MobileEntity) entity;
        if (mob.angle != angle) {
            mob.setAngle(angle);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, double angle, int bulletVelocity) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        MobileEntity mob = (MobileEntity) entity;
        if (mob.angle != angle) {
            mob.setAngle(angle);
        }
        if (mob.forwardVelocity != bulletVelocity) {
            mob.setForwardVelocity(bulletVelocity);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void printPool(String name) {
        System.out.println(name + " pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }
}
