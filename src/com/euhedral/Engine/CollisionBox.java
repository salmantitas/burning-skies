package com.euhedral.Engine;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBox {
    protected Entity parent;
    protected Rectangle2D[] bounds;

    public CollisionBox(Entity parent, int num) {
        this.parent = parent;
        bounds = new Rectangle2D[num];

        initializeBounds(num);
    }

    protected void initializeBounds(int num) {
        for (int i = 0; i < num ; i ++) {
            bounds[i] = new Rectangle();
        }
    }

    public Rectangle2D getBounds() {
        return bounds[0];
    }

    public void setBounds(double x, double y, int width, int height) {
        bounds[0].setRect(x,y,width,height);
    }

    public Rectangle2D getBounds(int num) {
        return bounds[num];
    }

    public void setBounds(int num, double x, double y, int width, int height) {
        bounds[num].setRect(x,y,width,height);
    }

    public boolean checkCollision(Entity other) {
        CollisionBox otherCollisionBox = other.getCollisionBox();
        boolean collides = false;

        for (int i = 0; i < this.bounds.length; i++) {
            for (int j = 0; j < otherCollisionBox.bounds.length; j++) {
                collides = collides || this.bounds[i].intersects(otherCollisionBox.bounds[j]);
            }
        }
        return collides;
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) (parent.pos.x + 0.2*parent.width), (int) parent.pos.y,  (int) (0.6* parent.width),  parent.height/4);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle((int) (parent.pos.x + 0.2*parent.width), (int) parent.pos.y + 3*parent.height/4,  (int) (0.6* parent.width),  parent.height/4);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle((int) parent.pos.x, (int) (parent.pos.y + 0.35*parent.height),  parent.width/4,  (int) (parent.height * 0.3));
    }

    public Rectangle getBoundsRight() {
        return new Rectangle((int) parent.pos.x + 3*parent.width/4, (int) (parent.pos.y + 0.35*parent.height),  parent.width/4,  (int) (parent.height * 0.3));
    }
}
