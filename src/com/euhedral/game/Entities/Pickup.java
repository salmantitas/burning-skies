package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.*;
//import com.euhedral.game.PickupID;

import java.awt.*;

public class Pickup extends MobileEntity {

    private int value;

    public Pickup(int x, int y, EntityID entityID, int value) {
        super(x, y, entityID);
        width = Utility.intAtWidth640(16);
        height = width * 2;
//        if (entityID == entityID.PickupHealth)
//            color = Color.green;
//        else if (entityID == entityID.PickupShield)
//            color = Color.YELLOW;
//        else color = Color.orange;
        velY = 1.8f;
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

    public void resurrect(int x, int y, EntityID id) {
        this.x = x;
        this.y = y;
        this.id = id;
        selectImage();
        super.resurrect(x, y, id);
    }

    public void collision() {
        if (id == EntityID.PickupHealth)
            VariableHandler.health.increase(value);
        else if (id == EntityID.PickupShield)
            VariableHandler.shield.increase(value);
        else VariableHandler.power.increase(value);
        SoundHandler.playSound(SoundHandler.PICKUP);
        disable();
    }

    private void selectImage() {
        if (id == EntityID.PickupHealth)
            image = GameController.getTexture().pickup[0];
        else if (id == EntityID.PickupPower)
            image = GameController.getTexture().pickup[1];
        else
            image = GameController.getTexture().pickup[2];
    }

    public void setValue(int value) {
        this.value = value;
    }
}