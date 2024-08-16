package com.euhedral.game.Entities;

import com.euhedral.engine.Utility;
import com.euhedral.game.SoundHandler;

import java.awt.*;

public class BulletEnemy extends Bullet{

    BulletEnemy(int x, int y) {
        super(x, y);
    }

    public BulletEnemy(int x, int y, double angle) {
        super(x, y, angle);
        height = width * 6;
        forwardVelocity = Utility.intAtWidth640(6);
        initSound = SoundHandler.BULLET_ENEMY;
        color = Color.orange;
        impactColor = Color.red;
        SoundHandler.playSound(initSound);
    }

    public BulletEnemy(int x, int y, double angle, int vel) {
        super(x, y, angle);
//        this.vel = vel;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
        } else if (state == STATE_IMPACT) {
            impactTimer++;
            this.velX = entity.getVelX();
            this.velY = entity.getVelY();
            super.update();
        }
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, width, height);
        } else if (state == STATE_IMPACT) {
            g.setColor(impactColor);
            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);
        }
    }

    @Override
    public void resurrect(int x, int y) {
        this.calculated = false;
        super.resurrect(x, y);
        SoundHandler.playSound(initSound);
    }
}
