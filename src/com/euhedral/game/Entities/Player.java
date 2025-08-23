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
    private final int shootTimerDefault = 12;
    private BulletPool bullets;
    int turretY;
    int turretMidX, turretLeftX, turretRightX;
    int bulletVelocity;

    // Personal
    private int levelHeight;
    //    private int power;
    //    private boolean ground = false;
//    private boolean airBullet = true;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = Utility.intAtWidth640(5);

    private Attribute health, shield, firepower;
//    private int shootRateBoost;

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
    private BufferedImage damageImage;

    int jitter = 0, jitter_MULT = 1, jitter_MAX;

    int shieldTimer = 0;
    int shieldTimer_MAX = 30;

    int pulseRadius;
    int pulseRadius_MAX = Engine.WIDTH;

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

        bulletVelocity = Utility.intAtWidth640(6);

        bullets = new BulletPool();
//        for (int i = 0; i < 360; i ++)
//            bullets.spawn(-1, -1, 90);

        velX = 0;
        velY = 0;
        physics.acceleration = 0.1;
//        shootRateBoost = 0;
//        VariableHandler.shootRateBoostDuration = 0;
        velY_MIN = 4;
        velX_MIN = 4;
        velY_MAX = 10;
        velX_MAX = 9;

        jitter_MAX = Utility.intAtWidth640(2);

        pulseRadius = -1;

//        physics.friction = 1; // instantenous is equal to (minVel - 2)

        resetMovement();

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

        bullets.disableIfOutsideBounds(levelHeight);
        bullets.update();
        bullets.checkDeathAnimationEndPlayer();
//        bullets.printPool("Bullets");

        setImage();

        if (jitter > 0) {
            jitter--;
            jitter_MULT *= -1;
        }

        if (shieldTimer > 0) {
            shieldTimer--;
        }

