package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.game.Entities.*;
import com.euhedral.game.Entities.Enemy.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

// todo: Remove Contact ID
// Manages all entities in game
public class EntityHandler {

    private static int levelHeight;

//    private Pool entities;

    // Player
    private Player player;// = new Player(0, 0, 0);
    public static double playerX, playerY;

    // Enemy Types
    public static final int TYPE_BASIC1 = 0;
    public static final int TYPE_BASIC2 = TYPE_BASIC1 + 1;
    public static final int TYPE_HEAVY = TYPE_BASIC2 + 1;
    public static final int TYPE_BASIC3 = TYPE_HEAVY + 1;
    public static final int TYPE_DRONE1 = TYPE_BASIC3 + 1;
    public static final int TYPE_STATIC1 = TYPE_DRONE1 + 1;
    public static final int TYPE_SIDE1 = TYPE_STATIC1 + 1;
    public static final int TYPE_FAST = TYPE_SIDE1 + 1;
    public static final int TYPE_DRONE2 = TYPE_FAST + 1;
    public static final int TYPE_SIDE2 = TYPE_DRONE2 + 1;
    public static final int TYPE_STATIC2 = TYPE_SIDE2 + 1;
    public static final int TYPE_DRONE3 = TYPE_STATIC2 + 1;
    public static final int TYPE_STATIC3 = TYPE_DRONE3 + 1;

    public static final int enemyTypes = TYPE_STATIC3 + 1;

    // Entity Lists
    private Enemy enemy;
    private Flag flag; // todo: Remove
    private static EnemyPool enemies;
    private BulletPool bullets;
    private PickupPool pickups;
//    int pickupValue = 0;

    // Last Destroyed Enemy
    public static double lastDestroyedX = -1; // < 0 means nothing was destroyed
    public static double lastDestroyedY = -1;
    public static double lastDestroyedType = -1;
//    public static int spawnProbablity = -1;

    private int pickupValue;
    private EnemyBoss boss;

    Graphics2D g2d;
    float transparency;

    // Background Scrolling
    private static double scrollRate = 64d/20;
    public static final double backgroundScrollingSpeed = scrollRate/2;

    EntityHandler() {
        bullets = new BulletPool();
        pickups = new PickupPool();
        enemies = new EnemyPool(bullets);
//        this.variableHandler = variableHandler;
//        initializeAnimations();
    }

    public void initializeGraphics() {
        /*************
         * Game Code *
         *************/
//        playerSpriteSheet = new SpriteSheet("/player.png");
//        playerImage = new BufferedImage[2];
//        playerImage[0] = playerSpriteSheet.grabImage(1,1,32,32);
//        playerImage[1] = playerSpriteSheet.grabImage(2,1,32,32);
    }

    public void initializeAnimations() {
        /*************
         * Game Code *
         *************/
        TextureHandler textureHandler = GameController.getTexture();

    }

    public void update() {
        updatePlayer();
        updateBullets();

        updateEnemies();

        updatePickups();

//        updateFlag();

        checkCollisions();
    }

//    public void cleanDisabledEntities() {
//        cleanBullets();
//        cleanEnemies();
//        cleanPickups();
//    }

    public void render(Graphics g) {
//        renderShadows(g);
        renderReflections(g);
        player.render(g);
        bullets.render(g);
        pickups.render(g);
        enemies.render(g);
        //renderFlag(g);
    }

    public void spawnEntity(int x, int y, int enemyType, int distance, int direction) {
        //todo: Check by enemyType

        if (enemies.getPoolSize(enemyType) > 0) {
            enemies.spawnFromPool(x, y, enemyType, direction, distance);
        }
        else {
            spawnNew(x, y, enemyType, direction, distance);
        }
        enemies.printPool("Enemy");
    }

