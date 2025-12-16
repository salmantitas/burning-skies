package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;

import java.awt.*;

public class EnemyBoss1 extends EnemyBoss {

    public EnemyBoss1(double x, double y, int levelHeight) {
        super(x,y, levelHeight);
        height = 128;
        width = height * 2;
        pos.x = x - width/2;

        destination = new Position(0, 490);
        minX = Utility.percWidth(25);
        maxX = Utility.percWidth(75) - (int) 1.8 * width;

        currentGun = 0;
        guns_MAX = 4;
        shotMode = 0;
        shotMode_MAX = guns_MAX;

        color = Color.orange;

        turretOffsetY = height/2 + 8;

        velX = 2;
        velY = offscreenVelY;
        health_MAX = 25;
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
        if (shotMode < shotMode_MAX) {
            bulletsPerShot++;
            shotMode++;
        }
        else {
            bulletsPerShot += guns_MAX;
            shotMode = 0;
            currentGun = 0;
            }
    }

    @Override
    protected void resetShootTimer() {
        double factor = 1;

        if (shotMode == shotMode_MAX) {
            factor = 2;
        }
        shootTimer = (int) (factor * shootTimerDefault / Difficulty.getEnemyFireRateMult());
    }

    @Override
    public void moveInScreen() {
        if (destination.y >= pos.y + height) {
            pos.y += velY;
        }
        else {
            if (!moveLeft)
                pos.x += velX;
            else
                pos.x -= velX;
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
}
