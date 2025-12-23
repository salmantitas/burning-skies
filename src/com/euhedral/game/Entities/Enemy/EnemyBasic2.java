package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.GameController;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

public class EnemyBasic2 extends Enemy{

    int movementDistance_MAX = 3*64;

    public EnemyBasic2(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[1]);

        shootTimerDefault = 125;
        score = 30;
        movementDistance = movementDistance_MAX;
//        attackEffect = true;

        turret = new Turret(turretOffsetX, turretOffsetY, this);

        health_MAX = 2;
        commonInit();

        velX_MIN = 1;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {

            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
//                velX = 0;
                if (hMove.equals(HorizontalMovement.LEFT)) {
                    setHMove(-1);
                } else {
                    setHMove(1);
                }
                movementDistance = movementDistance_MAX;
            }
        }
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
        double angle = turret.getAngle();
        double velocity = getBulletVelocity();
        boolean tracking = this.tracking;

        createBullet(x, y, angle, velocity, tracking);

//        bullets.printPool("Enemy Bullet");
    }

    public void createBullet(double x, double y, double angle, double velocity, boolean tracking) {
        projectiles.bullets.spawn(x, y, angle, velocity, tracking);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC2;
    }
}