    private void spawnNew(int x, int y, int enemyType, int direction, int distance) {
        enemy = null;

        if (enemyType == TYPE_BASIC1) {
            enemy = new EnemyBasic1(x, y, levelHeight);
        } else if (enemyType == TYPE_HEAVY) {
            enemy = new EnemyHeavy(x, y, levelHeight);
        } else if (enemyType == TYPE_BASIC2) {
            enemy = new EnemyBasic2(x, y, levelHeight);
            enemy.setHMove(direction);
//            enemy.setMovementDistance(distance);
        } else if (enemyType == TYPE_BASIC3) {
            enemy = new EnemyBasic3(x, y, levelHeight);
            enemy.setHMove(direction);
        } else if (enemyType == TYPE_FAST) {
            enemy = new EnemyFast(x, y, levelHeight);
            enemy.setHMove(direction);
        } else if (enemyType == TYPE_DRONE1) {
            enemy = new EnemyDrone(x, y, levelHeight);
        } else if (enemyType == TYPE_SIDE1) {
            enemy = new EnemySide1(x, y, levelHeight);
        } else if (enemyType == TYPE_STATIC1) {
            enemy = new EnemyStatic1(x, y, levelHeight);
        } else if (enemyType == TYPE_DRONE2) {
            enemy = new EnemyDrone2(x, y, levelHeight);
        } else if (enemyType == TYPE_SIDE2) {
            enemy = new EnemySide2(x, y, levelHeight);
        } else if (enemyType == TYPE_STATIC2) {
            enemy = new EnemyStatic2(x, y, levelHeight);
        } else if (enemyType == TYPE_DRONE3) {
            enemy = new EnemyDrone3(x, y, levelHeight);
        } else if (enemyType == TYPE_STATIC3) {
            enemy = new EnemyStatic3(x, y, levelHeight);
        }

        if (enemy != null) {
            enemies.add(enemy);
        }
//        enemies.increaseActive(enemyType);
//        else if (id == EntityID.EnemySnake) {
//            Enemy enemy = new EnemySnake(x, y, ContactID.Air, color, levelHeight);
//            enemies.add(enemy);
//        } else if (id == EntityID.EnemyFast) {
//            Enemy enemy = new EnemyFast(x, y, ContactID.Air, color, levelHeight);
//            enemies.add(enemy);
//        }

        // Ground Enemies

//        else if (id == EntityID.EnemyGround) {
//            spawnEnemy(x, y, TYPE_BASIC, ContactID.Ground, color);
//        }
    }

    // todo: Causes issue with vertical movement
    private void spawnNew(int x, int y, int enemyType) {
        Enemy enemy;
        if (enemyType == TYPE_BASIC1) {
            enemy = new EnemyBasic1(x, y, levelHeight);
            enemies.add(enemy);
//                System.out.println("Pool: " + poolEnemy + " | Enemies: " + enemies.size());
        } else if (enemyType == TYPE_HEAVY) {
            enemy = new EnemyHeavy(x, y, levelHeight);
            enemies.add(enemy);
        } else {
            return;
        }
        enemies.add(enemy);
    }

    /********************
     * Player Functions *
     ********************/

    // Helper function exists because GameController needs to call on this during tutorial
    public void updatePlayer() {
        player.update();
        playerX = player.getCenterX();
        playerY = player.getCenterY();
    }

    public void renderPlayer(Graphics g) {
        player.render(g);
    }

    public void movePlayer(char c) {
        if (c == 'l')
            player.moveLeft(true);
        else if (c == 'r')
            player.moveRight(true);

        if (c == 'u')
            player.moveUp(true);
        else if (c == 'd')
            player.moveDown(true);
    }

    public void stopMovePlayer(char c) {
        if (c == 'l')
            player.moveLeft(false);
        else if (c == 'r')
            player.moveRight(false);

        if (c == 'u')
            player.moveUp(false);
        else if (c == 'd')
            player.moveDown(false);
    }

    public void giveDestination(int mx, int my) {
        player.giveDestination(mx, my);
    }

    public boolean canUpdateDestination(int mx, int my) {
        return !(player.getMx() == mx && player.getMy() == my);
    }

//    public int getPlayerPower() {
//        return player.getPower();
//    }

    public void playerCanShoot() {
        if (player != null)
            player.canShoot(true);
    }
    public void playerCannotShoot() {
        if (player != null)
            player.canShoot(false);
    }

    public void spawnPlayer(int x, int y) {
        if (player == null) {
            player = new Player(x, y, levelHeight);
        } else {
            player.setX(x);
            player.setY(y);
            player.resetMovement();
        }
//        player = new Player(x, y, levelHeight);
//        player.setGround(VariableHandler.gotGround());
//        player.setPower(VariableHandler.power.getValue());
        setCameraToPlayer();
    }

