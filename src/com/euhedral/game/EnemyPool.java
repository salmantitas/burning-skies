package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Enemy.Enemy;

public class EnemyPool extends Pool {
    private int[] reusable;

    public EnemyPool() {
        super();
        int enemyTypes = EntityHandler.enemyTypes;
        reusable = new int[enemyTypes];
        for (int i = 0; i < enemyTypes; i++) {
            reusable[i] = 0;
        }
    }

    public int getPoolSize(int enemyType) {
        return reusable[enemyType];
    }

    public void increase(int enemyType) {
        int tempNum = reusable[enemyType];
        tempNum++;
        reusable[enemyType] = tempNum;
    }

    public void increase(Entity entity, int enemyType) {
        entity.disable();
        increase(enemyType);
    }

    public void decrease(int enemyType) {
        int tempNum = reusable[enemyType];
        tempNum--;
        reusable[enemyType] = tempNum;
    }

    @Override
    public void clear() {
        int len = reusable.length;
        for (int i = 0; i < len; i ++) {
            reusable[i] = 0;
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
    }

}
