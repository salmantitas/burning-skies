package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/*
 *  Standard Enemies, flies downwards and shoots a missile at intervals
 * */
public class Enemy extends MobileEntity {

    protected int health;
    protected int power = 1;
    protected ContactID contactId;
    protected EnemyID enemyID;
    protected float offscreenVelY;
    protected boolean moveLeft, moveRight;
    protected Color color;
    protected int shootTimerDef = 250;
    protected int shootTimer = shootTimerDef;
    protected LinkedList<Bullet> bullets = new LinkedList<>();
    protected boolean inscreen = false;
    protected Camera cam;
    protected Random r;
    protected int score = 50;
    protected int distance;
    protected int shotNum = 0;
    protected int movementTimer;

    public Enemy(int x, int y, EnemyID enemyID, ContactID contactID) {
        super(x, y, EntityID.Enemy);
        this.enemyID = enemyID;
        contactId = ContactID.Air;
        velY = 1.95f;
        offscreenVelY = velY;
        moveRight = false;
        moveLeft = false;
        cam = GameController.getCamera();
        power = 1;
        width = Engine.intAtWidth640(32);
        height = width;
        color = Color.red;
        this.contactId = contactID;
        r = new Random();
        initialize();
    }

    public Enemy(int x, int y, EnemyID enemyID, ContactID contactID, Color color) {
        this(x, y, enemyID, contactID);
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
        shootTimer--;
        if (!inscreen) {
            inscreen = y > cam.getMarker() + 100;
        }
        if (inscreen) {
            if (shootTimer <= 0) {
                shoot();
            }
        }

        if (enemyID == EnemyID.Move) {
            moveHorizontally();
        } else if (enemyID == EnemyID.Snake) {
            moveLikeSnake();
        }
    }

    @Override
    public void move() {
        x = Engine.clamp(x, 0, Engine.WIDTH - width);

        if (inscreen) {
            moveInScreen();
        } else {
            y += offscreenVelY;
        }
    }

    @Override
    public void initialize() {
        if (enemyID == EnemyID.Basic) {
            power = 1;
            shootTimerDef = 250;
            velY = 1.8f;
            healthRange(4,6);
            score = 50;
        }
        if (enemyID == EnemyID.Fast) {
            power = 2;
            shootTimerDef = 150;
            velY = 4f;
            healthRange(2,4);
            score = 100;
        }
        if (enemyID == EnemyID.Snake) {
            shootTimerDef = 60;
            healthRange(6,10);
            score = 150;
            velX = velY;
            distance = width * 2;
            movementTimer = distance;
        }
        if (enemyID == EnemyID.Move) {
            width = Engine.intAtWidth640(48);
            shootTimerDef = 120;
            velY = 1.75f;
            healthRange(8,12);
            score = 200;
            distance = width * 5;
            movementTimer = distance;
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }

    protected void shoot() {
        shotNum++;
        resetShooter();

        if (enemyID == EnemyID.Snake) {
            snakeShoot();
        } else if (enemyID == EnemyID.Fast) {
            fastShoot();
        } else if (enemyID == EnemyID.Move) {
            moveShoot();
        } else {
            shootDownDefault();
        }
    }

    private void moveShoot() {
        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
        bullets.add(new BulletEnemy(x + (int) (0.8 * width), y + height / 2, 90));
    }

    private void fastShoot() {
        int newVel = Engine.intAtWidth640(5);
        double angle = 75;
        bullets.add(new BulletEnemy(x + width/2,y, angle, newVel));
        bullets.add(new BulletEnemy(x + width/2,y, angle + 2 * (90 - angle), newVel));
        if (power == 2) {
            bullets.add(new BulletEnemy(x + width/2,y, 90, newVel));
        }
    }

    private void snakeShoot() {
        int var = shotNum % 3;
        int newVel = Engine.intAtWidth640(5);
        double angle = 75;

        if (var == 0) {
            bullets.add(new BulletEnemy(x + width/2,y, 90));
        } else if (var == 1) {
            bullets.add(new BulletEnemy(x + width/2,y, angle, newVel));
        } else {
            bullets.add(new BulletEnemy(x + width/2,y, angle + 2 * (90 - angle), newVel));
        }

        resetShooter();
    }

    public void moveInScreen() {
        y += velY;
        x += velX;

        if (hMove == HorizontalMovement.LEFT) {
            velX = -2f;
        } else if (hMove == HorizontalMovement.RIGHT) {
            velX = 2f;
        } else {
            velX = 0;
        }
    }

    public void moveHorizontally() {
        if (movementTimer > 0) {
            movementTimer--;
        } else {
            movementTimer = distance;
        }

        int
                int0 = 0,
                int1 = Engine.perc(distance, 30),
                int2 = Engine.perc(distance, 50),
                int3 = Engine.perc(distance, 80);


        if (movementTimer <= distance && movementTimer > int3) {
            hMove = HorizontalMovement.LEFT;
        } else if (movementTimer <= int3 && movementTimer > int2 || movementTimer <= int1 && movementTimer > int0) {
            hMove = HorizontalMovement.NONE;
        } else if (movementTimer <= int2 && movementTimer > int1) {
            hMove = HorizontalMovement.RIGHT;
        }
//        } else if (movementTimer <= int1 && movementTimer > int0) {
//            hMove = HorizontalMovement.NONE;
//        }
    }

    public void moveLikeSnake() {
        if (movementTimer > 0) {
            movementTimer--;
        } else {
            movementTimer = distance;
        }

        int
                int0 = 0,
                int1 = Engine.perc(distance, 30),
                int2 = Engine.perc(distance, 50),
                int3 = Engine.perc(distance, 80);


        if (movementTimer <= distance && movementTimer > int3) {
            hMove = HorizontalMovement.LEFT;
        } else if (movementTimer <= int3 && movementTimer > int2 || movementTimer <= int2 && movementTimer > int1) {
            hMove = HorizontalMovement.RIGHT;
//        } else if (movementTimer <= int2 && movementTimer > int1) {
//            hMove = HorizontalMovement.RIGHT;
        } else if (movementTimer <= int1 && movementTimer > int0) {
            hMove = HorizontalMovement.LEFT;
        }
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

    protected void shootDownDefault() {
        bullets.add(new BulletEnemy(x + width/2,y, 90));
    }

    protected void resetShooter() {
        shootTimer = shootTimerDef;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    protected void healthRange(int min, int max) {
        health = r.nextInt(max - min) + min;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY =  velY;
    }

    public ContactID getID() {
        return contactId;
    }

    public int getScore() {
        return score;
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }

    public void clearBullets() {
        bullets.clear();
    }

    // Private Methods
}
