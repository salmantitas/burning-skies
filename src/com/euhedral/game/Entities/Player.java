package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends MobileEntity {

    // Shooting Entity
    private boolean canShoot;
    private int shootTimer = 0;
    private final int shootTimerDefault = 9;
    private BulletPool bullets = new BulletPool();
    int turretY;
    int turretMidX, turretLeftX, turretRightX;

    // Personal
    private int levelHeight;
//    private int power;
    //    private boolean ground = false;
//    private boolean airBullet = true;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = Utility.intAtWidth640(5);

    private Attribute health;
    private Attribute shield;
    private Attribute power;

    private final int HEALTH_HIGH = 66;
    private final int HEALTH_MED = 33;

    // Bounds
    Rectangle2D boundsVertical;
    Rectangle2D boundsHorizontal;

    // Collisions
    boolean collidesVertically;
    boolean collidesHorizontally;

    // Test
    private int mx, my;
    private boolean destinationGiven = false;

    // Graphics
    Graphics2D g2d;
    private TextureHandler textureHandler;

    int jitter = 0, jitter_MULT = 1, jitter_MAX;

    private Reflection reflection;

    public Player(int x, int y, int levelHeight) {
        super(x, y, EntityID.Player);
        textureHandler = GameController.getTexture();
        setAttributes();
        setImage();
        this.levelHeight = levelHeight;
        width = Utility.intAtWidth640(32);
        height = width;
        color = Color.WHITE;

        velX = 0;
        velY = 0;
        physics.acceleration = 0.1;
        velY_MIN = 4;
        velX_MIN = 4;
        velY_MAX = 10;
        velX_MAX = 9;

        jitter_MAX = Utility.intAtWidth640(2);

//        physics.friction = 1; // instantenous is equal to (minVel - 2)

        resetMovement();

//        this.power = 1;

        boundsVertical = new Rectangle2D.Double();
        boundsHorizontal = new Rectangle2D.Double();

        clampOffsetX = -5 * width / 4;
        clampOffsetY = height - 20;

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

        if (jitter > 0) {
            jitter--;
            jitter_MULT *= -1;
        }
    }

    private void setAttributes() {
        health = VariableHandler.health;
        shield = VariableHandler.shield;
        power = VariableHandler.power;
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
            g.drawOval((int) x - width / 2, (int) y - height / 2, width * 2, height * 2);
        }

//        renderStats(g);
//        renderBounds(g);
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image) {
        g.drawImage(image, (int) x + (jitter_MULT * jitter), (int) y + (jitter_MULT * jitter), null);
    }

    public void renderShadow(Graphics2D g2d) {
        g2d.setComposite(Utility.makeTransparent(0.4f));

        int sizeOffset = 10;
        int xCorrection = 8;
        int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
        int offsetY = 10 + (int) y / 500;
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
        reflection.render(g2d, image, x + (jitter_MULT * jitter), getCenterX(), y + (jitter_MULT * jitter), getCenterY(), width, height, transparency);

//        g2d.setComposite(Utility.makeTransparent(1f));
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        bullets.renderReflections(g2d, transparency);
//        LinkedList<Entity> list = bullets.getEntities();
//        for (Entity entity : list) {
//            Bullet bullet = (Bullet) entity;
//            bullet.renderReflection(g2d, transparency);
//        }
    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        boundsVertical.setRect(getBoundsVertical());
        boundsHorizontal.setRect(getBoundsHorizontal());
        g2d = (Graphics2D) g;

        g2d.draw(boundsVertical);
        g2d.draw(boundsHorizontal);
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
//        Bullet bullet = null;
//        for (Entity entity : bullets.getEntities()) {
//            BulletPlayer bulletPlayer = (BulletPlayer) entity;
//            boolean intersectsEnemy = bulletPlayer.getBounds().intersects(enemy.getBoundsHorizontal()) || bulletPlayer.getBounds().intersects(enemy.getBoundsVertical());
//            if (bulletPlayer.isActive() && intersectsEnemy)
////                    && (bulletPlayer.getContactId() == enemy.getContactId() || bulletPlayer.getContactId() == ContactID.Air && enemy.getContactId() == ContactID.Boss))
//            {
//                bullet = bulletPlayer;
//            }
//        }
//        return bullet;
        return bullets.checkCollision(enemy);
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
        boundsVertical.setRect(getBoundsVertical());
        boundsHorizontal.setRect(getBoundsHorizontal());
        collidesVertically = object.intersects(boundsVertical);
        collidesHorizontally = object.intersects(boundsHorizontal);

        return collidesVertically || collidesHorizontally;
    }

    public boolean checkCollision(Enemy enemy) {
        boundsVertical.setRect(getBoundsVertical());
        boundsHorizontal.setRect(getBoundsHorizontal());
        collidesVertically = enemy.checkCollision(boundsVertical);
        collidesHorizontally = enemy.checkCollision(boundsHorizontal);

        return collidesVertically || collidesHorizontally;
    }

    public Rectangle2D getBoundsHorizontal() {
        boundsHorizontal.setRect(x + 1, y + 2 * height / 3 - 2, width - 3, height / 3 - 6);
        return boundsHorizontal;
    }

    public Rectangle2D getBoundsVertical() {
        boundsVertical.setRect(x + (width / 4) + 1, y - 1, (2 * width) / 4 - 3, height - 1);
        return boundsVertical;
    }

    public void setX(int x) {
        this.x = x;
    }

