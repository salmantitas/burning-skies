package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Entity;
import com.euhedral.engine.Pool;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.*;
import com.euhedral.game.Entities.Enemy.*;

import java.awt.*;
import java.util.LinkedList;

// Manages all entities in game
public class EntityHandler {

    private VariableHandler variableHandler;
    private int levelHeight;

//    private LinkedList<Entity> entities;

    // Player
    private Player player;// = new Player(0, 0, 0);

    // Entity Lists
    private Flag flag;
    private Pool enemies = new Pool();
    private Pool bullets = new Pool();
    private Pool pickups = new Pool();
//    private LinkedList<Enemy> enemies = new LinkedList<>();
//    private LinkedList<Bullet> bullets = new LinkedList<>();
//    private LinkedList<Pickup> pickups = new LinkedList<>();

//    private int poolEnemy = enemies.size();
//    private int poolBullet = bullets.size();
//    private int poolPickup = pickups.size();

    private EnemyBoss boss;

    EntityHandler(VariableHandler variableHandler) {
        this.variableHandler = variableHandler;
        initializeAnimations();
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
        pickups.update();
//        updatePickup();
//        updateFlag();

        checkCollisions();
    }

//    public void cleanDisabledEntities() {
//        cleanBullets();
//        cleanEnemies();
//        cleanPickups();
//    }

    public void render(Graphics g) {
        bullets.render(g);
//        renderBullets(g);
        pickups.render(g);
//        renderPickup(g);
        player.render(g);
//        renderPlayer(g);
        enemies.render(g);
//        renderEnemies(g);
        //renderFlag(g);
    }

    public void spawnEntity(int x, int y, EntityID id, Color color) {
        // todo: Player

        // Air Enemies

        // todo: Fix inconsistent spawning. Causes the difference to go up to 5 seconds!
        if (enemies.getPoolSize() > 0) {
            spawnFromPool(x, y, id);
        }
        else {
            spawnNew(x, y, id, color);
        }

        // Pickups

//        else if (id == EntityID.Pickup) {
//            spawnPickup(x, y, PickupID.Health, color);
//        }
//
//        else if (id == EntityID.PickupShield) {
//            spawnPickup(x, y, PickupID.Shield, color);
//        }

        // todo: Boss
    }

    private void spawnFromPool(int x, int y, EntityID id) {
        if (id == EntityID.EnemyBasic) {
            Enemy enemy = (Enemy) enemies.findInList();
            enemy.ressurect(x, y); // todo: pool is potentially being updated before enemy is deactivated
            enemies.decrease();
            System.out.println("Pool: " + enemies.getPoolSize() + " | Enemies: " + enemies.getEntities().size());
        }
    }

    private void spawnNew(int x, int y, EntityID id, Color color) {
        if (id == EntityID.EnemyBasic) {
            Enemy enemy = new EnemyBasic(x, y, ContactID.Air, color, levelHeight);
            enemies.add(enemy);
//                System.out.println("Pool: " + poolEnemy + " | Enemies: " + enemies.size());
        } else if (id == EntityID.EnemyMove) {
            Enemy enemy = new EnemyMove(x, y, ContactID.Air, color, levelHeight);
            enemies.add(enemy);
        } else if (id == EntityID.EnemySnake) {
            Enemy enemy = new EnemySnake(x, y, ContactID.Air, color, levelHeight);
            enemies.add(enemy);
        } else if (id == EntityID.EnemyFast) {
            Enemy enemy = new EnemyFast(x, y, ContactID.Air, color, levelHeight);
            enemies.add(enemy);
        }

        // Ground Enemies

        else if (id == EntityID.EnemyGround) {
            spawnEnemy(x, y, EnemyID.Basic, ContactID.Ground, color);
        }
    }

    /********************
     * Player Functions *
     ********************/

