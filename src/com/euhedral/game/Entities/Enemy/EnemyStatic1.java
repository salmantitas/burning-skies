package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyStatic1 extends Enemy {

    Tracker tracker;
    double deceleration;

    public EnemyStatic1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);

        bulletVelocity = 5;
        shootTimerDefault = 200;
        score = 75;

        deceleration = 0.012;

        tracker = new Tracker();

        attackEffect = true;

        setImage(textureHandler.enemyStatic[0]);

        health_MAX = 4;

        velX = 0;
        velY_MIN = 1.75f;
        tracking = true;
        commonInit();
    }

//    public EnemyStatic1(int x, int y, Color color, int levelHeight) {
//        this(x, y, levelHeight);
//        this.color = color;
//    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && shootTimer <= 50) {
            tracker.updateDestination();
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 1;
    }


    @Override
    protected void moveInScreen() {
        velY = Math.max(0, velY - deceleration);
        pos.y += velY;
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = 2.5f;
    }

    @Override
    public double getTurretX() {
        return pos.x + width / 2 - Utility.intAtWidth640(2);
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(getTurretX(), getTurretY(), tracker.destinationX, tracker.destinationY); // stub
    }

//    @Override
//    public void resurrect(double x, double y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    @Override
    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            boolean secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);

                attackPathX = pos.x - (0.5) * (double) width;
                attackPathY = getTurretY() - (0.5) * (double) height - 24;

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(calculateShotTrajectory()) - bulletArcAngle / 2, bulletArcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    @Override
    public double getTurretY() {
        return (int) pos.y + 12;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_STATIC1;
    }

}
