package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Bullet;
import com.euhedral.game.Entities.BulletEnemy;
import com.euhedral.game.Entities.Player;

import java.awt.*;
import java.util.Random;

/*
 *  Standard Enemies, flies downwards and shoots a missile at intervals
 * */
public class Enemy extends MobileEntity {

    protected int health;
    protected int power = 1;
    protected ContactID contactId;
    protected EnemyID enemyID;
    protected double offscreenVelY;
    protected boolean moveLeft, moveRight;
    protected Color color;
    protected int shootTimerDefault = 150;
    protected int shootTimer = shootTimerDefault;
    protected boolean inscreen = false;
    protected float cam;
    protected Random r;
    protected int score = 50;
    protected int distance;
    protected int shotNum = 0;
    protected int movementTimer;

    protected Animation explosion;

    protected int levelHeight;
    protected boolean alive = true;

    protected boolean shot = false;

    public Enemy(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, EntityID.Enemy);
        this.enemyID = EnemyID.Basic;
        contactId = ContactID.Air;
        offscreenVelY = velY;
        moveRight = false;
        moveLeft = false;
        cam = GameController.getCamera().getMarker();
        power = 1;
        width = Utility.intAtWidth640(32);
        height = width;
        color = Color.red;
        this.contactId = contactID;
        r = new Random();
        setLevelHeight(levelHeight);
        initialize();
    }

    public Enemy(int x, int y, ContactID contactID, Color color, int levelHeight) {
        this(x, y, contactID, levelHeight);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
        shootTimer--;
        if (!inscreen) {
            inscreen = y > cam + Utility.percHeight(30);
        }
        if (inscreen && alive) {
            if (shootTimer <= 0) {
                shoot();
            }
        }

//        updateBullets();
//        bullets.checkIfBelowScreen(levelHeight);
    }

    @Override
    public void move() {
        x = Utility.clamp(x, 0, Engine.WIDTH - Utility.intAtWidth640(width));

        if (inscreen) {
            moveInScreen();
        } else {
            y += offscreenVelY;
        }
    }

    @Override
    public void initialize() {
        if (contactId == ContactID.Ground) {
            width = Utility.intAtWidth640(32);
            height = 2* width;
            color = Color.pink;
            r = new Random();
            health = r.nextInt(3) + 2;
            minVelY = 2f;
            velY = minVelY;
        }
        explosion = new Animation(10, GameController.getTexture().explosion[0],
                GameController.getTexture().explosion[1],
                GameController.getTexture().explosion[2],
                GameController.getTexture().explosion[3],
                GameController.getTexture().explosion[4]
        );
    }

    @Override
    public void render(Graphics g) {
//        bullets.render(g);
        super.render(g);
    }

    protected void shoot() {
        shotNum++;
        resetShooter();
    }

    protected void shootDownDefault() {
        shot = true;
//        spawnBullet(x + width/2, y, SOUTH);
    }

//    private void spawnBullet(int x, int y, double dir) {
//        if (bullets.getPoolSize() > 0) {
//            bullets.spawnFromPool(x, y, dir);
//        }
//        else
//            bullets.add(new BulletEnemy(x, y, dir));
//    }

    public void moveInScreen() {
        y += velY;
        x += velX;
    }

    public void damage() {
        this.health--;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isInscreen() {
        return inscreen;
    }

    public void setInscreen(boolean inscreen) {
        this.inscreen = inscreen;
    }

    protected void resetShooter() {
        shootTimer = shootTimerDefault;
    }

    protected void healthRange(int min, int max) {
        health = Utility.randomRange(min, max);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY =  velY;
    }

    public ContactID getContactId() {
        return contactId;
    }

    public int getScore() {
        return score;
    }

//    public LinkedList<Bullet> getBullets() {
//        return bullets;
//    }

//    public void clearBullets() {
//        bullets.clear();
//    }

    public void setHMove(String move) {
        if (move == "left") {
            hMove = HorizontalMovement.LEFT;
            velX = -minVelX;
        } else if (move == "right") {
            hMove = HorizontalMovement.RIGHT;
            velX = minVelX*1.45f;//1.95f;
        } else {
            velX = 0;
        }
    }

    public void setLevelHeight(int levelHeight) {
        this.levelHeight = levelHeight;
    }

    public void destroy() {
        alive = false;
        velX = 0;
        SoundHandler.playSound(SoundHandler.EXPLOSION);
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getBoundsHorizontal() {
        Rectangle bounds = new Rectangle(x, y, width, 1*height/3 + 2);
        return bounds;
    }

    public Rectangle getBoundsVertical() {
        Rectangle bounds = new Rectangle(x + (width / 4), y, (2 * width) / 4, height);
        return bounds;
    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        Rectangle r1 = getBoundsVertical();
        Rectangle r2 = getBoundsHorizontal();
        g.drawRect(r1.x, r1.y, r1.width, r1.height);
        g.drawRect(r2.x, r2.y, r2.width, r2.height);

    }

    @Override
    public void resurrect(int x, int y) {
        super.resurrect(x, y);
        shootTimer = shootTimerDefault;
        inscreen = false;
        alive = true;
    }

    public boolean checkDeathAnimationEnd() {
        return explosion.playedOnce;
    }

    public void setMovementTimer(int time) {
        this.movementTimer = time;
    }

//    public boolean checkCollisions(Player player) {
//        for (Entity entity: bullets.getEntities()) {
//            Bullet bullet = (Bullet) entity;
//
//            if (bullet.isActive() && player.checkCollision(bullet.getBounds())) {
//                bullet.disable();
//                bullets.increase();
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void clear() {
        super.clear();
//        bullets.clear();
    }

//    public void updateBullets() {
//        bullets.update();
//        bullets.disableIfBelowScreen(levelHeight);
//    }

    public boolean hasShot() {
        return shot;
    }

    public void resetShot() {
        shot = false;
    }

    //stub
    public int getTurretX() {
        return 0;
    }

    // Private Methods
}
