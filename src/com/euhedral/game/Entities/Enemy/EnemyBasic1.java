package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyBasic1 extends Enemy{

    public EnemyBasic1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
//        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[0]);

        shootTimerDefault = 150;
//        attackEffect = true;

        //        enemyType = EntityHandler.TYPE_BASIC;

        health_MAX = 2;
        commonInit();
        score = 20;
        spawnInterval = 2 * UPDATES_PER_SECOND;
//        velX_MIN = 1.5f;
    }

    public EnemyBasic1(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }


    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
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
        return (int) pos.x + width/2 - Utility.intAtWidth640(2);
    }

    private void renderPath(Graphics g) {
        g.setColor(Color.RED);
        int pathLength = Engine.HEIGHT;
        int originX = (int) pos.x + width/2;
        int originY = (int) pos.y + height/2;
        for (int i = 0; i < pathLength; i ++) {
            g.drawLine(originX, originY, originX + 0, originY + i);
        }
    }

    @Override
    public void setHMove(int direction) {

    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_BASIC1;
    }
}
