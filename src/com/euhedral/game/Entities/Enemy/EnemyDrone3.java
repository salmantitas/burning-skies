package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyDrone3 extends EnemyDrone{

    int explosionTimer;
    int explosionTimer_MAX = 400; // too high = 1000; too low = 100
    int explosionOffset;

    public EnemyDrone3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[2]);

        score = 75;
        damage = 30;
    }

    @Override
    public void initialize() {
        super.initialize();

//        enemyType = EntityHandler.TYPE_DRONE;

//        power = 1;
//        shootTimerDefault = 250;
//        minVelX = 2f;
        health_MAX = 1;
        commonInit();
    }

    @Override
    public void update() {
        super.update();
        if (isActive() && inscreenY) {
            explosionTimer++;
            if (explosionTimer >= explosionTimer_MAX) {
                state = STATE_EXPLODING;
            }
            explosionOffset = explosionTimer / 2 - Utility.intAtWidth640(7);
        }
    }

    @Override
    public void render(Graphics g) {
        // Render Explsion Radius
        if (isActive()) {
            g.setColor(Color.RED);
            Graphics2D g2d = (Graphics2D) g;

//            maxOpacity = 0.4f;
            float minOpacity = 0.1f;
            float opacity = (float) explosionTimer / 1000;

            g2d.setComposite(Utility.makeTransparent(Math.max(minOpacity, opacity)));
            g.fillOval((int) x - explosionOffset, (int) y - explosionOffset, explosionTimer, explosionTimer);
            g2d.setComposite(Utility.makeTransparent(1f));
        }

        super.render(g);
    }

    @Override
    protected void renderExplosion(Graphics g) {
        if (!explosion.playedOnce) {
            int size = Math.max(explosionTimer, explosionTimer);
            int expX = (int) x - (size - width)/2;
            int expY = (int) y - (size - height)/2;
            explosion.drawAnimation(g, expX, expY, size, size);
        }
    }

    @Override
    public void move() {
        if (isActive() && inscreenY) {

        } else if (isExploding()) {
            velY = explodingVelocity;
            velX = 0;
        }
        moveInScreen();
    }

    @Override
    public Rectangle2D getBounds() {
        int size = Math.max(explosionTimer, explosionTimer);
        int expX = (int) x - (size - width)/3;
        int expY = (int) y - (size - height)/3;
        return new Rectangle(expX, expY, 2*size/3, 2*size/3);
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_DRONE3;
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        forwardVelocity = 3;
        explosionTimer = 0;
    }

}