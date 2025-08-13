package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.game.Background;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;

import java.awt.*;

public class BackgroundObject extends MobileEntity {

    public BackgroundObject(double x, double y, EntityID id) {
        super(x, y, id);
        color = Color.GREEN;
        forwardVelocity = Background.scrollRate;
        velY = forwardVelocity;
    }
}
