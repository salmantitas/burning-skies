package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyMinefield2 extends Enemy{

    double xMin, xMax;

    public EnemyMinefield2(int x, int y, int levelHeight) {
        super(x, y,  levelHeight);

        shootTimerFirst = 190;
        shootTimerDefault = 30;

        score = 80;

        xMin = -width;
        xMax = Engine.WIDTH;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[4]);

        health_MAX = 3;
        velX_MIN = 7f;
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
            setImage(textureHandler.enemySide[7]);
            damageImage = textureHandler.enemyDamage[2];
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[6]);
            damageImage = textureHandler.enemyDamage[1];
        }
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        velX = velX_MIN;
        velY = 0;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_MINE2;
    }

    @Override
    public double getTurretY() {
        return  pos.y + Utility.intAtWidth640(3);
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
