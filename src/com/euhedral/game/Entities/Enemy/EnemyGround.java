package com.euhedral.game.Entities.Enemy;

import com.euhedral.game.ContactID;

import java.awt.*;

public class EnemyGround extends Enemy{
    public EnemyGround(int x, int y, int levelHeight) {
        super(x, y, ContactID.Ground, levelHeight);
    }

    public EnemyGround(int x, int y, Color color, int levelHeight) {
        super(x, y, ContactID.Ground, color, levelHeight);
    }

    @Override
    public void initialize() {
        super.initialize();

        power = 1;
        shootTimerDef = 250;
        minVelY = 1.8f;
        velY = minVelY;
        hMove = HorizontalMovement.RIGHT;
        healthRange(4,6);
        score = 50;
        minVelX = 1.95f;
    }

    @Override
    protected void shoot() {
        super.shoot();
        shootDownDefault();
    }

    public float getVelX() {
        return velX;
    }
}
