package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;
//import com.euhedral.game.ContactID;
//import com.euhedral.game.EnemyID;

import java.awt.*;

public abstract class EnemyBoss extends Enemy {

    boolean alive = true;

    public EnemyBoss(int x, int y, int levelHeight) {
        super(x,y, levelHeight);
        shootTimerDefault = 150;
//        enemyID = EnemyID.Boss;
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
        if (inscreenY) {
            moveInScreen();
        } else {
            pos.y += velY;
            pos.x = Utility.clamp(pos.x, 0, Engine.WIDTH - width);
        }

//        System.out.println("Boss at (" + x + ", " + y + ")");
    }



    @Override
    public abstract void shoot();

    @Override
    public void destroy() {
        super.destroy();
        VariableHandler.setBossAlive(false);
        SoundHandler.playBGMPlay();
    }
}
