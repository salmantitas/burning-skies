package com.euhedral.Game.Entities;

import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.*;
//import com.euhedral.game.PickupID;

import java.awt.*;

public class Pickup extends MobileEntity {

    private int value;

    public Pickup(double x, double y, EntityID entityID, int value) {
        super(x, y, entityID);
        width = Utility.intAtWidth640(16);
        height = width;
//        height = width * 2;
//        if (entityID == entityID.PickupHealth)
//            color = Color.green;
//        else if (entityID == entityID.PickupShield)
//            color = Color.YELLOW;
//        else color = Color.orange;
        velY = EntityHandler.backgroundScrollingSpeed;
        setValue(value);
        selectImage();

    }

    public Pickup(int x, int y, EntityID entityID, Color color, int value) {
        this(x, y, entityID, value);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
//        drawDefault(g);
    }

    public void resurrect(double x, double y, EntityID id) {
        super.resurrect(x, y, id);
//        this.x = x;
//        this.y = y;
        this.id = id;
        selectImage();
    }

    public void collision() {
        int pickupSound = SoundHandler.PICKUP;
        if (id == EntityID.PickupHealth)
            VariableHandler.health.increase(value);
        else if (id == EntityID.PickupShield) {
            VariableHandler.shield.increase(value);
        }
        else if (id == EntityID.PickupHoming) {
//            if (VariableHandler.power.isMax())
                VariableHandler.homing = true;
//            VariableHandler.power.increase(value);
        }
        else if (id == EntityID.PickupFirepower) {
            VariableHandler.firepower.increase(value);
        }
        else if (id == EntityID.PickupPulse)
            VariableHandler.pulse = true;
//        else if (id == EntityID.PickupBomb)
//            VariableHandler.shield.increase(value);
        SoundHandler.playSound(pickupSound);
        disable();
    }

    private void selectImage() {
        if (id == EntityID.PickupHealth)
            image = GameController.getTexture().pickup[0];
        else if (id == EntityID.PickupShield)
            image = GameController.getTexture().pickup[1];
        else if (id == EntityID.PickupHoming)
            image = GameController.getTexture().pickup[3];
        else if (id == EntityID.PickupFirepower)
            image = GameController.getTexture().pickup[2];
        else if (id == EntityID.PickupPulse) {
            image = GameController.getTexture().pickup[4];
        }
    }

    public void setValue(int value) {
        this.value = value;
    }
}