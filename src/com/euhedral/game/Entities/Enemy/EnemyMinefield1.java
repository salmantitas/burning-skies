package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class EnemyMinefield1 extends Enemy{

    double yMin, yMax;

    public EnemyMinefield1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[3]);

        shootTimerDefault = 60;
//        attackEffect = true;

        health_MAX = 2;
        commonInit();
        score = 60;
        forwardVelocity = EntityHandler.backgroundScrollingSpeed + 1;
        velY_MIN = forwardVelocity;

        yMin = -height;
        yMax = levelHeight + 3*height;

        disableOffset = height * 4;
        bottomBounds = levelHeight + disableOffset;

        bulletVelocity = 3;
    }

//    @Override
//    protected void commonInit() {
//        super.commonInit();
////        updateBulletAngle();
//    }

    @Override
    protected void shoot() {
        updateBulletAngle();
        super.shoot();
    }

    @Override
    public void move() {
        if (isActive()) {

            velX = 0;
            if (pos.y <= yMin) {
                velY = velY_MIN;
            } else if (pos.y >= yMax) {
                velY = -velY_MIN;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }

        moveInScreen();
    }

    private void updateBulletAngle() {
        int targetX = Engine.WIDTH/2;
        int targetY = Engine.HEIGHT/2;

//        if (velY > 0) {
//            targetY = levelHeight;
//        } else {
            targetY = 0;
//        }

        bulletAngle = calculateAngle(targetX, targetY);
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform original = g2d.getTransform();
        if (velY < 0 ) {
            g2d.rotate(Math.toRadians(180), pos.intX() + width/2, pos.intY() + height/2);
        }
        super.drawImage(g, image);
        g2d.setTransform(original);
    }

    @Override
    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));

        reflectionX = reflection.calculateReflectionX(pos.x, getCenterX());
        reflectionY = reflection.calculateReflectionY(pos.y, getCenterY());
        newWidth = (int) (width * reflection.sizeOffset);
        newHeight = (int) (height * reflection.sizeOffset);

        if (state == STATE_ACTIVE) {
            AffineTransform original = g2d.getTransform();
            if (velY < 0 ) {
                g2d.rotate(Math.toRadians(180), pos.intX() + width/2, pos.intY() + height/2);
            }
            g2d.drawImage(image, reflectionX + (jitter_MULT * jitter), reflectionY + (jitter_MULT * jitter), newWidth, newHeight, null);
            g2d.setTransform(original);
        } else if (state == STATE_EXPLODING) {
            explosion.drawAnimation(g2d, reflectionX, reflectionY, newWidth, newHeight);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    @Override
    public double getTurretX() {
        return pos.x + width/2 - Utility.intAtWidth640(2);
    }

    private void renderPath(Graphics g) {
        g.setColor(Color.RED);
        int pathLength = Engine.HEIGHT;
        int originX = (int) pos.x + width/2;
        int originY = (int) pos.y + height/2;
        for (int i = 0; i < pathLength; i ++) {
            g.drawLine(originX, originY, originX + 0, originY + i);
        }
    }

    @Override
    public void setHMove(int direction) {

    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_MINE1;
    }
}
