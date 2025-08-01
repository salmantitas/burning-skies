package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyMinefield2 extends Enemy{

    double xMin, xMax;

    public EnemyMinefield2(int x, int y, int levelHeight) {
        super(x, y,  levelHeight);

        shootTimerDefault = 15;

        score = 80;

        xMin = -width;
        xMax = Engine.WIDTH;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[4]);

        health_MAX = 3;
        velX_MIN = 8f;
        bulletVelocity = 1;

        commonInit();
    }

    public EnemyMinefield2(int x, int y, Color color, int levelHeight) {
        super(x, y, color, levelHeight);
    }

    @Override
    public void move() {
        if (isActive()) {

            velY = 0;
            if (x <= xMin) {
                velX = velX_MIN;
            } else if (x >= xMax) {
                velX = -velX_MIN;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }

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
            setImage(textureHandler.enemySide[5]);
            damageImage = textureHandler.enemyDamage[2];
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[4]);
            damageImage = textureHandler.enemyDamage[1];
        }
    }

    @Override
    protected void commonInit() {
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
        shootTimer = shootTimerDefault;
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_MINE2;
    }

    @Override
    public int getTurretY() {
        return (int) y + Utility.intAtWidth640(3);
    }

    @Override
    public Rectangle2D getBoundsHorizontal() {
        boundsHorizontal.setRect(x, y + 20, width, 1*height/3 + 2);
        return boundsHorizontal;
    }

    @Override
    public Rectangle2D getBoundsVertical() {
//        Rectangle2D bounds = null;
        if (velX > 0)
            boundsVertical.setRect(x + (width / 4) - 10, y, (2 * width) / 4, height);
        else
            boundsVertical.setRect(x + (width / 4) + 10, y, (2 * width) / 4, height);
        return boundsVertical;
    }
}
