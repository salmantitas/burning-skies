package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Entities.Enemy.Component.Turret;
import com.euhedral.Game.Entities.Projectile.BulletEnemy;
import com.euhedral.Game.Pool.BulletPool;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyBasic1 extends Enemy{

    private Turret turret;

    public EnemyBasic1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
        setImage(textureHandler.enemy[0]);

//        attackEffect = true;

        health_MAX = 2;

        turret = new Turret(turretOffsetX, turretOffsetY, this);

        commonInit();

        score = 20;
        spawnInterval = 2 * UPDATES_PER_SECOND;
    }

    @Override
    protected void shoot1() {
        updateShootTimer();
        if (shootTimer <= 0) {
            resetShootTimer();
            loadShots();
            while (hasShot()) {
                spawnProjectiles();
                decrementShot();
            }
        }
    }

    protected void loadShots() {
        bulletsPerShot++;
    }

    @Override
    protected void spawnBullet() {
        double x = turret.getX();
        double y = turret.getY();
        double angle = turret.getAngle();
        double velocity = getBulletVelocity();
        boolean tracking = this.tracking;

        createBullet(x, y, angle, velocity, tracking);

//        bullets.printPool("Enemy Bullet");
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
