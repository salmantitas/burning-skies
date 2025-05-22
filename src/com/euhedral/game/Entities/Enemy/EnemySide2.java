package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

public class EnemySide2 extends Enemy{

    double destinationX, destinationY;

    public EnemySide2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        score = 100;
//        shootTimerDefault = 200;
        attackEffect = true;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[0]);
    }


    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 2;
        velX_MIN = 3f;

        commonInit();
    }

    @Override
    public void move() {
        if (isActive()) {
            double xMin = -width, xMax = Engine.WIDTH;
//            double angleCorrection = 7;

            velY = 0;
            if (x <= xMin) {
//                bulletAngle = 0 - angleCorrection;
                velX = velX_MIN;
                y = destinationY;
            } else if (x >= xMax) {
                velX = -velX_MIN;
//                bulletAngle = 180 + angleCorrection;
                y = destinationY;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }
//        if (!inscreen)
//            Utility.log("min: " + minVelX + "| vel: " + velX);
        moveInScreen();
    }

//    @Override
//    protected void shoot() {
//        super.shoot();
//        shot += 1;
////        fastShoot();
//    }

    @Override
    public void update() {
        super.update();
        updateDestination();
        setImage();
    }

    private void setImage() {
        if (velX > 0) {
            setImage(textureHandler.enemySide[1]);
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[0]);
        }
    }

    private void updateDestination() {
        int offsetY = -32;
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY + offsetY;
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void commonInit() {
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_SIDE2;
    }

    @Override
    public int getTurretY() {
        return (int) y + Utility.intAtWidth640(3);
    }
}
