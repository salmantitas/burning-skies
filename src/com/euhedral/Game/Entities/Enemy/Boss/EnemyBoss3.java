package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Pool.EnemyPool;
import com.euhedral.Game.Entities.Enemy.Behavior.Tracker;
import com.euhedral.Game.Entities.Projectile.Laser;
import com.euhedral.Game.Pool.ProjectilePool;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class EnemyBoss3 extends EnemyBoss {

//    double facing = 0;
    Tracker tracker;

    private Laser laser;
    public boolean laserCollision;
    int bulletWidth = 16 / 2;

    public EnemyBoss3(double x, double y, ProjectilePool projectiles, EnemyPool enemies, int levelHeight) {
        super(x,y, projectiles, enemies, levelHeight);
        height = 128;
        width = height;
//        pos.x = x - width/2;
//        color = Color.orange;
//
//        turretOffsetY = height/2 + 8;
//
//        velX = Utility.intAtWidth640(2);
//        velY = offscreenVelY;

        turretOffsetY = height/2;
        destination = new Position(pos.x, 490);

        tracker = new Tracker();
        attackEffect = true;
        bulletArcAngle = 10;

        health_MAX = 75;
        setHealth(health_MAX);

        velX = 2;
        velY = offscreenVelY;
        angle = 90;

        minX = Utility.percWidth(10);
        maxX = Utility.percWidth(90) - width;
        minY = Utility.percHeight(40);
        maxY = Utility.percHeight(105) - height;

        shootTimerDefault = 30;
        shootTimerFirst = shootTimerDefault * 4;

        currentGun = 0;
        guns_MAX = 4;

        shotMode = -1;
        shotMode_MAX = 4;

        int laserTime = 90;
        Utility.log("Laser Time: " + laserTime);
        laser = new Laser(this, laserTime);
        laser.resetTime(laserTime);

        setImage(textureHandler.enemyBoss[2]);

//        debug = true;
    }

    @Override
    public void update() {
        int offset = 5;
        double increment = 0.5;
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {
            tracker.updateDestination();
            double playerAngle = calculateAngle(getTurretX(), getTurretY(), tracker.destinationX + 8, tracker.destinationY - 48);
            laser.update();
            laser.updateDestination(angle);
            if (playerAngle - angle > offset) {
                angle += increment;
            } else if (angle - playerAngle > offset) {
                angle -= increment;
            }
        }
    }

    @Override
    protected void drawDefault(Graphics g) {
        g2d = (Graphics2D) g;
        AffineTransform currentTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(angle - 90), pos.intX() + width/2, pos.intY() + height/2);
        super.drawDefault(g);
        g2d.setTransform(currentTransform);
    }

    @Override
    protected void shoot() {
        if (shotMode == shotMode_MAX) {
            laser.start();
            resetShootTimer();
            shotMode = 0;
        } else {
            shootDefault();
            resetShootTimer();
            shotMode++;
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 2;
    }

    @Override
    protected void resetShootTimer() {
        double factor = 1;


        if (shotMode == shotMode_MAX) {
            factor = 6;
        }

        shootTimer = (int) (factor * shootTimerDefault / Difficulty.getEnemyFireRateMult());
    }


    @Override
    public void moveInScreen() {
        if (moveDown)
            pos.y += velY;

        if (moveUp)
            pos.y -= velY;

        if (moveLeft)
            pos.x -= velX;

        if (moveRight)
            pos.x += velX;

        if (destination.y >= pos.y + height) {
            moveDown = true;
        } else if (destination.y < pos.y) {
            moveUp = true;
        } else {
            resetMovement();
            destination.y = Utility.randomRange(minY, maxY);
        }

        if (pos.x + width <= destination.x) {
            moveRight = true;
        } else if (pos.x > destination.x) {
            moveLeft = true;
        } else {
            resetMovement();
            destination.x = Utility.randomRange(minX, maxX);
        }
    }

    // Private Methods

    // Needs to override


//    @Override
//    protected void setEnemyType() {
//        enemyType = VariableHandler.enemyTypes;
//    }


    @Override
    public double getTurretX() {
//        if (shotMode == shotMode_MAX) {
            return pos.x + width/2;
//        } else {
//            return super.getTurretX();
//        }
    }


    private void updateGun() {
        currentGun++;
        if (currentGun >= guns_MAX) {
            currentGun = 0;
        }
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(getTurretX(), getTurretY(), tracker.destinationX, tracker.destinationY); // stub
    }

    @Override
    public boolean checkCollision(Entity other) {
        collision = super.checkCollision(other);
        laserCollision = laser.checkCollision(other);
        return laserCollision || collision;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isActive() && debug) {
            g.setColor(Color.RED);
            g.drawRect(minX, minY, maxX - minX, maxY - minY);
        }
    }

    @Override
    protected void renderAttackPath(Graphics g) {
        g.setColor(Color.RED);

        if (isActive() && inscreenY) {

            laser.render(g);

            if (shotMode == shotMode_MAX) {

                int showTime = 120; // todo: Needs better name

                if (shootTimer <= showTime) {
                    g2d = (Graphics2D) g;
                    float transparency = 0.5f - shootTimer / (2f * showTime);
                    g2d.setComposite(Utility.makeTransparent(transparency));

                    g2d.setStroke(new BasicStroke((Math.max(shootTimer / 5, 2))));
                    int drawX = (int) getTurretX();
                    g.drawLine(drawX, (int) getTurretY(), laser.destination.intX(), laser.destination.intY());

                    g2d.setComposite(Utility.makeTransparent(1f));
                }
            } else {
                if (attackEffect) {
                    boolean secondsTillShotFire = (shootTimer < 20);
                    if (isActive() && secondsTillShotFire) {
                        g.setColor(Color.red); // todo: Redundancy?

                        g2d = (Graphics2D) g;
                        g.setColor(Color.RED);

                        attackPathX = pos.x - (0.5) * (double) width;
                        attackPathY = pos.y - (0.5) * (double) height;
//                double drawY = y - (0.5) * (double) height;

                        g2d.setComposite(Utility.makeTransparent(0.5f));
                        g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(calculateShotTrajectory()) - bulletArcAngle / 2, bulletArcAngle);
                        g2d.setComposite(Utility.makeTransparent(1f));
                    }
                }
            }
        }
    }

    @Override
    public double getDamage() {
        if (laserCollision)
            return laser.getDamage();
        else if (collision) {
            return super.getDamage();
        }
        return 0;
    }

    @Override
    public void destroy() {
        super.destroy();
        laser.stop();
    }

    @Override
    public void clear() {
        super.clear();
        laser.stop();
    }
}
