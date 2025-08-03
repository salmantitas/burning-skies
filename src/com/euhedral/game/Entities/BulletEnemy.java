package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class BulletEnemy extends Bullet{

    private boolean tracking;
    public boolean canPlayImpactAnimation;

    BulletEnemy(int x, int y) {
        super(x, y);
    }

    public BulletEnemy(int x, int y, double angle) {
        super(x, y, angle);
        width = Utility.intAtWidth640(8);
        height = width;
        forwardVelocity = Utility.intAtWidth640(5);
        initSound = SoundHandler.BULLET_ENEMY;
        image = textureHandler.bulletEnemy[0];
//        color = Color.orange;
//        impactColor = Color.red;
        SoundHandler.playSound(initSound);

        newWidth = (int) (width * reflection.sizeOffset);
        newHeight = (int) (height * reflection.sizeOffset);

        impactSize = Math.max(newWidth, newHeight);

        canPlayImpactAnimation = true;
    }

    public BulletEnemy(int x, int y, double angle, double vel, boolean tracking) {
        this(x, y, angle);
        this.tracking = tracking;
        forwardVelocity = vel;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
        } else if (state == STATE_IMPACT) {
            if (!canPlayImpactAnimation) {
                impact.playedOnce = true;
                impact.endAnimation();
            } else {
                impact.runAnimation();
//            impactTimer++;
                this.velX = entity.getVelX();
                this.velY = entity.getVelY();
                super.update();
            }
        }
    }

    @Override
    protected void move() {
        x += velX;
        if (state == STATE_ACTIVE) {
            if (isTracking()) {
                y += velY;
            } else {
                y += velY + EntityHandler.backgroundScrollingSpeed;
            }
        } else if (state == STATE_IMPACT) {
            y += velY;
        }

    }

//    @Override
//    protected void drawDefault(Graphics g) {
//        if (state == STATE_ACTIVE) {
//            drawImage(g, image, width, height);
////            g.setColor(color);
////            g.fillOval((int) x, (int) y, width, height);
//        } else if (state == STATE_IMPACT) {
////            g.setColor(impactColor);
////            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);
//
//            impact.drawAnimation(g, (int) x, (int) y, impactSize, impactSize);
//        }
//    }b

    @Override
    public void resurrect(double x, double y) {
        this.calculated = false;
        super.resurrect(x, y);
        SoundHandler.playSound(initSound);
    }

    @Override
    public void destroy(MobileEntity entity) {
        canPlayImpactAnimation = VariableHandler.shield.getValue() <= 0;
        state = STATE_IMPACT;
        this.entity = entity;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isTracking() {
        return tracking;
    }
}
