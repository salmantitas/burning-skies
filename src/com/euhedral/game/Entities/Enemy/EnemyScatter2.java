package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyScatter2 extends Enemy {

    double destinationX, destinationY;
    double deceleration;

    int movementState;
    int MOVING_DOWN = 0;
    int MOVING_CIRCLE = 1;

    double tempAngle;
    int degreesPerBullet;
    int shotCount = 1, shotSign = 1;

    public EnemyScatter2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        score = 150;

        bulletVelocity = 3;

        shootTimerFirst = 45;
        shootTimerDefault = 250;


        double decelerationMAX = 0.015;
        deceleration = decelerationMAX;

        bulletsPerShot_MAX = 5;
        bulletArcAngle = 50;
        degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;

        attackEffect = true;

        movementState = MOVING_DOWN;
        angle = 90;
        forwardVelocity = 1;

        setImage(textureHandler.enemyStatic[2]);

        health_MAX = 6;

        velX = 0;
        velY_MIN = 1.75;
        velY_MAX = 1;
        commonInit();
    }

    public EnemyScatter2(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && shootTimer <= 50) {
            updateDestination();
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += bulletsPerShot_MAX;
    }

    @Override
    public void move() {
        super.move();
    }


    @Override
    protected void moveInScreen() {
        if (movementState == MOVING_DOWN) {
            velY = Math.max(0, velY - deceleration);
            pos.y += velY;
            if (velY == 0) {
                movementState = MOVING_CIRCLE;
                vMove = VerticalMovement.UP;
                hMove = HorizontalMovement.LEFT;
            }
        } else if (movementState == MOVING_CIRCLE) {

            calculateVelocities();
            angle = (angle + 1) % 360;

            super.moveInScreen();
        }
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        velY = 2.5f;
        velX = 0;
        movementState = MOVING_DOWN;
    }

    @Override
    public double getTurretX() {
        return pos.x + width / 2 - Utility.intAtWidth640(2);
    }

    @Override
    public double calculateShotTrajectory() {
        return calculateAngle(getTurretX(), getTurretY(), destinationX, destinationY);
    }

    @Override
    public double getBulletAngle() {
        tempAngle = calculateShotTrajectory();
        tempAngle = tempAngle + shotCount/2 * shotSign * degreesPerBullet;
        shotCount++;
        shotSign *= -1;
        if (shotCount > bulletsPerShot_MAX)
            shotCount = 1;
        return tempAngle;// - (2 * degreesPerBullet) + (bulletsPerShot - 1) * degreesPerBullet; // * (bulletsPerShot % 2 == 0 ? 1 : -1); // stub
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerPositon.x;
        destinationY = EntityHandler.playerPositon.y;
    }

    @Override
    public void resurrect(double x, double y) {
        commonInit();
        explosion.playedOnce = false;
        super.resurrect(x, y);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_SCATTER2;
    }

}
