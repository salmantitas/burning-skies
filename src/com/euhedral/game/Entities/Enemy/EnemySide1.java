package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemySide1 extends Enemy{
    public EnemySide1(int x, int y, int levelHeight) {
        super(x, y,  levelHeight);

        shootTimerDefault = 50;
//        shootTimer = 50;
        score = 200;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[2]);
    }

    public EnemySide1(int x, int y, Color color, int levelHeight) {
        super(x, y, color, levelHeight);
//        enemyID = EnemyID.Fast;
    }

    @Override
    public void initialize() {
        super.initialize();

//        enemyType = EntityHandler.TYPE_SIDE;

        health_MAX = 3;
        velX_MIN = 10f;

//        shootTimer = shootTimerDefault;
        commonInit();
    }

    @Override
    public void move() {
        if (isActive()) {
            double xMin = -width, xMax = Engine.WIDTH;

            velY = 0;
            if (x <= xMin) {
                velX = velX_MIN;
            } else if (x >= xMax) {
                velX = -velX_MIN;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }
//        if (!inscreen)
//            Utility.log("min: " + minVelX + "| vel: " + velX);
        moveInScreen();
    }

//    @Override
//    protected void shoot() {
//        super.shoot();
//        shot += 1;
////        fastShoot();
//    }

    @Override
    public void update() {
        super.update();
        setImage();
    }

    private void setImage() {
        if (velX > 0) {
            setImage(textureHandler.enemySide[3]);
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[2]);
        }
    }

    @Override
    protected void commonInit() {
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_SIDE1;
    }

    @Override
    public int getTurretY() {
        return (int) y + Utility.intAtWidth640(3);
    }

    @Override
    public Rectangle2D getBoundsHorizontal() {
        Rectangle2D bounds = new Rectangle2D.Double(x, y + 20, width, 1*height/3 + 2);
        return bounds;
    }

    @Override
    public Rectangle2D getBoundsVertical() {
        Rectangle2D bounds = null;
        if (velX > 0)
            bounds = new Rectangle2D.Double(x + (width / 4) - 10, y, (2 * width) / 4, height);
        else
            bounds = new Rectangle2D.Double(x + (width / 4) + 10, y, (2 * width) / 4, height);
        return bounds;
    }
}
