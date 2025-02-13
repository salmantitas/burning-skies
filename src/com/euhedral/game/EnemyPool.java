package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.Pool;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.util.ArrayList;

public class EnemyPool extends Pool {
    private int[] active;
    private int[] reusable;
    private ArrayList<Integer> exclusionZones;

    public EnemyPool() {
        super();
        int enemyTypes = EntityHandler.enemyTypes;
        reusable = new int[enemyTypes];
        for (int i = 0; i < enemyTypes; i++) {
            reusable[i] = 0;
        }

        active = new int[enemyTypes];
        for (int i = 0; i < enemyTypes; i++) {
            active[i] = 0;
        }

        exclusionZones = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            exclusionZones.add(-100);
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

//        // render exclusion zone
//        g.setColor(Color.RED);
//        for (int i = 0; i < 10; i++) {
//            int x = exclusionZones.get(i);
//            if (x != -100) {
//                g.drawRect((int) x*64, 400, 64, 64);
//            }
//        }
    }

    public int getPoolSize(int enemyType) {
        return reusable[enemyType];
    }

    public void increase(int enemyType) {
        int tempNum = reusable[enemyType];
        tempNum++;
        reusable[enemyType] = tempNum;

        decreaseActive(enemyType);
    }

    public void increase(Entity entity, int enemyType) {
        entity.disable();

        int entityX = (int) entity.getX()/64;

        if (enemyType == EntityHandler.TYPE_STATIC) {
            // find spot in exclusion zone
            for (int i = 0; i < 10; i++) {
                if (exclusionZones.get(i) == entityX) {
                    exclusionZones.set(i, -100);
                }
            }
        }

        increase(enemyType);
    }

    public void decrease(int enemyType) {
        int tempNum = reusable[enemyType];
        tempNum--;
        reusable[enemyType] = tempNum;

        increaseActive(enemyType);
    }

    @Override
    public void add(Entity entity) {
        super.add(entity);
        increaseActive(entity);
    }

    @Override
    public void clear() {
        int len = reusable.length;
        for (int i = 0; i < len; i ++) {
            reusable[i] = 0;
            active[i] = 0;
            exclusionZones.set(i, -100);
        }

        super.clear();
    }

    public Entity findInList(int enemyType) throws NullPointerException {
        for (Entity e: entities) {
            Enemy enemy = (Enemy) e;
            int enemyType2 = enemy.getEnemyType();
            if (enemyType == enemyType2) {
                if (e.isInactive())
                    return e;
            }
        }
        return null;
    }

    public void spawnFromPool(int x, int y, int enemyType, String move, int time) {
        Entity entity = findInList(enemyType);
        entity.resurrect(x, y);
        Enemy enemy = (Enemy) entity;
        enemy.setHMove(move);
        enemy.setMovementDistance(time);
        decrease(enemy.getEnemyType());

//        int tempNum = active[enemyType];

        addExclusionZone(entity, enemyType);
    }

    @Override
    public void disableIfBelowScreen(Entity entity, int levelHeight) {
        int offset = 200;

        Enemy enemy = (Enemy) entity;
        int enemyType = enemy.getEnemyType();

        if (enemy.getY() > levelHeight + offset ) {
            enemy.disable();
            increase(enemyType);
        }
    }

    public int getActive(int enemyType) {
        return active[enemyType];
    }

    public void increaseActive(int enemyType) {
        int tempNum = active[enemyType];
        tempNum++;
        active[enemyType] = tempNum;
    }

    public void increaseActive(Entity entity) {
        int enemyType = ((Enemy) entity).getEnemyType();
        int tempNum = active[enemyType];

        addExclusionZone(entity, enemyType);

        tempNum++;
        active[enemyType] = tempNum;
    }

    public void decreaseActive(int enemyType) {
        int tempNum = active[enemyType];
        tempNum--;
        active[enemyType] = tempNum;
    }

    private void addExclusionZone(Entity entity, int enemyType) {
        if (enemyType == EntityHandler.TYPE_STATIC) {
            boolean done = false;
            for (int i = 0; i < 10; i++) {
                if (done) { }
                else {
                    if (exclusionZones.get(i) == -100) {
                        exclusionZones.set(i, ((int) entity.getX()/64));
                        done = true;
                    }
                }
            }

            for (int i = 0; i < 10; i++) {
                Utility.log("Ex " + i + ": " + exclusionZones.get(i));
            }
        }
    }

    public ArrayList<Integer> getExclusionZones() {
        return exclusionZones;
    }

}
