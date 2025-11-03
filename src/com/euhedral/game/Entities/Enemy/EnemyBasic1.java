package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.ShieldEnemy;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyBasic1 extends Enemy{

    public EnemyBasic1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
        setImage(textureHandler.enemy[0]);

//        attackEffect = true;

        health_MAX = 2;

        commonInit();

        score = 20;
        spawnInterval = 2 * UPDATES_PER_SECOND;
    }

    public EnemyBasic1(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;

    }

//    @Override
//    public void update() {
//        super.update();
//        if (isActive()) {
//            shield.update();
//        }
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
