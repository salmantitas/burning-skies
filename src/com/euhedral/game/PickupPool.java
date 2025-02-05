package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Pickup;

public class PickupPool extends Pool {
    public PickupPool() {
        super();
    }

    public void spawnFromPool(int x, int y, EntityID id, int value) {
        Entity entity = findInList();
        entity.resurrect(x, y);
        Pickup pickup = (Pickup) entity;
        pickup.setValue(value);
        decrease();
    }
}
