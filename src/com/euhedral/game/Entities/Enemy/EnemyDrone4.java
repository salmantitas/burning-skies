package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

public class EnemyDrone4 extends EnemyDrone1 {

    int bulletAngle_MIN = 30;
    int bulletAngle_MAX = 180 - bulletAngle_MIN;
    int bulletAngleIncrements = 10;

    public EnemyDrone4(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[3]);

        shootTimerDefault = 20;
        score = 150;
        damage = 30;

//        attackEffect = true;

        health_MAX = 3;
        bulletVelocity = 3;
        commonInit();
    }

    @Override
    protected void shoot() {
        resetShootTimer();
        shootDefault();
    }

    @Override
    protected void shootDefault() {
        super.shootDefault();
        bulletAngle += bulletAngleIncrements;
        if (bulletAngle > bulletAngle_MAX)
            bulletAngleIncrements = - 10;
        else if (bulletAngle < bulletAngle_MIN) {
            bulletAngleIncrements = 10;
        }
    }

    @Override
    protected void updateDestination() {
        destinationX = Engine.WIDTH/2;
        destinationY = Engine.HEIGHT/2;
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        forwardVelocity = 1;
        if (x < Engine.WIDTH /2)
            bulletAngle = bulletAngle_MIN;
        else {
            bulletAngle = bulletAngle_MAX;
        }
    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_DRONE4;
    }
}