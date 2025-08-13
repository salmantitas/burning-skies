package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;
import com.euhedral.game.VariableHandler;

public class EnemyBasic3 extends Enemy{

    double angleIncrement = 0.50;

    public EnemyBasic3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[2]);

        shootTimerDefault = 50; // too low: 10, too high: 100
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

            if (x < -width || x > Engine.WIDTH -0*width) {
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
        if (x < -width || x > Engine.WIDTH - 0*width) {

        } else
            super.shoot();
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
//        velY = forwardVelocity;
//        velX_MIN = 0;
        angle = SOUTH;
    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC3;
    }
}
