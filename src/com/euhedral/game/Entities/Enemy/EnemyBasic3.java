package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.GameController;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EnemyBasic3 extends Enemy{

    double angleIncrement = 0.50;
    Tracker tracker;

    public EnemyBasic3(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[2]);

        shootTimerFirst = 20;
        shootTimerDefault = 60;

        score = 40;
        angle = SOUTH;

        health_MAX = 4;

        tracker = new Tracker();
        turret = new Turret(turretOffsetX, turretOffsetY, this);
        tracking = true;

        commonInit();
    }

    @Override
    public void update() {
        super.update();
        int angleAdjustment = 10;
        if (state == STATE_ACTIVE && inscreenY) {

            if (pos.x < 5 * width || pos.x > Engine.WIDTH - 6 * width) {
//                if (angle != SOUTH) {
                if (hMove == HorizontalMovement.LEFT)
                    angle = Math.max(angle - angleIncrement, SOUTH);
                else if (hMove == HorizontalMovement.RIGHT) {
                    angle = Math.min(angle + angleIncrement, SOUTH);
                }
//                    angle = SOUTH;
//                }
            } else {

                if (hMove == HorizontalMovement.LEFT)
                    angle = Math.min(angle + angleIncrement, EAST - angleAdjustment);
                else if (hMove == HorizontalMovement.RIGHT) {
                    angle = Math.max(angle - angleIncrement, WEST + angleAdjustment);
                }
            }


        }
    }

    @Override
    protected void moveInScreen() {
        super.moveInScreen();
        calculateVelocities();
    }

    @Override
    protected void shoot() {
        updateShootTimer();
        if (shootTimer <= 0) {
            resetShootTimer();
            loadShots();
            while (hasShot()) {
                spawnProjectiles();
                decrementShot();
            }
        }
    }

    protected void loadShots() {
        bulletsPerShot++;
    }

    @Override
    protected void spawnBullet() {
        double x = turret.getX();
        double y = turret.getY();
        double angle = tracker.calculateAngle(turret.getX(), turret.getY());
        double velocity = getBulletVelocity();
        boolean tracking = this.tracking;

        createBullet(x, y, angle, velocity, tracking);

//        bullets.printPool("Enemy Bullet");
    }

    @Override
    protected void drawDefault(Graphics g) {
        g2d = (Graphics2D) g;
        AffineTransform currentTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(tracker.calculateAngle(turret.getX(), turret.getY()) - 90), pos.intX() + width/2, pos.intY() + height/2);
        super.drawDefault(g);
        g2d.setTransform(currentTransform);
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        angle = SOUTH;
    }

    @Override
    public double getTurretX() {
        return pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC3;
    }
}
