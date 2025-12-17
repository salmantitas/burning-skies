package com.euhedral.Game;

import com.euhedral.Engine.*;
import com.euhedral.Game.Entities.*;
import com.euhedral.Game.Entities.Enemy.*;
import com.euhedral.Game.Entities.Enemy.Boss.*;
import com.euhedral.Game.Entities.Projectile.Bullet;

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
    public static Position playerPositon;
    public static int playerRadius;

    // Entity Lists
    private Enemy enemy;
    private Flag flag; // todo: Remove
    private static EnemyPool enemies;
    private BulletPool bullets;
    private LinkedList<Bullet> bulletsPlayerImpacting;
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
    public static final double backgroundScrollingSpeed = Background.scrollRateGame * 35/32; // 3.5; // 3 < v < 4 // = scrollRate/2;
    private long spawnInterval = 3 * 60;

    EntityHandler() {
        bullets = new BulletPool();
        pickups = new PickupPool();
        enemies = new EnemyPool(bullets);
        bulletsPlayerImpacting = new LinkedList<>();
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
        bulletsPlayerImpacting = player.getImpactingBulletsList();
    }

//    public void cleanDisabledEntities() {
//        cleanBullets();
//        cleanEnemies();
//        cleanPickups();
//    }

    public void render(Graphics g) {
//        renderShadows(g);
        renderReflections(g);
        GameController.renderClouds(g);
        player.render(g);
        bullets.render(g);
        pickups.render(g);
        enemies.render(g);

//        if (bulletsPlayerImpacting != null) {
            for (Entity entity : bulletsPlayerImpacting) {
                entity.render(g);
            }
//        }
        //renderFlag(g);
    }

    public void spawnEntity(int x, int y, int enemyType, int distance, int direction) {
        enemies.spawnEntity(x, y, enemyType, distance, direction);
        spawnInterval = enemies.getSpawnInterval();
    }

    /********************
     * Player Functions *
     ********************/

    // Helper function exists because GameController needs to call on this during tutorial
    public void updatePlayer() {
        player.update();
        if (playerPositon == null) {
            playerPositon = new Position(player.getCenterX(), player.getCenterY());
        } else {
            playerPositon.setPosition(player.getCenterX(), player.getCenterY());
        }

        playerRadius = player.getRadius();
    }

    public void renderPlayer(Graphics g) {
        player.render(g);
    }

    public void movePlayer(char c) {
        if (player != null) {
            if (c == 'l')
                player.moveLeft(true);
            else if (c == 'r')
                player.moveRight(true);

            if (c == 'u')
                player.moveUp(true);
            else if (c == 'd')
                player.moveDown(true);
        }
    }

    public void stopMovePlayer(char c) {
        if (player != null) {
            if (c == 'l')
                player.moveLeft(false);
            else if (c == 'r')
                player.moveRight(false);

            if (c == 'u')
                player.moveUp(false);
            else if (c == 'd')
                player.moveDown(false);
        }
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
        if (player != null) {
            player.canShoot(true);
        }
    }

    public void playerCannotShoot() {
        if (player != null)
            player.canShoot(false);
    }

    public void playerSpecial() {
        if (player != null)
            player.special();
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

        if (playerPositon == null) {
            playerPositon = new Position(player.getCenterX(), player.getCenterY());
        } else {
            playerPositon.setPosition(player.getCenterX(), player.getCenterY());
        }

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

//    private void clearBullets() {
//        bullets.clear();
//    }

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
            case PickupHoming:
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
        transparency = 0.15f;
        renderBulletReflections(g2d, transparency);
        player.renderReflection(g2d, transparency);
        renderEnemyReflections(g2d, transparency);
    }

    private void renderEnemyReflections(Graphics2D g2d, float transparency) {
        enemies.renderReflections(g2d, transparency);
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        bullets.renderReflections(g2d, transparency);
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
            if (lastDestroyedType > VariableHandler.TYPE_BASIC2) {
                EntityID pickupID = null;
                int pickupValue = 0;
                int minChanceCoefficient = 50;
                if (lastDestroyedType == VariableHandler.enemyTypes) {
                    pickupID = EntityID.PickupPulse;
                    pickupValue = 1;
                    minChanceCoefficient = 100;
//                } else if (lastDestroyedType == TYPE_DRONE2) {
//                    pickupID = EntityID.PickupHoming;
//                    pickupValue = 1;
//                    // todo: Spawn Homing Bullets
//                } else if (lastDestroyedType == TYPE_DRONE3) {
//                    // todo: Spawn Bomb Pickup
                } else
                    if (lastDestroyedType == VariableHandler.TYPE_FAST || lastDestroyedType == VariableHandler.TYPE_SIDE3) {
                    pickupID = EntityID.PickupFirepower;
                    pickupValue = 1;
                } else if (lastDestroyedType == VariableHandler.TYPE_STATIC1 || lastDestroyedType == VariableHandler.TYPE_DRONE2
                        || lastDestroyedType == VariableHandler.TYPE_DRONE4 || lastDestroyedType == VariableHandler.TYPE_SIDE1
                        || lastDestroyedType == VariableHandler.TYPE_MINE2) {
                    pickupID = EntityID.PickupFirepower;
                    pickupValue = 1;
                } else if (lastDestroyedType == VariableHandler.TYPE_SCATTER1 || lastDestroyedType == VariableHandler.TYPE_SCATTER2) {
                    pickupID = EntityID.PickupShield;
                    pickupValue = 100;
                } else {
                    pickupID = EntityID.PickupHealth;
                    pickupValue = 10;
                }
                if (pickupID != null) {
                    if (pickupID == EntityID.PickupFirepower) {
                        if (VariableHandler.firepower.getValue() >= 25) {
                            minChanceCoefficient -= VariableHandler.getLevel();
                        }
                    } else if (pickupID == EntityID.PickupHealth) {
                        minChanceCoefficient -= VariableHandler.getLevel();
                    }
                    minChanceCoefficient = Math.max(minChanceCoefficient, 0);
                    int rand = Utility.randomRangeInclusive(0, 100);
                    if (rand <= minChanceCoefficient) {
                        spawnPickup(lastDestroyedX, lastDestroyedY, pickupID, pickupValue);
                    }
                }
            }
            lastDestroyedX = -1;
        }
        pickups.update();
    }

    public void clearEnemies() {
        enemies.clear();
        bullets.clear();
    }

    /*
    * Boss Functions
    * */

