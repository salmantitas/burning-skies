package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Entity;
import com.euhedral.game.Entities.*;

import java.awt.*;
import java.util.LinkedList;

// Manages all entities in game
public class EntityManager {
    private VariableManager variableManager;

    private LinkedList<Entity> entities;

    // Player
    private Player player = new Player(0, 0, 0);

    // Entity Lists
    private Flag flag;
    private LinkedList<Enemy> enemies = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();
    private LinkedList<Pickup> pickups = new LinkedList<>();

    private EnemyBoss boss;

    EntityManager(VariableManager variableManager) {
        this.variableManager = variableManager;
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
    }

    public void update() {
        updatePlayer();
        updateBullets();
        updateEnemies();
        updatePickup();
        updateFlag();
    }

    public void render(Graphics g) {
        renderBullets(g);
        renderPickup(g);
        renderEnemies(g);
        renderFlag(g);
        renderPlayer(g);
    }

    public void spawnEntity(int x, int y, EntityID id, Color color) {
        // todo: Player

        // Air Enemies

        if (id == EntityID.EnemyBasic) {
            spawnEnemy(x, y, EnemyID.Basic, ContactID.Air, color);
        }

        else if (id == EntityID.EnemyMove) {
            spawnEnemy(x, y, EnemyID.Move, ContactID.Air, color);
        }

        else if (id == EntityID.EnemySnake) {
            spawnEnemy(x, y, EnemyID.Snake, ContactID.Air, color);
        }

        else if (id == EntityID.EnemyFast) {
            spawnEnemy(x, y, EnemyID.Fast, ContactID.Air, color);
        }

        // Ground Enemies

        else if (id == EntityID.EnemyGround) {
            spawnEnemy(x, y, EnemyID.Basic, ContactID.Ground, color);
        }

        // Pickups

        else if (id == EntityID.Pickup) {
            spawnPickup(x, y, PickupID.Health, color);
        }

        // todo: Boss
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
        player.canShoot(true);
    }
    public void playerCannotShoot() {
        player.canShoot(false);
    }

    public void spawnPlayer(int width, int height, int levelHeight, int power, boolean ground) {
        player = new Player(width, height, levelHeight);
        player.setGround(ground);
        player.setPower(power);
    }

    public Bullet checkPlayerCollision(Enemy enemy) {
        return player.checkCollision(enemy);
    }

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

    public void updateBullets() {
        for (Bullet bullet : bullets) {
            if (bullet.isActive())
                bullet.update();
        }

        if (boss != null) {
            bullets.addAll(boss.getBullets());
            boss.clearBullets();
        }
    }
    public void renderBullets(Graphics g) {
        for (Bullet bullet: bullets) {
            if (bullet.isActive())
                bullet.render(g);
        }
    }

    public void clearBullets() {
        bullets.clear();
    }

    public void addToBullets(Enemy enemy) {
        bullets.addAll(enemy.getBullets());
    }

    private void destroy(Bullet bullet) {
        bullet.disable();
    }

    /********************
     * Pickup Functions *
     ********************/

    public void updatePickup() {
        for (Pickup pickup : pickups) {
            if (pickup.isActive()) {
                pickup.update();
            }
        }
    }

    public void renderPickup(Graphics g) {
        for (Pickup pickup: pickups) {
            if (pickup.isActive())
                pickup.render(g);
        }
    }

    public void spawnPickup(int x, int y, PickupID id, Color color) {
        pickups.add(new Pickup(x, y, id, color));
//        System.out.println("Pickup spawned");
    }

    public void clearPickups() {
        pickups.clear();
    }

    /******************
     * Flag Functions *
     ******************/

    public void updateFlag() {
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
        return flag.getY();
    }

    /*******************
     * Enemy Functions *
     *******************/

    public void spawnEnemy(int x, int y, EnemyID enemyID, ContactID contactId, Color color) {
        addEnemy(x, y, enemyID, contactId, color);
    }

    private void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    private void addEnemy(int x, int y, EnemyID eID, ContactID cID) {
        Enemy enemy = new Enemy(x, y, eID, cID);
        enemies.add(enemy);
    }

    private void addEnemy(int x, int y, EnemyID eID, ContactID cID, Color color) {
        Enemy enemy = new Enemy(x, y, eID, cID, color);
        enemies.add(enemy);
    }

