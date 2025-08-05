package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Bullet;
import com.euhedral.game.Entities.BulletEnemy;
import com.euhedral.game.Entities.BulletPlayer;
import com.euhedral.game.Entities.Enemy.Enemy;
import com.euhedral.game.Entities.Player;

import java.awt.*;
import java.util.LinkedList;

public class BulletPool extends Pool {
    Entity entity;
    Bullet collidingBullet;
    BulletEnemy bulletEnemy;
    BulletPlayer bulletPlayer;
    Bullet bullet;
    boolean intersectsEnemy;

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
        bullet.target = (Enemy) target;
        if (bullet.getForwardVelocity() != forwardVelocity) {
            bullet.setForwardVelocity(forwardVelocity);
        }
        decrease();
//        System.out.println("Pool: " + getPoolSize() + " | Total: " + getEntities().size());
    }

    public void spawnFromPool(int x, int y, double angle, double bulletVelocity, boolean tracking) {
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
            intersectsEnemy = bulletPlayer.getBounds().intersects(enemy.getBoundsHorizontal()) || bulletPlayer.getBounds().intersects(enemy.getBoundsVertical());
            if (bulletPlayer.isActive() && intersectsEnemy)
            {
                collidingBullet = bulletPlayer;
            }
        }
        return collidingBullet;
    }

    public void checkCollision(Player player) {
        for (Entity entity: entities) {
            bullet = (Bullet) entity;

            if (bullet.isActive() && player.checkCollision(bullet.getBounds())) {
//                bullets.increase(bullet);
                destroy(bullet, player);

                if (GameController.godMode) {

                } else
                    player.damage(10);
            }
        }
    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        if (VariableHandler.shield.getValue() <= 0)
            SoundHandler.playSound(SoundHandler.IMPACT);
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
                    bulletPlayer.target = null;
                }
            } else if (bulletPlayer.isActive()) {
                if (bulletPlayer.target != null) {
                    if (!bulletPlayer.target.isActive()) {
                        increase(bulletPlayer);
                    }
                }
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

}
