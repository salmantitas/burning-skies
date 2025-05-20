package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class Player extends MobileEntity {

    // Shooting Entity
    private boolean canShoot;
    private int shootTimer = 0;
    private final int shootTimerDefault = 7;
    private BulletPool bullets = new BulletPool();

    // Personal
    private int levelHeight;
    private int power;
    private boolean ground = false;
    private boolean airBullet = true;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = Utility.intAtWidth640(5);

    private Attribute health;
    private Attribute shield;

    private final int HEALTH_HIGH = 66;
    private final int HEALTH_MED = 33;

    // Test
    private int mx, my;
    private boolean destinationGiven = false;

    // Graphics
    private TextureHandler textureHandler;

    // Lazy Physics
    private float acceleration; //stub //todo:delete
    private float frictionalForce; //stub //todo:delete
    private Reflection reflection;

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
        minVelY = 5;
        minVelX = 4.5;
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

        reflection = new Reflection();
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
        if (health.getValue() > HEALTH_HIGH) {
            if (isMovingLeft()) {
                image = textureHandler.player[6];
            } else if (isMovingRight()) {
                image = textureHandler.player[3];
            } else
                image = textureHandler.player[0];
        } else if (health.getValue() > HEALTH_MED) {
            if (isMovingLeft()) {
                image = textureHandler.player[7];
            } else if (isMovingRight()) {
                image = textureHandler.player[4];
            } else
                image = textureHandler.player[1];
        } else {
            if (isMovingLeft()) {
                image = textureHandler.player[8];
            } else if (isMovingRight()) {
                image = textureHandler.player[5];
            } else
                image = textureHandler.player[2];
        }
        VariableHandler.setHealthColor();
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

    public void renderShadow(Graphics2D g2d) {
        g2d.setComposite(Utility.makeTransparent(0.4f));

        int sizeOffset = 10;
        int xCorrection = 8;
        int offsetX = (int) (Engine.WIDTH/2 - getCenterX()) / 15;
        int offsetY = 10 + (int) y/500;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(xCorrection + (int) x - offsetX, (int) y - offsetY, width - sizeOffset, height - sizeOffset);

        g2d.setComposite(Utility.makeTransparent(1f));
    }

    public void renderReflection(Graphics2D g2d, float transparency) {
        renderBulletReflections(g2d, transparency);

//        g2d.setComposite(Utility.makeTransparent(transparency));

//        int reflectionX = reflection.calculateReflectionX(x, getCenterX());
//        int reflectionY = reflection.calculateReflectionY(y, getCenterY());
//        int newWidth = (int) (width * reflection.sizeOffset);
//        int newHeight = (int) (height * reflection.sizeOffset);
//
//        g2d.drawImage(image, reflectionX, reflectionY, newWidth, newHeight, null);
        reflection.render(g2d, image, x, getCenterX(), y, getCenterY(), width, height, transparency);

//        g2d.setComposite(Utility.makeTransparent(1f));
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        LinkedList<Entity> list = bullets.getEntities();
        for (Entity entity : list) {
            Bullet bullet = (Bullet) entity;
            bullet.renderReflection(g2d, transparency);
        }
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

    public Bullet checkCollisionBullet(Enemy enemy) {
        Bullet bullet = null;
        for (Entity entity: bullets.getEntities()) {
            BulletPlayer bulletPlayer = (BulletPlayer) entity;
            boolean intersectsEnemy = bulletPlayer.getBounds().intersects(enemy.getBoundsHorizontal()) || bulletPlayer.getBounds().intersects(enemy.getBoundsVertical());
            if (bulletPlayer.isActive() && intersectsEnemy)
//                    && (bulletPlayer.getContactId() == enemy.getContactId() || bulletPlayer.getContactId() == ContactID.Air && enemy.getContactId() == ContactID.Boss))
            {
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

    public boolean checkCollision(Enemy enemy) {
        Rectangle2D rV = getBoundsVertical();
        Rectangle2D rH = getBoundsHorizontal();
        boolean collidesVertically = enemy.checkCollision(rV);
        boolean collidesHorizontally = enemy.checkCollision(rH);

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
        if (bullets.getPoolSize() > 0) {
            bullets.spawnFromPool(x, y, dir);
        } else
            bullets.add(new BulletPlayer(x, y, dir));
    }

    private void keyboardMove() {
        // Moving Left
        if (isMovingLeft()) {
            velX -= acceleration;
            velX = Utility.clamp(velX, - maxVelX, - minVelX);
        }

        // Moving Right
        else if (isMovingRight()) {
            velX += acceleration;
            velX = Utility.clamp(velX, minVelX, maxVelX);
        }

        // Not Moving Left or Right
        else if ((!moveLeft && !moveRight) || (moveLeft && moveRight)) {
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

    public double getCenterX() {
        return x + width/2 - 9; // todo: find out why we need 12
    }
    public double getCenterY() {
        return (y + height / 2 - 2);
    }

    private boolean isMovingLeft() {
        return (moveLeft && !moveRight);
    }

    private boolean isMovingRight() {
        return (!moveLeft && moveRight);
    }
}
