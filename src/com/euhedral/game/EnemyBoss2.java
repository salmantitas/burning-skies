package com.euhedral.game;

import com.euhedral.engine.Engine;

import java.awt.*;

public class EnemyBoss2 extends EnemyBoss{

    int
            distToCover = Engine.HEIGHT/8,
            min = Engine.percWidth(25) ,
            max = Engine.percWidth(75) - (int) 1.8 * width;

    public EnemyBoss2(int x, int y) {
        super(x,y);
        height = Engine.intAtWidth640(48);
        width = height*3;
        this.x = x - width/2;
        color = Color.orange;
        velX = Engine.intAtWidth640(2);
        health = 100;
        left = true;
    }

//    @Override
//    public void update() {
//        super.update();
//        move();
//    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(color);
        g.fillRect((int) x, (int) y, width, height);
    }

    @Override
    public void shoot() {
        // left gun
        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 110));

        // right gun
        bullets.add(new BulletEnemy(x + (int) (0.8 * width) , y + height / 2, 70));

        // front guns
        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
        bullets.add(new BulletEnemy(x + (int) (0.8 * width) , y + height / 2, 90));

        shootTimer = shootTimerDef;

//        System.out.println("Shooting at (" + (x + width/2) + ", " + y + ")" );
    }

    public void moveInScreen() {
        if (distToCover > 0) {
            distToCover--;
            y += velY;
        }
        else {
            if (!left)
                x += velX;
            else
                x -= velX;
        }

        if (x < min)
            left = false;
        if (x > max)
            left = true;
    }

    // Private Methods

    // Needs to override


}
