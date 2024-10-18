package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.game.Entities.*;
import com.euhedral.game.Entities.Enemy.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;

// Manages all entities in game
public class EntityHandler {

//    private VariableHandler variableHandler;
    private int levelHeight;

//    private Pool entities;

    // Player
    private Player player;// = new Player(0, 0, 0);

    // Entity Lists
    private Flag flag;
    private Pool enemies = new Pool();
    private Pool bullets = new Pool();
    private Pool pickups = new Pool();

    private EnemyBoss boss;

    EntityHandler() {
//        this.variableHandler = variableHandler;
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
        bullets.render(g);
        pickups.render(g);
        enemies.render(g);
        player.render(g);
        //renderFlag(g);
    }

    public void spawnEntity(int x, int y, EntityID id, Color color, String move, int time) {
        // todo: Player

        // Air Enemies
        if (enemies.getPoolSize() > 0) {
            enemies.spawnFromPool(x, y, move, time);
        }
        else {
            spawnNew(x, y, id, color, move, time);
        }
//        enemies.printPool("Enemy");

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

    private void spawnNew(int x, int y, EntityID id, Color color, String move, int time) {
        if (id == EntityID.EnemyBasic) {
            Enemy enemy = new EnemyBasic(x, y, ContactID.Air, color, levelHeight);
            enemy.setHMove(move);
            enemy.setMovementDistance(time);
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

    // Helper function exists because GameController needs to call on this during tutorial
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

    public void spawnPlayer(int x, int y) {
        if (player == null) {
            player = new Player(x, y, levelHeight);
        } else {
            player.setX(x);
            player.setY(y);
            player.resetMovement();
        }
//        player = new Player(x, y, levelHeight);
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
        bullets.disableIfBelowScreen(levelHeight);
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
        int value = 0;
        switch (id) {
            case PickupHealth:
                value = 30;
                break;
            case PickupShield:
                value = 100;
                break;
            case PickupPower:
                value = 1;
                break;
        }
        spawnPickup(x, y, id, value);
    }

    private void spawnPickup(int x, int y, EntityID id, int value) {
        if (pickups.getPoolSize() > 0) {
            pickups.spawnFromPool(x, y, id, value);
//            spawnPickupsFromPool(x, y, id);
        }
        else
            pickups.add(new Pickup(x, y, id, value));
//        System.out.println("Pickup spawned");
    }

//    public void spawnPickupsFromPool(int x, int y, EntityID id) {
//        Pickup pickup = (Pickup) pickups.findInList();
//        pickup.resurrect(x, y, id);
//        pickups.decrease();
//        System.out.println("Pool: " + pickups.getPoolSize() + " | Pickups: " + pickups.getEntities().size());
//    }

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
        Graphics2D g2d = (Graphics2D) g;
        float transparency = 0.2f;
        renderBulletReflections(g2d, transparency);
        player.renderReflection(g2d, transparency);
        renderEnemyReflections(g2d, transparency);
    }

    private void renderEnemyReflections(Graphics2D g2d, float transparency) {
        LinkedList<Entity> list = enemies.getEntities();
        for (Entity entity: list) {
            Enemy enemy = (Enemy) entity;
            enemy.renderReflection(g2d, transparency);
        }
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        LinkedList<Entity> list = bullets.getEntities();
        for (Entity entity : list) {
            Bullet bullet = (Bullet) entity;
            bullet.renderReflection(g2d, transparency);
        }
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
        enemies.disableIfBelowScreen(levelHeight);
        LinkedList<Entity> enemies = this.enemies.getEntities();
        for (Entity entity : enemies) {
            Enemy enemy = (Enemy) entity;
            if(true) {
                enemy.update();
                checkDeathAnimationEnd(enemy);
                if (enemy.hasShot()) {
                    enemy.resetShot();
                    spawnEnemyBullet(enemy);
                }
//                addToBullets(enemy);
            }
//            else {
//                enemy.updateBullets();
//            }
        }
    }

    private void spawnEnemyBullet(Enemy enemy) {
        int x = enemy.getTurretX();
        double y = enemy.getY();
        int dir = 90;
        if (bullets.getPoolSize() > 0) {
            bullets.spawnFromPool(x, (int) y, dir);
        }
        else
            bullets.add(new BulletEnemy(x, (int) y, dir));
    }

    public void clearEnemies() {
        enemies.clear();
        clearBullets();
    }

    private void disable(Entity entity) {
        entity.disable();
    }

    private void destroy(Enemy enemy) {
        enemy.destroy();
        VariableHandler.increaseScore(enemy.getScore());
//        spawnPickup((int) enemy.getX(), (int) enemy.getY(), EntityID.PickupHealth, 5);
    }

    private void destroy(Bullet bullet, MobileEntity entity) {
        SoundHandler.playSound(SoundHandler.IMPACT);
        bullet.destroy(entity);
    }

    /*
    * Boss Functions
    * */

    private void destroyBoss() {
        boss.setAlive(false);
        destroy(boss);
        VariableHandler.increaseScore(VariableHandler.getBossScore());
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
            if (boss.isInscreen() && boss.isAlive())
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
        for (Entity entity: bullets.getEntities()) {
            Bullet bullet = (Bullet) entity;

            if (bullet.isActive() && player.checkCollision(bullet.getBounds())) {
//                bullets.increase(bullet);
                destroy(bullet, player);
                damagePlayer(10);
            }
        }
    }

    private void playerVsEnemyCollision() {
        for (Entity entity : enemies.getEntities()) {
            Enemy enemy = (Enemy) entity;
            boolean enemyInAir = enemy.getContactId() == ContactID.Air;
            if (enemyInAir && enemy.isActive())
                if (enemy.isInscreen() && enemy.isActive()) {
                    boolean collision1 = player.checkCollision(enemy.getBoundsHorizontal());
                    boolean collision2 = player.checkCollision(enemy.getBoundsVertical());
                    if (collision1 || collision2) {
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
            if (enemy.isInscreen() && enemy.isActive()) {
                Bullet bullet = player.checkCollision(enemy);
                if (bullet != null) {
                    destroy(bullet, enemy);
                    if (enemy.getContactId() == ContactID.Boss) {
                        boss.damage();
                        VariableHandler.setHealthBoss(boss.getHealth());
                        if (boss.getHealth() <= 0) {
                            destroyBoss();
                        }
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

    public void playerVsPickupCollision() {
        // Player vs pickup collision
        LinkedList<Entity> entities = pickups.getEntities();
        for (Entity entity: entities) {
            Pickup pickup = (Pickup) entity;
            if (pickup.isActive()) {
                if (pickup.getBounds().intersects(getPlayerBounds())) {
                    pickup.collision();
                    pickups.increase();
//                    Utility.log("Pool: " + pickups.getPoolSize());
                }
            }
        }
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

    private void checkDeathAnimationEnd(Enemy enemy) {
        if (enemy.isExploding()) {
            if (enemy.checkDeathAnimationEnd()) {
                enemies.increase(enemy);
            }
        }
    }

    private void checkDeathAnimationEnd() {
        for (Entity entity: bullets.getEntities()) {

            Bullet bullet = (Bullet) entity;

            if (bullet.isImpacting()) {
                if (bullet.checkDeathAnimationEnd()) {
                    bullets.increase(bullet);
                }
            }
        }

    }

}
