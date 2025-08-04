package com.euhedral.game;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Pool;
import com.euhedral.game.Entities.Bullet;
import com.euhedral.game.Entities.BulletEnemy;
import com.euhedral.game.Entities.Enemy.Enemy;
import com.euhedral.game.Entities.Player;

import java.awt.*;
import java.util.ArrayList;

public class EnemyPool extends Pool {
    private int[] active;
    private int[] reusable;
    private ArrayList<Integer> exclusionZones;
    int tempNum;
    int entityX;
    Entity entity;
    Enemy enemy;
    int enemyType, enemyType2;
    boolean done;

    private BulletPool bullets;

    public EnemyPool(BulletPool bullets) {
        super();
        int enemyTypes = EntityHandler.enemyTypes;
        this.bullets = bullets;
        reusable = new int[enemyTypes];
        active = new int[enemyTypes];
        exclusionZones = new ArrayList<>(enemyTypes);

        for (int i = 0; i < enemyTypes; i++) {
            reusable[i] = 0;
            active[i] = 0;
            exclusionZones.add(-100);
        }

//        for (int i = 0; i < enemyTypes; i++) {
//        }
//
//        for (int i = 0; i < enemyTypes; i++) {
//        }
    }

    @Override
    public void update() {
//        enemies.disableIfOutsideBounds(levelHeight);
//        LinkedList<Entity> enemies = this.enemies.getEntities();
        for (Entity entity : entities) {
            enemy = (Enemy) entity;
//            if(true) { // todo: Why tho?
                enemy.update();
                checkDeathAnimationEnd(enemy);
                while (enemy.hasShot()) {
                    spawnEnemyBullet(enemy);
                    enemy.decrementShot();
                }

//            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

//        // render exclusion zone
//        g.setColor(Color.RED);
//        for (int i = 0; i < 10; i++) {
//            int x = exclusionZones.get(i);
//            if (x != -100) {
//                g.drawRect((int) x*64, 400, 64, 64);
//            }
//        }
    }

    public int getPoolSize(int enemyType) {
        return reusable[enemyType];
    }

    public void increase(int enemyType) {
        tempNum = reusable[enemyType];
        tempNum++;
        reusable[enemyType] = tempNum;

        decreaseActive(enemyType);
    }

    public void increase(Entity entity, int enemyType) {
        entity.disable();

        entityX = (int) entity.getX()/64;

        if (enemyType == EntityHandler.TYPE_STATIC1 || enemyType == EntityHandler.TYPE_SCATTER1) {
            // find spot in exclusion zone
            for (int i = 0; i < 10; i++) {
                if (exclusionZones.get(i) == entityX) {
                    exclusionZones.set(i, -100);
                }
            }
        }

        increase(enemyType);
    }

    public void decrease(int enemyType) {
        tempNum = reusable[enemyType];
        tempNum--;
        reusable[enemyType] = tempNum;

        increaseActive(enemyType);
    }

    @Override
    public void add(Entity entity) {
        super.add(entity);
        increaseActive(entity);
    }

    @Override
    public void clear() {
//        int len = reusable.length;
        for (int i = 0; i < reusable.length; i ++) {
            reusable[i] = 0;
            active[i] = 0;
            exclusionZones.set(i, -100);
        }

        super.clear();
    }

    public Entity findInList(int enemyType) throws NullPointerException {
        for (Entity e: entities) {
            enemy = (Enemy) e;
            enemyType2 = enemy.getEnemyType();
            if (enemyType == enemyType2) {
                if (e.isInactive())
                    return e;
            }
        }
        return null;
    }

    public void spawnFromPool(int x, int y, int enemyType, int direction, int distance) {
//        Enemy entity = (Enemy) findInList(enemyType);

        entity = findInList(enemyType);
        entity.resurrect(x, y);
        enemy = (Enemy) entity;
        enemy.setHMove(direction);
//        enemy.setMovementDistance(distance);
        decrease(enemy.getEnemyType());

//        int tempNum = active[enemyType];

        addExclusionZone(entity, enemyType);
    }

    public void destroyIfWithinRadius(double x, double y, int radius) {
        for (int i = 0; i < entities.size(); i ++) {
            entity = entities.get(i);
            if (entity.isActive())
                destroyIfWithinRadiusHelper(x, y, radius);
        }
    }

    private void destroyIfWithinRadiusHelper(double x, double y, int radius) {
        enemy = (Enemy) entity;
        if (enemy.inRadius(x,y,radius)) {
            if (enemy.isInscreenY()) {
                destroy(enemy);
            }
        }
    }

    @Override
    public void disableIfOutsideBounds(Entity entity, int levelHeight) {
//        int offset = 200;

        enemy = (Enemy) entity;
        enemyType = enemy.getEnemyType();

        if (enemy.canDisable()) {
            enemy.disable();
            increase(enemyType);
        }
    }

//    @Override
//    public void disableIfOutsideBounds(Entity entity, int levelHeight) {
//
//        if (entity.canDisable()) {
//            entity.disable();
//            reusable++;
//        }
//    }

    public int getActive(int enemyType) {
        return active[enemyType];
    }

    public void increaseActive(int enemyType) {
        tempNum = active[enemyType];
        tempNum++;
        active[enemyType] = tempNum;
    }

    public void increaseActive(Entity entity) {
        enemyType = ((Enemy) entity).getEnemyType();
        tempNum = active[enemyType];

        addExclusionZone(entity, enemyType);

        tempNum++;
        active[enemyType] = tempNum;
    }

    public void decreaseActive(int enemyType) {
        tempNum = active[enemyType];
        tempNum--;
        active[enemyType] = tempNum;
    }

    private void addExclusionZone(Entity entity, int enemyType) {
        if (enemyType == EntityHandler.TYPE_STATIC1 || enemyType == EntityHandler.TYPE_SCATTER1) {
            done = false;
            for (int i = 0; i < 10; i++) {
                if (done) { }
                else {
                    if (exclusionZones.get(i) == -100) {
                        exclusionZones.set(i, ((int) entity.getX()/64));
                        done = true;
                    }
                }
            }

//            for (int i = 0; i < 10; i++) {
//                Utility.log("Ex " + i + ": " + exclusionZones.get(i));
//            }
        }
    }

//    public ArrayList<Integer> getExclusionZones() {
//        return exclusionZones;
//    }

    public boolean exclusionZonesContains(int x) {
        return exclusionZones.contains(x);
    }

    public void renderReflections(Graphics2D g2d, float transparency) {
        for (Entity entity: entities) {
            enemy = (Enemy) entity;
            if (enemy.getEnemyType() == EntityHandler.TYPE_DRONE3) {
                transparency = 0.1f;
            }
            enemy.renderReflection(g2d, transparency);
        }
    }

    private void checkDeathAnimationEnd(Enemy enemy) {
        if (enemy.isExploding()) {
            if (enemy.checkDeathAnimationEnd()) {
                enemyType = enemy.getEnemyType();
                increase(enemy, enemyType);
            }
        }
    }

    private void spawnEnemyBullet(Enemy enemy) {
        int x = enemy.getTurretX();
        double dir = enemy.getBulletAngle();
        double y = enemy.getTurretY();
        double bulletVelocity = enemy.getBulletVelocity();
        boolean tracking = false;
        if (enemy.getEnemyType() == EntityHandler.TYPE_STATIC1 || enemy.getEnemyType() == EntityHandler.TYPE_SCATTER1 ||
                enemy.getEnemyType() == EntityHandler.TYPE_SIDE3 || enemy.getEnemyType() == EntityHandler.TYPE_DRONE2) {
            tracking = true;
        }

        if (bullets.getPoolSize() > 0) {
            bullets.spawnFromPool(x, (int) y, dir, bulletVelocity, tracking);
        }
        else {
            bullets.add(new BulletEnemy(x, (int) y, dir, bulletVelocity, tracking));
        }
//        bullets.printPool("Enemy Bullet");
    }

    public void checkCollisions(Player player) {
        for (Entity entity : entities) {
            enemy = (Enemy) entity;
            if (enemy.isActive())
                if (enemy.isInscreenY() && enemy.isActive()) {
                    boolean collision = player.checkCollision(enemy);
//                    boolean collision2 = player.checkCollision(enemy.getBoundsVertical());
                    if (collision) {
                        int damage = enemy.getDamage();
                        if (GameController.godMode) {

                        } else
                            player.damage(10);
                        destroy(enemy);
                    }
                }
//            else if (enemy.getContactId() == ContactID.Boss) {
//                damagePlayer(10);
//            }
        }
    }

    public void checkCollisionBullet(Player player) {
        for (Entity entity : entities) {
            enemy = (Enemy) entity;
            if (enemy.isInscreenY() && enemy.isActive()) {
                Bullet bullet = player.checkCollisionBullet(enemy);
                if (bullet != null) {
                    destroy(bullet, enemy);
                    boolean isBoss = false;
                    if (isBoss) {
//                        boss.damage();
//                        VariableHandler.setHealthBoss(boss.getHealth());
//                        if (boss.getHealth() <= 0) {
//                            destroyBoss();
//                        }
                    } else {
                        enemy.damage();
                        if (enemy.getHealth() <= 0) {
                            destroy(enemy);
                        }
                    }
//                    player.increaseBullets();
                }
            }
        }
    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        SoundHandler.playSound(SoundHandler.IMPACT);
        bullet.destroy(entity);
    }

    private void destroy(Enemy enemy) {
        enemy.destroy();
        if (!GameController.godMode)
            VariableHandler.increaseScore(enemy.getScore());
        EntityHandler.lastDestroyedType = enemy.getEnemyType();
        EntityHandler.lastDestroyedX = enemy.getX();
        EntityHandler.lastDestroyedY = enemy.getY();
//        EntityHandler.spawnProbablity = Utility.randomRange(0, EntityHandler.enemyTypes);
//        spawnPickup((int) enemy.getX(), (int) enemy.getY(), EntityID.PickupHealth, 5);
    }
}
