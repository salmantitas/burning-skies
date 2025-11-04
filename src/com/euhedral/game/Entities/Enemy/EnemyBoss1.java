package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;

import java.awt.*;

public class EnemyBoss1 extends EnemyBoss{

    int
            distToCover = Engine.HEIGHT/8,
            min = Utility.percWidth(25) ,
            max = Utility.percWidth(75) - (int) 1.8 * width;

    public EnemyBoss1(int x, int y, int levelHeight) {
        super(x,y, levelHeight);
        height = Utility.intAtWidth640(48);
        width = height*3;
        pos.x = x - width/2;
        color = Color.orange;
        velX = Utility.intAtWidth640(2);
        velY = offscreenVelY;
        health = 100;
        moveLeft = true;
    }

    @Override
    public void update() {
        super.update();
//        move();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(color);
        g.fillRect((int) pos.x, (int) pos.y, width, height);
    }

    @Override
    public void shoot() {
        // left gun
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 110));
//
//        // right gun
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width) , y + height / 2, 70));
//
//        // front guns
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width) , y + height / 2, 90));

        shootTimer = shootTimerDefault;

//        System.out.println("Shooting at (" + (x + width/2) + ", " + y + ")" );
    }

    public void moveInScreen() {
        if (distToCover > 0) {
            distToCover--;
            pos.y += velY;
        }
        else {
            if (!moveLeft)
                pos.x += velX;
            else
                pos.x -= velX;
        }

        if (pos.x < min)
            moveLeft = false;
        if (pos.x > max)
            moveLeft = true;
    }

    // Private Methods

    // Needs to override


}