//    public void switchBullet() {
//        if (ground)
//            airBullet = !airBullet;
//    }

    // Private Methods

    // This function governs the movementTimer of the player
    @Override
    protected void move() {
        super.move();

//        System.out.printf("VelX: %f | VelY: %f\n", velX, velY);

        // Clamp
//        x = Utility.clamp(x, VariableHandler.deadzoneWidth, VariableHandler.deadzoneRightX - VariableHandler.deadzoneWidth);
//        y = Utility.clamp(y, levelHeight - 640, levelHeight + clampOffsetY);

        // movement
        mouseMove();
        keyboardMove();

    }

    private void shoot() {
        // Bullet Spawn Points
        // todo: positioning adjustment of bullet spawn point
        turretMidX = (int) (x + width / 2 - 2);
        turretY = (int) (y + height * 2 / 3 - velY);

        turretRightX = (int) (x + width - 8);
        turretLeftX = (int) (x + 4);

//        System.out.printf("Left to Mid: %d, Mid to Right: %d", spawnMidX-spawnLeftX, spawnRightX-spawnMidX);


//        Utility.log("VelY:" + velY);

//        power = VariableHandler.power.getValue();
//
//        // tempSolution
//        double correctionFactor = .715;
//        double shootAngleLeft = NORTH - shootAngle * correctionFactor;
//        double shootAngleRight = NORTH + shootAngle;

//        if (power == 5) {
//            spawnBullet(spawnRightX, turretY, shootAngleRight);
//            spawnBullet(spawnRightX, turretY, NORTH);
//            spawnBullet(spawnMidX, (int) y, NORTH);
//            spawnBullet(spawnLeftX, turretY, NORTH);
//            spawnBullet(spawnLeftX, turretY, shootAngleLeft);
//        } else if (power == 4) {
//            spawnBullet(spawnRightX, turretY, shootAngleRight);
//            spawnBullet(spawnRightX, turretY, NORTH);
//            spawnBullet(spawnLeftX, turretY, NORTH);
//            spawnBullet(spawnLeftX, turretY, shootAngleLeft);
//        } else if (power == 3) {
//            spawnBullet(spawnRightX, turretY, shootAngleRight);
//            spawnBullet(spawnMidX, (int) y, NORTH);
//            spawnBullet(spawnLeftX, turretY, shootAngleLeft);
//        } else
        if (power.getValue() == 2) {
            spawnBullet(turretRightX, turretY, NORTH);
            spawnBullet(turretLeftX, turretY, NORTH);
        } else {
            spawnBullet(turretMidX, turretY, NORTH);
        }

        // reset shoot timer to default
        shootTimer = shootTimerDefault;
    }

    private void spawnBullet(int x, int y, double dir) {
//        if (bullets.getPoolSize() > 0) {
//            bullets.spawnFromPool(x, y, dir);
//        } else
//            bullets.add(new BulletPlayer(x, y, dir));
        bullets.spawn(x, y, dir);
//        bullets.printPool("Player Bullets");
    }

    private void keyboardMove() {

        if (isMovingLeft()) {
            if (x < VariableHandler.deadzoneWidth) {
                velX += Math.abs(velX) / 4;
                velX = Utility.clamp(velX, -velX_MAX, 0);
            } else {
                velX -= physics.acceleration;
                velX = Utility.clamp(velX, -velX_MAX, -velX_MIN);
            }
//            Utility.log("VelX: " + velX);
        }

        else if (isMovingRight()) {
            if (x > VariableHandler.deadzoneRightX - VariableHandler.deadzoneWidth) {
                velX -= Math.abs(velX) / 4;
                velX = Utility.clamp(velX, 0, velX_MAX);
            } else {
                velX += physics.acceleration;
                velX = Utility.clamp(velX, velX_MIN, velX_MAX);
            }
        }

        // Not Moving Left or Right
        else if ((!moveLeft && !moveRight) || (moveLeft && moveRight)) {
//            if (velX > 0) {
//                velX -= physics.friction;
//                velX = Utility.clamp(velX, 0, velX_MAX);
//            } else if (velX < 0) {
//                velX += physics.friction;
//                velX = Utility.clamp(velX, -velX_MAX, 0);
//            }
            velX = 0;
        }

//        y = Utility.clamp(y, levelHeight - 640, levelHeight + clampOffsetY);

        if (isMovingUp()) {
            if (y < levelHeight - 600) {
                velY += Math.abs(velY) / 4;
                velY = Utility.clamp(velY, -velY_MAX, 0);
            } else {
                velY -= physics.acceleration;
                velY = Utility.clamp(velY, -velY_MAX, -velY_MIN);
            }
        }

        else if (isMovingDown()) {
            if (y > levelHeight + clampOffsetY) {
                velY -= Math.abs(velY) / 4;
                velY = Utility.clamp(velY, 0, velY_MAX);
            } else {
                velY += physics.acceleration;
                velY = Utility.clamp(velY, velY_MIN, velY_MAX);
            }
        }

        // Not Moving Up or Down
        else if (!moveUp && !moveDown || (moveUp && moveDown)) {
//            if (velY > 0) {
//                velY -= physics.friction;
//                velY = Utility.clamp(velY, 0, velY_MAX);
//            } else if (velY < 0) {
//                velY += physics.friction;
//                velY = Utility.clamp(velY, -velY_MAX, 0);
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

//    public void setPower(int power) {
//        this.power = power;
//    }

    public int getMx() {
        return mx;
    }

    public int getMy() {
        return my;
    }

//    public int getPower() {
//        return power;
//    }

//    public void setGround(Boolean ground) {
//        this.ground = ground;
//    }

    public void damage(int damage) {
        if (shield.getValue() > 0) {
            int temp = damage - shield.getValue();
            shield.decrease(damage * 2);
            if (temp > 0) {
                damageHealth(temp);
            }
        } else {
            damageHealth(damage);
        }
    }

    private void damageHealth(int damage) {
        health.decrease(damage);
        if (power.getValue() > 1) {
            power.decrease(1);
        }
        jitter = jitter_MAX;
    }

    // Bullet Functions

    public void increaseBullets() {
        bullets.increase();
    }

    @Override
    public void clear() {
        canShoot(false);
        bullets.clear();
    }

    private void checkDeathAnimationEnd() {
        bullets.checkDeathAnimationEnd();
//        for (Entity entity : bullets.getEntities()) {
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

    public void resetMovement() {
        moveRight = false;
        moveLeft = false;
        moveDown = false;
        moveUp = false;
    }

    public double getCenterX() {
        return x + width / 2 - 9; // todo: find out why we need 12
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

    private boolean isMovingUp() {
        return (moveUp && !moveDown);
    }

    private boolean isMovingDown() {
        return (moveDown && !moveUp);
    }
}
