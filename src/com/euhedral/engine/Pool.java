package com.euhedral.engine;

import com.euhedral.game.Entities.Enemy.Enemy;

import java.util.LinkedList;

public class Pool {
    private LinkedList<Entity> entities;
    private int reusable;

    public Pool() {
        entities = new LinkedList<>();
        reusable = 0;
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

    private Entity findInList() {
        for (Entity e: entities) {
            if (!e.isActive())
                return e;
        }
        return null; // redundant, shouldn't happen
    }
}
