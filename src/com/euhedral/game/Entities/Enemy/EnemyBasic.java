package com.euhedral.game.Entities.Enemy;

import com.euhedral.game.ContactID;

import java.awt.*;

public class EnemyBasic extends Enemy{
    public EnemyBasic(int x, int y, ContactID contactID) {
        super(x, y, contactID);
    }

    public EnemyBasic(int x, int y, ContactID contactID, Color color) {
        super(x, y, contactID, color);
    }

    @Override
    public void initialize() {
        super.initialize();

        power = 1;
        shootTimerDef = 250;
        velY = 1.8f;
        healthRange(4,6);
        score = 50;
    }

    @Override
    protected void shoot() {
        super.shoot();
        shootDownDefault();
    }
}
