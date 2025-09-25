package com.euhedral.engine;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBox {
    protected Rectangle2D bounds;

    public CollisionBox() {
        bounds = new Rectangle();
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public void setBounds(double x, double y, int width, int height) {
        bounds.setRect(x,y,width,height);
    }
}
