package com.euhedral.Game.Entities;

import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

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
                if (entity == null) {
                    this.velX = 0;
                    this.velY = EntityHandler.backgroundScrollingSpeed;
                } else {
                    this.velX = entity.getVelX();
                    this.velY = entity.getVelY();
                }
                super.update();
            }
        }
    }

    @Override
    protected void move() {
        pos.x += velX;
        if (state == STATE_ACTIVE) {
            if (isTracking()) {
                pos.y += velY;
            } else {
                pos.y += velY + EntityHandler.backgroundScrollingSpeed;
            }
        } else if (state == STATE_IMPACT) {
            pos.y += velY;
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
        super.destroy(entity);
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isTracking() {
        return tracking;
    }

    public boolean inRadius(double x, double y, int radius) {
        double aX = pos.x;
        double aY = pos.y;
        if (pos.x < x)
            aX = pos.x + width;
        if (pos.y < y)
            aY = pos.y + height;
        return Math.abs(calculateMagnitude(aX, aY, x,y)) < radius;
    }

    public boolean isBelowDeadZoneTop() {
        return pos.y > VariableHandler.deadzoneTop + 20;
    }
}
