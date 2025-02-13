package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.SoundHandler;

import java.awt.*;

public class BulletEnemy extends Bullet{

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
    }

    public BulletEnemy(int x, int y, double angle, int vel) {
        this(x, y, angle);
        forwardVelocity = vel;
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
        } else if (state == STATE_IMPACT) {
            impact.runAnimation();
//            impactTimer++;
            this.velX = entity.getVelX();
            this.velY = entity.getVelY();
            super.update();
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
//    }

    @Override
    public void resurrect(int x, int y) {
        this.calculated = false;
        super.resurrect(x, y);
        SoundHandler.playSound(initSound);
    }
}
