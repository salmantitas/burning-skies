package com.euhedral.Game.Entities.Projectile;

import java.awt.*;

public class MissileEnemy extends BulletEnemy{

    public MissileEnemy(double x, double y, double angle) {
        super(x, y, angle, 3, false);

        color = Color.GRAY;
        width = 8;
        height = 32;

        damage = 20;

        impactSize = 48;
        reflectedImpactSize = (int) (impactSize * reflection.sizeOffset);

        physics.acceleration = 0.2;
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);
            g.fillRect(pos.intX(), pos.intY(), width, height);
            drawOutline(g, width, height);
        } else if (state == STATE_IMPACT) {
            if (entity == null) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            } else if (entity.isActive()) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            }
        }
    }

    @Override
    protected void drawOutline(Graphics g, int targetWidth, int targetHeight) {
        if (state == STATE_ACTIVE) {
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawRect(pos.intX(), pos.intY(), targetWidth, targetHeight);
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
