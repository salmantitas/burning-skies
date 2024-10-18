package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.ContactID;
import com.euhedral.game.EnemyID;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyMove extends Enemy {
    public EnemyMove(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, contactID, levelHeight);
        enemyID = EnemyID.Move;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyHeavy[0]);
    }

    public EnemyMove(int x, int y, ContactID contactID, Color color, int levelHeight) {
        this(x,y, contactID, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

        width = width*2; // todo: check, it's being called twice
        shootTimerDefault = 60;
        score = 150;
        velX = 0;
        minVelY = 1.75f;
        distance = 0; // stub ; width * 2;
        movementDistance = distance;
        commonInit();
    }

    @Override
    protected void shoot() {
        super.shoot();
        moveShoot();
    }

    @Override
    public void move() {
        super.move();
        moveHorizontally();
    }

    private void moveShoot() {
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width), y + height / 2, 90));
    }

    public void moveHorizontally() {
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
        } else if (movementDistance <= int3 && movementDistance > int2 || movementDistance <= int1 && movementDistance > int0) {
            hMove = HorizontalMovement.NONE;
        } else if (movementDistance <= int2 && movementDistance > int1) {
            hMove = HorizontalMovement.RIGHT;
        }
//        } else if (movementTimer <= int1 && movementTimer > int0) {
//            hMove = HorizontalMovement.NONE;
//        }
    }

    @Override
    protected void commonInit() {
        this.setHealth(10);
        velY = 2.5f;
    }
}
