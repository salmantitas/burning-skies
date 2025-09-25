package com.euhedral.game.Entities;

import com.euhedral.engine.CollisionBox;
import com.euhedral.engine.Entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBoxAirplane extends CollisionBox {
    protected Rectangle2D[] bounds;

    public CollisionBoxAirplane(Entity parent) {
        super(parent,2);
        initializeBounds(2);
    }

    public boolean checkCollision(Rectangle2D object, Rectangle2D horizontalBounds, Rectangle2D verticalBounds) {
        boolean collidesVertically = object.intersects(verticalBounds);
        boolean collidesHorizontally = object.intersects(horizontalBounds);

        return collidesVertically || collidesHorizontally;
    }
}
