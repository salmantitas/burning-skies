package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;

public class BulletPool extends Pool {
    public BulletPool() {
        super();
    }

    public void spawnFromPool(int x, int y, double angle) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        MobileEntity mob = (MobileEntity) entity;
        if (mob.getAngle() != angle) {
            mob.setAngle(angle);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, double angle, int bulletVelocity) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        MobileEntity mob = (MobileEntity) entity;
        if (mob.getAngle() != angle) {
            mob.setAngle(angle);
        }
        if (mob.getForwardVelocity() != bulletVelocity) {
            mob.setForwardVelocity(bulletVelocity);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }
}
