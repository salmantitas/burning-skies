package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemySide extends Enemy{
    public EnemySide(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        enemyType = EntityHandler.TYPE_SIDE;
//        velX = minVelX;
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemySide[0]);
    }

    public EnemySide(int x, int y, Color color, int levelHeight) {
        super(x, y, color, levelHeight);
//        enemyID = EnemyID.Fast;
    }

    @Override
    public void initialize() {
        super.initialize();

//        power = 2;
        minVelX = 6f;
        shootTimerDefault = 50;
//        shootTimer = shootTimerDefault;
        commonInit();
        score = 350;
    }

    @Override
    public void move() {
        if (isActive()) {
            double xMin = -width, xMax = Engine.WIDTH;

            velY = 0;
            if (x <= xMin) {
                velX = minVelX;
            } else if (x >= xMax) {
                velX = -minVelX;
            }
        } else if (isExploding()) {
            velY = explodingVelocity;
        }
        if (!inscreen)
            Utility.log("min: " + minVelX + "| vel: " + velX);
        moveInScreen();
    }

    @Override
    protected void shoot() {
        super.shoot();
        shot += 1;
//        fastShoot();
    }

    @Override
    public void update() {
        super.update();
        setImage();
    }

    private void setImage() {
        if (velX > 0) {
            setImage(textureHandler.enemySide[1]);
        } else if (velX < 0) {
            setImage(textureHandler.enemySide[0]);
        }
    }

    @Override
    protected void commonInit() {
        setHealth(3);
        velX = minVelX;
        velY = 0;
    }

    @Override
    public int getTurretY() {
        return (int) y + Utility.intAtWidth640(3);
    }
}
