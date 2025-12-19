package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

public class EnemyScatter1 extends Enemy {

    Tracker tracker;
    double deceleration;

    int movementState;
    int MOVING_DOWN = 0;
    int MOVING_CIRCLE = 1;

    int degreesPerBullet;
    double tempAngle;
    int shotCount = 1, shotSign = 1;

    public EnemyScatter1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        score = 125;

        bulletVelocity = 5;
        shootTimerFirst = 30;
        shootTimerDefault = 200;

        deceleration = 0.012;

        bulletsPerShot_MAX = 3;
        bulletArcAngle = 30;
        degreesPerBullet = bulletArcAngle / bulletsPerShot_MAX;

        tracker = new Tracker();

        attackEffect = true;

        movementState = MOVING_DOWN;
        angle = 90;
        forwardVelocity = 1;

        setImage(textureHandler.enemyStatic[1]);

        health_MAX = 4;

        velX = 0;
//        velY_MIN = 1.75;
        velY_MAX = 1;
        velX_MAX = velY_MAX;

        tracking = true;

        commonInit();
    }

//    public EnemyScatter1(int x, int y, Color color, int levelHeight) {
//        this(x, y, levelHeight);
//        this.color = color;
//    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && shootTimer <= 50) {
            tracker.updateDestination();
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

            if (vMove == VerticalMovement.UP) {
                velY -= deceleration;
                if (Math.abs(velY) >= velY_MAX / 2) {
                    vMove = VerticalMovement.DOWN;
                }
            }

            if (vMove == VerticalMovement.DOWN) {
                velY += deceleration;
                if (Math.abs(velY) >= velY_MAX / 2) {
                    vMove = VerticalMovement.UP;
                }
            }

            if (hMove == HorizontalMovement.RIGHT) {
                velX += deceleration;
                if (Math.abs(velX) >= velX_MAX) {
                    hMove = HorizontalMovement.LEFT;
                }
            }

            if (hMove == HorizontalMovement.LEFT) {
                velX -= deceleration;
                if (Math.abs(velX) >= velX_MAX) {
                    hMove = HorizontalMovement.RIGHT;
                }
            }

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
        return calculateAngle(getTurretX(), getTurretY(), tracker.destinationX, tracker.destinationY);
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
    public void resurrect(double x, double y) {
        commonInit();
        explosion.playedOnce = false;
        super.resurrect(x, y);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_SCATTER1;
    }

}
