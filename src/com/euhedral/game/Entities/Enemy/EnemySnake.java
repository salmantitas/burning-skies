package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
//import com.euhedral.game.EnemyID;

import java.awt.*;

public class EnemySnake extends Enemy {
    public EnemySnake(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
//        enemyID = EnemyID.Snake;
    }

    public EnemySnake(int x, int y, Color color, int levelHeight) {
        super(x, y, color, levelHeight);
//        enemyID = EnemyID.Snake;
    }

    @Override
    public void initialize() {
        super.initialize();

        width = Utility.intAtWidth640(48);
        shootTimerDefault = 120;
        velY = 1.75f;
        setHealth(8,12);
        score = 200;
        distance = width * 5;
        movementDistance = distance;
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
//        int var = shotNum % 3;
        int newVel = Utility.intAtWidth640(5);
        double angle = 75;

//        if (var == 0) {
//            bullets.add(new BulletEnemy(x + width/2,y, 90));
//        } else if (var == 1) {
//            bullets.add(new BulletEnemy(x + width/2,y, angle, newVel));
//        } else {
//            bullets.add(new BulletEnemy(x + width/2,y, angle + 2 * (90 - angle), newVel));
//        }

        resetShooter();
    }

    public void moveLikeSnake() {
        if (movementDistance > 0) {
            movementDistance--;
        } else {
            movementDistance = distance;
        }

        int
                int0 = 0,
                int1 = Utility.perc(distance, 30),
                int2 = Utility.perc(distance, 50),
                int3 = Utility.perc(distance, 80);


        if (movementDistance <= distance && movementDistance > int3) {
            hMove = HorizontalMovement.LEFT;
        } else if (movementDistance <= int3 && movementDistance > int2 || movementDistance <= int2 && movementDistance > int1) {
            hMove = HorizontalMovement.RIGHT;
//        } else if (movementTimer <= int2 && movementTimer > int1) {
//            hMove = HorizontalMovement.RIGHT;
        } else if (movementDistance <= int1 && movementDistance > int0) {
            hMove = HorizontalMovement.LEFT;
        }
    }
}
