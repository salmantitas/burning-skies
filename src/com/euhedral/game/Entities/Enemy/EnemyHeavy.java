package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.*;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.Pool.ProjectilePool;

import java.awt.*;

public class EnemyHeavy extends Enemy {

    int movementDistance_MAX = 4*64;

    private Turret[] turrets;

    int leftTurret, rightTurret;

    int bulletAngleMIN = 60;

    Tracker tracker;
    int offsetLeft = 8, offsetRight = 2;

    boolean secondsTillShotFire;
    double tempAngle;
    boolean playerInRange;
    boolean rangeCheck1, rangeCheck2;

    // Shoot State
    protected int MISSILE = 1;

    public EnemyHeavy(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);

        bulletAngle = 60;
        shootTimerDefault = 60;
        score = 50;
        leftTurret = width / 3  - 4;
        rightTurret = 2 * width / 3 + 6;

        turrets = new Turret[2];
        Turret turret1 = new Turret(leftTurret, turretOffsetY, 3, 120, false, this);
        Turret turret2 = new Turret(rightTurret, turretOffsetY, 3, 60, false, this);
        turrets[0] = turret1;
        turrets[1] = turret2;

        // stub
        velX_MIN = 1;
        movementDistance = movementDistance_MAX;
        setHMove(1);

        tracker = new Tracker();

//        attackEffect = true;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[4]);

        velX = 0;
        velY_MIN = 1.7f;

        health_MAX = 4;
        commonInit();

        debug = true;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {
            tracker.updateDestination();

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

            rangeCheck1 = (tracker.destinationX - offsetLeft > pos.x) && (tracker.destinationX - offsetLeft < pos.x + width);
            rangeCheck2 = (tracker.destinationX + 32 + offsetRight < pos.x + width) && (tracker.destinationX + 32 + offsetRight > pos.x);
            playerInRange = rangeCheck1 || rangeCheck2;

            if (playerInRange)
                shootState = MISSILE;
            else
                shootState = BULLET;
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (debug) {
            for (int i = 0; i < 2; i++) {
                turrets[i].render(g);
            }
        }
    }

    @Override
    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g.setColor(Color.red);

                g2d = (Graphics2D) g;
                g.setColor(Color.RED);


                attackPathX = pos.x - (0.5) * (double) width;
                if (bulletAngle == bulletAngleMIN) {
                    attackPathY = getTurretY() - (double) height;
                } else {
                    attackPathY = getTurretY() - (0.5) * (double) height;
                }

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(getBulletAngle()) - bulletArcAngle / 2, bulletArcAngle);

                if (bulletAngle == bulletAngleMIN)
                    g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(90 + (90 - getBulletAngle())) - bulletArcAngle / 2, bulletArcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 2;
    }

    @Override
    protected void resetShootTimer() {
        if (shootState == BULLET)
            shootTimer = (int) (shootTimerDefault / Difficulty.getEnemyFireRateMult());
        else
            shootTimer = (int) ((shootTimerDefault + 30) / Difficulty.getEnemyFireRateMult());
    }

//    @Override
//    public double getBulletAngle() {
//
//        if (turretLeft) {
//            tempAngle = bulletAngle;
//        }
//        else {
//            tempAngle = 90 + (90 - bulletAngle);
//        }
////        boolean bothShotsFired = (shot == 1);
////        if (bothShotsFired)
////            incrementBulletAngle();
//
//        rangeCheck1 = (tracker.destinationX - offsetLeft > pos.x) && (tracker.destinationX - offsetLeft < pos.x + width);
//        rangeCheck2 = (tracker.destinationX + 32 + offsetRight < pos.x + width) && (tracker.destinationX + 32 + offsetRight > pos.x);
//        playerInRange = rangeCheck1 || rangeCheck2;
//
//        if (playerInRange)
//            tempAngle = 90;
//        return tempAngle;
//    }

    @Override
    protected void shoot1() {
        updateShootTimer();
        if (shootTimer <= 0) {
            resetShootTimer();
//            loadShots();
//            while (hasShot()) {
                spawnProjectiles();
//                decrementShot();
//            }
        }
    }

    protected void loadShots() {
        bulletsPerShot++;
    }

    @Override
    protected void spawnProjectiles() {
        for (int i = 0; i < 2; i++) {
            turret = turrets[i];
            if (shootState == BULLET)
                spawnBullet();
            else {
                spawnMissiles();
            }
        }
    }

    @Override
    protected void spawnBullet() {
        int projectileWidth = 16/2;
        double x = turret.getX() - projectileWidth;
        double y = turret.getY();
        double angle = turret.getAngle();
        double velocity = getBulletVelocity();
        boolean tracking = this.tracking;

        createBullet(x, y, angle, velocity, tracking);
    }

    private void spawnMissiles() {
        int projectileWidth = 8/2;
        double x = turret.getX() - projectileWidth;
        double y = turret.getY();

        projectiles.missiles.spawn(x, y, 90);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_HEAVY;
    }
}
