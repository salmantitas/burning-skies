package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Player;

import java.awt.*;

public class EnemyBoss2 extends EnemyBoss{

    Player player;

    int
            distToCover = Engine.HEIGHT/8,
            min = Utility.percWidth(25) ,
            max = Utility.percWidth(75) - (int) 1.8 * width;
    private Graphics g;

    int midPoint, playerMid, playerEnd, offset;
    double playerStart;
    int shootTimerSmall = 10;

    public EnemyBoss2(int x, int y, Player player, int levelHeight) {
        super(x,y, levelHeight);
        height = Utility.intAtWidth640(48);
        width = height*3;
        pos.x = x - width/2;
        color = Color.orange;
        velX = Utility.intAtWidth640(2);
        health = 200;
        this.player = player;

        midPoint = x + width/2;
        playerStart = player.getX();
        playerMid = (int) playerStart + player.getWidth()/2;
        playerEnd = (int) playerStart + player.getWidth();
        offset = width/3;
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
        g.fillRect((int) pos.x, (int) pos.y, width, height);
    }

    @Override
    public void shoot() {
        double leftGun = pos.x + offset;
        double rightGun = pos.x + width- offset;
        // left gun
//        bullets.add(new BulletEnemy((int) (leftGun), y + height / 2, 110));

        // right gun
//        bullets.add(new BulletEnemy((int) (rightGun) , y + height / 2, 70));

        // front guns
//        bullets.add(new BulletEnemy((int) (leftGun), y + height / 2, 90));
//        bullets.add(new BulletEnemy((int) (rightGun) , y + height / 2, 90));

//        shotNum += 4;
//
//        if (shotNum % 12 == 0) {
//            shootTimer = shootTimerDefault;
//        } else shootTimer = shootTimerSmall;

//        System.out.println("Shooting at (" + (x + width/2) + ", " + y + ")" );
    }

    public void moveInScreen() {
        if (distToCover > 0) {
            distToCover--;
            pos.y += velY;
        }
        else {
            if (isPlayerCenter()) {

            } else if (isPlayerLeft()) {
                pos.x = (int) Math.max(pos.x - velX, 0);
            } else if (isPlayerRight()) {
                pos.x = (int) Math.min(pos.x + velX, Engine.WIDTH - width);
            }
        }
    }

    // Private Methods

    private boolean isPlayerLeft() {
        midPoint = (int) pos.x + width/2;
        playerStart = player.getX();
        playerEnd = (int) playerStart + player.getWidth();

        return  (midPoint > playerEnd);
    }

    private boolean isPlayerRight() {
        midPoint = (int) pos.x + width/2;
        playerStart = player.getX();

        return midPoint < playerStart;
    }

    private boolean isPlayerCenter() {
        playerStart = player.getX();
        playerEnd = (int) playerStart + player.getWidth();

        return (pos.x + offset < playerStart) && ((pos.x + width) - offset > playerEnd);
    }
}