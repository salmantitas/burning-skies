package com.euhedral.Game.Entities.Projectile;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class BulletEnemy extends Bullet{

    private boolean tracking;
    public boolean canPlayImpactAnimation;

    BulletEnemy(int x, int y) {
        super(x, y);
    }

    public BulletEnemy(double x, double y, double angle) {
        super(x, y, angle);
        width = Utility.intAtWidth640(8);
        height = width;
        forwardVelocity = Utility.intAtWidth640(5);
        initSound = SoundHandler.BULLET_ENEMY;
        image = textureHandler.bulletEnemy[0];
        SoundHandler.playSound(initSound);

        newWidth = (int) (width * reflection.sizeOffset);
        newHeight = (int) (height * reflection.sizeOffset);

        impactSize = Math.max(newWidth, newHeight);

        canPlayImpactAnimation = true;
    }

    public BulletEnemy(double x, double y, double angle, double vel, boolean tracking) {
        this(x, y, angle);
        this.tracking = tracking;
        forwardVelocity = vel;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
            updateBounds(2);
        } else if (state == STATE_IMPACT) {
            if (!canPlayImpactAnimation) {
                impact.playedOnce = true;
                impact.endAnimation();
            } else {
                impact.runAnimation();
                if (entity == null) {
                    this.velX = 0;
                    this.velY = EntityHandler.backgroundScrollingSpeed;
                } else {
                    this.velX = entity.getVelX();
                    this.velY = entity.getVelY();
                }
                super.update();
                updateBounds(2);
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

    public boolean inRadius(Position otherPos, int radius) {
        double aX = pos.x;
        double aY = pos.y;
        if (pos.x < otherPos.x)
            aX = pos.x + width;
        if (pos.y < otherPos.y)
            aY = pos.y + height;
        return Math.abs(calculateMagnitude(aX, aY, otherPos.x, otherPos.y)) < radius;
    }

    public boolean isBelowDeadZoneTop() {
        return pos.y > VariableHandler.deadzoneTop + 20;
    }
}
