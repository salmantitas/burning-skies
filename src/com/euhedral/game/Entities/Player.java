package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Player extends MobileEntity {

    // Shooting Entity
    private boolean canShoot;
    private int shootTimer = 0;
    private final int shootTimerDefault = 7;
    private Pool bullets = new Pool();

    // Personal
    private int levelHeight;
    private int power;
    private boolean ground = false;
    private boolean airBullet = true;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = Utility.intAtWidth640(5);

    private int center;

    private Attribute health;
    private Attribute shield;

    // Test
    private int mx, my;
    private boolean destinationGiven = false;

    // Graphics
    private TextureHandler textureHandler;

    // Lazy Physics
    private float acceleration; //stub //todo:delete
    private float frictionalForce; //stub //todo:delete

    // Decay
    int decayCounter = 0;

    public Player(int x, int y, int levelHeight) {
        super(x,y, EntityID.Player);
        textureHandler = GameController.getTexture();
        setAttributes();
        setImage();
        this.levelHeight = levelHeight;
        width = Utility.intAtWidth640(32);
        height = width;
        color = Color.WHITE;

        velX = 0;
        velY = 0;
        physics.setAcceleration(0.05f);
        acceleration = 0.1f; // todo: delete
        minVelY = 4;
        minVelX = 5;
//        velY = minVelY;
//        velX = minVelX;
        maxVelY = 2 * minVelY;
        maxVelX = 2 * minVelX;

        physics.enableFriction();
        physics.setFrictionalForce(0.9f);
        frictionalForce = 0.9f; // todo: delete

        resetMovement();

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

        bullets.update();
        bullets.checkIfAboveScreen();
        checkDeathAnimationEnd();

        setImage();

//        decay();
    }

    private void setAttributes() {
        health = VariableHandler.health;
        shield = VariableHandler.shield;
    }

    private void setImage() {
        if (health.getValue() > 66) {
            image = textureHandler.player[0];
        } else if (health.getValue() > 33) {
            image = textureHandler.player[1];
        } else {
            image = textureHandler.player[2];
        }
        VariableHandler.setHealthColor();
    }

    private void decay() {
        int maxDecay = 15;
        decayCounter++;
        if (decayCounter >= maxDecay) {
            VariableHandler.health.decrease(1);
            decayCounter = 0;
        }
    }

    @Override
    public void render(Graphics g) {
        bullets.render(g);

        super.render(g);

        if (shield.getValue() > 0) {
            g.setColor(Color.yellow);
            g.drawOval((int) x-width/2, (int) y-height/2, width*2, height*2);
        }

//        renderStats(g);
//        renderBounds(g);
    }

    public void renderShadow(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(Utility.makeTransparent(0.5f));

        int offsetX = (int) (Engine.WIDTH/2 - getCenterX()) / 15;
        int sizeOffset = 10;
        int offsetY = 10 + (int) y/500;
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) x - offsetX, (int) y - offsetY, width - sizeOffset, height - sizeOffset);

        g2d.setComposite(Utility.makeTransparent(1f));
    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        Rectangle2D r1 = getBoundsVertical();
        Rectangle2D r2 = getBoundsHorizontal();
        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(r1);
        g2d.draw(r2);
