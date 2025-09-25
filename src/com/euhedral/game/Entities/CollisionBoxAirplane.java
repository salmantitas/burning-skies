package com.euhedral.game.Entities;

import com.euhedral.engine.CollisionBox;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CollisionBoxAirplane extends CollisionBox {
    protected Rectangle2D[] bounds;

    public CollisionBoxAirplane() {
        super(2);
        initializeBounds(2);
    }
}
