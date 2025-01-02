package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.ContactID;
//import com.euhedral.game.EnemyID;

import java.awt.*;

public class EnemyFast extends Enemy{
    public EnemyFast(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, contactID, levelHeight);
//        enemyID = EnemyID.Fast;
    }

    public EnemyFast(int x, int y, ContactID contactID, Color color, int levelHeight) {
        super(x, y, contactID, color, levelHeight);
//        enemyID = EnemyID.Fast;
    }

    @Override
    public void initialize() {
        super.initialize();

        power = 2;
        shootTimerDefault = 150;
        velY = 4f;
        setHealth(2,4);
        score = 100;
    }

    @Override
    protected void shoot() {
        super.shoot();
        fastShoot();
    }

    private void fastShoot() {
        int newVel = Utility.intAtWidth640(5);
        double angle = 75;
//        bullets.add(new BulletEnemy(x + width/2,y, angle, newVel));
//        bullets.add(new BulletEnemy(x + width/2,y, angle + 2 * (90 - angle), newVel));
        if (power == 2) {
//            bullets.add(new BulletEnemy(x + width/2,y, 90, newVel));
        }
    }
}
