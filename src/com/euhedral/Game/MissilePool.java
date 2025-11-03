package com.euhedral.Game;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Pool;
import com.euhedral.Engine.Position;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.Entities.Player;
import com.euhedral.Game.Entities.Projectile.Bullet;
import com.euhedral.Game.Entities.Projectile.BulletEnemy;
import com.euhedral.Game.Entities.Projectile.BulletPlayer;
import com.euhedral.Game.Entities.Projectile.MissilePlayer;

import java.awt.*;
import java.util.LinkedList;

public class MissilePool extends Pool {
    Entity entity;
    Bullet collidingBullet;
    BulletEnemy bulletEnemy;
    MissilePlayer bulletMissile;
    Bullet bullet;

    public MissilePool() {
        super();
    }

    public void spawn(int x, int y, double forwardVelocity) {
        if (getPoolSize() > 0) {
            spawnFromPool(x, y, forwardVelocity);
        } else
            add(new MissilePlayer(x, y, forwardVelocity));
    }

    public void spawnFromPool(double x, double y, double forwardVelocity) {
        entity = findInList();
        entity.resurrect(x, y);
        decrease();
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
            bulletMissile = (MissilePlayer) entity;
            boolean intersectsEnemy = enemy.checkCollision(bulletMissile);

            if (bulletMissile.isActive() && intersectsEnemy)
            {
                collidingBullet = bulletMissile;
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

            bulletMissile = (MissilePlayer) entity;

            if (bulletMissile.isImpacting()) {
                if (bulletMissile.checkDeathAnimationEnd()) {
                    increase(bulletMissile);
                    bulletMissile.resetEntity();
                }
            } else if (bulletMissile.isActive()) {
                if (!bulletMissile.isEntityNull()) {
                    if (!bulletMissile.isEntityActive()) {
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
