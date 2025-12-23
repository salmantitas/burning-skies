package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.GameController;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.TextureHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyHeavy2 extends EnemyHeavy1 {

    // Shoot State
    protected int MISSILE = 1;

    public EnemyHeavy2(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        turrets[0].setAngle(110);
        turrets[1].setAngle(70);
        setImage(textureHandler.enemyHeavy[1]);
        color = Color.RED;
    }

    @Override
    public void update() {
        super.update();
        if (inscreenY && isActive()) {
            if (playerInRange)
                shootState = MISSILE;
            else
                shootState = BULLET;
        }
    }

        @Override
    protected void resetShootTimer() {
        if (shootState == BULLET)
            shootTimer = (int) (shootTimerDefault / Difficulty.getEnemyFireRateMult());
        else
            shootTimer = (int) ((shootTimerDefault + 30) / Difficulty.getEnemyFireRateMult());
    }

    @Override
    protected void spawnProjectiles() {
        for (int i = 0; i < 2; i++) {
            turret = turrets[i];
            if (shootState == BULLET)
                spawnBullet();
            else {
                spawnMissiles();
            }
        }
    }

    private void spawnMissiles() {
        int projectileWidth = 8/2;
        double x = turret.getX() - projectileWidth;
        double y = turret.getY();

        projectiles.missiles.spawn(x, y, 90);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_HEAVY2;
    }
}
