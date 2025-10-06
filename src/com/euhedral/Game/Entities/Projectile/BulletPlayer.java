package com.euhedral.Game.Entities.Projectile;

import com.euhedral.Game.*;
import com.euhedral.Game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BulletPlayer extends Bullet{

    int impactAnimationAdjustmentX;

//    public Enemy target;

    public BulletPlayer(double x, double y, double angle) {
        super(x, y, angle);
//        this.contactId = contactId;
        setImage(textureHandler.bulletPlayer[0]);
        width = image.getWidth(); //4
        height = width; //10
//        impactColor = Color.GREEN;
        commonInit();

        impactAnimationAdjustmentX = (impact.getImageWidth() - width)/4;
        physics.acceleration = 0.1;

//        impact = new Animation(10, GameController.getTexture().impactSmall[0],
//                GameController.getTexture().impactSmall[1],
//                GameController.getTexture().impactSmall[2],
//                GameController.getTexture().impactSmall[3]
//        );
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
            this.velX = entity.getVelX();
            this.velY = entity.getVelY();
            super.update();
        }
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            drawImage(g, image, width, height);
        } else if (state == STATE_IMPACT && entity.isActive()) {
            impact.drawAnimation(g, (int) pos.x - impactAnimationAdjustmentX, (int) pos.y, impactSize, impactSize);
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

//    public ContactID getContactId() {
//        return contactId;
//    }

    @Override
    public void resurrect(double x, double y) {
//        this.x = x;
//        this.y = y;
        this.calculated = false;
        entity = null;
        super.resurrect(x, y);
        commonInit();
    }

    private void commonInit() {
        state = STATE_ACTIVE;
        SoundHandler.playSound(initSound);
    }

    @Override
    public void disable() {
        state = STATE_INACTIVE;
    }
}
