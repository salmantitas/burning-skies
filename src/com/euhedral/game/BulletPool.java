package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Bullet;
import com.euhedral.game.Entities.BulletEnemy;

public class BulletPool extends Pool {
    public BulletPool() {
        super();
    }

    public void spawnFromPool(int x, int y, double angle) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        BulletEnemy bullet = (BulletEnemy) entity;
        if (bullet.getAngle() != angle) {
            bullet.setAngle(angle);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, double angle, double bulletVelocity, boolean tracking) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        BulletEnemy bullet = (BulletEnemy) entity;
        if (bullet.getAngle() != angle) {
            bullet.setAngle(angle);
        }
        if (bullet.getForwardVelocity() != bulletVelocity) {
            bullet.setForwardVelocity(bulletVelocity);
        }
        if (bullet.isTracking() != tracking) {
            bullet.setTracking(tracking);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }
}
