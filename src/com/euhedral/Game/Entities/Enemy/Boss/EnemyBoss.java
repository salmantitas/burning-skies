package com.euhedral.Game.Entities.Enemy.Boss;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;
//import com.euhedral.game.ContactID;
//import com.euhedral.game.EnemyID;

import java.awt.*;

public abstract class EnemyBoss extends Enemy {

//    protected boolean alive = true;

    protected Position destination;
    protected int minX,maxX, minY, maxY;

    protected int currentGun = 0;
    protected int guns_MAX = 4;
    protected int shotMode = 0;
    protected int shotMode_MAX = guns_MAX;

    public EnemyBoss(double x, double y, int levelHeight) {
        super(x,y, levelHeight);
        damage = 100;
        shootTimerDefault = 150;
    }

//    public boolean isAlive() {
//        return alive;
//    }

//    public void setAlive(boolean alive) {
//        this.alive = alive;
//    }

    @Override
    public void update() {
        super.update();
//        move();
//        if (health <= 0)
//            alive = false;
    }

//    @Override
//    public void render(Graphics g) {
//        super.render(g);
//    }

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



//    @Override
//    public abstract void shoot();

    @Override
    public void damage(int damageValue, boolean isMissile) {
        super.damage(damageValue, isMissile);
        VariableHandler.setHealthBoss(health);
    }

    @Override
    public void destroy() {
        if (isActive()) {
            SoundHandler.BGMUp();
        }
        super.destroy();
        VariableHandler.setBossAlive(false);

    }
}
