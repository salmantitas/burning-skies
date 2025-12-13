package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

public class EnemyBasic2 extends Enemy{

    int movementDistance_MAX = 3*64;

    public EnemyBasic2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[1]);

        shootTimerDefault = 125;
        score = 30;
        movementDistance = movementDistance_MAX;
//        attackEffect = true;

        health_MAX = 2;
        commonInit();

        velX_MIN = 1;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {

            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
//                velX = 0;
                if (hMove.equals(HorizontalMovement.LEFT)) {
                    setHMove(-1);
                } else {
                    setHMove(1);
                }
                movementDistance = movementDistance_MAX;
            }
        }
    }

    @Override
    public double getTurretX() {
        return  pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC2;
    }
}
