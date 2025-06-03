package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyFast extends Enemy{

    double acceleration = 0.08;
    double destinationX, destinationY;

    public EnemyFast(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyFast[0]);
//        attackEffect = true;

        // todo: These happen last
        shootTimerDefault = 35; // too low: 10, too high: 100
//        shootTimer = shootTimerDefault;
        score = 125;
        determineMovement();
    }

    public EnemyFast(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

    @Override
    public void initialize() {
        super.initialize();

        health_MAX = 3;
        commonInit();
        velX_MIN = 1.75;
        velX_MAX = 4.75;

//        minVelX = maxVelX;

    }

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

            double factor = 2;
            velY += acceleration;
            velX_MIN = factor * Math.pow(velY, 0.5);

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
        updateDestination();
//        if (destinationX > x) {
//            setHMove(-1);
//        } else {
//            setHMove(1);
//        }
        movementDistance = Engine.WIDTH; // Math.abs((int) (x - destinationX));
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY;
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    @Override
    public int getTurretX() {
        return (int) x + width/2 - Utility.intAtWidth640(2);
    }

    private void renderPath(Graphics g) {
        g.setColor(Color.RED);
        int pathLength = Engine.HEIGHT;
        int originX = (int) x + width/2;
        int originY = (int) y + height/2;
        for (int i = 0; i < pathLength; i ++) {
            g.drawLine(originX, originY, originX + 0, originY + i);
        }
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_BASIC3;
    }
}
