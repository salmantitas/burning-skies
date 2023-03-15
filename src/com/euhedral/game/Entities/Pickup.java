package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityID;
//import com.euhedral.game.PickupID;

import java.awt.*;

public class Pickup extends MobileEntity {

//    private PickupID pickupID;

    public Pickup(int x, int y, EntityID entityID) {
        super(x, y, entityID);
        width = Utility.intAtWidth640(16);
        height = width * 2;
//        this.pickupID = pickupID;
        if (entityID == entityID.PickupHealth)
            color = Color.green;
        else color = Color.YELLOW;
        velY = 1.8f;
    }

    public Pickup(int x, int y, EntityID entityID, Color color) {
        this(x, y, entityID);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Graphics g) {
//        super.render(g);
        drawDefault(g);
    }
}
