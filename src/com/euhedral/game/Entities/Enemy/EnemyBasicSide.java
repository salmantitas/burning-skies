package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyBasicSide extends Enemy{

    boolean turretLeft = true;

    public EnemyBasicSide(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[1]);
//        attackEffect = true;
    }

    public EnemyBasicSide(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

    @Override
    public void initialize() {
        super.initialize();

        bulletAngle = 0;

        shootTimerDefault = 150;
        health_MAX = 3;
        commonInit();
        score = 50;
        velX_MIN = 1.75f;
    }

//    @Override
//    protected void shoot() {
//        super.shoot();
//        shot += 2;
//    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 2;
    }

//    @Override
//    public void update() {
//        super.update();
//        if (state == STATE_ACTIVE && inscreen) {
//            if (movementDistance >= 0) {
//                movementDistance -= Math.abs(velX);
//            } else {
//                velX = 0;
//            }
//        }
//    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
    }

    @Override
    public double getBulletAngle() {
        return turretLeft ? bulletAngle : bulletAngle + 180;
    }

    @Override
    public int getTurretX() {
        if (turretLeft) {
            turretLeft = !turretLeft;
            return (int) x + width / 3 - Utility.intAtWidth640(2);
        }
        else {
            turretLeft = !turretLeft;
            return (int) x + 2 * width / 3 - Utility.intAtWidth640(2);
        }
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
        enemyType = EntityHandler.TYPE_BASIC1;
    }
}
