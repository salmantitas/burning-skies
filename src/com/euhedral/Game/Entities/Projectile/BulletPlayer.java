package com.euhedral.Game.Entities.Projectile;

import com.euhedral.Game.*;
import com.euhedral.Game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BulletPlayer extends Bullet{

    int impactAnimationAdjustmentX;
    protected boolean shieldKiller;

//    public Enemy target;

    public BulletPlayer(double x, double y, double angle) {
        super(x, y, angle);
        setImage(textureHandler.bulletPlayer[0]);
        width = image.getWidth();
        height = width;
//        impactColor = Color.GREEN;
        commonInit();

        damage = 1;

        impactAnimationAdjustmentX = (impact.getImageWidth() - width)/4;
        physics.acceleration = 0.1;

        shieldKiller = false;
    }

    public BulletPlayer(double x, double y, double forwardVelocity, double angle) {
        this(x, y, angle);
        this.forwardVelocity = forwardVelocity;
    }

    public BulletPlayer(double x, double y, double forwardVelocity, Enemy target) {
        this(x, y, 0);
        this.entity = target;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            if (entity != null) {
                forwardVelocity += physics.acceleration;
                if (entity.isActive()) {
                    calculateVelocities(entity.getX() + entity.getWidth() / 2, entity.getY() + entity.getHeight() / 2);
                    angle = calculateAngle(entity.getX(), entity.getY());
                }
                else {
                    if (!calculated) {
                        calculateAngle(entity.getX(), entity.getY());
                        calculated = true;
                    }
                }
            }
            super.update();
            updateBounds();
        } else if (state == STATE_IMPACT) {
            impact.runAnimation();
//            impactTimer++;
//            Utility.log(state + " " + impactTimer);
            if (entity == null) {
                this.velX = 0;
                this.velY = 0;
            } else {
                this.velX = entity.getVelX();
                this.velY = entity.getVelY();
            }
            super.update();
        }
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            drawImage(g, image, width, height);
        } else if (state == STATE_IMPACT) {
            if (entity == null) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            } else if (entity.isActive()) {
                impact.drawAnimation(g, (int) pos.x - impactAnimationAdjustmentX, (int) pos.y, impactSize, impactSize);
            }
        }
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image, int targetWidth, int targetHeight) {
        if (entity == null)
            g.drawImage(image, (int) pos.x, (int) pos.y, targetWidth, targetHeight, null);
        else {

            Graphics2D g2d = (Graphics2D) g;
//            AffineTransform original = g2d.getTransform();
            g2d.setColor(Color.RED);

//            g2d.rotate(Math.toRadians(180 - angle), pos.x + 8, pos.y + 8);
            g2d.fillOval((int) pos.x, (int) pos.y, 16, 16);

//            g2d.setTransform(original);
        }
    }

    @Override
    public void resurrect(double x, double y) {
        super.resurrect(x, y);
        this.calculated = false;
        entity = null;
        commonInit();
    }

    private void commonInit() {
//        state = STATE_ACTIVE;
        SoundHandler.playSound(initSound);
    }

    @Override
    public void disable() {
        state = STATE_INACTIVE;
    }

    public boolean isShieldKiller() {
        if (entity != null)
            return true;
        return shieldKiller;
    }
}
