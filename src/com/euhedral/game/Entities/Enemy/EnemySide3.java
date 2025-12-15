package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.geom.Rectangle2D;

public class EnemySide3 extends Enemy{

    double destinationX, destinationY;
    boolean firstEntry;

    double xMin, xMax;

    public EnemySide3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        bulletVelocity = 5;
        score = 100;

        shootTimerFirst = 40;

        attackEffect = true;

        xMin = -2*width;
        xMax = Engine.WIDTH + width;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[2]);

        health_MAX = 2;
        velX_MIN = 3f;

        tracking = true;

        commonInit();
    }


//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//    }

    @Override
    public void move() {
        if (isActive()) {
            double xMin = -2*width, xMax = Engine.WIDTH + width;

            velY = 0;
            if (pos.x <= xMin && velX < 0) {
                velX = velX_MIN;
                if (!firstEntry)
                    pos.y = destinationY;
            } else if (pos.x >= xMax && velX > 0) {
                velX = -velX_MIN;
                if (!firstEntry)
                    pos.y = destinationY;
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
        updateDestination();
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

    private void updateDestination() {
        int offsetY = -32;
        destinationX = EntityHandler.playerPositon.x;
        destinationY = EntityHandler.playerPositon.y + offsetY;
    }

    @Override
    public double getBulletAngle() {
        firstEntry = false; // todo: move somewhere else
        return calculateAngle(destinationX, destinationY); // stub
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        velX = velX_MIN;
        velY = 0;
        firstEntry = true;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_SIDE3;
    }

    @Override
    public double getTurretY() {
        return pos.y + Utility.intAtWidth640(3);
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
