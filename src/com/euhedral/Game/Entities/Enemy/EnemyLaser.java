package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Entity;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Projectile.Laser;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyLaser extends Enemy {

    double deceleration;

    int bulletAngleMIN = 60;

    int destinationX;
    int offsetLeft = 0, offsetRight = 0;

    boolean playerLeft, playerRight, playerInRange;

    private Laser laser;
    public boolean laserCollision;
    int bulletWidth = 16 / 2;

    public EnemyLaser(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        bulletAngle = 90;
        shootTimerDefault = 200;
        score = 150;

        velX_MIN = 1;
        deceleration = 0.015;
        bulletVelocity = 75;

        attackEffect = true;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[7]);

        velX = 0;
        velY_MIN = 1.7f;

        health_MAX = 4;
        commonInit();
        damage = 30;

        laser = new Laser(this, 30);
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {
            updateDestination();
            laser.update();

            playerRight = destinationX - offsetRight > pos.x + width/2 + velX_MIN;
            playerLeft = destinationX - offsetLeft <= pos.x - velX_MIN ;
        }
    }


    @Override
    protected void moveInScreen() {
        velY = Math.max(0, velY - deceleration);
        if (playerRight) {
            velX = velX_MIN;
        } else if (playerLeft) {
            velX = -velX_MIN;
        } else {
            velX = 0;
        }
        super.moveInScreen();
    }

    @Override
    protected void shoot() {
        resetShootTimer();
        laser.start();
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
        bulletAngle = bulletAngleMIN;
        resetShootTimer();
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerPositon.intX();
    }

    @Override
    public boolean checkCollision(Entity other) {
        collision = super.checkCollision(other);
        laserCollision = laser.checkCollision(other);
        return laserCollision || collision;
    }

    @Override
    protected void renderAttackPath(Graphics g) {
        g.setColor(Color.RED);

        if (isActive()) {
            laser.render(g);

            if (shootTimer <= 50) {
                g2d = (Graphics2D) g;
                g2d.setComposite(Utility.makeTransparent(0.5f));

                g2d.setStroke(new BasicStroke((Math.max(shootTimer / 5, 2))));
                int drawX = getTurretX() + 16 / 2;
                g.drawLine(drawX, getTurretY(), drawX, levelHeight * 2);

                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    @Override
    public int getTurretX() {
        return pos.intX() + width / 2 - bulletWidth;
    }

    @Override
    public double getBulletAngle() {
        return 90;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_LASER;
    }

    @Override
    public int getTurretY() {
        return (int) pos.y + height - 2;
    }

    @Override
    public double getDamage() {
        if (laserCollision)
            return laser.getDamage();
        else if (collision) {
            return super.getDamage();
        }
        return 0;
    }

    @Override
    public void destroy() {
        super.destroy();
        laser.stop();
    }
}
