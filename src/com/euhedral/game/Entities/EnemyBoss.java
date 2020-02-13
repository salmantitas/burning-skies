package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.ContactID;
import com.euhedral.game.EnemyID;

import java.awt.*;

public abstract class EnemyBoss extends Enemy {

    boolean left = false, up = false, down = false; // if all is false, boss will move down like normal enemies
    boolean alive = true;

    public EnemyBoss(int x, int y) {
        super(x,y, EnemyID.Boss, ContactID.Boss);
        shootTimerDef = 150;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void update() {
        super.update();
//        move();
        if (health <= 0)
            alive = false;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    // Private Methods

    // Needs to override

    @Override
    public void move() {
        if (inscreen) {
            moveInScreen();
        } else {
            y += velY;
            x = Utility.clamp(x, 0, Engine.WIDTH - width);
        }

//        System.out.println("Boss at (" + x + ", " + y + ")");
    }



    @Override
    public abstract void shoot();
}
