package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.GameController;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class EnemySide1 extends Enemy{

    double xMin, xMax;

    public EnemySide1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);

        turretOffsetY = Utility.intAtWidth640(3);
        turret = new Turret(turretOffsetX, turretOffsetY, this);

        shootTimerFirst = 30;
        shootTimerDefault = 50;

        bulletVelocity = 4;
        turret.setVelocity(bulletVelocity);

        score = 60;

        xMin = -width;
        xMax = Engine.WIDTH;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[0]);
        damageImage = textureHandler.enemyDamage[1];

        health_MAX = 3;
        velX_MIN = 9f;

        commonInit();
    }

    @Override
    public void update() {
        super.update();
        setImage();
    }

    @Override
    public void move() {
        if (isActive()) {

            velY = 0;
            if (pos.x <= xMin) {
                velX = velX_MIN;
            } else if (pos.x >= xMax) {
                velX = -velX_MIN;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }
        moveInScreen();
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

    private void setImage() {
        if (velX > 0) {
            angle = -90;
        } else if (velX < 0) {
            angle = 90;
        }
    }

    @Override
    protected void commonInit() {
//        super.commonInit();
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
        jitter = 0;
        calibrateShootTimerFirst();
    }

    @Override
    protected void drawDefault(Graphics g) {
        g2d = (Graphics2D) g;
        AffineTransform currentTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(angle - 90), pos.intX() + width/2, pos.intY() + height/2);
        super.drawDefault(g);
        g2d.setTransform(currentTransform);

        // todo: do the same with reflection
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_SIDE1;
    }

//    @Override
//    public double getTurretY() {
//        return pos.y + Utility.intAtWidth640(3);
//    }

    @Override
    public Rectangle2D getBoundsHorizontal() {
        collisionBox.setBounds(0, pos.x, pos.y + 20, width, 1*height/3 + 2);
        return collisionBox.getBounds(0);
    }

    @Override
    public Rectangle2D getBoundsVertical() {
//        Rectangle2D bounds = null;
        if (velX > 0)
            collisionBox.setBounds(1, pos.x + (width / 4) - 10, pos.y, (2 * width) / 4, height);
        else
            collisionBox.setBounds(1, pos.x + (width / 4) + 10, pos.y, (2 * width) / 4, height);
        return collisionBox.getBounds(1);
    }
}
