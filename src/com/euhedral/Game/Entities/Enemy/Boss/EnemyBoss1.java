package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Projectile.BulletEnemy;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.EntityID;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Pool.EnemyPool;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyBoss1 extends EnemyBoss {

    int MISSILES = 1;

    public EnemyBoss1(double x, double y, ProjectilePool projectiles, EnemyPool enemies, int levelHeight) {
        super(x,y, projectiles, enemies, levelHeight);
        height = 128;
        width = height * 2;
        pos.x = x - width/2;

        destination = new Position(0, 490);
        minX = Utility.percWidth(25);
        maxX = Utility.percWidth(75) - (int) 1.8 * width;

        currentGun = 0;
        guns_MAX = 4;
        shotMode = 0;
        shotMode_MAX = guns_MAX + 1;

        color = Color.orange;

        turretOffsetY = height/2 + 8;

        velX = 2;
        velY = offscreenVelY;
        health_MAX = 50;
        setHealth(health_MAX);
        shootTimerDefault = 30;

        moveLeft = true;
        setImage(textureHandler.enemyBoss[0]);
    }

//    @Override
//    public void update() {
//        super.update();
//        move();
//    }

//    @Override
//    public void render(Graphics g) {
//        super.render(g);
//        g.setColor(color);
//        g.fillRect((int) pos.x, (int) pos.y, width, height);
//    }


//    @Override
//    protected void shoot() {
//        super.shoot();
//    }

    @Override
    protected void shootDefault() {
        if (shotMode < guns_MAX) {
            bulletsPerShot++;
            shotMode++;
        } else if (shotMode < shotMode_MAX) {
            bulletsPerShot += guns_MAX;
            shotMode++;
            shootState = MISSILES;
        }
        else {
            shootState = BULLET;
            enemies.spawnEntity(pos.intX(), pos.intY(), VariableHandler.TYPE_DRONE1, 0, 0);
            shotMode = 0;
            currentGun = 0;
            }
    }

    @Override
    protected void resetShootTimer() {
        double factor = 1;

        if (health < health_MAX/2) {
            factor *= 0.5;
        }

        if (health < health_MAX/4) {
            factor *= 0.5;
        }

        if (shotMode == guns_MAX) {
            factor *= 2;
        }
        shootTimer = (int) (factor * shootTimerDefault / Difficulty.getEnemyFireRateMult());
    }

    @Override
    public void moveInScreen() {
        if (destination.y >= pos.y + height) {
            pos.y += velY;
        }
        else {
            if (health > 3 * health_MAX / 4) {

            } else {
                if (!moveLeft)
                    pos.x += velX;
                else
                    pos.x -= velX;
            }
        }

        if (pos.x < minX)
            moveLeft = false;
        if (pos.x > maxX)
            moveLeft = true;
    }

    // Private Methods

    // Needs to override


//    @Override
//    protected void setEnemyType() {
//        enemyType = VariableHandler.enemyTypes;
//    }


    @Override
    public double getTurretX() {
        int ends = 16 + 4;
        turretOffsetX = currentGun * (width)/guns_MAX;
        updateGun();

        return ends + super.getTurretX();
    }

    protected void updateGun() {
        currentGun++;
        if (currentGun >= guns_MAX) {
            currentGun = 0;
        }
    }

    @Override
    protected void spawnProjectiles() {
        if (shootState == BULLET)
            super.spawnProjectiles();
        else {
            spawnMissiles();
        }
    }

    protected void spawnBullet() {
        double x = getTurretX();
        double dir = getBulletAngle();
        double y = getTurretY();
        double bulletVelocity = getBulletVelocity();
        boolean tracking = this.tracking;

        if (health < health_MAX/2) {
            dir = calculateAngle(EntityHandler.playerPositon.x, EntityHandler.playerPositon.y);
        }

        if (projectiles.bullets.getPoolSize() > 0) {
            projectiles.bullets.spawnFromPool(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking);
        }
        else {
            projectiles.bullets.add(new BulletEnemy(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking));
        }

//        bullets.printPool("Enemy Bullet");
    }

    private void spawnMissiles() {
        double x = getTurretX();
        double y = getTurretY();

        double angle = 90;

        if (health < health_MAX/2) {
            angle = calculateAngle(EntityHandler.playerPositon.x, EntityHandler.playerPositon.y);
        }

        projectiles.missiles.spawn(x, y, angle);
    }
}
