package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.BulletEnemy;

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
        this.x = x - width/2;
        color = Color.orange;
        velX = Utility.intAtWidth640(2);
        health = 100;
        moveLeft = true;
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
            if (!moveLeft)
                x += velX;
            else
                x -= velX;
        }

        if (x < min)
            moveLeft = false;
        if (x > max)
            moveLeft = true;
    }

    // Private Methods

    // Needs to override


}
