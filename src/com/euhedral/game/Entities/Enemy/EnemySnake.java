package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.ContactID;
import com.euhedral.game.EnemyID;
import com.euhedral.game.Entities.BulletEnemy;

import java.awt.*;

public class EnemySnake extends Enemy {
    public EnemySnake(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, contactID, levelHeight);
        enemyID = EnemyID.Snake;
    }

    public EnemySnake(int x, int y, ContactID contactID, Color color, int levelHeight) {
        super(x, y, contactID, color, levelHeight);
        enemyID = EnemyID.Snake;
    }

    @Override
    public void initialize() {
        super.initialize();

        width = Utility.intAtWidth640(48);
        shootTimerDefault = 120;
        velY = 1.75f;
        healthRange(8,12);
        score = 200;
        distance = width * 5;
        movementTimer = distance;
    }

    @Override
    protected void shoot() {
        super.shoot();
        snakeShoot();
    }

    @Override
    public void move() {
        super.move();
        moveLikeSnake();
    }

    private void snakeShoot() {
        int var = shotNum % 3;
        int newVel = Utility.intAtWidth640(5);
        double angle = 75;

        if (var == 0) {
            bullets.add(new BulletEnemy(x + width/2,y, 90));
        } else if (var == 1) {
            bullets.add(new BulletEnemy(x + width/2,y, angle, newVel));
        } else {
            bullets.add(new BulletEnemy(x + width/2,y, angle + 2 * (90 - angle), newVel));
        }

        resetShooter();
    }

    public void moveLikeSnake() {
        if (movementTimer > 0) {
            movementTimer--;
        } else {
            movementTimer = distance;
        }

        int
                int0 = 0,
                int1 = Utility.perc(distance, 30),
                int2 = Utility.perc(distance, 50),
                int3 = Utility.perc(distance, 80);


        if (movementTimer <= distance && movementTimer > int3) {
            hMove = HorizontalMovement.LEFT;
        } else if (movementTimer <= int3 && movementTimer > int2 || movementTimer <= int2 && movementTimer > int1) {
            hMove = HorizontalMovement.RIGHT;
//        } else if (movementTimer <= int2 && movementTimer > int1) {
//            hMove = HorizontalMovement.RIGHT;
        } else if (movementTimer <= int1 && movementTimer > int0) {
            hMove = HorizontalMovement.LEFT;
        }
    }
}
