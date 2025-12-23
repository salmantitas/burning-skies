package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Pool.EnemyPool;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Pool.ProjectilePool;

import java.awt.*;

public class EnemyBoss4 extends EnemyBoss1 {

    boolean firstMove = true;
    Tracker tracker;

    int bulletAngle_MIN = 30;
    int bulletAngle_MAX = 180 - bulletAngle_MIN;
    int bulletAngleIncrements = 10;

    double tempAngle;
    int degreesPerBullet;

    int shotCount = 1, shotSign = 1;

    public EnemyBoss4(double x, double y, ProjectilePool projectiles, EnemyPool enemies, int levelHeight) {
        super(x,y, projectiles, enemies, levelHeight);
        height = 128;
        width = 64;
//        pos.x = x - width/2;
//        color = Color.orange;
//
//        turretOffsetY = height/2 + 8;
//
//        velX = Utility.intAtWidth640(2);
//        velY = offscreenVelY;

        minX = Utility.percWidth(5);
        maxX = Utility.percWidth(95) - width;
        minY = Utility.percHeight(35);
        maxY = Utility.percHeight(100);

        health_MAX = 100;
        setHealth(health_MAX);

        shootTimerFirst = 30;
        shootTimerDefault = 20;
        calibrateShootTimerFirst();

        shotMode = 0;
        shotMode_MAX = 48;

        velX = 2;
        velY = velX;
        resetMovement();
        moveDown = true;

        bulletsPerShot_MAX = 18;
        bulletArcAngle = 180;
        degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;

        currentGun = guns_MAX;

        tracker = new Tracker();
        tracking = true;

//        debug = true;

        setImage(textureHandler.enemyBoss[3]);
    }

    @Override
    public void update() {
        super.update();
        tracker.updateDestination();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isActive() && debug) {
            g.setColor(Color.RED);
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
        }
    }


    @Override
    protected void shoot2() {
        resetShootTimer();
        shootDefault();

        int offset = 20;

        if (Math.abs(pos.x - minX) < offset && Math.abs(pos.y - minY) < offset) {
            shotMode = 0;
        }

        if (Math.abs(pos.x - minX) < offset && Math.abs(pos.y + height - maxY) < offset) {
            shotMode = 0;
        }

        if (Math.abs(pos.x - maxX) < offset && Math.abs(pos.y - minY) < offset) {
            shotMode = 0;
        }

        if (Math.abs(pos.x - maxX) < offset && Math.abs(pos.y - maxY) < offset) {
            shotMode = 0;
        }
    }

    @Override
    protected void shootDefault() {
        if (shotMode == 0) {
            bulletsPerShot += 18;
        }

        if (shotMode > 0 && shotMode <= 2) {
            bulletsPerShot++;
        }
//        if (shotMode > 0 && shotMode <= shotMode_MAX) {

//        }
    }

    @Override
    protected void resetShootTimer() {
        double factor = 1;

        if (shotMode == 0) {
            factor = 2;
        }

        if (shotMode == 2) {
            factor = 2;
        }

        shootTimer = (int) (factor * shootTimerDefault / Difficulty.getEnemyFireRateMult());
    }

    @Override
    public void moveInScreen() {
//        if (distToCover > 0) {
//            distToCover--;
//            pos.y += velY;
//        } else {

        velY = velX;

        if (moveDown) {
            pos.y += velY;
        }

        if (moveUp)
            pos.y -= velY;

        if (moveLeft)
            pos.x -= velX;

        if (moveRight)
            pos.x += velX;
//        }

        boolean topCentre = pos.y > minY && pos.x >minX && pos.x < maxX;
        boolean topLeft = pos.x < minX && pos.y < maxY;
        boolean bottomLeft = pos.x < maxX && pos.y > maxY;
        boolean bottomRight = pos.x > maxX && pos.y > minY;
        boolean topRight = pos.x > maxX && pos.y < minY;

        if (firstMove && topCentre) {
            resetMovement();
            moveLeft = true;
        }

        if (topLeft) {
            resetMovement();
            moveDown = true;
        }

        if (bottomLeft) {
            resetMovement();
            moveRight = true;
        }

        if (bottomRight) {
            resetMovement();
            moveUp = true;
        }

        if (topRight) {
            resetMovement();
            moveLeft = true;
        }
    }

    // Private Methods

    // Needs to override


//    @Override
//    protected void setEnemyType() {
//        enemyType = VariableHandler.enemyTypes;
//    }


    @Override
    public double getTurretX() {
        turretOffsetX = width/2 - 6;
        return pos.x + turretOffsetX;
    }

    protected void updateGun() {
        if (shotMode == 0) {
            super.updateGun();
        }
    }

    @Override
    public double calculateShotTrajectory() {
        if (shotMode == 0)
            return tracker.calculateAngle(getTurretX(), getTurretY());
        else return super.calculateShotTrajectory();
    }

    @Override
    public double getBulletAngle() {
        if (shotMode == 0) {
            tempAngle = calculateShotTrajectory();
            degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;
            tempAngle = tempAngle + shotCount / 2 * shotSign * degreesPerBullet;
            shotCount++;
            shotSign *= -1;
            if (shotCount > bulletsPerShot_MAX) {
                shotCount = 1;
                if (moveLeft) {
                    shotMode = 1;
                } else {
                    shotMode = 2;
                }
            }
            return tempAngle;// - (2 * degreesPerBullet) + (bulletsPerShot - 1) * degreesPerBullet; // * (bulletsPerShot % 2 == 0 ? 1 : -1); // stub
        } else if (shotMode == 1) {
            bulletAngle += bulletAngleIncrements;
            if (bulletAngle > bulletAngle_MAX)
                bulletAngleIncrements = -10;
            else if (bulletAngle < bulletAngle_MIN) {
                bulletAngleIncrements = 10;
            }
            return super.getBulletAngle();
        } else if (shotMode == 2) {
            return calculateAngle(Engine.WIDTH/2, 0);
        } else {
            return super.getBulletAngle();
        }
    }
}
