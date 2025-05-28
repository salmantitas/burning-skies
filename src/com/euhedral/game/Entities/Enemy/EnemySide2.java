package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

public class EnemySide2 extends Enemy{

    double destinationX, destinationY;
    boolean firstEntry;

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
            double xMin = -2*width, xMax = Engine.WIDTH + width;

            velY = 0;
            if (x <= xMin && velX < 0) {
                velX = velX_MIN;
                if (!firstEntry)
                    y = destinationY;
            } else if (x >= xMax && velX > 0) {
                velX = -velX_MIN;
                if (!firstEntry)
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
        firstEntry = false; // todo: move somewhere else
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void commonInit() {
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
        firstEntry = true;
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