//    private void destroyBoss() {
//        boss.setAlive(false);
//        destroy(boss);
//        VariableHandler.increaseScore(VariableHandler.getBossScore());
//    }

    public void spawnBoss(int x, int y, int bossType) {
        // todo: Boss needs its own enemytype, otherwise Pool will crash
        if (bossType == 3)
            boss = new EnemyBoss4(x, y, levelHeight);
        else if (bossType == 2)
            boss = new EnemyBoss3(x, y, levelHeight);
        else if (bossType == 1)
            boss = new EnemyBoss2(x, y, levelHeight);
        else
            boss = new EnemyBoss1(x, y, levelHeight);

        VariableHandler.setBossAlive(true);
        enemies.addBoss(boss);
        VariableHandler.initHealthBoss(boss.getHealth());
//        VariableHandler.setHealthBossDefault(boss.getHealth());
//        VariableHandler.setHealthBoss(VariableHandler.getHealthBossDefault());

        SoundHandler.BGMUp();
    }

//    public void spawnBoss(int level, int x, int y) {
//        if (level == 2) {
//            boss = new EnemyBoss1(x, y, levelHeight);
//        } else if (level == 3) {
//            boss = new EnemyBoss2(x, y, levelHeight);
//        } else if (level == 4) {
////        boss = new EnemyBoss3(x, y);
//        }
//
//        if (boss != null) {
//            VariableHandler.setBossAlive(true);
//            enemies.add(boss);
//            VariableHandler.initHealthBoss(boss.getHealth());
//        }
//        this.boss = boss;
//    }

//    public void checkBoss() {
//        if (boss != null) {
//            if (VariableHandler.isBossAlive() != boss.isAlive()) {
//                VariableHandler.setBossAlive(boss.isAlive());
//            }
//        }
//    }
//
//    public void renderBossHealth(Graphics g) {
//        if (boss != null) {
////            if (boss.isInscreenY() && boss.isAlive())
////                VariableHandler.drawBossHealth(g);
//        }
//    }

    /***********************
     * Collision Functions *
     ***********************/

    private void playerHostileCollision() {
        playerVsEnemyCollision();
        playerVsEnemyBulletCollision();
    }

    private void playerVsEnemyBulletCollision() {
        if (playerRadius > -1)
            bullets.destroyIfWithinRadius(playerPositon, playerRadius);
        bullets.checkCollision(player);
    }

    private void playerVsEnemyCollision() {
        enemies.checkCollisions(player);
    }

    private void enemyVsPlayerBulletCollision() {
        if (playerRadius > -1)
            enemies.damageIfWithinRadius(playerPositon.x, playerPositon.y, playerRadius);
        enemies.checkCollisionBullet(player);
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

    private void checkDeathAnimationEnd() {
        bullets.checkDeathAnimationEnd();

    }

    public static int getActiveEnemies(int enemyType) {
        return enemies.getActive(enemyType);
    }

    public static boolean exclusionZonesContains(int x) {
        return enemies.exclusionZonesContains(x);
    }

    public static int getLevelHeight() {
        return levelHeight;
    }

    // Temporary Functions until better implementation

    public static LinkedList<Entity> getEnemyList() {
        return enemies.getEntities();
    }

    public long getSpawnInterval() {
        return spawnInterval;
    }
}
