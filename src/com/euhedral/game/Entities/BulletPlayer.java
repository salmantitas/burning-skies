package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletPlayer extends Bullet{

    int impactAnimationAdjustmentX;

    public Enemy target;

    public BulletPlayer(double x, double y, double angle) {
        super(x, y, angle);
//        this.contactId = contactId;
        setImage(textureHandler.bulletPlayer[0]);
        width = image.getWidth(); //4
        height = width; //10
//        impactColor = Color.GREEN;
        commonInit();

        impactAnimationAdjustmentX = (impact.getImageWidth() - width)/4;

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
        this.target = (Enemy) target;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            if (target != null) {
                calculateVelocities(target.getX() + target.getWidth()/2, target.getY() + target.getHeight()/2);
            }
            super.update();
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
        if (target == null)
            g.drawImage(image, (int) pos.x, (int) pos.y, targetWidth, targetHeight, null);
        else {
            g.setColor(Color.RED);
            g.fillOval((int) pos.x, (int) pos.y, width, height);
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
        target = null;
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
