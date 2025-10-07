package com.euhedral.Game.Entities.Projectile;

import com.euhedral.Engine.*;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.EntityID;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.Timer;

import java.awt.*;

public class Laser extends MobileEntity {

    private Enemy parent;
    private Timer lifetime;
    private double damage;

    boolean finished;

    int width_MIN;
    int width_MAX;

    int updateCount_Width = 0;

    public Laser(Enemy parent, int lifetime) {
        super(-100, -100, EntityID.Bullet);
        this.parent = parent;
        width_MIN = 4;
        width_MAX = 16;
        width = width_MIN;
        height = Engine.HEIGHT * 2;
        this.lifetime = new Timer(lifetime);
        collisionBox = new CollisionBox(this, 1);
        damage = 1;
//        debug = true;
    }

    @Override
    public void render(Graphics g) {
        if (lifetime.getTimeLeft() <= 0) {
            return;
        }

        g.setColor(Color.RED);
        g.fillRect(pos.intX(), pos.intY(), width, height);

        if (debug) {
            g.setColor(Color.GREEN);
            collisionBox.render(g);
        }
    }

    @Override
    public void update() {
        updateCount_Width++;

        setPos(parent.getTurretX() - width / 2, parent.getTurretY());
        collisionBox.setBounds(pos.x + 1, pos.y + 1, width - 2*1,  height - 2*1 );

        lifetime.update();

        if (lifetime.getTimeLeft() <= 0) {
            finished = true;
            SoundHandler.stop(SoundHandler.LASER);
            width = width_MIN;
            updateCount_Width = 0;
        } else {
            if (updateCount_Width % 5 == 0) {
                width = Math.min(width_MAX, width + 1);

            }
        }
    }

    public void start() {
        finished = false;
        lifetime.start();
        SoundHandler.loopSound(SoundHandler.LASER);
    }

    @Override
    public boolean checkCollision(Entity other) {
        if (lifetime.getTimeLeft() <= 0) {
            return false;
        }
        return super.checkCollision(other);
    }

    public double getDamage() {
        if (lifetime.getTimeLeft() <= 0 && lifetime.getTimeLeft() < lifetime.getStartTime() - 5)
            return 0;
        return damage * width / width_MAX;
    }

    public void stop() {
        lifetime.stop();
        SoundHandler.stop(SoundHandler.LASER);
    }

    public void resetTime(int laserTime) {
        lifetime.resetTime(laserTime);
    }

    public boolean isFinished() {
        return finished;
    }

    public int getTimeLeft() {
        return lifetime.getTimeLeft();
    }
}
