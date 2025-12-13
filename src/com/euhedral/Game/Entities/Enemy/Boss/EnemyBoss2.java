package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;

import java.awt.*;

public class EnemyBoss2 extends EnemyBoss1 {

    public EnemyBoss2(double x, double y, int levelHeight) {
        super(x,y, levelHeight);
        height = 128;
        width = height;
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

//    @Override
//    protected void shootDefault() {
//        if (shotMode < shotMode_MAX) {
//            bulletsPerShot++;
//            shotMode++;
//        }
//        else {
//            bulletsPerShot += guns_MAX;
//            shotMode = 0;
//            currentGun = 0;
//            }
//    }
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
//
//    public void moveInScreen() {
//        if (distToCover > 0) {
//            distToCover--;
//            pos.y += velY;
//        }
//        else {
//            if (!moveLeft)
//                pos.x += velX;
//            else
//                pos.x -= velX;
//        }
//
//        if (pos.x < min)
//            moveLeft = false;
//        if (pos.x > max)
//            moveLeft = true;
//    }

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
}
