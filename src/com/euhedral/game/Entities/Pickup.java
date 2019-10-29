package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;
import com.euhedral.game.EntityID;
import com.euhedral.game.PickupID;

import java.awt.*;

public class Pickup extends MobileEntity {

    private PickupID pickupID;

    public Pickup(int x, int y, PickupID pickupID, Color color) {
        super(x, y, EntityID.Pickup);
        width = Engine.intAtWidth640(16);
        height = width * 2;
        this.pickupID = pickupID;
        this.color = color;
        velY = 1.8f;
    }

    @Override
    public void update() {
        super.update();
//        y += velY;
//        System.out.println(x + ", " + y);
    }

    @Override
    public void render(Graphics g) {
//        super.render(g);
        drawDefault(g);
    }

    public PickupID getID() {
        return pickupID;
    }
}
