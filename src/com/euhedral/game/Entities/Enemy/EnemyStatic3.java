package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyStatic3 extends Enemy {

    double destinationX, destinationY;
    double deceleration;

    int movementState;
    int MOVING_DOWN = 0;
    int MOVING_CIRCLE = 1;

    int movementFactor;

    boolean first;

    public EnemyStatic3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

//        bulletVelocity = Utility.intAtWidth640(6);
        shootTimerDefault = 300;
        shootTimer = 50;
        score = 200;

        double decelerationMAX = 0.012;
        double decelerationMIN = 0.010;
        int randMAX = (int) (decelerationMAX / decelerationMIN);
        int decelerationInt = Utility.randomRangeInclusive(1, randMAX);
        deceleration = (double) (decelerationInt) * decelerationMIN;
        deceleration = decelerationMAX;

        bulletsPerShot_MAX = 5;
        bulletArcAngle = 10 * bulletsPerShot_MAX;

        attackEffect = true;

        movementState = MOVING_DOWN;
        angle = 90;
        forwardVelocity = 1;

        setImage(textureHandler.enemyStatic[2]);
    }

    public EnemyStatic3(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 6;

        velX = 0;
        velY_MIN = 1.75;
        velY_MAX = 1;
        commonInit();
        damage = 90;
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
            y += velY;
            if (velY == 0) {
                movementState = MOVING_CIRCLE;
                vMove = VerticalMovement.UP;
                hMove = HorizontalMovement.LEFT;
            }
        } else if (movementState == MOVING_CIRCLE) {

//            if (vMove == VerticalMovement.UP) {
//                velY -= deceleration;
//                if (Math.abs(velY) >= velY_MAX / 2) {
//                    vMove = VerticalMovement.DOWN;
//                }
//            }
//
//            if (vMove == VerticalMovement.DOWN) {
//                velY += deceleration;
//                if (Math.abs(velY) >= velY_MAX / 2) {
//                    vMove = VerticalMovement.UP;
//                }
//            }
//
//            if (hMove == HorizontalMovement.RIGHT) {
//                velX += deceleration;
//                if (Math.abs(velX) >= velY_MAX) {
//                    hMove = HorizontalMovement.LEFT;
//                }
//            }
//
//            if (hMove == HorizontalMovement.LEFT) {
//                velX -= deceleration;
//                if (Math.abs(velX) >= velY_MAX) {
//                    hMove = HorizontalMovement.RIGHT;
//                }
//            }

            calculateVelocities();
            angle = (angle + 1) % 360;

            super.moveInScreen();
        }
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = 2.5f;
        velX = 0;
        movementState = MOVING_DOWN;
    }

    @Override
    public int getTurretX() {
        return (int) x + width / 2 - Utility.intAtWidth640(2);
    }

    @Override
    public double calculateShotTrajectory() {
        return calculateAngle(getTurretX(), getTurretY(), destinationX, destinationY);
    }

    @Override
    public double getBulletAngle() {
        double tempAngle = calculateShotTrajectory();
        int a = bulletArcAngle / bulletsPerShot_MAX;
//        double maxArc = tempAngle + 20;
        return tempAngle - (2 * a) + (bulletsPerShot - 1) * a; // * (bulletsPerShot % 2 == 0 ? 1 : -1); // stub
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY;
    }

    @Override
    public void resurrect(int x, int y) {
        commonInit();
        explosion.playedOnce = false;
        super.resurrect(x, y);
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_STATIC3;
    }

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D r = getBounds();
        return object.intersects(r);
    }
}