//        g.drawRect(r1.x, r1.y, r1.width, r1.height);
//        g.drawRect(r2.x, r2.y, r2.width, r2.height);

    }

    private void renderStats(Graphics g) {
        g.setColor(Color.white);
        int x = 10, y = 10, width = 1000, height = 1000;
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString("Velocity: " + velX + ", " + velY, x, y);

        Utility.log("Velocity: " + velX + ", " + velY);
    }

    public Bullet checkCollision(Enemy enemy) {
        Bullet bullet = null;
        for (Entity entity: bullets.getEntities()) {
            BulletPlayer bulletPlayer = (BulletPlayer) entity;
            if (bulletPlayer.isActive() && bulletPlayer.getBounds().intersects(enemy.getBounds()) &&
                    (bulletPlayer.getContactId() == enemy.getContactId() || bulletPlayer.getContactId() == ContactID.Air && enemy.getContactId() == ContactID.Boss)) {
                bullet = bulletPlayer;
            }
        }
        return bullet;
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

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D rV = getBoundsVertical();
        Rectangle2D rH = getBoundsHorizontal();
        boolean collidesVertically = object.intersects(rV);
        boolean collidesHorizontally = object.intersects(rH);

        return collidesVertically || collidesHorizontally;
    }

    public Rectangle2D getBoundsHorizontal() {
        Rectangle2D bounds = new Rectangle2D.Double(x, y + 2*height/3 - 2, width, height/3 + 2);
        return bounds;
    }

    public Rectangle2D getBoundsVertical() {
        Rectangle2D bounds = new Rectangle2D.Double(x + (width / 4), y, (2 * width) / 4, height);
        return bounds;
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
        y = Utility.clamp(y, levelHeight - 600, levelHeight + clampOffsetY);

        // movement
        mouseMove();
        keyboardMove();

    }

    private void shoot() {
        // Bullet Spawn Points
        // todo: positioning adjustment of bullet spawn point
        int spawnLeftX = (int) (x + 4);
        int spawnMidX = (int) (x + width / 2 - 2);
        int spawnRightX = (int) (x + width - 8);

//        System.out.printf("Left to Mid: %d, Mid to Right: %d", spawnMidX-spawnLeftX, spawnRightX-spawnMidX);

        int spawnY = (int) (y + height * 2 / 3);

        int power = VariableHandler.power.getValue();

        // tempSolution
        double correctionFactor = .715;
        double shootAngleLeft = NORTH - shootAngle*correctionFactor;
        double shootAngleRight = NORTH + shootAngle;

        if (power == 5) {
            spawnBullet(spawnRightX, spawnY, shootAngleRight);
            spawnBullet(spawnRightX, spawnY, NORTH);
            spawnBullet(spawnMidX, (int) y, NORTH);
            spawnBullet(spawnLeftX, spawnY, NORTH);
            spawnBullet(spawnLeftX, spawnY, shootAngleLeft);
        }
        else if (power == 4) {
            spawnBullet(spawnRightX, spawnY, shootAngleRight);
            spawnBullet(spawnRightX, spawnY, NORTH);
            spawnBullet(spawnLeftX, spawnY, NORTH);
            spawnBullet(spawnLeftX, spawnY, shootAngleLeft);
        } else if (power == 3) {
            spawnBullet(spawnRightX, spawnY, shootAngleRight);
            spawnBullet(spawnMidX, (int) y, NORTH);
            spawnBullet(spawnLeftX, spawnY, shootAngleLeft);
        } else if (power == 2) {
            spawnBullet(spawnRightX, spawnY, NORTH);
            spawnBullet(spawnLeftX, spawnY, NORTH);
        } else {
            spawnBullet(spawnMidX, (int) y, NORTH);
        }
        // reset shoot timer to default
        shootTimer = shootTimerDefault;
    }

    private void spawnBullet(int x, int y, double dir) {
        if (airBullet) {
            if (bullets.getPoolSize() > 0) {
                bullets.spawnFromPool(x, y, dir);
            }
            else
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
//            if (velX > 0) {
//                velX -= frictionalForce;
//            } else if (velX < 0) {
//                velX += frictionalForce;
//            }
            velX = 0;
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
//            if (velY > 0) {
//                velY -= frictionalForce;
//            }
//            if (velY < 0) {
//                velY += frictionalForce;
//            }
            velY = 0;
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

    // Bullet Functions

    public void increaseBullets() {
        bullets.increase();
    }

    @Override
    public void clear() {
        bullets.clear();
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

    public void resetMovement() {
        moveRight = false;
        moveLeft = false;
        moveDown = false;
        moveUp = false;
    }

//    private double getCenterX() {
//        return (x + width / 2 - 2);
//    }

    private double getCenterY() {
        return (y + height / 2 - 2);
    }
}
