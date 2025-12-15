package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Behavior.Tracker;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyFast extends Enemy{

    Tracker tracker;

    double velXCoefficient = 2;
    double acceleration = 0.08;

    public EnemyFast(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyFast[0]);
//        attackEffect = true;

        // todo: These happen last
        shootTimerDefault = 40; // too low: 10, too high: 100
//        shootTimer = shootTimerDefault;
        score = 100;
        tracker = new Tracker();
        determineMovement();


        health_MAX = 3;
        commonInit();
        velX_MIN = 1.75;
        velX_MAX = 4.75;
    }

    public EnemyFast(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//
////        minVelX = maxVelX;
//
//    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {
            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
                velX = 0;
            }
            // x = k*y^2

            velY += acceleration;
            velX_MIN = velXCoefficient * Math.pow(velY, 0.5);

            setHMove(hMove);
        }
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
        velX_MIN = 1.75f;

        determineMovement();
    }

    private void determineMovement() {
        tracker.updateDestination();
//        if (destinationX > x) {
//            setHMove(-1);
//        } else {
//            setHMove(1);
//        }
        movementDistance = Engine.WIDTH; // Math.abs((int) (x - destinationX));
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    @Override
    public double getTurretX() {
        super.getTurretX();
        return (int) pos.x + width/2 - Utility.intAtWidth640(2);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_FAST;
    }
}
