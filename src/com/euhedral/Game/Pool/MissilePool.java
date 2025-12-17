package com.euhedral.Game.Pool;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Pool;
import com.euhedral.Engine.Position;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.Entities.Player;
import com.euhedral.Game.Entities.Projectile.*;
import com.euhedral.Game.GameController;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.util.LinkedList;

public class MissilePool extends BulletPool {
    Entity entity;
    Bullet collidingBullet;
    BulletEnemy bulletEnemy;
    MissileEnemy missile;
    Bullet bullet;

    public MissilePool() {
        super();
    }

    public void spawn(double x, double y) {
        if (getPoolSize() > 0) {
            spawnFromPool(x, y);
        } else
            add(new MissileEnemy(x, y, 90));
    }

    public void spawnFromPool(double x, double y) {
        entity = findInList();
        entity.resurrect(x, y);
        decrease();
    }

//    public void renderReflections(Graphics2D g2d, float transparency) {
//        for (Entity entity : entities) {
//            bullet = (Bullet) entity;
//            bullet.renderReflection(g2d, transparency);
//        }
//    }
//
//    public Bullet checkCollision(Enemy enemy) {
//        collidingBullet = null;
//        for (Entity entity : entities) {
//            missile = (MissileEnemy) entity;
//            boolean intersectsEnemy = enemy.checkCollision(missile);
//
//            if (missile.isActive() && intersectsEnemy)
//            {
//                collidingBullet = missile;
//                return collidingBullet;
//            }
//        }
//        return collidingBullet;
//    }
//
//    public void checkCollision(Player player) {
//        for (Entity entity: entities) {
//            bullet = (Bullet) entity;
//
//            if (bullet.isActive() && player.checkCollision(bullet)) {
//                destroy(bullet, player);
//
//                if (GameController.godMode) {
//                    return;
//                }
//
//                int bulletDamage = 10;
//                player.damage((int ) (bulletDamage * Difficulty.getDamageTakenMult()) );
//            }
//        }
//    }
//
//    private void destroy(Bullet bullet, MobileEntity entity) {
//        if (VariableHandler.shield.getValue() <= 0)
//            SoundHandler.playSound(SoundHandler.IMPACT_PLAYER);
//        bullet.destroy(entity);
//    }
//
//    public void checkDeathAnimationEnd() {
//        for (Entity entity : entities) {
//
//            bullet = (Bullet) entity;
//
//            if (bullet.isImpacting()) {
//                if (bullet.checkDeathAnimationEnd()) {
//                    increase(bullet);
//                    bullet.resetEntity();
//                    bullet.resetBounds();
//                }
//            }
//        }
//    }
//
//    public void checkDeathAnimationEndPlayer() {
//        for (Entity entity : entities) {
//
//            missile = (MissileEnemy) entity;
//
//            if (missile.isImpacting()) {
//                if (missile.checkDeathAnimationEnd()) {
//                    increase(missile);
//                    missile.resetEntity();
//                }
//            } else if (missile.isActive()) {
//                if (!missile.isEntityNull()) {
//                    if (!missile.isEntityActive()) {
////                        increase(bulletPlayer);
//                    }
//                }
//            }
//        }
//    }
//
//    public void destroyIfWithinRadius(Position pos, int radius) {
//        for (int i = 0; i < entities.size(); i ++) {
//            entity = entities.get(i);
//            if (entity.isActive())
//                destroyIfWithinRadiusHelper(pos, radius);
//        }
//    }
//
//    private void destroyIfWithinRadiusHelper(Position pos, int radius) {
//        bulletEnemy = (BulletEnemy) entity;
//        if (bulletEnemy.inRadius(pos,radius)) {
//            if (bulletEnemy.isBelowDeadZoneTop()) {
//                destroy(bulletEnemy);
//            }
//        }
//    }
//
//    public LinkedList<Bullet> getImpactingBulletsList() {
//        LinkedList<Bullet> impactingBullets = new LinkedList<>();
//
//        for (Entity entity: entities) {
//            Bullet bullet = (Bullet) entity;
//            if (bullet.isImpacting())
//                impactingBullets.add(bullet);
//        }
//
//        return impactingBullets;
//    }
//
//    public void destroy(BulletEnemy bulletEnemy) {
//        bulletEnemy.destroy();
//    }

}
