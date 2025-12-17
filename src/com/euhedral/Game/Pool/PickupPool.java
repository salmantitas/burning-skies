package com.euhedral.Game.Pool;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.Pool;
import com.euhedral.Game.Entities.Pickup;
import com.euhedral.Game.Entities.Player;
import com.euhedral.Game.EntityID;

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
                if (pickup.checkCollision(player)) {
                    pickup.collision(player);
                    increase();
//                    Utility.log("Pool: " + pickups.getPoolSize());
                }
            }
        }
    }
}
