package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends MobileEntity {

    // Shooting Entity
    private boolean canShoot;
    private int shootTimer = 0;
    private final int shootTimerDefault = 10;
    private LinkedList<Bullet> bullets = new LinkedList<>();

    // Personal
    private int levelHeight;
    private int power;
    private boolean ground = false;
    private boolean airBullet = true;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = 5;

    private Attribute health;
    private Attribute shield;

    // Test
    private int mx, my;
    private boolean destinationGiven = false;

    // Graphics
    private Texture texture;


    public Player(int x, int y, int levelHeight) {
        super(x,y, EntityID.Player);
        texture = GameController.getTexture();
        setAttributes();
        setImage();
        this.levelHeight = levelHeight;
        width = Utility.intAtWidth640(32);
        height = width;
        color = Color.WHITE;

        velX = 0;
        velY = 0;
        acceleration = 0.05f;
        minVelY = 3;
        minVelX = 4;
        velY = minVelY;
        velX = minVelX;
        maxVelY = 2 * minVelY;
        maxVelX = 2 * minVelX;

        friction = true;
        frictionalForce = 0.9f;

        moveRight = false;
        moveLeft = false;
        moveDown = false;
        moveUp = false;

        this.power = 1;

        clampOffsetX = - 5 * width / 4;
        clampOffsetY = height;
    }

//    public Player(int x, int y, int levelHeight, BufferedImage image) {
//        this(x, y, levelHeight);
//        this.image = image;
//    }

    public void update() {
//        System.out.println("Player at (" + x + ", " + y + ")");
        super.update();
        shootTimer--;

        if (canShoot && shootTimer <= 0)
            shoot();

        for (Bullet bullet : bullets) {
            if (bullet.isActive())
                bullet.update();
        }

        setImage();
    }

    private void setAttributes() {
        health = VariableManager.health;
        shield = VariableManager.shield;
    }

    private void setImage() {
        if (health.getValue() > 66) {
            image = texture.player[0];
        } else if (health.getValue() > 33) {
            image = texture.player[1];
        } else {
            image = texture.player[2];
        }
    }

    public void render(Graphics g) {
        for (Bullet bullet : bullets) {
            if (bullet.isActive())
                bullet.render(g);
        }

        super.render(g);

        if (shield.getValue() > 0) {
            g.setColor(Color.yellow);
            g.drawOval(x-width/2, y-height/2, width*2, height*2);
        }
    }

    public Bullet checkCollision(Enemy enemy) {
        Bullet b = null;
        for (Bullet bullet : bullets) {
            BulletPlayer bulletPlayer = (BulletPlayer) bullet;
            if (bulletPlayer.isActive() && bulletPlayer.getBounds().intersects(enemy.getBounds()) &&
                    (bulletPlayer.getContactId() == enemy.getContactId() || bulletPlayer.getContactId() == ContactID.Air && enemy.getContactId() == ContactID.Boss)) {
                b = bulletPlayer;
            }
        }
        return b;
    }

    public void moveLeft(boolean b) {
        moveLeft = b;
    }

    public void moveRight(boolean b) {
        moveRight = b;
    }

    public void moveUp(boolean b) {
        moveUp = b;
    }

    public void moveDown(boolean b) {
        moveDown = b;
    }

    public void canShoot(boolean b) {
        canShoot = b;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void switchBullet() {
        if (ground)
            airBullet = !airBullet;
    }

    // Private Methods

    // This function governs the movementTimer of the player
    @Override
    protected void move() {
        super.move();

//        System.out.printf("VelX: %f | VelY: %f\n", velX, velY);

        // Clamp
        x = Utility.clamp(x, 0, Engine.WIDTH + clampOffsetX);
        y = Utility.clamp(y, levelHeight - 500, levelHeight + clampOffsetY);

        // movement
        mouseMove();
        keyboardMove();

    }

    private void shoot() {
        // Bullet Spawn Points
        // todo: positioning adjustment of bullet spawn point
        int bltSpnLeft = x + width - 8;
        int bltSpnMid = x + width / 2;
        int bltSpnRight = x + 4;

        int bltSpnDown = y + height * 2 / 3;

        if (power == 5) {
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH + shootAngle);
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH);
            spawnBullet(bltSpnMid, y, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH - shootAngle);
        }
        else if (power == 4) {
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH + shootAngle);
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH - shootAngle);
        } else if (power == 3) {
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH + shootAngle);
            spawnBullet(bltSpnMid, y, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH - shootAngle);
        } else if (power == 2) {
            spawnBullet(bltSpnLeft, bltSpnDown, NORTH);
            spawnBullet(bltSpnRight, bltSpnDown, NORTH);
        } else {
            spawnBullet(bltSpnMid, y, NORTH);
        }
        // reset shoot timer to default
        shootTimer = shootTimerDefault;
    }

    private void spawnBullet(int x, int y, int dir) {
        if (airBullet) {
            bullets.add(new BulletPlayerAir(x, y, dir));
        } else {
            bullets.add(new BulletPlayerGround(x, y, dir));
        }
    }

    private void keyboardMove() {
        // Moving Left
        if (moveLeft && !moveRight) {
            velX -= acceleration;
            velX = Utility.clamp(velX, - maxVelX, - minVelX);
        }

        // Moving Right
        else if (moveRight && !moveLeft) {
            velX += acceleration;
            velX = Utility.clamp(velX, minVelX, maxVelX);
        }

        // Not Moving Left or Right
        else if (!moveLeft && !moveRight || (moveLeft && moveRight)) {
            if (velX > 0) {
                velX -= frictionalForce;
            } if (velX < 0) {
                velX += frictionalForce;
            }
        }

        // Moving Up
        if (moveUp && !moveDown) {
            velY -= acceleration;
            velY = Utility.clamp(velY, - maxVelY, - minVelY);
        }

        // Moving Down
        else if (moveDown && !moveUp) {
            velY += acceleration;
            velY = Utility.clamp(velY, minVelY, maxVelY);

        }

        // Not Moving Up or Down
        else if (!moveUp && !moveDown || (moveUp && moveDown)) {
            if (velY > 0) {
                velY -= frictionalForce;
            }
            if (velY < 0) {
                velY += frictionalForce;
            }
        }
    }

    private void mouseMove() {

        if (destinationGiven) {
            int positionOffset = 5;
            if (mx == y && my == y) {
                destinationGiven = false;
            }
            if (Math.abs(mx - x) < positionOffset) {
                moveLeft = false;
                moveRight = false;
            } else if (mx > x) {
                moveLeft = false;
                moveRight = true;
            } else if (mx < x) {
                moveLeft = true;
                moveRight = false;
            }
            if (Math.abs(my - y) < positionOffset) {
                moveUp = false;
                moveDown = false;
            } else if (my > y) {
                moveUp = false;
                moveDown = true;
            } else if (my < y) {
                moveUp = true;
                moveDown = false;
            }
        }
    }

    public void giveDestination(int mx, int my) {
        this.mx = mx - width / 2;
        this.my = levelHeight - Utility.percHeight(83.5) + my;
        System.out.println("Destination: " + this.my);
        destinationGiven = true;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getMx() {
        return mx;
    }

    public int getMy() {
        return my;
    }

    public int getPower() {
        return power;
    }

    public void setGround(Boolean ground) {
        this.ground = ground;
    }

    public void damage(int num) {
        if (shield.getValue() > 0) {
            int temp = num - shield.getValue();
            shield.decrease(num * 2);
            if (temp > 0) {
                health.decrease(temp);
            }
        }
        else health.decrease(num);
    }
}