    public void spawnPlayer(int width, int height, int power, boolean ground) {
        player = new Player(width, height, levelHeight);
//        player.setGround(ground);
//        player.setPower(power);
        setCameraToPlayer();
    }

    public void checkCollisions() {
        playerVsPickupCollision();
        playerHostileCollision();
        enemyVsPlayerBulletCollision();
    }

//    prgaivate Bullet checkPlayerCollision(Enemy enemy) {
//        return player.checkCollision(enemy);
//    }

    // Temp Functions
    public Rectangle2D getPlayerBounds() {
        return player.getBounds();
    }

    public double getPlayerY() {
        return player.getY();
    }

    public void resetPlayer() {
        if (player != null) {
            player.clear();
        }
    }

    public void damagePlayer(int num){
        if (GameController.godMode) {

        } else
            player.damage(num);
    }

    /********************
     * Bullet Functions *
     ********************/

    private void updateBullets() {
        bullets.update();
        bullets.disableIfOutsideBounds(levelHeight);
        checkDeathAnimationEnd();

        if (boss != null) {
//            addToBullets(boss);
//            LinkedList<Bullet> bossBullets = boss.getBullets();
//            this.bullets.addAll(bossBullets);
//            boss.clearBullets();
        }

//        Utility.log("Bullet pool size" + bullets.getPoolSize());
    }

    private void clearBullets() {
        bullets.clear();
    }

    /********************
     * Pickup Functions *
     ********************/

    // Spawns Pickup with default health value = 30
    public void spawnPickup(int x, int y, EntityID id) {
        switch (id) {
            case PickupHealth:
                pickupValue = 30;
                break;
            case PickupShield:
                pickupValue = 100;
                break;
            case PickupPower:
                pickupValue = 1;
                break;
        }
        spawnPickup(x, y, id, pickupValue);
    }

    private void spawnPickup(double x, double y, EntityID id, int value) {
        if (pickups.getPoolSize() > 0) {
            pickups.spawnFromPool(x, y, id, value);
//            spawnPickupsFromPool(x, y, id);
        }
        else
            pickups.add(new Pickup(x, y, id, value));
//        System.out.println("Pickup spawned");
    }

    public void clearPickups() {
        pickups.clear();
    }

    /******************
     * Flag Functions *
     ******************/

    public void updateFlag() {
        if (flag != null)
            flag.update();
    }

    public void renderFlag(Graphics g) {
        flag.render(g);
    }

//    public void spawnFlag() {
//        flag = new Flag(Engine.WIDTH / 2, -Engine.HEIGHT / 2, ContactID.Air);
//    }

    public void respawnFlag() {
        flag.reset();
    }

    public int getFlagY() {
        if (flag == null)
            return levelHeight;
        return flag.getY();
    }

    /******************
     * Shadow Functions *
     ******************/

    private void renderShadows(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        player.renderShadow(g2d);
        renderEnemyShadows(g2d);
    }

    private void renderEnemyShadows(Graphics2D g2d) {
        LinkedList<Entity> list = enemies.getEntities();
        for (Entity entity: list) {
            Enemy enemy = (Enemy) entity;
            enemy.renderShadow(g2d);
        }
    }

    /******************
     * Reflection Functions *
     ******************/

    private void renderReflections(Graphics g) {
        g2d = (Graphics2D) g;
        transparency = 0.2f;
        renderBulletReflections(g2d, transparency);
        player.renderReflection(g2d, transparency);
        renderEnemyReflections(g2d, transparency);
    }

    private void renderEnemyReflections(Graphics2D g2d, float transparency) {
        enemies.renderReflections(g2d, transparency);
//        LinkedList<Entity> list = enemies.getEntities();
//        for (Entity entity: list) {
//            Enemy enemy = (Enemy) entity;
//            // todo: fix bad code
//            if (enemy.getEnemyType() == TYPE_DRONE3) {
//                transparency = 0.1f;
//            }
//            enemy.renderReflection(g2d, transparency);
//        }
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        bullets.renderReflections(g2d, transparency);

//        LinkedList<Entity> list = bullets.getEntities();
//        for (Entity entity : list) {
//            Bullet bullet = (Bullet) entity;
//            bullet.renderReflection(g2d, transparency);
//        }
    }

