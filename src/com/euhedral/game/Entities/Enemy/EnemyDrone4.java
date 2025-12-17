package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

public class EnemyDrone4 extends EnemyDrone1 {

    double destinationX, destinationY;

    int bulletAngle_MIN = 30;
    int bulletAngle_MAX = 180 - bulletAngle_MIN;
    int bulletAngleIncrements = 10;

    public EnemyDrone4(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        setImage(textureHandler.enemyDrone[3]);

        shootTimerFirst = 100;
        shootTimerDefault = 45;
        score = 150;

//        attackEffect = true;

        health_MAX = 3;
        bulletVelocity = 2;
        commonInit();
    }

    @Override
    protected void moveInScreen() {
        pos.y += velY;
        if (pos.x < tracker.destinationX - width || pos.x > destinationX + width)
            pos.x += velX;
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

    protected void updateDestination() {
        destinationX = Engine.WIDTH/2;
        destinationY = Engine.HEIGHT/2;
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        forwardVelocity = 1;
        bulletAngle = 90;

        if (pos.x < destinationX)
            bulletAngleIncrements = - 10;
        else {
            bulletAngleIncrements = 10;
        }
    }

    @Override
    public double getTurretX() {
        return pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE4;
    }
}