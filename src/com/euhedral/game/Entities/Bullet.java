package com.euhedral.game.Entities;

import com.euhedral.engine.Animation;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet extends MobileEntity {
//    protected int vel;
    protected boolean collided;
    protected Color color;
    protected Color impactColor;
    protected boolean calculated = false;
    protected Animation impact;
    protected int impactSize = 32;

    protected int initSound = SoundHandler.BULLET_PLAYER;

    // Impact
    protected int impactTimer = 0;
    protected final int impactTimerCheck = 10;
    protected int impactFactor = 2;
    protected MobileEntity entity;
    protected TextureHandler textureHandler;

    // State Machine
    protected final int STATE_IMPACT = 2;

    // Reflection
    protected Reflection reflection;

    Bullet(int x, int y) {
        super(x ,y, EntityID.Bullet);
        this.x = x;
        this.y = y;
        collided = false;
        width = Utility.intAtWidth640(4);
        height = Utility.intAtWidth640(24)/2;
        forwardVelocity = Utility.intAtWidth640(6);

        textureHandler = GameController.getTexture();

        impact = new Animation(2, GameController.getTexture().impactSmall[0],
                GameController.getTexture().impactSmall[1],
                GameController.getTexture().impactSmall[2],
                GameController.getTexture().impactSmall[3]
        );

        reflection = new Reflection();
    }

    Bullet(int x, int y, double angle) {
        this(x,y);
        this.angle = angle % 360;
    }

    @Override
    protected void calculateVelocities() {
        super.calculateVelocities();
        calculated = true;
    }

    public void update() {
        if (!calculated)
            calculateVelocities();
        move();
    }

    @Override
    public void render(Graphics g) {
        drawDefault(g);
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            drawImage(g, image, width, height);
//            g.setColor(color);
//            g.fillOval((int) x, (int) y, width, height);
        } else if (state == STATE_IMPACT && entity.isActive()) {
//            g.setColor(impactColor);
//            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);

            impact.drawAnimation(g, (int) x, (int) y, impactSize, impactSize);
        }
    }

    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));

        int reflectionX = reflection.calculateReflectionX(x, getCenterX());
        int reflectionY = reflection.calculateReflectionY(y, getCenterY());
        int newWidth = (int) (width * reflection.sizeOffset);
        int newHeight = (int) (height * reflection.sizeOffset);

        if (state == STATE_ACTIVE) {
            g2d.drawImage(image, reflectionX, reflectionY, newWidth, newHeight, null);
        } else if (state == STATE_IMPACT) {
            int impactSize = Math.max(newWidth, newHeight);
            impact.drawAnimation(g2d, reflectionX, reflectionY, impactSize, impactSize);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    protected void move() {
        x += velX;
        y += velY;
    }

    public void mirror(Bullet bullet, double angle) {
        if (angle == 90) {
            velX = -bullet.velX;
            velY = bullet.velY;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public void resurrect(int x, int y) {
        super.resurrect(x, y);
        impact.endAnimation();
        impact.playedOnce = false;
    }

    public void destroy(MobileEntity entity) {
        state = STATE_IMPACT;
        this.entity = entity;
    }

    public boolean isImpacting() {
        return state == STATE_IMPACT;
    }

    public boolean checkDeathAnimationEnd() {
//        if (impactTimer > impactTimerCheck) {
//            impactTimer = 0;
//            return true;
//        }
//        return impactTimer > impactTimerCheck;

        return impact.playedOnce;
    }

    protected double getCenterY() {
        return (y + height / 2);
    }

}