    /*******************
     * Enemy Functions *
     *******************/

//    public void spawnEnemy(int x, int y, int enemyType, ContactID contactId, Color color) {
//            addEnemy(x, y, enemyType, contactId, color);
//    }

    public void addEnemy(Enemy enemy) {
        enemy.setLevelHeight(levelHeight);
        enemies.add(enemy);
    }

//    private void addEnemy(int x, int y, int enemyType, ContactID cID) {
////        Enemy enemy = new Enemy(x, y, eID, cID);
////        enemies.add(enemy);
//    }

//    private void addEnemy(int x, int y, int enemyType, ContactID cID, Color color) {
//        Enemy enemy = new Enemy(x, y, cID, color, levelHeight);
//        addEnemy(enemy);
//    }

    public void updateEnemies() {
        enemies.disableIfOutsideBounds(levelHeight); // todo: Move within update loop
        enemies.update();
    }

    public void updatePickups() {
        if (lastDestroyedX > -1) {
            // todo: determine Pickup Value and Type based on Enemy Type
            if (lastDestroyedType > TYPE_BASIC2) {
                EntityID pickupID = null;
                int pickupValue = 0;
                if (lastDestroyedType == TYPE_DRONE1 || lastDestroyedType == TYPE_DRONE2) {
                    // todo: Spawn Homing Bullets
                } else if (lastDestroyedType == TYPE_DRONE3) {
                    // todo: Spawn Bomb Pickup
                } else if (lastDestroyedType == TYPE_FAST) {
                    // todo: Spawn Speed Pickup
                } else if (lastDestroyedType == TYPE_STATIC1) {
                    pickupID = EntityID.PickupPower;
                    pickupValue = 1;
                } else if (lastDestroyedType == TYPE_STATIC2 || lastDestroyedType == TYPE_STATIC3) {
                    pickupID = EntityID.PickupShield;
                    pickupValue = 10;
                } else {
                    pickupID = EntityID.PickupHealth;
                    pickupValue = 5;
                }
                if (pickupID != null) {
                    spawnPickup(lastDestroyedX, lastDestroyedY, pickupID, pickupValue);
                }
            }
            lastDestroyedX = -1;
        }
        pickups.update();
    }

    public void clearEnemies() {
        enemies.clear();
        clearBullets();
    }

    private void disable(Entity entity) {
        entity.disable();
    }

//    private void destroy(Enemy enemy) {
//        enemy.destroy();
//        if (!GameController.godMode)
//            VariableHandler.increaseScore(enemy.getScore());
//        int health = 5;
//        spawnPickup((int) enemy.getX(), (int) enemy.getY(), EntityID.PickupHealth, health);
//    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        SoundHandler.playSound(SoundHandler.IMPACT);
        bullet.destroy(entity);
    }

    /*
    * Boss Functions
    * */

