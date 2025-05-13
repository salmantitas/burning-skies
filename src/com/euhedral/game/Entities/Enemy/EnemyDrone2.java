package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;

public class EnemyDrone2 extends EnemyDrone{

    public EnemyDrone2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[1]);
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
    protected void commonInit() {
        super.commonInit();
        this.setHealth(2);
    }
}