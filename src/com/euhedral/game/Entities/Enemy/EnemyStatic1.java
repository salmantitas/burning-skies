package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyStatic1 extends Enemy {

    int verticalPosition = 400;
    double destinationX, destinationY;
    double deceleration;

    public EnemyStatic1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        bulletVelocity = Utility.intAtWidth640(6);
        shootTimerDefault = 200;
//        shootTimer = shootTimerDefault;
        score = 150;

        double decelerationMAX = 0.012;
        double decelerationMIN = 0.010;
//        int randMAX = (int) (decelerationMAX / decelerationMIN);
//        int decelerationInt = Utility.randomRangeInclusive(1, randMAX);
//        deceleration = (double) (decelerationInt) * decelerationMIN;
        deceleration = decelerationMAX;

        attackEffect = true;

        setImage(textureHandler.enemyStatic[0]);
    }

    public EnemyStatic1(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 6;

        velX = 0;
        velY_MIN = 1.75f;
//        distance = 0; // stub ; width * 2;
//        movementDistance = distance;
        commonInit();
        damage = 90;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && shootTimer <= 50) {
            updateDestination();
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 1;
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
//        if (y < verticalPosition) {
        velY = Math.max(0, velY - deceleration);
        y += velY;
//        }
    }

//    public void moveHorizontally() {
//        if (movementDistance > 0) {
//            movementDistance--;
//        } else {
//            movementDistance = distance;
//        }
//
//        int
//                int0 = 0,
//                int1 = Utility.perc(distance, 30),
//                int2 = Utility.perc(distance, 50),
//                int3 = Utility.perc(distance, 80);
//
//
//        if (movementDistance <= distance && movementDistance > int3) {
//            hMove = HorizontalMovement.LEFT;
//        } else if (movementDistance <= int3 && movementDistance > int2 || movementDistance <= int1 && movementDistance > int0) {
//            hMove = HorizontalMovement.NONE;
//        } else if (movementDistance <= int2 && movementDistance > int1) {
//            hMove = HorizontalMovement.RIGHT;
//        }

    /// /        } else if (movementTimer <= int1 && movementTimer > int0) {
    /// /            hMove = HorizontalMovement.NONE;
    /// /        }
//    }
    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = 2.5f;
    }

    @Override
    public int getTurretX() {
        return (int) x + width / 2 - Utility.intAtWidth640(2);
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(getTurretX(), getTurretY(), destinationX, destinationY); // stub
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

//    @Override
//    public void render(Graphics g) {
//        g.setColor(Color.BLACK);
//
//        g.fillRect((int) destinationX, (int) destinationY, 10, 10);
//
//        super.render(g);
//    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        Rectangle2D r = getBounds();

        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(r);
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_STATIC1;
    }

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D r = getBounds();
        return object.intersects(r);
    }
}
