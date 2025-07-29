package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyBasic1 extends Enemy{

    public EnemyBasic1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[0]);

        shootTimerDefault = 150;
//        attackEffect = true;

        //        enemyType = EntityHandler.TYPE_BASIC;

        health_MAX = 2;
        commonInit();
        score = 50;
        velX_MIN = 1.75f;
    }

    public EnemyBasic1(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//    }

//    @Override
//    protected void shoot() {
//        super.shoot();
//        shootDefault();
//    }

//    @Override
//    public void update() {
//        super.update();
//        if (state == STATE_ACTIVE && inscreen) {
//            if (movementDistance >= 0) {
//                movementDistance -= Math.abs(velX);
//            } else {
//                velX = 0;
//            }
//        }
//    }

//    @Override
//    public void render(Graphics g) {
//        if (attackEffect) {
//            boolean secondsTillShotFire = (shootTimer < 20);
//            if (isActive() && secondsTillShotFire) {
//                g.setColor(Color.red);
//
//                Graphics2D g2d = (Graphics2D) g;
//                g.setColor(Color.RED);
//
//
//                double drawX = x - (0.5) * (double) width;
//                double drawY = y - (0.5) * (double) height;
//                int arcAngle = 20;
//
//                g2d.setComposite(Utility.makeTransparent(0.5f));
//                g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(getBulletAngle()) - arcAngle / 2, arcAngle);
//                g2d.setComposite(Utility.makeTransparent(1f));
//            }
//        }
//
//        g.setColor(color);
//        super.render(g);
////        super.render(g);
////        if (isActive()) {
////            super.render(g);
//////            renderBounds(g);
////            renderPath(g);
////        } else {
////            if (!explosion.playedOnce) {
////                explosion.drawAnimation(g, (int) x, (int) y, width, height);
////            }
////        }
//    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    private void renderPath(Graphics g) {
        g.setColor(Color.RED);
        int pathLength = Engine.HEIGHT;
        int originX = (int) x + width/2;
        int originY = (int) y + height/2;
        for (int i = 0; i < pathLength; i ++) {
            g.drawLine(originX, originY, originX + 0, originY + i);
        }
    }

    @Override
    public void setHMove(int direction) {

    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_BASIC1;
    }
}
