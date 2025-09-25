package com.euhedral.engine;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBox {
    protected Rectangle2D[] bounds;

    public CollisionBox(int num) {
        bounds = new Rectangle2D[num];

        initializeBounds(num);
    }

    private void initializeBounds(int num) {
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
}
