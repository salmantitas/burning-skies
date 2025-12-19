package com.euhedral.Game.Entities;

import com.euhedral.Engine.*;
import com.euhedral.Game.*;
import com.euhedral.Game.Entities.Enemy.Enemy;
import com.euhedral.Game.Entities.Projectile.Bullet;
import com.euhedral.Game.Pool.BulletPool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Player extends Airplane {

    // Shooting Entity
    private boolean canShoot;
    private BulletPool bullets;
//    private MissilePool missiles;
    int turretY;
    int turretMidX, turretLeftX, turretRightX;

    private int shootTimerMissile = 0;
    private int shootTimerDefaultMissile;
    private double missileVelocity;

    // Personal
    private int levelHeight;
    private int clampOffsetX;
    private int clampOffsetY;
    private int shootAngle = 5;
    private double shootAngleLeft = NORTH - shootAngle;
    private double shootAngleRight = NORTH + shootAngle;

    private Attribute health, shield_A, firepower;

    private ShieldPlayer shield;
    private Pulse pulse;
    private int homingAngle = 180;

    // Test
    private int mx, my;
    private boolean destinationGiven = false;


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

        bulletVelocity = 12;
        missileVelocity = 6;

        bullets = new BulletPool();
//        missiles = new MissilePool();
//        for (int i = 0; i < 360; i ++)
//            bullets.spawn(-1, -1, 90);

        velX = 0;
        velY = 0;
        physics.acceleration = 0.1;

        velY_MIN = 5;
        velX_MIN = 5;
        velY_MAX = 10;
        velX_MAX = 10;

        jitter_MAX = Utility.intAtWidth640(2);

        shield = new ShieldPlayer(shield_A);
        pulse = new Pulse();


//        physics.friction = 1; // instantenous is equal to (minVel - 2)

        resetMovement();

        clampOffsetX = -5 * width / 4;
        clampOffsetY = height - 20;

        shootTimerDefault = 12;
        shootTimerDefaultMissile = shootTimerDefault * 2;

        reflection = new Reflection();

//        debug = true;
    }

