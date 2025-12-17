package com.euhedral.Game.Pool;

import com.euhedral.Engine.Position;
import com.euhedral.Game.Entities.Player;

public class ProjectilePool {
    public BulletPool bullets;
    public MissilePool missiles;

    public ProjectilePool(BulletPool bullets, MissilePool missiles) {
        this.bullets = bullets;
        this.missiles = missiles;
    }

    public void playerVsEnemyProjectileCollision(int playerRadius, Position playerPositon, Player player) {
        if (playerRadius > -1) {
            bullets.destroyIfWithinRadius(playerPositon, playerRadius);
            missiles.destroyIfWithinRadius(playerPositon, playerRadius);
        }
        bullets.checkCollision(player);
        missiles.checkCollision(player);
    }
}
