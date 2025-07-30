package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Pickup;
import com.euhedral.game.Entities.Player;

import java.util.LinkedList;

public class PickupPool extends Pool {
    Entity entity;
    Pickup pickup;
    public PickupPool() {
        super();
    }

    public void spawnFromPool(double x, double y, EntityID id, int value) {
        entity = findInList();
//        entity.resurrect(x, y, id);
        pickup = (Pickup) entity;
        pickup.resurrect(x, y, id);
        pickup.setValue(value);
        pickup.setId(id);
        decrease();
    }

    public void checkCollisions(Player player) {
        for (Entity entity: entities) {
            pickup = (Pickup) entity;
            if (pickup.isActive()) {
                if (pickup.getBounds().intersects(player.getBounds())) {
                    pickup.collision();
                    increase();
//                    Utility.log("Pool: " + pickups.getPoolSize());
                }
            }
        }
    }
}