//    public Player(int x, int y, int levelHeight, BufferedImage image) {
//        this(x, y, levelHeight);
//        this.image = image;
//    }

    public void update() {
//        System.out.println("Player at (" + x + ", " + y + ")");
        super.update();
        updateShootTimer();

        if (canShoot) {
            calculateTurretPositions();
            if (shootTimer <= 0) {
                shoot();
            }
//            if (shootTimerMissile <= 0) {
//                shootMissiles();
//            }
        }

        updateProjectiles();

        setImage();

        jitter();

        shield.update();

        if (VariableHandler.shield.getValue() > 0) {
            VariableHandler.shield.decrease(0.5); // 1 too high
        }

        pulse.update(bulletVelocity);
        homingAngle++;
    }

    private void shootMissiles() {
//        missiles.spawn(turretMidX, turretY, missileVelocity);

        // reset shoot timer to default
        shootTimerMissile = shootTimerDefaultMissile - (int) (firepower.getValue() - 1) % 5;
    }

    @Override
    protected void updateShootTimer() {
        shootTimer--;
        shootTimerMissile--;
    }

    private void updateProjectiles() {
//        missiles.disableIfOutsideBounds(levelHeight);
//        missiles.update();
//        missiles.checkDeathAnimationEndPlayer();

        bullets.disableIfOutsideBounds(levelHeight);
        bullets.update();
        bullets.checkDeathAnimationEndPlayer();
//        bullets.printPool("Bullets");
    }

    @Override
    protected void updateBounds() {
        collisionBox.setBounds(
                0,
                pos.x + 16,
                pos.y + 2* height / 3 - 2 ,
                width - 32,
                height / 3 - 6
        );
        collisionBox.setBounds(
                1,
                pos.x + (width / 4) + 7,
                pos.y + 16,
                (2 * width) / 4 - 14,
                height - 24
        );
    }

    private void setAttributes() {
        health = VariableHandler.health;
        shield_A = VariableHandler.shield;
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

        VariableHandler.setHealthColor();
    }

    @Override
    public void render(Graphics g) {
        renderProjectiles(g);

        g2d = (Graphics2D) g;

        super.render(g);
        float transparency = (1f - (float) health.getValue() / 100) / 2;

        g2d.setComposite(Utility.makeTransparent(transparency));
        renderDamageImage();
        g2d.setComposite(Utility.makeTransparent(1f));

//        g2d.setComposite(Utility.makeTransparent(0.2f));
//        g2d.setColor(Color.GRAY);
//        int maxLength = 32;
//        double shootTimerMissileProgressRatio = Math.min(1, ((shootTimerDefaultMissile - shootTimerMissile) / (double) shootTimerDefaultMissile));
//        System.out.println(shootTimerMissileProgressRatio);
//        int meterHeight = (int) (maxLength * shootTimerMissileProgressRatio);
//        g.fillRect(pos.intX() + width/2 - 3, pos.intY() + height/2 - 8, 8, meterHeight );
//        g2d.setComposite(Utility.makeTransparent(1f));

        shield.render(g2d, pos, width, height);
        pulse.render(g2d, pos, width, height, bulletVelocity);
//        renderHoming(g2d);

//        renderStats(g);
    }

    private void renderProjectiles(Graphics g) {
        bullets.render(g);
//        missiles.render(g);
    }

    private void renderHoming(Graphics2D g2d) {
        if (VariableHandler.homing) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(pos.intX(), pos.intY(), width, height, homingAngle, 30);
        }
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
        renderProjectilesReflections(g2d, transparency);

        reflection.render(g2d, image, pos.x + (jitter_MULT * jitter), getCenterX(), pos.y + (jitter_MULT * jitter), getCenterY(), width, height, transparency);
    }

    private void renderProjectilesReflections(Graphics2D g2d, float transparency) {
        bullets.renderReflections(g2d, transparency);
//        missiles.renderReflections(g2d, transparency);

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
//        Bullet colliding = missiles.checkCollision(enemy);
//        if (colliding != null) {
//            return colliding;
//        }
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


    public boolean checkCollision(Enemy enemy) {
        return collisionBox.checkCollision(enemy);
    }

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

    @Override
    protected void shoot() {
        // Bullet Spawn Points
        // todo: positioning adjustment of bullet spawn point
        int offset = 12;
        turretRightX = (int) (pos.x + width - (8 + offset));
        turretLeftX = (int) (pos.x + offset);

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

        offset = 6;

        if (firepower.getValue() <= 5) {
            bullets.spawn(turretMidX, turretY, bulletVelocity, NORTH);
//            bullets.spawn(turretLeftX + offset, turretY, bulletVelocity, NORTH);
        }

        if (firepower.getValue() > 5) {
            bullets.spawn(turretRightX - offset, turretY, bulletVelocity, NORTH);
            bullets.spawn(turretLeftX + offset, turretY, bulletVelocity, NORTH);
        }


        if (firepower.getValue() > 10) {
            bullets.spawn(turretRightX, turretY, bulletVelocity, shootAngleRight);
            bullets.spawn(turretLeftX, turretY, bulletVelocity, shootAngleLeft);
        }

        offset = 6;

        if (firepower.getValue() > 15) {
            bullets.spawn(turretRightX + offset * 1, turretY, bulletVelocity, shootAngleRight);
            bullets.spawn(turretLeftX - offset * 1, turretY, bulletVelocity, shootAngleLeft);
        }

        if (firepower.getValue() > 20) {
            bullets.spawn(turretMidX, turretY, bulletVelocity, NORTH);
//            bullets.spawn(turretRightX + offset * 2, turretY, bulletVelocity, shootAngleRight);
//            bullets.spawn(turretLeftX - offset * 2, turretY, bulletVelocity, shootAngleLeft);
        }

        // reset shoot timer to default
        shootTimer = shootTimerDefault - (int) (firepower.getValue() - 1) % 5;
    }

    public void special() {
//        VariableHandler.pulse = true; // todo: Test Only

        if (VariableHandler.pulse) {
            pulse();
            SoundHandler.playSound(SoundHandler.PULSE);
        }
    }

    private void pulse() {

        if (VariableHandler.pulse) {
            pulse.radius = 0;
            VariableHandler.pulse = false;
        }
    }

    private void calculateTurretPositions() {
        turretMidX = (int) (pos.x + width / 2 - 4);
        turretY = (int) (pos.y + height * 2 / 3 - velY) ;
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

    public void damage(double damage) {
        if (shield_A.getValue() > 0) {
//            double temp = damage - shield.getValue();
//            shield.decrease(damage * 2);
//            if (temp > 0) {
//                damageHealth(temp);
//            }
//            shieldTimer = shieldTimer_MAX;
            SoundHandler.playSound(SoundHandler.SHIELD_2);
        } else {
            damageHealth(damage);
        }
    }

    private void damageHealth(double damage) {
        health.decrease(damage);
        if (Difficulty.isFirePowerLoss()) {
            if (firepower.getValue() > 0) {
                firepower.decrease(1);
            }
        }
        jitter = jitter_MAX;

        GameController.screenShake = 5;
        StatePlay.resetKillstreak();
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
        return pulse.radius;
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