//        if (VariableHandler.shootRateBoostDuration > 0) {
//            shootRateBoost = 4;
//            VariableHandler.shootRateBoostDuration--;
//        } else {
//            shootRateBoost = 0;
//        }

        if (pulseRadius > -1) {

            pulseRadius += pulseRadius / 10 + bulletVelocity;

            if (pulseRadius >= pulseRadius_MAX) {
                pulseRadius = -1;
            }
        }
    }

    private void setAttributes() {
        health = VariableHandler.health;
        shield = VariableHandler.shield;
//        shield.setValue(100);
//        power = VariableHandler.firepower;
        firepower = VariableHandler.firepower;
    }

    private void setImage() {
//        if (health.getValue() > HEALTH_HIGH) {
        if (isMovingLeft()) {
            image = textureHandler.player[6];
            damageImage = textureHandler.playerDamage[1];
        } else if (isMovingRight()) {
            image = textureHandler.player[3];
            damageImage = textureHandler.playerDamage[2];
        } else {
            image = textureHandler.player[0];
            damageImage = textureHandler.playerDamage[0];
        }
//        }

//        else if (health.getValue() > HEALTH_MED) {
//            if (isMovingLeft()) {
//                image = textureHandler.player[7];
//            } else if (isMovingRight()) {
//                image = textureHandler.player[4];
//            } else
//                image = textureHandler.player[1];
//        } else {
//            if (isMovingLeft()) {
//                image = textureHandler.player[8];
//            } else if (isMovingRight()) {
//                image = textureHandler.player[5];
//            } else
//                image = textureHandler.player[2];
//        }
        VariableHandler.setHealthColor();
    }

    @Override
    public void render(Graphics g) {
        bullets.render(g);

        // Render Speed Boost
        g2d = (Graphics2D) g;
//        if (VariableHandler.speedBoostDuration > 0) {
//
//
//            g.setColor(Color.ORANGE);
//            g2d.setComposite(Utility.makeTransparent(0.2f));
//            if (isMovingLeft()) {
//                g.fillRect((int) pos.x + width / 2, (int) pos.y, width / 2, height);
//            }
//            if (isMovingRight()) {
//                g.fillRect((int) pos.x, (int) pos.y, width / 2, height);
//            }
//            if (isMovingUp()) {
//                g.fillRect((int) pos.x, (int) pos.y + height / 2, width, height / 2);
//            }
//            if (isMovingDown()) {
//                g.fillRect((int) pos.x, (int) pos.y, width, height / 2);
//            }
//            g2d.setComposite(Utility.makeTransparent(1f));
//        }

        super.render(g);
        float transparency = (1f - (float) health.getValue() / 100) / 2;

        g2d.setComposite(Utility.makeTransparent(transparency));
        g.drawImage(damageImage, (int) pos.x, (int) pos.y, null);
        g2d.setComposite(Utility.makeTransparent(1f));

        // Render Shield
        int shieldOffset = 8;
        if (shield.getValue() > 0) {
            float shieldTransparency = 0.2f;
            if (shieldTimer > 0) {
                shieldTransparency *= (float) Math.pow(shieldTimer_MAX / 2 - shieldTimer, 2) / (shieldTimer_MAX * 15);
            }
            g2d.setColor(Color.blue);
            g2d.setComposite(Utility.makeTransparent(shieldTransparency));
            g2d.fillOval((int) pos.x - width / shieldOffset, (int) pos.y - height / shieldOffset + 5, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
            g2d.setComposite(Utility.makeTransparent(1f));
            g2d.setStroke(new BasicStroke(3, 1, 1));
            g2d.drawOval((int) pos.x - width / shieldOffset, (int) pos.y - height / shieldOffset + 5, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
        }

        // Render Ring Of Fire
        if (pulseRadius > -1) {
            g.setColor(Color.YELLOW);

            g2d.setComposite(Utility.makeTransparent(0.f));
            g.fillOval((int) pos.x - pulseRadius, (int) pos.y - pulseRadius, width + pulseRadius * 2, height + pulseRadius * 2);
            g2d.setComposite(Utility.makeTransparent(1f));

            g2d.setStroke(new BasicStroke(pulseRadius / (bulletVelocity * 3)));
            g.drawOval((int) pos.x - pulseRadius, (int) pos.y - pulseRadius, width + pulseRadius * 2, height + pulseRadius * 2);
        }

//        renderStats(g);
//        renderBounds(g);
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image) {
        g.drawImage(image, (int) pos.x + (jitter_MULT * jitter), (int) pos.y + (jitter_MULT * jitter), null);
    }

    public void renderShadow(Graphics2D g2d) {
        g2d.setComposite(Utility.makeTransparent(0.4f));

        int sizeOffset = 10;
        int xCorrection = 8;
        int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
        int offsetY = 10 + (int) pos.y / 500;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(xCorrection + (int) pos.x - offsetX, (int) pos.y - offsetY, width - sizeOffset, height - sizeOffset);

        g2d.setComposite(Utility.makeTransparent(1f));
    }

    public void renderReflection(Graphics2D g2d, float transparency) {
        renderBulletReflections(g2d, transparency);

        reflection.render(g2d, image, pos.x + (jitter_MULT * jitter), getCenterX(), pos.y + (jitter_MULT * jitter), getCenterY(), width, height, transparency);
    }

    private void renderBulletReflections(Graphics2D g2d, float transparency) {
        bullets.renderReflections(g2d, transparency);

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
        boundsHorizontal.setRect(pos.x + 1, pos.y + 2 * height / 3 - 2, width - 3, height / 3 - 6);
        return boundsHorizontal;
    }

    public Rectangle2D getBoundsVertical() {
        boundsVertical.setRect(pos.x + (width / 4) + 1, pos.y - 1, (2 * width) / 4 - 3, height - 1);
        return boundsVertical;
    }

//    public void setX(int x) {
//        super.setX(x);
////        this.x = x;
//    }

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
//        mouseMove();
        keyboardMove();

    }

    private void shoot() {
        // Bullet Spawn Points
        // todo: positioning adjustment of bullet spawn point
        calculateTurretPositions();
        turretRightX = (int) (pos.x + width - 8);
        turretLeftX = (int) (pos.x + 4);

//        VariableHandler.homing = true; // todo: Test Only
        if (VariableHandler.homing) {
            VariableHandler.homing = false;

            LinkedList<Entity> enemies = EntityHandler.getEnemyList();
            for (Entity entity : enemies) {
                if (entity.isActive()) {
                    Enemy enemy = (Enemy) entity;
                    if (enemy.isBelowDeadZoneTop()) {
                        bullets.spawn(turretMidX, turretY, bulletVelocity, enemy);
                    }
                }
            }
        }

//        System.out.printf("Left to Mid: %d, Mid to Right: %d", spawnMidX-spawnLeftX, spawnRightX-spawnMidX);


//        Utility.log("VelY:" + velY);

//        power = VariableHandler.power.getValue();
//
//        // tempSolution
//        double correctionFactor = .715;
        double shootAngleLeft = NORTH - shootAngle;
        double shootAngleRight = NORTH + shootAngle;

        if (firepower.getValue() <= 5) {
            bullets.spawn(turretMidX, turretY, bulletVelocity, NORTH);
        } else if (firepower.getValue() <= 10) {
            bullets.spawn(turretRightX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, NORTH);
        } else if (firepower.getValue() <= 15) {
            bullets.spawn(turretRightX, turretY, bulletVelocity, shootAngleRight);
            bullets.spawn(turretMidX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, shootAngleLeft);
        } else if (firepower.getValue() <= 20) {
            bullets.spawn(turretRightX, turretY, bulletVelocity, shootAngleRight);
            bullets.spawn(turretRightX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, shootAngleLeft);
        } else {
            bullets.spawn(turretRightX, turretY, bulletVelocity, shootAngleRight);
            bullets.spawn(turretRightX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretMidX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, shootAngleLeft);
        }

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

//        } else
        if (firepower.getValue() <= 2) {

        } else {

        }

        // reset shoot timer to default
        shootTimer = shootTimerDefault - (firepower.getValue() - 1) % 5;
    }

    public void special() {
//        VariableHandler.pulse = true; // todo: Test Only

        if (VariableHandler.pulse) {
            pulse();
            SoundHandler.playSound(SoundHandler.RING);
        }
    }

    private void pulse() {

        if (VariableHandler.pulse) {
            pulseRadius = 0;
            VariableHandler.pulse = false;
        }
    }

    private void calculateTurretPositions() {
        turretMidX = (int) (pos.x + width / 2 - 2);
        turretY = (int) (pos.y + height * 2 / 3 - velY);
    }

    private void keyboardMove() {

        if (isMovingLeft()) {
            if (pos.x < VariableHandler.deadzoneWidth) {
                velX += Math.abs(velX) / 4;
                velX = Utility.clamp(velX, -velX_MAX, 0);
            } else {
                velX -= (physics.acceleration);
                velX = Utility.clamp(velX, -(velX_MAX), -(velX_MIN));
            }
//            Utility.log("VelX: " + velX);
        } else if (isMovingRight()) {
            if (pos.x > VariableHandler.deadzoneRightX - VariableHandler.deadzoneWidth) {
                velX -= Math.abs(velX) / 4;
                velX = Utility.clamp(velX, 0, velX_MAX);
            } else {
                velX += (physics.acceleration);
                velX = Utility.clamp(velX, (velX_MIN), (velX_MAX));
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
            if (pos.y < levelHeight - 600) {
                velY += Math.abs(velY) / 4;
                velY = Utility.clamp(velY, -velY_MAX, 0);
            } else {
                velY -= (physics.acceleration);
                velY = Utility.clamp(velY, -(velY_MAX), -velY_MIN);
            }
        } else if (isMovingDown()) {
            if (pos.y > levelHeight + clampOffsetY) {
                velY -= Math.abs(velY) / 4;
                velY = Utility.clamp(velY, 0, velY_MAX);
            } else {
                velY += (physics.acceleration);
                velY = Utility.clamp(velY, velY_MIN, (velY_MAX));
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
            if (mx == pos.y && my == pos.y) {
                destinationGiven = false;
            }
            if (Math.abs(mx - pos.x) < positionOffset) {
                moveLeft = false;
                moveRight = false;
            } else if (mx > pos.x) {
                moveLeft = false;
                moveRight = true;
            } else if (mx < pos.x) {
                moveLeft = true;
                moveRight = false;
            }
            if (Math.abs(my - pos.y) < positionOffset) {
                moveUp = false;
                moveDown = false;
            } else if (my > pos.y) {
                moveUp = false;
                moveDown = true;
            } else if (my < pos.y) {
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
            shieldTimer = shieldTimer_MAX;
            SoundHandler.playSound(SoundHandler.SHIELD_2);
        } else {
            damageHealth(damage);
        }
    }

    private void damageHealth(int damage) {
        health.decrease(damage);
        if (firepower.getValue() > 0) {
            firepower.decrease(1);
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

    public void resetMovement() {
        moveRight = false;
        moveLeft = false;
        moveDown = false;
        moveUp = false;
    }

    public LinkedList<Bullet> getImpactingBulletsList() {
        return bullets.getImpactingBulletsList();
    }

    public double getCenterX() {
        return pos.x + width / 2 - 9; // todo: find out why we need 12
    }

    public double getCenterY() {
        return (pos.y + height / 2 - 2);
    }

    public int getRadius() {
        return pulseRadius;
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
