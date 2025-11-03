package com.euhedral.Game.Entities.Projectile;

import java.awt.*;

public class MissilePlayer extends BulletPlayer{

    public MissilePlayer(double x, double y, double forwardVelocity) {
        super(x, y, forwardVelocity, -90);

        color = Color.GRAY;
        width = 8;
        height = 32;

        damage = 2;

        physics.acceleration = 0.5;

        shieldKiller = true;
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);
            g.fillRect(pos.intX(), pos.intY(), width, height);

//            g.setColor(Color.BLACK);
//            g.drawRect(pos.intX(), pos.intY(), width, height);
        } else if (state == STATE_IMPACT) {
            if (entity == null) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            } else if (entity.isActive()) {
                impact.drawAnimation(g, (int) pos.x - impactAnimationAdjustmentX, (int) pos.y, impactSize, impactSize);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (isActive()) {
            double sign = velY > 0 ? 1 : -1;
            double newVelY = Math.abs(velY) + physics.acceleration;
            velY = newVelY * sign;
        }
    }
}
