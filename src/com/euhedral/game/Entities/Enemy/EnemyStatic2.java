package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyStatic2 extends Enemy {

    double destinationX, destinationY;
    double deceleration;

    public EnemyStatic2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        bulletVelocity = Utility.intAtWidth640(6);
        shootTimerDefault = 300;
        shootTimer = 50;
        score = 150;

        double decelerationMAX = 0.012;
        double decelerationMIN = 0.010;
        int randMAX = (int) (decelerationMAX / decelerationMIN);
        int decelerationInt = Utility.randomRangeInclusive(1, randMAX);
        deceleration = (double) (decelerationInt) * decelerationMIN;
        deceleration = decelerationMAX;

        bulletsPerShot_MAX = 5;

        attackEffect = true;

        setImage(textureHandler.enemyStatic[1]);
    }

    public EnemyStatic2(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 6;

        velX = 0;
        velY_MIN = 1.75f;
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
        bulletsPerShot += bulletsPerShot_MAX;
    }

    @Override
    public void move() {
        super.move();
    }


    @Override
    protected void moveInScreen() {
        velY = Math.max(0, velY - deceleration);
        y += velY;
    }

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
        double tempAngle = calculateAngle(getTurretX(), getTurretY(), destinationX, destinationY);
        return tempAngle + (bulletsPerShot - 1)*10 * (bulletsPerShot % 2 == 0 ? 1 : -1); // stub
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
        enemyType = EntityHandler.TYPE_STATIC2;
    }

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D r = getBounds();
        return object.intersects(r);
    }
}
