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

    public void spawnFromPool(int x, int y, EntityID id, int value) {
        entity = findInList();
        entity.resurrect(x, y);
        pickup = (Pickup) entity;
        pickup.setValue(value);
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
