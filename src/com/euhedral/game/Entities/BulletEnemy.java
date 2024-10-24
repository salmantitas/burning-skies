package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
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
        forwardVelocity = Utility.intAtWidth640(5);
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
            impact.runAnimation();
//            impactTimer++;
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
//            g.setColor(impactColor);
//            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);

            impact.drawAnimation(g, (int) x, (int) y, height, height);
        }
    }

    @Override
    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));
        double sizeOffset = 0.9;
        int xCorrection = 8;
        int yCorrection = 12;
        int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
        int offsetY = (int) (Engine.HEIGHT/2 - getCenterY()) / 15;
        int reflectionX, reflectionY;
//        int reflectionX = xCorrection + (int) x - offsetX;
//        int reflectionY = yCorrection + (int) y + offsetY;

        if (state == STATE_ACTIVE) {
            reflectionX = xCorrection + (int) x - offsetX;
            reflectionY = yCorrection + (int) y + offsetY;
            g2d.setColor(color);
            g2d.fillOval(reflectionX, reflectionY, (int) (width*sizeOffset) ,  (int) (height*sizeOffset));
        } else if (state == STATE_IMPACT) {
//            g2d.setColor(impactColor);
//            int impactX = (int) x - impactFactor;
//            int impactY = (int) y - impactFactor;
//            reflectionX = xCorrection + impactX - offsetX;
//            reflectionY = yCorrection + impactY + offsetY;
//            int impactWidth = width + impactFactor*2;
//            int impactHeight = height + impactFactor*2;
//            g2d.fillOval(reflectionX, reflectionY, (int) (impactWidth*sizeOffset), (int) (impactHeight*sizeOffset));

            int impactX = (int) x + xCorrection - offsetX;
            int impactY = (int) y + yCorrection + offsetY;
            int impactWidth = (int) ((double )height*sizeOffset);
            int impactHeight = impactWidth;
            impact.drawAnimation(g2d, impactX, impactY, impactWidth, impactHeight);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    @Override
    public void resurrect(int x, int y) {
        this.calculated = false;
        super.resurrect(x, y);
        SoundHandler.playSound(initSound);
    }
}
