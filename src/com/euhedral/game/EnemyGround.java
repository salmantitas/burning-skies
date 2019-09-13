package com.euhedral.game;

import com.euhedral.engine.Engine;

import java.awt.*;
import java.util.Random;

public class EnemyGround extends Enemy {

    public EnemyGround(int x, int y) {
        super(x,y, EnemyID.Basic, ContactID.Ground);
        width = Engine.intAtWidth640(32);
        height = 2* width;
        color = Color.pink;
        r = new Random();
        health = r.nextInt(3) + 2;
        velY = 1.75f;
    }

    @Override
    public void update() {
        super.update();
        if (inscreen)
            System.out.println(velY);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }

//    protected void shoot() {
//        bullets.add(new BulletEnemy(x + width/2,y));
//        shootTimer = shootTimerDef;
//    }

    // Private Methods



//    public void move() {
//        y += velY;
//        x = Engine.clamp(x, 0, Engine.WIDTH - width);
//    }
}
