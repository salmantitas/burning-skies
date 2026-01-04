package com.euhedral.Game.Entities.Enemy.Behavior;

import com.euhedral.Engine.MobileEntity;
import com.euhedral.Game.Entities.Enemy.Enemy;

public class MoveLeftRight {

    Enemy enemy;

    int movementDistance_MAX = 3*64;
    int movementDistance;

    public MoveLeftRight(Enemy enemy) {
        this.enemy = enemy;
        movementDistance = movementDistance_MAX;
    }

    public void update() {
        if (movementDistance >= 0) {
            movementDistance -= Math.abs(enemy.getVelX());
        } else {
            if (enemy.isMovingLeft()) {
                enemy.setHMove(-1);
            } else {
                enemy.setHMove(1);
            }
            movementDistance = movementDistance_MAX;
        }
    }

    public void setDistance(int movementDistance_MAX, boolean center) {
        this.movementDistance_MAX = movementDistance_MAX;
        movementDistance = movementDistance_MAX;

        if (center) {
            movementDistance /= 2;
        }
    }
}
