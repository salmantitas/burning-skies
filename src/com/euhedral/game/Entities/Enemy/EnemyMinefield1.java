package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyMinefield1 extends Enemy{

    double yMin, yMax;

    public EnemyMinefield1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[3]);

        shootTimerDefault = 30;
//        attackEffect = true;

        health_MAX = 2;
        commonInit();
        score = 60;
        forwardVelocity = EntityHandler.backgroundScrollingSpeed + 1;
//        velX_MIN = 1.75f;
        velY_MIN = forwardVelocity;

        yMin = -height;
        yMax = levelHeight + 3*height;

        disableOffset = height * 4;
        bottomBounds = levelHeight + disableOffset;

        bulletVelocity = 3;
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
        updateBulletAngle();
        shootTimer = shootTimerDefault;
    }

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

//    @Override
//    protected void moveInScreen() {
//        pos.y += (velY + EntityHandler.backgroundScrollingSpeed);
//        pos.x += velX;
//    }

    private void updateBulletAngle() {
        bulletAngle = calculateAngle(Engine.WIDTH/2, Engine.HEIGHT/2);
    }

    @Override
    public int getTurretX() {
        return (int) pos.x + width/2 - Utility.intAtWidth640(2);
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
