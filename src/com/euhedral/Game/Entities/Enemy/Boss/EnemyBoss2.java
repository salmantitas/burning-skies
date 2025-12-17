package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.*;
import com.euhedral.Game.Entities.Enemy.Behavior.Tracker;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.Pool.EnemyPool;
import com.euhedral.Game.Pool.ProjectilePool;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EnemyBoss2 extends EnemyBoss1 {

    boolean firstMove = true;
    Tracker tracker;

    int minY,
    maxY;

    public EnemyBoss2(double x, double y, ProjectilePool projectiles, EnemyPool enemies, int levelHeight) {
        super(x,y, projectiles, enemies, levelHeight);
        height = 128;
        width = height;

        minX = Utility.percWidth(10);
        maxX = Utility.percWidth(90) - width;
        minY = Utility.percHeight(35);
        maxY = Utility.percHeight(100) - height;

        debug = false;
        tracking = true;

        resetMovement();
        moveDown = true;

        tracker = new Tracker();
//        pos.x = x - width/2;
//        color = Color.orange;
//
//        turretOffsetY = height/2 + 8;
//
//        velX = Utility.intAtWidth640(2);
//        velY = offscreenVelY;
        health_MAX = 50;
        setHealth(health_MAX);
//        shootTimerDefault = 30;
//
//        moveLeft = true;
        setImage(textureHandler.enemyBoss[1]);
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && shootTimer <= 50) {
            tracker.updateDestination();
        }
    }

//    @Override
//    public void update() {
//        super.update();
//        move();
//    }

    @Override
    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        AffineTransform currentTransform = g2d.getTransform();

        if (moveLeft) {
            g2d.rotate(Math.toRadians(90), pos.intX() + width/2, pos.intY() + height/2);
        }

        if (moveRight) {
            g2d.rotate(Math.toRadians(-90), pos.intX() + width/2, pos.intY() + height/2);
        }

        if (moveUp) {
            g2d.rotate(Math.toRadians(180), pos.intX() + width/2, pos.intY() + height/2);
        }

        super.render(g);

        g2d.setTransform(currentTransform);

        if (isActive() && debug) {
            g.setColor(Color.RED);
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
        }


    }


//    @Override
//    protected void shoot() {
//        super.shoot();
//    }

    @Override
    protected void shootDefault() {
        if (shotMode < shotMode_MAX) {
            bulletsPerShot++;
            shotMode++;
        }
        else {
            enemies.spawnEntity(pos.intX(), pos.intY(), VariableHandler.TYPE_DRONE1, 0, 0);
            shotMode = 0;
            currentGun = 0;
            }
    }
//
//    @Override
//    protected void resetShootTimer() {
//        double factor = 1;
//
//        if (shotMode == shotMode_MAX) {
//            factor = 2;
//        }
//        shootTimer = (int) (factor * shootTimerDefault / Difficulty.getEnemyFireRateMult());
//    }

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

        boolean bottomCentre = pos.y > maxY && pos.x >minX && pos.x < maxX;
        boolean bottomLeft = pos.x < minX && pos.y > maxY;
        boolean bottomRight = pos.x > maxX && pos.y > maxY;
        boolean topLeft = pos.x < minX && pos.y < minY;
        boolean topRight = pos.x  > maxX && pos.y < minY;

        if (firstMove && bottomCentre) {
            moveRight = true;
            moveDown = false;
            firstMove = false;
        }

        if (bottomLeft) {
            moveLeft = false;
            moveRight = true;
            moveDown = false;
            moveUp = false;
        }

        if (bottomRight) {
            moveLeft = false;
            moveRight = false;
            moveDown = false;
            moveUp = true;
        }

        if (topLeft) {
            moveUp = false;
            moveDown = true;
            moveLeft = false;
            moveRight = false;
        }

        if (topRight) {
            moveUp = false;
            moveDown = false;
            moveLeft = true;
            moveRight = false;
        }
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
//
//    private void updateGun() {
//        currentGun++;
//        if (currentGun >= guns_MAX) {
//            currentGun = 0;
//        }
//    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(getTurretX(), getTurretY(), tracker.destinationX, tracker.destinationY); // stub
    }
}