    public void updatePlayer() {
        player.update();
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

    public void switchPlayerBullet() {
        player.switchBullet();
    }

    public int getPlayerPower() {
        return player.getPower();
    }

    public void playerCanShoot() {
        if (player != null)
            player.canShoot(true);
    }
    public void playerCannotShoot() {
        if (player != null)
            player.canShoot(false);
    }

    public void spawnPlayer(int width, int height) {
        player = new Player(width, height, levelHeight);
        player.setGround(VariableHandler.gotGround());
        player.setPower(VariableHandler.power.getValue());
        setCameraToPlayer();
    }

    public void spawnPlayer(int width, int height, int power, boolean ground) {
        player = new Player(width, height, levelHeight);
        player.setGround(ground);
        player.setPower(power);
        setCameraToPlayer();
    }

    public void checkCollisions() {
        playerVsPickupCollision();
        playerHostileCollision();
        enemyVsPlayerBulletCollision();
    }

//    private Bullet checkPlayerCollision(Enemy enemy) {
//        return player.checkCollision(enemy);
//    }

    // Temp Functions
    public Rectangle getPlayerBounds() {
        return player.getBounds();
    }

    public int getPlayerY() {
        return player.getY();
    }

    /********************
     * Bullet Functions *
     ********************/

    private void updateBullets() {
        LinkedList<Entity> bullets = this.bullets.getEntities();
//        Utility.log("Bulletsize: " + bullets.size());
        for (Entity entity : bullets) {
            Bullet bullet = (Bullet) entity;
            if (bullet.isActive()) {
                bullet.update();
                this.bullets.checkIfBelowScreen(bullet, levelHeight);
//                checkIfBelowScreen(bullet);
            }
        }

        if (boss != null) {
            addToBullets(boss);
//            LinkedList<Bullet> bossBullets = boss.getBullets();
//            this.bullets.addAll(bossBullets);
//            boss.clearBullets();
        }
    }

    private void clearBullets() {
        bullets.clear();
    }

    private void addToBullets(Enemy enemy) {
        LinkedList<Bullet> enemyBullets = enemy.getBullets();
        for (Bullet bullet: enemyBullets) {
            this.bullets.add(bullet);
        }
        enemy.clearBullets();
    }

    /********************
     * Pickup Functions *
     ********************/

//    public void updatePickup() {
//        for (Pickup pickup : pickups) {
//            if (pickup.isActive()) {
//                pickup.update();
//            }
//        }
//    }

//    public void renderPickup(Graphics g) {
//        for (Pickup pickup: pickups) {
//            if (pickup.isActive())
//                pickup.render(g);
//        }
//    }

    public void spawnPickup(int x, int y, EntityID id) {
        if (pickups.getPoolSize() > 0) {
            Pickup pickup = (Pickup) pickups.findInList();
            pickup.resurrect(x, y, id);
            pickups.decrease();
            System.out.println("Pool: " + pickups.getPoolSize() + " | Pickups: " + pickups.getEntities().size());
        }
        else
            pickups.add(new Pickup(x, y, id));
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

    public void spawnFlag() {
        flag = new Flag(Engine.WIDTH / 2, -Engine.HEIGHT / 2, ContactID.Air);
    }

    public void respawnFlag() {
        flag.reset();
    }

    public int getFlagY() {
        if (flag == null)
            return levelHeight;
        return flag.getY();
    }

    /*******************
     * Enemy Functions *
     *******************/

    public void spawnEnemy(int x, int y, EnemyID enemyID, ContactID contactId, Color color) {
            addEnemy(x, y, enemyID, contactId, color);
    }

    public void addEnemy(Enemy enemy) {
        enemy.setLevelHeight(levelHeight);
        enemies.add(enemy);
    }

    private void addEnemy(int x, int y, EnemyID eID, ContactID cID) {
//        Enemy enemy = new Enemy(x, y, eID, cID);
//        enemies.add(enemy);
    }

    private void addEnemy(int x, int y, EnemyID eID, ContactID cID, Color color) {
        Enemy enemy = new Enemy(x, y, cID, color, levelHeight);
        addEnemy(enemy);
    }

    public void updateEnemies() {
        LinkedList<Entity> enemies = this.enemies.getEntities();
        for (Entity entity : enemies) {
            Enemy enemy = (Enemy) entity;
            if(enemy.isActive()) {
                enemy.update();
                this.enemies.checkIfBelowScreen(enemy, levelHeight);
                checkDeathAnimationEnd(enemy);
                addToBullets(enemy);
            }
        }
    }

//    public void cleanEnemies() {
////        System.out.println("Enemies before cleaning: " + enemies.size());
//
//        for (int i = 0; i < enemies.size(); i++) {
//            Enemy enemy = enemies.get(i);
//            if (!enemy.isActive()) {
//                enemies.remove(enemy);
//                i--; // list is smaller by 1 now, so index is subtracted to prevent out of bounds
//            }
//        }
//
////        System.out.println("Enemies after cleaning: " + enemies.size());
//    }

//    public void cleanBullets() {
//        for (int i = 0; i < bullets.getEntities().size(); i++) {
//            Bullet bullet = (Bullet) bullets.getEntities().get(i);
//            if (!bullet.isActive()) {
//                bullets.remove(bullet);
//                i--; // list is smaller by 1 now, so index is subtracted to prevent out of bounds
//            }
//        }
//    }

//    public void cleanPickups() {
//        for (int i = 0; i < pickups.size(); i++) {
//            Pickup pickup = pickups.get(i);
//            if (!pickup.isActive()) {
//                pickups.remove(pickup);
//                i--; // list is smaller by 1 now, so index is subtracted to prevent out of bounds
//            }
//        }
//    }

    public void clearEnemies() {
        enemies.clear();
        clearBullets();
    }

    private void destroy(Entity entity) {
        entity.disable();
    }

    private void destroy(Enemy enemy) {
        enemy.destroy();
    }

    /*
    * Boss Functions
    * */

    private void destroyBoss() {
        boss.setAlive(false);
        destroy(boss);
        variableHandler.increaseScore(variableHandler.getBossScore());
    }

    public void spawnBoss(int level, int x, int y) {
        if (level == 2) {
            boss = new EnemyBoss1(x, y, levelHeight);
        } else if (level == 3) {
            boss = new EnemyBoss2(x, y, player, levelHeight);
        } else if (level == 4) {
//        boss = new EnemyBoss3(x, y);
        }

        if (boss != null) {
            variableHandler.setBossLives(true);
//            bossLives = true;
            enemies.add(boss);
            variableHandler.setHealthBossDef(boss.getHealth());
//            healthBossDef = boss.getHealth();
            variableHandler.setHealthBoss(variableHandler.getHealthBossDef());
//            healthBoss = healthBossDef;
        }
        this.boss = boss;
    }

    public void checkBoss() {
        if (boss != null) {
            if (variableHandler.isBossLives() != boss.isAlive()) {
                variableHandler.setBossLives(boss.isAlive());
            }
        }
    }

    public void renderBossHealth(Graphics g) {
        if (boss != null) {
            if (boss.isInscreen() && boss.isAlive())
                variableHandler.drawBossHealth(g);
        }
    }

    public void damagePlayer(int num){
        player.damage(num);
    }

    /***********************
     * Collision Functions *
     ***********************/

    private void playerHostileCollision() {
        playerVsEnemyCollision();
        playerVsEnemyBulletCollision();
    }

    private void playerVsEnemyBulletCollision() {
        for (Entity entity : bullets.getEntities()) {
            Bullet bullet = (Bullet) entity;
            if (bullet.isActive() && player.checkCollision(bullet.getBounds())) {
//                GameController.getSound().playSound(SoundHandler.IMPACT); // feels off
                damagePlayer(10);
                destroy(bullet);
                bullets.increase();
            }
        }
    }

    private void playerVsEnemyCollision() {
        for (Entity entity : enemies.getEntities()) {
            Enemy enemy = (Enemy) entity;
            boolean enemyInAir = enemy.getContactId() == ContactID.Air;
            if (enemyInAir && enemy.isAlive())
                if (enemy.isInscreen() && enemy.isActive()) {
                    boolean collision1 = player.checkCollision(enemy.getBoundsHorizontal());
                    boolean collision2 = player.checkCollision(enemy.getBoundsVertical());
                    if (collision1 || collision2) {
                        variableHandler.increaseScore(enemy.getScore());
                        damagePlayer(30);
                        destroy(enemy);
                    }
                } else if (enemy.getContactId() == ContactID.Boss) {
                    damagePlayer(10);
                }
        }
    }

    private void enemyVsPlayerBulletCollision() {
        for (Entity entity : enemies.getEntities()) {
            Enemy enemy = (Enemy) entity;
            if (enemy.isInscreen() && enemy.isAlive()) {
                Bullet bullet = player.checkCollision(enemy);
                if (bullet != null) {
//                    GameController.getSound().playSound(SoundHandler.IMPACT); // feels off
                    if (enemy.getContactId() == ContactID.Boss) {
                        boss.damage();
                        variableHandler.setHealthBoss(boss.getHealth());
                        if (boss.getHealth() <= 0) {
                            destroyBoss();
                        }
                    } else {
                        enemy.damage();
                        if (enemy.getHealth() <= 0) {
                            destroy(enemy);
                            variableHandler.increaseScore(enemy.getScore());
                        }
                    }
                    destroy(bullet);
                }
            }
        }
    }

    public void playerVsPickupCollision() {
        // Player vs pickup collision
        LinkedList<Entity> entities = pickups.getEntities();
        for (Entity entity: entities) {
            Pickup pickup = (Pickup) entity;
            if (pickup.isActive()) {
                if (pickup.getBounds().intersects(getPlayerBounds())) {
                    if (pickup.getID() == EntityID.PickupHealth)
                        variableHandler.health.increase(25);
                    else if (pickup.getID() == EntityID.PickupShield)
                        variableHandler.shield.increase(25);
                    else variableHandler.power.increase(1);
                    SoundHandler.playSound(SoundHandler.PICKUP);
                    pickup.disable();
                    pickups.increase();
                }
            }
        }
    }

    // Creates an instance of the player and sets the camera to follow it
    public void setCameraToPlayer() {
        int offsetVertical = Engine.HEIGHT - Utility.intAtWidth640(32)*3;

        // sets the camera's width to center the player horizontally, essentially to 0, and
        // adjust the height so that player is at the bottom of the screen
        GameController.camera = new Camera(0,getPlayerY() - offsetVertical);
        GameController.camera.setMarker(getPlayerY());
    }

    /*******************************
     * Entity Management Functions *
     ****************-**************/

//    public void addEntity(Entity entity) {
//        entities.add(entity);
//
//        /*************
//         * Game Code *
//         *************/
//    }
//
//    public void removeEntity(Entity entity) {
//        entities.remove(entity);
//
//        /*************
//         * Game Code *
//         *************/
//    }
//
//    private void updateEntities() {
//        for (int i = 0; i < entities.size(); i++) {
//            Entity entity = entities.get(i);
//            entity.update();
//        }
//    }
//
//    private void updateActiveEntities(LinkedList<Entity> list) {
//        for (int i = 0; i < list.size(); i++) {
//            Entity entity = list.get(i);
//            if (entity.isActive())
//                entity.update();
//        }
//    }
//
//    private void renderEntities(Graphics g) {
//        for (int i = 0; i < entities.size(); i++) {
//            Entity entity = entities.get(i);
//            entity.render(g);
//        }
//    }

    private void cleanEntities(LinkedList<Entity> list) {
        for (int i = 0; i < list.size(); i++) {
            Entity entity = list.get(i);
            if (!entity.isActive()) {
                list.remove(entity);
                i--; // list is smaller by 1 now, so index is subtracted to prevent out of bounds
            }
        }
    }

    public void setLevelHeight(int levelHeight) {
        this.levelHeight = levelHeight;
    }

    private void checkIfPoolable(Entity entity) {
        if (entity.getY() > levelHeight + (2.5 * entity.getHeight())) {
            entity.disable();
        }
    }

//    private void checkIfBelowScreen(Entity entity) {
//        if (entity.getY() > levelHeight + (2.5 * entity.getHeight())) {
//            entity.disable();
//        }
//    }

    private void checkDeathAnimationEnd(Enemy enemy) {
        if (!enemy.isAlive()) {
            if (enemy.checkDeathAnimationEnd()) {
                enemies.increase();
            }
        }

    }
}
