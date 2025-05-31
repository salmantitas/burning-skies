package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

public class EnemyBasic3 extends Enemy{

    public EnemyBasic3(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[2]);

        // todo: These happen last
        shootTimerDefault = 50; // too low: 10, too high: 100
        score = 100;
        angle = SOUTH;
//        forwardVelocity = 5;
    }

    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 4;
        commonInit();

    }

    @Override
    public void update() {
        super.update();
        double increment = 0.50;
        if (state == STATE_ACTIVE && inscreen) {
            if (hMove == HorizontalMovement.LEFT)
                angle = Math.min(angle + increment, EAST);
            else if (hMove == HorizontalMovement.RIGHT) {
                angle = Math.max(angle - increment, WEST);
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
        enemyType = EntityHandler.TYPE_BASIC3;
    }
}
