package com.euhedral.Game;

import com.euhedral.Engine.Entity;
import com.euhedral.Engine.MobileEntity;
import com.euhedral.Engine.Pool;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.*;
import com.euhedral.Game.Entities.Enemy.Boss.EnemyBoss;
import com.euhedral.Game.Entities.Enemy.Boss.EnemyBoss1;
import com.euhedral.Game.Entities.Enemy.Boss.EnemyBoss2;
import com.euhedral.Game.Entities.Projectile.Bullet;
import com.euhedral.Game.Entities.Projectile.BulletEnemy;
import com.euhedral.Game.Entities.Player;
import com.euhedral.Game.Entities.Projectile.BulletPlayer;

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
    Enemy enemyBoss;
    int enemyType, enemyType2;
    boolean done;

    private BulletPool bullets;

    double explodingDroneX, explodingDroneY;
    double explodingDroneRadius;
    private long spawnInterval;

    public EnemyPool(BulletPool bullets) {
        super();
        int enemyTypes = VariableHandler.enemyTypes;
        this.bullets = bullets;
        reusable = new int[enemyTypes];
        active = new int[enemyTypes];
        exclusionZones = new ArrayList<>(enemyTypes);

        for (int i = 0; i < enemyTypes; i++) {
            reusable[i] = 0;
            active[i] = 0;
            exclusionZones.add(-100);
        }

        explodingDroneX = -1;
        explodingDroneY = -1;
        explodingDroneRadius = -1;

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
                enemy.update();
                checkDeathAnimationEnd(enemy);
                while (enemy.hasShot()) {
                    spawnEnemyBullet(enemy);
                    enemy.decrementShot();
                }

//                if (enemy.getEnemyType() == EntityHandler.TYPE_DRONE3) {
//                    if (enemy.isExploding()) {
//                        explodingDroneRadius = ((EnemyDrone3) enemy).getRadius();
//                        explodingDroneX = enemy.getX();
//                        explodingDroneY = enemy.getY();
//                    }
//                }

//            }
        }

        updateBoss();
    }

    private void updateBoss() {
        if (enemyBoss != null) {

            while (enemyBoss.hasShot()) {
                spawnEnemyBullet(enemyBoss);
                enemyBoss.decrementShot();

                if (enemyBoss instanceof EnemyBoss1) {
                    if (((EnemyBoss1) enemyBoss).spawnDrone) {

                        spawnEntity(enemyBoss.getPos().intX(), enemyBoss.getPos().intY(), VariableHandler.TYPE_DRONE1, 0, 0);
                        ((EnemyBoss1) enemyBoss).spawnDrone = false;
                    }
                }

                if (enemyBoss instanceof EnemyBoss2) {
                    if (((EnemyBoss2) enemyBoss).spawnDrone) {

                        spawnEntity(enemyBoss.getPos().intX(), enemyBoss.getPos().intY(), VariableHandler.TYPE_DRONE1, 0, 0);
                        ((EnemyBoss2) enemyBoss).spawnDrone = false;
                    }
                }
            }

            enemyBoss.update();
            if (enemyBoss.isExploding()) {
                if (enemyBoss.checkDeathAnimationEnd()) {
                    enemyBoss = null;
                }
            }
        }

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        if (enemyBoss != null) {
            enemyBoss.render(g);
        }

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

        if (enemyType == VariableHandler.TYPE_STATIC1 || enemyType == VariableHandler.TYPE_SCATTER1) {
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

    public void addBoss(Enemy boss) {
        this.enemyBoss = boss;
    }

    @Override
    public void clear() {
//        int len = reusable.length;
        for (int i = 0; i < reusable.length; i ++) {
            reusable[i] += active[i];
            active[i] = 0;
            exclusionZones.set(i, -100);
        }

        super.clear();
    }

    public void spawnEntity(int x, int y, int enemyType, int distance, int direction) {
        if (getPoolSize(enemyType) > 0) {
            spawnFromPool(x, y, enemyType, direction, distance);
        } else {
            spawnNew(x, y, enemyType, direction, EntityHandler.getLevelHeight());
        }
    }

    private void spawnNew(int x, int y, int enemyType, int direction, int levelHeight) {
        enemy = null;

        if (enemyType == VariableHandler.TYPE_BASIC1) {
            enemy = new EnemyBasic1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_HEAVY) {
            enemy = new EnemyHeavy(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_BASIC2) {
            enemy = new EnemyBasic2(x, y, levelHeight);
            enemy.setHMove(direction);
        } else if (enemyType == VariableHandler.TYPE_BASIC3) {
            enemy = new EnemyBasic3(x, y, levelHeight);
            enemy.setHMove(direction);
        } else if (enemyType == VariableHandler.TYPE_FAST) {
            enemy = new EnemyFast(x, y, levelHeight);
            enemy.setHMove(direction);
        } else if (enemyType == VariableHandler.TYPE_DRONE1) {
            enemy = new EnemyDrone1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_SIDE1) {
            enemy = new EnemySide1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_STATIC1) {
            enemy = new EnemyStatic1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_DRONE2) {
            enemy = new EnemyDrone2(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_SIDE2) {
            enemy = new EnemySide2(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_SIDE3) {
            enemy = new EnemySide3(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_SCATTER1) {
            enemy = new EnemyScatter1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_MINE1) {
            enemy = new EnemyMinefield1(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_MINE2) {
            enemy = new EnemyMinefield2(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_DRONE3) {
            enemy = new EnemyDrone3(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_SCATTER2) {
            enemy = new EnemyScatter2(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_DRONE4) {
            enemy = new EnemyDrone4(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_DRONE5) {
            enemy = new EnemyDrone5(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_DRONE6) {
            enemy = new EnemyDrone6(x, y, levelHeight);
        } else if (enemyType == VariableHandler.TYPE_LASER) {
            enemy = new EnemyLaser(x, y, levelHeight);
        }

        if (enemy != null) {
            add(enemy);
            spawnInterval = enemy.getSpawnInterval();
        }

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
        spawnInterval = enemy.getSpawnInterval();
        enemy.setHMove(direction);
//        enemy.setMovementDistance(distance);
        decrease(enemy.getEnemyType());

//        int tempNum = active[enemyType];

        addExclusionZone(entity, enemyType);
    }

    public void damageIfWithinRadius(double x, double y, int radius) {
        for (int i = 0; i < entities.size(); i ++) {
            entity = entities.get(i);
            if (entity.isActive())
                damageIfWithinRadiusHelper(x, y, radius);
        }

        if (enemyBoss != null) {
            if (enemyBoss.inRadius(x,y,radius)) {
                enemyBoss.damage(2, false);
            }
        }
    }

    private void damageIfWithinRadiusHelper(double x, double y, int radius) {
        enemy = (Enemy) entity;
        if (enemy.inRadius(x,y,radius)) {
            if (enemy.isBelowDeadZoneTop()) {
                enemy.damage(10, false);
                destroy(enemy);
            }
        }
    }

    @Override
    protected void disableIfOutsideBoundsHelper(Entity entity, int levelHeight) {
        //        int offset = 200;

        enemy = (Enemy) entity;
        enemyType = enemy.getEnemyType();

        if (enemy.canDisable()) {
            enemy.disable();
            increase(enemyType);

            StatePlay.resetKillstreak();
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
        if (enemyType == VariableHandler.TYPE_STATIC1 || enemyType == VariableHandler.TYPE_SCATTER1 || enemyType == VariableHandler.TYPE_SCATTER2
        || enemyType == VariableHandler.TYPE_MINE1) {
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

    public boolean exclusionZonesContains(int x) {
        return exclusionZones.contains(x);
    }

    public void renderReflections(Graphics2D g2d, float transparency) {
        for (Entity entity: entities) {
            enemy = (Enemy) entity;
            if (enemy.getEnemyType() == VariableHandler.TYPE_DRONE3) {
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
        double x = enemy.getTurretX();
        double dir = enemy.getBulletAngle();
        double y = enemy.getTurretY();
        double bulletVelocity = enemy.getBulletVelocity();
        boolean tracking = enemy.tracking;

        if (bullets.getPoolSize() > 0) {
            bullets.spawnFromPool(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking);
        }
        else {
            bullets.add(new BulletEnemy(x, y, dir, bulletVelocity * Difficulty.getEnemyBulletSpeedMult(), tracking));
        }

//        bullets.printPool("Enemy Bullet");
    }

    public void checkCollisions(Player player) {
        for (Entity entity : entities) {
            enemy = (Enemy) entity;
            checkCollisionHelper(enemy, player);
//            else if (enemy.getContactId() == ContactID.Boss) {
//                damagePlayer(10);
//            }
        }

        if (enemyBoss != null) {
            checkCollisionHelper(enemyBoss, player);
        }
    }

    private void checkCollisionHelper(Enemy enemy, Player player) {
        if (enemy.isActive())
            if (enemy.isInscreenY() && enemy.isActive()) {
                boolean collision = enemy.checkCollision(player);
                if (collision) {
                    double damage = enemy.getDamage();
                    if (GameController.godMode) {

                    } else
                        player.damage( (damage * Difficulty.getDamageTakenMult()));
                    if (enemy.collision)
                        destroy(enemy);
                }
            }
    }

    public void checkCollisionBullet(Player player) {
        for (Entity entity : entities) {
            enemy = (Enemy) entity;
            checkCollisionBulletHelper(enemy, player);
        }

        if (enemyBoss != null) {
            checkCollisionBulletHelper(enemyBoss, player);
        }
    }

    private void checkCollisionBulletHelper(Enemy enemy, Player player) {
        if (enemy.isInscreenY() && enemy.isActive()) {
            BulletPlayer bullet = (BulletPlayer) player.checkCollisionBullet(enemy);
//            if (bullet != null && !enemy.collision) {
//                Utility.log("Bullet exists but doesn't 'coolide'");
//            }
            if (bullet != null) {

                if (enemy.collision) {
                    destroy(bullet, enemy);
                } else {
                    bullet.destroy();
                }

                boolean isBoss = false;
                if (isBoss) {
//                        boss.damage();
//                        VariableHandler.setHealthBoss(boss.getHealth());
//                        if (boss.getHealth() <= 0) {
//                            destroyBoss();
//                        }
                } else {
                    int damage = bullet.getDamage();
                    boolean isMissile = bullet.isShieldKiller();
                    if (enemy.collision)
                        enemy.damage((int) (damage * Difficulty.getDamageDealtMult()), isMissile);
                    if (enemy.getHealth() <= 0) {
                        destroy(enemy);
                    }

//                        if (explodingDroneRadius > -1)
//                            destroyIfWithinRadius(explodingDroneX, explodingDroneY, (int) explodingDroneRadius);
                }
//                    player.increaseBullets();
            }
        }
    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        SoundHandler.playSound(SoundHandler.IMPACT_ENEMY);
        bullet.destroy(entity);
    }

    private void destroy(Enemy enemy) {
        enemy.destroy();
        if (!GameController.godMode) {
            VariableHandler.increaseScore(enemy.getScore());
            StatePlay.increaseKillstreak();
        }
        EntityHandler.lastDestroyedType = enemy.getEnemyType();
        EntityHandler.lastDestroyedX = enemy.getX();
        EntityHandler.lastDestroyedY = enemy.getY();

        if (enemy instanceof EnemyBoss) {
            EntityHandler.lastDestroyedType = VariableHandler.enemyTypes;
        }
//        EntityHandler.spawnProbablity = Utility.randomRange(0, EntityHandler.enemyTypes);
//        spawnPickup((int) enemy.getX(), (int) enemy.getY(), EntityID.PickupHealth, 5);
    }

    @Override
    public void printPool(String name) {
        super.printPool(name);
        String temp = "";
        for (int i = 0; i < reusable.length; i++) {
            temp += i;
            temp += ": ";
            temp += reusable[i];
            temp += "/";
            temp += active[i] + reusable[i];
            temp += "| ";
        }

        Utility.log(temp);
    }

    public long getSpawnInterval() {
        return spawnInterval;
    }

//    @Override
//    public int getPoolSize() {
//        return reusable[enemyType];
//    }
}
