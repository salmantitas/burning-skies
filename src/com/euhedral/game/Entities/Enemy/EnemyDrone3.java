package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyDrone3 extends EnemyDrone1 {

    int explosionTimer;
    int explosionTimer_MAX = 400; // too high = 1000; too low = 100
    int explosionOffset;

    double radius;

    float minOpacity = 0.1f;
    float explosionRadiusOpacity;

    int score_MAX = 150;

    public EnemyDrone3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemyDrone[2]);

        score = 10;
        damage = 50;

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
            score = (explosionTimer * score_MAX) / explosionTimer_MAX;
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
            g2d = (Graphics2D) g;

//            maxOpacity = 0.4f;
//            float minOpacity = 0.1f;
            explosionRadiusOpacity = (float) explosionTimer / 1000;

            g2d.setComposite(Utility.makeTransparent(Math.max(minOpacity, explosionRadiusOpacity)));
            g.fillOval((int) pos.x - explosionOffset, (int) pos.y - explosionOffset, explosionTimer, explosionTimer);
            g2d.setComposite(Utility.makeTransparent(1f));
        }

        super.render(g);
    }

    @Override
    protected void renderExplosion(Graphics g) {
        if (!explosion.playedOnce) {
            size = Math.max(explosionTimer, explosionTimer);
            expX = (int) pos.x - (size - width)/2;
            expY = (int) pos.y - (size - height)/2;
            explosion.drawAnimation(g, expX, expY, size, size);

            g2d = (Graphics2D)  g;
            g2d.setComposite(Utility.makeTransparent(1 - explosion.getProgress()));
            renderScore(g);
            g2d.setComposite(Utility.makeTransparent(1f));
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
    public void updateBounds() {
        size = Math.max(explosionTimer, explosionTimer);
        expX = (int) pos.x - (size - width)/3;
        expY = (int) pos.y - (size - height)/3;

        collisionBox.setBounds(
                expX, expY, 2*size/3, 2*size/3
        );
    }

    @Override
    public double getBulletAngle() {
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE3;
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        forwardVelocity = 3;
        explosionTimer = 0;
    }

    public double getRadius() {
        Rectangle2D bounds = getBounds();
        radius = Math.sqrt(Math.pow(bounds.getWidth(), 2) + Math.pow(bounds.getHeight(), 2));
        return radius;
    }

}