package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;

import java.awt.*;

public class Bullet extends MobileEntity {
    protected int vel;
    protected boolean collided;
    protected Color color;
    protected boolean calculated = false;

    Bullet(int x, int y) {
        super(x ,y, EntityID.Bullet);
        this.x = x;
        this.y = y;
        collided = false;
        width = Utility.intAtWidth640(8)/2;
        height = Utility.intAtWidth640(24)/2;
        forwardVelocity = Utility.intAtWidth640(4);
        SoundHandler.playSound(SoundHandler.BULLET);
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

    public void render(Graphics g) {

    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void resurrect(int x, int y) {
        super.resurrect(x, y);
        SoundHandler.playSound(SoundHandler.BULLET);
    }
}
