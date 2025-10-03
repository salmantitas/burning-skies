package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.VariableHandler;

public class EnemyDrone6 extends EnemyDrone1 {

    int recoilPause = 25; // too low: 20, too high: 50
    double deceleration = 0.1;

    double tempAngle;
    int degreesPerBullet;

    public EnemyDrone6(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDroneL[0]);

        shootTimerFirst = 75;
        shootTimerDefault = 350;
        score = 200;
        damage = 40;

        bulletsPerShot_MAX = 36;
        int tempAngle = 360/bulletsPerShot_MAX;
        bulletArcAngle = tempAngle * bulletsPerShot_MAX;

        attackEffect = true;

//        width = 64;
//        height = 64;

        health_MAX = 1;
        bulletVelocity = 1;
        commonInit();
    }

    @Override
    protected void shoot() {
        resetShootTimer();
        shootDefault();
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += bulletsPerShot_MAX;
    }

    @Override
    public void move() {
        if (isActive() && inscreenY) {
            if (shootTimer < recoilPause) {
//                velY = forwardVelocity / 2;
                forwardVelocity = 0;
            } else {
                resetForwardVelocity();
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
            velX = 0;
        }
        moveInScreen();
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        resetForwardVelocity();
    }

    private void resetForwardVelocity() {
        forwardVelocity = 1;
    }

    @Override
    public int getTurretX() {
        return (int) pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    public double calculateShotTrajectory() {
        return calculateAngle(getTurretX(), getTurretY(), destinationX, destinationY);
    }

    @Override
    public double getBulletAngle() {
        tempAngle = calculateShotTrajectory();
        degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;
        return tempAngle - (2 * degreesPerBullet) + (bulletsPerShot - 1) * degreesPerBullet;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE6;
    }
}