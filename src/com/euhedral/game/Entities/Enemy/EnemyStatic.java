package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyStatic extends Enemy {

    int verticalPosition = 400;
    double destinationX, destinationY;

    public EnemyStatic(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        enemyType = EntityHandler.TYPE_STATIC;
        bulletVelocity = Utility.intAtWidth640(6);

//        textureHandler = GameController.getTexture();
//        setImage(textureHandler.enemyHeavy[0]);
    }

    public EnemyStatic(int x, int y, Color color, int levelHeight) {
        this(x,y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

        shootTimerDefault = 200;
        score = 200;
        velX = 0;
        minVelY = 1.75f;
        distance = 0; // stub ; width * 2;
        movementDistance = distance;
        commonInit();
        damage = 90;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreen) {
            updateDestination();
            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
                velX = 0;
            }
        }

//        if (state == STATE_EXPLODING) {
////            explosion.runAnimation();
//            if (explosion.playedOnce) {
////                disable();
//            }
//        }
    }

    @Override
    public void render(Graphics g) {
        boolean secondsTillShotFire = (shootTimer < 10);
        if (isActive() && secondsTillShotFire) {
            g.setColor(Color.red);
            int turretY = (int) y + height / 2;
            g.drawLine(getTurretX(), turretY, (int) destinationX, (int) destinationY);
        }

        g.setColor(color);
        super.render(g);
    }

    @Override
    protected void shoot() {
        super.shoot();
        shootDownDefault();
    }

    @Override
    protected void shootDownDefault() {
        shot += 1;
    }

    @Override
    public void move() {
        super.move();
//        moveHorizontally();
    }

    private void moveShoot() {
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width), y + height / 2, 90));
    }

    @Override
    protected void moveInScreen() {
        if (y < verticalPosition) {
            y += velY;
        }
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
        this.setHealth(5);
        velY = 2.5f;
    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(destinationX, destinationY); // stub
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY;
    }

    @Override
    public void resurrect(int x, int y) {
        commonInit();
        explosion.playedOnce = false;
        super.resurrect(x, y);
    }
}
