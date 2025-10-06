package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

public class EnemyBasic3 extends Enemy{

    double angleIncrement = 0.50;

    public EnemyBasic3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[2]);

        shootTimerFirst = 20;
        shootTimerDefault = 60;

        score = 40;
        angle = SOUTH;

        health_MAX = 4;
        commonInit();
    }

//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//
//    }

    @Override
    public void update() {
        super.update();
        int angleAdjustment = 10;
        if (state == STATE_ACTIVE && inscreenY) {
            if (hMove == HorizontalMovement.LEFT)
                angle = Math.min(angle + angleIncrement, EAST - angleAdjustment);
            else if (hMove == HorizontalMovement.RIGHT) {
                angle = Math.max(angle - angleIncrement, WEST + angleAdjustment);
            }

            if (pos.x < -width || pos.x > Engine.WIDTH -0*width) {
                angle = SOUTH;
            }
        }
    }

    @Override
    protected void moveInScreen() {
        super.moveInScreen();
        calculateVelocities();
    }

    @Override
    protected void shoot() {
        if (pos.x < -width || pos.x > Engine.WIDTH - 0*width) {

        } else
            super.shoot();
    }

    @Override
    protected void commonInit() {
        super.commonInit();
        angle = SOUTH;
    }

    @Override
    public int getTurretX() {
        return (int) pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC3;
    }
}
