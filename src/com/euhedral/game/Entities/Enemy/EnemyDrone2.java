package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

public class EnemyDrone2 extends EnemyDrone{

    public EnemyDrone2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[1]);

        shootTimerDefault = 100;
        attackEffect = true;
//        shootTimer = shootTimerDefault;
    }

    @Override
    public void initialize() {
        super.initialize();

//        enemyType = EntityHandler.TYPE_DRONE;

//        power = 1;
//        shootTimerDefault = 250;
//        minVelX = 2f;
        health_MAX = 3;
        score = 50;
        damage = 30;
        commonInit();
    }

    @Override
    protected void shoot() {
        resetShootTimer();
        shootDefault();
    }

    @Override
    public void move() {
        if (isActive()) {
            int recoilPause = 25; // too low: 20, too high: 50
            double deceleration = 0.1;
            if (shootTimer < recoilPause) {
                forwardVelocity -= deceleration;
            } else {
                forwardVelocity = 4f;
//                forwardVelocity += 0.2; // todo: Use for future drones // too high = 0.5 // too low = 0.1
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
            velX = 0;
        }
        moveInScreen();
    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_DRONE2;
    }
}