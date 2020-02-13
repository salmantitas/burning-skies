package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityID;

import java.awt.*;

public class Bullet extends MobileEntity {
    protected int x, y, vel;
    protected float velX, velY;
    protected double angle;
    protected boolean collided;
    protected int width, height;
    protected Color color;
    protected boolean calculated = false;

    Bullet(int x, int y) {
        super(x ,y, EntityID.Bullet);
        this.x = x;
        this.y = y;
        collided = false;
        width = Utility.intAtWidth640(8)/2;
        height = Utility.intAtWidth640(24)/2;
        vel = Utility.intAtWidth640(4);
    }

    Bullet(int x, int y, double angle) {
        this(x,y);
        this.angle = angle % 360;
    }

    protected void calculateVelocities() {
        double angleX;
        double angleY;
//        if (angle >= - 90 && angle <= 90) {
            angleX = Math.toRadians(360 - angle);
            angleY = Math.toRadians(angle);
//        } else {
//            angleX = Math.toRadians(angle);
//            angleY = Math.toRadians(360 - angle);
//        }
            velX = (float) (vel * Math.cos(angleX));
            velY = (float) (vel * Math.sin(angleY));
//        } else {
//            velX = (float) (vel * Math.sin(angleX));
//            velY = (float) (vel * Math.cos(angleY));
//        }
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
}
