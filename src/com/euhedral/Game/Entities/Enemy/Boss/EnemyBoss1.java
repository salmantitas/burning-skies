package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
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

    private Tracker tracker;
    protected Turret[] turrets;

    int MISSILES = 1;

    int bossStage = 0;

    public EnemyBoss1(double x, double y, ProjectilePool projectiles, EnemyPool enemies, int levelHeight) {
        super(x,y, projectiles, enemies, levelHeight);
        height = 128;
        width = height * 2;
        pos.x = x - width/2;

        destination = new Position(0, 490);
        minX = Utility.percWidth(25);
        maxX = Utility.percWidth(75) - (int) 1.8 * width;

        tracker = new Tracker();

        currentGun = 0;
        guns_MAX = 4;
        shotMode = 0;
        shotMode_MAX = guns_MAX;

        turretOffsetY = height/2 + 8;

        turrets = new Turret[guns_MAX];
        for (int i = 0; i < 4; i++) {
            int ends = 16 + 4;
            turretOffsetX = i * (width)/guns_MAX;
            turrets[i] = new Turret(ends + turretOffsetX,turretOffsetY,this);
        }

        turrets[0].setAngle(70);
        turrets[3].setAngle(110);

        color = Color.orange;

        velX = 2;
        velY = offscreenVelY;
        health_MAX = 50;
        setHealth(health_MAX);
        shootTimerDefault = 30;

        moveLeft = true;
        setImage(textureHandler.enemyBoss[0]);
    }

    @Override
    public void update() {
        if (bossStage == 0) {
            if (health < 3* health_MAX / 4) {
                bossStage = 1;
            }
        }

        if (bossStage == 1) {
            if (health < health_MAX / 2) {
                bossStage = 2;

                for (int i = 0; i < 4; i++) {
                    turrets[i].setVelocity(bulletVelocity + 1);
//                    turrets[i].setTracking(true);
                }
            }
        }

        if (bossStage == 2 ) {
            if (health < health_MAX / 4) {
                bossStage = 3;

                for (int i = 0; i < 4; i++) {
                    turrets[i].setVelocity(bulletVelocity + 1);
                    turrets[i].setTracking(true);
                }
            }
        }

        if (bossStage == 3) {

        }

        tracker.updateDestination();

        Utility.log("Boss Stage: " + bossStage);

        super.update();
    }

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
        } else {
//            if (bossStage == 0)
                bulletsPerShot += guns_MAX;
//            shotMode++;
//            shootState = MISSILES;
            shotMode = 0;
            currentGun = 0;
        }
//        else {
//            shootState = BULLET;
//            enemies.spawnEntity(pos.intX(), pos.intY(), VariableHandler.TYPE_DRONE1, 0, 0);
//            shotMode = 0;
//            currentGun = 0;
//            }
    }

    @Override
    protected void resetShootTimer() {
        double factor = 1;

        if (bossStage == 3) {
            factor *= 0.25;
        } else if (bossStage > 1) {
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


//    @Override
//    public double getTurretX() {
//        int ends = 16 + 4;
//        turretOffsetX = currentGun * (width)/guns_MAX;
//        updateGun();
//
//        return ends + super.getTurretX();
//    }

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
        updateGun();
    }

    protected void spawnBullet() {
        double x = turrets[currentGun].getX();
        double y = turrets[currentGun].getY();
        double angle = turrets[currentGun].getAngle();
        double bulletVelocity = getBulletVelocity();
        boolean tracking = this.tracking;

        if (bossStage > 2) {
            angle = tracker.calculateAngle(x,y);
//            tracking = true;
        }

        createBullet(x, y, angle, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking);

//        if (projectiles.bullets.getPoolSize() > 0) {
//            projectiles.bullets.spawnFromPool(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking);
//        }
//        else {
//            projectiles.bullets.add(new BulletEnemy(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking));
//        }

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
