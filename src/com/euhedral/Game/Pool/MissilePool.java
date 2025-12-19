package com.euhedral.Game.Pool;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Pool;
import com.euhedral.Engine.Position;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.Entities.Player;
import com.euhedral.Game.Entities.Projectile.*;
import com.euhedral.Game.GameController;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.util.LinkedList;

public class MissilePool extends BulletPool {
    Entity entity;
    Bullet collidingBullet;
    BulletEnemy bulletEnemy;
    MissileEnemy missile;
    Bullet bullet;

    public MissilePool() {
        super();
    }

    public void spawn(double x, double y, double angle, double destinationX, double destinationY) {
        if (getPoolSize() > 0) {
            spawnFromPool(x, y, angle, destinationX, destinationY);
        } else
            add(new MissileEnemy(x, y, angle, destinationX, destinationY));
    }

    public void spawn(double x, double y, double angle) {
        spawn(x, y, angle, x,y);
    }

    public void spawnFromPool(double x, double y, double angle, double destinationX, double destinationY) {
        entity = findInList();
        entity.resurrect(x, y);
        ((MissileEnemy) entity).setAngle(angle);
        ((MissileEnemy) entity).setDestination(destinationX, destinationY);
        decrease();
    }

}
