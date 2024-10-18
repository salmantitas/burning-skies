package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityID;
import com.euhedral.game.SoundHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet extends MobileEntity {
//    protected int vel;
    protected boolean collided;
    protected Color color;
    protected Color impactColor;
    protected boolean calculated = false;

    protected int initSound = SoundHandler.BULLET_PLAYER;

    // Impact
    protected int impactTimer = 0;
    protected final int impactTimerCheck = 10;
    protected int impactFactor = 2;
    protected MobileEntity entity;

    // State Machine
    protected final int STATE_IMPACT = 2;

    Bullet(int x, int y) {
        super(x ,y, EntityID.Bullet);
        this.x = x;
        this.y = y;
        collided = false;
        width = Utility.intAtWidth640(8)/2;
        height = Utility.intAtWidth640(24)/2;
        forwardVelocity = Utility.intAtWidth640(5);
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

    public void renderReflection(Graphics2D g2d, float transparency) {

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
    }

    public void destroy(MobileEntity entity) {
        state = STATE_IMPACT;
        this.entity = entity;
    }

    public boolean isImpacting() {
        return state == STATE_IMPACT;
    }

    public boolean checkDeathAnimationEnd() {
        if (impactTimer > impactTimerCheck) {
            impactTimer = 0;
            return true;
        }
        return impactTimer > impactTimerCheck;
    }

    protected double getCenterY() {
        return (y + height / 2);
    }
}