    public void updateEnemies() {
        for (Enemy enemy : enemies) {
            if(enemy.isActive()) {
                enemy.update();
                addToBullets(enemy);
                enemy.clearBullets();
            }
        }
    }

    public void renderEnemies(Graphics g) {
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.render(g);
            }
        }
    }

    public void clearEnemies() {
        enemies.clear();
    }

    private void destroy(Enemy enemy) {
        enemy.disable();
    }

    /*
    * Boss Functions
    * */

    private void destroyBoss() {
        boss.setAlive(false);
        destroy(boss);
        variableManager.increaseScore(variableManager.getBossScore());
//        score += bossScore;
    }

    public void spawnBoss(int level, int width, int height) {
        if (level == 1) {
            boss = new EnemyBoss1(width, height);
        } else if (level == 2) {
            boss = new EnemyBoss2(width, height);
        }
        if (boss != null) {
            variableManager.setBossLives(true);
//            bossLives = true;
            enemies.add(boss);
            variableManager.setHealthBossDef(boss.getHealth());
//            healthBossDef = boss.getHealth();
            variableManager.setHealthBoss(variableManager.getHealthBossDef());
//            healthBoss = healthBossDef;
        }
        this.boss = boss;
    }

    public void checkBoss() {
        if (boss != null) {
            if (variableManager.isBossLives() != boss.isAlive()) {
                variableManager.setBossLives(boss.isAlive());
            }
        }
    }

    public void renderBossHealth(Graphics g) {
        if (boss != null) {
            if (boss.isInscreen() && boss.isAlive())
                variableManager.drawBossHealth(g);
        }
    }

    public void damagePlayer(int num){
        player.decreaseHealth(num);
        variableManager.decreaseHealth(num);
    }

    /***********************
     * Collision Functions *
     ***********************/

    public void playerVsEnemyBulletCollision() {
        for (Bullet bullet: bullets) {
            if (bullet.isActive() && bullet.getBounds().intersects(player.getBounds())) {
                damagePlayer(10);
                destroy(bullet);
            }
        }
    }

    public void playerVsEnemyCollision() {
        for (Enemy enemy : enemies) {
            if (enemy.getID() == ContactID.Air)
                if (enemy.isInscreen() && enemy.getBounds().intersects(player.getBounds()) && enemy.isActive()) {
                    variableManager.increaseScore(enemy.getScore());
                    damagePlayer(30);
                    destroy(enemy);
                } else if (enemy.getID() == ContactID.Boss) {
                    damagePlayer(10);
                }
        }
    }

    public void enemyVsPlayerBulletCollision() {
        for (Enemy enemy : enemies) {
            if (enemy.isInscreen() && enemy.isActive()) {
                Bullet b = player.checkCollision(enemy);
                if (b != null) {
                    if (enemy.getID() == ContactID.Boss) {
                        boss.damage();
                        variableManager.setHealthBoss(boss.getHealth());
//                        healthBoss = boss.getHealth();
                        if (boss.getHealth() <= 0) {
                            destroyBoss();
                        }
                    } else {
                        enemy.damage();
                        if (enemy.getHealth() <= 0) {
                            destroy(enemy);
                            variableManager.increaseScore(enemy.getScore());
//                            score += enemy.getScore();
                        }
                    }
                    destroy(b);
                }
            }
        }
    }

    public void playerVsPickupCollision() {
        // Player vs pickup collision
        for (Pickup pickup: pickups) {
            if (pickup.isActive()) {
                if (pickup.getBounds().intersects(getPlayerBounds())) {
                    variableManager.increaseHealth(25);
                    pickup.disable();
                }
            }
        }
    }

    /*******************************
     * Entity Management Functions *
     ****************-**************/

    public void addEntity(Entity entity) {
        entities.add(entity);

        /*************
         * Game Code *
         *************/
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);

        /*************
         * Game Code *
         *************/
    }

    private void updateEntities() {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.update();
        }
    }

    private void updateActiveEntities(LinkedList<Entity> list) {
        for (int i = 0; i < list.size(); i++) {
            Entity entity = list.get(i);
            if (entity.isActive())
                entity.update();
        }
    }

    private void renderEntities(Graphics g) {
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.render(g);
        }
    }
}