//    private void destroyBoss() {
//        boss.setAlive(false);
//        destroy(boss);
//        VariableHandler.increaseScore(VariableHandler.getBossScore());
//    }

    public void spawnBoss(int level, int x, int y) {
        if (level == 2) {
            boss = new EnemyBoss1(x, y, levelHeight);
        } else if (level == 3) {
            boss = new EnemyBoss2(x, y, player, levelHeight);
        } else if (level == 4) {
//        boss = new EnemyBoss3(x, y);
        }

        if (boss != null) {
            VariableHandler.setBossLives(true);
//            bossLives = true;
            enemies.add(boss);
            VariableHandler.setHealthBossDef(boss.getHealth());
//            healthBossDef = boss.getHealth();
            VariableHandler.setHealthBoss(VariableHandler.getHealthBossDef());
//            healthBoss = healthBossDef;
        }
        this.boss = boss;
    }

    public void checkBoss() {
        if (boss != null) {
            if (VariableHandler.isBossLives() != boss.isAlive()) {
                VariableHandler.setBossLives(boss.isAlive());
            }
        }
    }

    public void renderBossHealth(Graphics g) {
        if (boss != null) {
            if (boss.isInscreenY() && boss.isAlive())
                VariableHandler.drawBossHealth(g);
        }
    }

    /***********************
     * Collision Functions *
     ***********************/

    private void playerHostileCollision() {
        playerVsEnemyCollision();
        playerVsEnemyBulletCollision();
    }

    private void playerVsEnemyBulletCollision() {
        bullets.checkCollision(player);

//        for (Entity entity: bullets.getEntities()) {
//            Bullet bullet = (Bullet) entity;
//
//            if (bullet.isActive() && player.checkCollision(bullet.getBounds())) {
////                bullets.increase(bullet);
//                destroy(bullet, player);
//                damagePlayer(10);
//            }
//        }
    }

    private void playerVsEnemyCollision() {
        enemies.checkCollisions(player);

//        for (Entity entity : enemies.getEntities()) {
//            Enemy enemy = (Enemy) entity;
////            boolean enemyInAir = enemy.getContactId() == ContactID.Air;
//            if (enemy.isActive())
//                if (enemy.isInscreenY() && enemy.isActive()) {
//                    boolean collision = player.checkCollision(enemy);
////                    boolean collision2 = player.checkCollision(enemy.getBoundsVertical());
//                    if (collision) {
//                        int damage = enemy.getDamage();
//                        damagePlayer(damage);
//                        destroy(enemy);
//                    }
//                }
////            else if (enemy.getContactId() == ContactID.Boss) {
////                damagePlayer(10);
////            }
//        }
    }

    private void enemyVsPlayerBulletCollision() {
        enemies.checkCollisionBullet(player);

//        for (Entity entity : enemies.getEntities()) {
//            Enemy enemy = (Enemy) entity;
//            if (enemy.isInscreenY() && enemy.isActive()) {
//                Bullet bullet = player.checkCollisionBullet(enemy);
//                if (bullet != null) {
//                    destroy(bullet, enemy);
//                    boolean isBoss = false;
//                    if (isBoss) {
//                        boss.damage();
//                        VariableHandler.setHealthBoss(boss.getHealth());
//                        if (boss.getHealth() <= 0) {
//                            destroyBoss();
//                        }
//                    } else {
//                        enemy.damage();
//                        if (enemy.getHealth() <= 0) {
//                            destroy(enemy);
//                        }
//                    }
////                    player.increaseBullets();
//                }
//            }
//        }
    }

    public void playerVsPickupCollision() {
        pickups.checkCollisions(player);
    }

    // Creates an instance of the player and sets the camera to follow it
    public void setCameraToPlayer() {
        int offsetVertical = Engine.HEIGHT - Utility.intAtWidth640(32)*3;

        // sets the camera's width to center the player horizontally, essentially to 0, and
        // adjust the height so that player is at the bottom of the screen
        GameController.camera = new Camera(0,(int)getPlayerY() - offsetVertical);
        GameController.camera.setMarker(((int)getPlayerY()));
    }

    /*******************************
     * Entity Management Functions *)
     ****************-**************/

    public void setLevelHeight(int levelHeight) {
        this.levelHeight = levelHeight;
    }

//    private void checkDeathAnimationEnd(Enemy enemy) {
//        if (enemy.isExploding()) {
//            if (enemy.checkDeathAnimationEnd()) {
//                int enemyType = enemy.getEnemyType();
//                enemies.increase(enemy, enemyType);
//            }
//        }
//    }

    private void checkDeathAnimationEnd() {
        bullets.checkDeathAnimationEnd();

//        for (Entity entity: bullets.getEntities()) {
//
//            Bullet bullet = (Bullet) entity;
//
//            if (bullet.isImpacting()) {
//                if (bullet.checkDeathAnimationEnd()) {
//                    bullets.increase(bullet);
//                }
//            }
//        }

    }

    public static int getActiveEnemies(int enemyType) {
        return enemies.getActive(enemyType);
    }

//    public static ArrayList<Integer> getExclusionZones() {
//        return enemies.getExclusionZones();
//    }

    public static boolean exclusionZonesContains(int x) {
        return enemies.exclusionZonesContains(x);
    }

    public static int getLevelHeight() {
        return levelHeight;
    }

}
