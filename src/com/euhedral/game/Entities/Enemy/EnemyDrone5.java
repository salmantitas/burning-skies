package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.VariableHandler;

public class EnemyDrone5 extends EnemyDrone1 {

    int recoilPause = 25; // too low: 20, too high: 50
    double deceleration = 0.1;

    double tempAngle;
    int degreesPerBullet;
    int shotCount = 1, shotSign = 1;

    public EnemyDrone5(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[4]);

        shootTimerFirst = 75;
        shootTimerDefault = 300;
        score = 200;
        damage = 40;

        bulletsPerShot_MAX = 18;
        bulletArcAngle = 180;
        degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;

        attackEffect = true;

        health_MAX = 2;
        bulletVelocity = 2;
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
                forwardVelocity -= deceleration;
            } else {
                forwardVelocity = 2;
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
        tempAngle = tempAngle + shotCount/2 * shotSign * degreesPerBullet;
        shotCount++;
        shotSign *= -1;
        if (shotCount > bulletsPerShot_MAX)
            shotCount = 1;
        return tempAngle;// - (2 * degreesPerBullet) + (bulletsPerShot - 1) * degreesPerBullet; // * (bulletsPerShot % 2 == 0 ? 1 : -1); // stub
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE5;
    }
}