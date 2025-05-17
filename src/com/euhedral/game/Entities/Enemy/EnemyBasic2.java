package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyBasic2 extends Enemy{

    public EnemyBasic2(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[1]);
//        attackEffect = true;

        // todo: These happen last
        shootTimerDefault = 50; // too low: 10, too high: 100
        shootTimer = shootTimerDefault;
        score = 75;
    }

    public EnemyBasic2(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

    @Override
    public void initialize() {
        super.initialize();

        healthMAX = 4;
        commonInit();
        minVelX = 1.75f;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreen) {
            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
                velX = 0;
            }
        }
    }

    @Override
    protected void commonInit() {
        this.setHealth(healthMAX);
        velY = forwardVelocity;
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
        enemyType = EntityHandler.TYPE_BASIC2;
    }
}
