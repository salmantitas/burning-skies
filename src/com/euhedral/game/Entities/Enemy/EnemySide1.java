package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.geom.Rectangle2D;

public class EnemySide1 extends Enemy{

    double xMin, xMax;

    public EnemySide1(int x, int y, int levelHeight) {
        super(x, y,  levelHeight);

        shootTimerFirst = 30;
        shootTimerDefault = 50;

        bulletVelocity = 4;

        score = 60;

        xMin = -width;
        xMax = Engine.WIDTH;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[0]);

        health_MAX = 3;
        velX_MIN = 9f;

        commonInit();
    }

    @Override
    public void move() {
        if (isActive()) {

            velY = 0;
            if (pos.x <= xMin) {
                velX = velX_MIN;
            } else if (pos.x >= xMax) {
                velX = -velX_MIN;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }
        moveInScreen();
    }


    @Override
    public void update() {
        super.update();
        setImage();
    }

    private void setImage() {
        if (velX > 0) {
            setImage(textureHandler.enemySide[1]);
            damageImage = textureHandler.enemyDamage[2];
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[0]);
            damageImage = textureHandler.enemyDamage[1];
        }
    }

    @Override
    protected void commonInit() {
//        super.commonInit();
        setHealth(health_MAX);
        velX = velX_MIN;
        velY = 0;
        jitter = 0;
        calibrateShootTimerFirst();
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_SIDE1;
    }

    @Override
    public int getTurretY() {
        return (int) pos.y + Utility.intAtWidth640(3);
    }

    @Override
    public Rectangle2D getBoundsHorizontal() {
        collisionBox.setBounds(0, pos.x, pos.y + 20, width, 1*height/3 + 2);
        return collisionBox.getBounds(0);
    }

    @Override
    public Rectangle2D getBoundsVertical() {
//        Rectangle2D bounds = null;
        if (velX > 0)
            collisionBox.setBounds(1, pos.x + (width / 4) - 10, pos.y, (2 * width) / 4, height);
        else
            collisionBox.setBounds(1, pos.x + (width / 4) + 10, pos.y, (2 * width) / 4, height);
        return collisionBox.getBounds(1);
    }
}
