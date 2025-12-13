package com.euhedral.Game;

import com.euhedral.Engine.*;
import com.euhedral.Game.Entities.Projectile.Bullet;
import com.euhedral.Game.Entities.Projectile.BulletEnemy;
import com.euhedral.Game.Entities.Projectile.BulletPlayer;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.Entities.Player;

import java.awt.*;
import java.util.LinkedList;

public class BulletPool extends Pool {
    Entity entity;
    Bullet collidingBullet;
    BulletEnemy bulletEnemy;
    BulletPlayer bulletPlayer;
    Bullet bullet;

    public BulletPool() {
        super();
    }

    public void spawn(int x, int y, double forwardVelocity, double angle) {
        if (getPoolSize() > 0) {
            spawnFromPool(x, y, forwardVelocity, angle);
        } else
            add(new BulletPlayer(x, y, forwardVelocity, angle));
    }

    public void spawn(int x, int y, double forwardVelocity, Enemy target) {
        if (getPoolSize() > 0) {
            spawnFromPool(x, y, forwardVelocity, target);
        } else
            add(new BulletPlayer(x, y, forwardVelocity, target));
    }

    public void spawnFromPool(double x, double y, double forwardVelocity, double angle) {
        entity = findInList();
        entity.resurrect(x, y);
        Bullet bullet = (Bullet) entity;
        if (bullet.getAngle() != angle) {
            bullet.setAngle(angle);
        }
        if (bullet.getForwardVelocity() != forwardVelocity) {
            bullet.setForwardVelocity(forwardVelocity);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(double x, double y, double forwardVelocity, Entity target) {
        entity = findInList();
        entity.resurrect(x, y);
        BulletPlayer bullet = (BulletPlayer) entity;
        bullet.setEntity((MobileEntity) target);
        if (bullet.getForwardVelocity() != forwardVelocity) {
            bullet.setForwardVelocity(forwardVelocity);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(double x, double y, double angle, double bulletVelocity, boolean tracking) {
        entity = findInList();
        entity.resurrect(x, y);
        bullet = (Bullet) entity;
        if (bullet.getAngle() != angle) {
            bullet.setAngle(angle);
        }
        if (bullet.getForwardVelocity() != bulletVelocity) {
            bullet.setForwardVelocity(bulletVelocity);
        }

        // todo: find a better way than InstanceOf
        if (bullet instanceof BulletEnemy) {
            bulletEnemy = (BulletEnemy) bullet;
            if (bulletEnemy.isTracking() != tracking) {
                bulletEnemy.setTracking(tracking);
            }
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void renderReflections(Graphics2D g2d, float transparency) {
        for (Entity entity : entities) {
            bullet = (Bullet) entity;
            bullet.renderReflection(g2d, transparency);
        }
    }

    public Bullet checkCollision(Enemy enemy) {
        collidingBullet = null;
        for (Entity entity : entities) {
            bulletPlayer = (BulletPlayer) entity;
            boolean intersectsEnemy = enemy.checkCollision(bulletPlayer);

            if (bulletPlayer.isActive() && intersectsEnemy)
            {
                collidingBullet = bulletPlayer;
                return collidingBullet;
            }
        }
        return collidingBullet;
    }

    public void checkCollision(Player player) {
        for (Entity entity: entities) {
            bullet = (Bullet) entity;

            if (bullet.isActive() && player.checkCollision(bullet)) {
                destroy(bullet, player);

                if (GameController.godMode) {
                    return;
                }

                int bulletDamage = 10;
                player.damage((int ) (bulletDamage * Difficulty.getDamageTakenMult()) );
            }
        }
    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        if (VariableHandler.shield.getValue() <= 0)
            SoundHandler.playSound(SoundHandler.IMPACT_PLAYER);
        bullet.destroy(entity);
    }

//    public void damagePlayer(int num){
//        if (GameController.godMode) {
//
//        } else
//            player.damage(num);
//    }

    public void checkDeathAnimationEnd() {
        for (Entity entity : entities) {

            bullet = (Bullet) entity;

            if (bullet.isImpacting()) {
                if (bullet.checkDeathAnimationEnd()) {
                    increase(bullet);
                    bullet.resetEntity();
                    bullet.resetBounds();
                }
            }
        }
    }

    public void checkDeathAnimationEndPlayer() {
        for (Entity entity : entities) {

            bulletPlayer = (BulletPlayer) entity;

            if (bulletPlayer.isImpacting()) {
                if (bulletPlayer.checkDeathAnimationEnd()) {
                    increase(bulletPlayer);
                    bulletPlayer.resetEntity();
                }
            } else if (bulletPlayer.isActive()) {
                if (!bulletPlayer.isEntityNull()) {
                    if (!bulletPlayer.isEntityActive()) {
//                        increase(bulletPlayer);
                    }
                }
            }
        }
    }

    public void destroyIfWithinRadius(Position pos, int radius) {
        for (int i = 0; i < entities.size(); i ++) {
            entity = entities.get(i);
            if (entity.isActive())
                destroyIfWithinRadiusHelper(pos, radius);
        }
    }

    private void destroyIfWithinRadiusHelper(Position pos, int radius) {
        bulletEnemy = (BulletEnemy) entity;
        if (bulletEnemy.inRadius(pos,radius)) {
            if (bulletEnemy.isBelowDeadZoneTop()) {
                destroy(bulletEnemy);
            }
        }
    }

    public LinkedList<Bullet> getImpactingBulletsList() {
        LinkedList<Bullet> impactingBullets = new LinkedList<>();

        for (Entity entity: entities) {
            Bullet bullet = (Bullet) entity;
            if (bullet.isImpacting())
                impactingBullets.add(bullet);
        }

        return impactingBullets;
    }

    public void destroy(BulletEnemy bulletEnemy) {
        bulletEnemy.destroy();
    }

}
