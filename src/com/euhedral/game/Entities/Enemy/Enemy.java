package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.*;
import com.euhedral.Game.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.euhedral.Game.Entities.Airplane;
import com.euhedral.Game.GameController;

/*
 *  Standard Enemies, flies downwards and shoots a missile at intervals
 * */
public class Enemy extends Airplane {
    protected int enemyType;

    protected int health;
    protected int health_MAX;

    protected double offscreenVelY;
    protected boolean moveLeft, moveRight;
//    protected Color color;
//    protected int shootTimerDefault;
    protected int shootTimerFirst = 50;
    protected boolean inscreenY = false;
//    protected boolean inscreenX = false;
    protected float cam;

    protected int score;
//    protected int distance;

    protected int movementDistance;
    protected int damage;
    protected double explodingVelocity = EntityHandler.backgroundScrollingSpeed;

    protected Animation explosion;

    protected int levelHeight;

    protected int bulletsPerShot_MAX = 1;
    protected int bulletsPerShot = 0;
    protected double bulletAngle;
    protected int bulletArcAngle;

    protected boolean attackEffect;
    double attackPathX;
    double attackPathY;

    // State Machine
    protected final int STATE_EXPLODING = 2;

    // Explosions
    int size;
    int expX;
    int expY;

    private Reflection reflection;
    int reflectionX, reflectionY, newWidth, newHeight;

    int disableOffset = 64 * 3;
    int bottomBounds;
    protected int UPDATES_PER_SECOND = 60;
    protected long spawnInterval = 3 * UPDATES_PER_SECOND;

    public Enemy(int x, int y, int levelHeight) {
        super(x, y, EntityID.Enemy);
        moveRight = false;
        moveLeft = false;
        cam = GameController.getCamera().getMarker();

        width = Utility.intAtWidth640(32);
        height = width;
        size = Math.max(width, height);
        jitter_MAX = Utility.intAtWidth640(2);
        color = Color.red;

        setLevelHeight(levelHeight);
        bottomBounds = levelHeight + disableOffset;

        score = 50;
        shootTimerDefault = 150;
        shootTimer = shootTimerFirst;

        bulletVelocity = Utility.intAtWidth640(2);
        bulletArcAngle = 15;

        textureHandler = GameController.getTexture();
        damageImage = textureHandler.enemyDamage[0];
        attackEffect = false;

//        boundsVertical = new Rectangle2D.Double();
//        boundsHorizontal = new Rectangle2D.Double();

        setEnemyType();
        forwardVelocity = EntityHandler.backgroundScrollingSpeed;
        offscreenVelY = forwardVelocity;

        bulletAngle = 90;

        explosion = GameController.getTexture().initExplosion(6);
        reflection = new Reflection();
        damage = 35;
        shootTimer = shootTimerDefault;
    }

    public Enemy(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void update() {
//        updateActive();
        if (state == STATE_ACTIVE) {
            super.update();
            updateShootTimer(); // todo: maybe move to when in screen
            if (!inscreenY) {
                inscreenY = pos.y > cam + Utility.percHeight(30);
            }
            if (inscreenY && isActive()) { // todo: potential redundant check for active
//                if (inscreenX) {
                    if (shootTimer <= 0) {
                        shoot();
                    }
//                }

                jitter();
            }
        } else if (state == STATE_EXPLODING) {
            explosion.runAnimation();
//            super.update();
            move();
//            Utility.log("Exploding");
        }
    }

    protected void updateActive() {
        if (isActive()) {

        }
    }

    protected void updateExploding() {
        if (isExploding()) {

        }
    }

    @Override
    public void move() {
        if (isActive()) {
            if (inscreenY) {
                moveInScreen();
            } else {
                pos.y += offscreenVelY;
            }
        } else if (isExploding()) {
            velY = 1.5f;
            pos.y += velY;
        }
    }

//    @Override
//    public void initialize() {
//
//    }

    @Override
    public void render(Graphics g) {
        if (isActive()) {
            renderAttackPath(g);
            super.render(g);
//            renderBounds(g);
//            if (inscreenX)
//                g.setColor(Color.green);
//            else g.setColor(Color.red);
//            g.drawString("InscreenX", (int) x, (int) y);
        } else {
            renderExplosion(g);
        }
        g.setColor(Color.RED);
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image) {
        g.drawImage(
                image,
                (int) pos.x + (jitter_MULT * jitter),
                (int) pos.y + (jitter_MULT * jitter),
                width,
                height,
                null);

        float transparency = (1f - (float) health/(float) health_MAX)/2;

        g2d = (Graphics2D) g;

        g2d.setComposite(Utility.makeTransparent(transparency));
        renderDamageImage();
        g2d.setComposite(Utility.makeTransparent(1f));

    }

    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            boolean secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g.setColor(Color.red); // todo: Redundancy?

                g2d = (Graphics2D) g;
                g.setColor(Color.RED);


                attackPathX = pos.x - (0.5) * (double) width;
                attackPathY = getTurretY() - (0.5) * (double) height;
//                double drawY = y - (0.5) * (double) height;

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(calculateShotTrajectory()) - bulletArcAngle / 2, bulletArcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    protected void renderExplosion(Graphics g) {
        if (!explosion.playedOnce) {
            size = Math.max(width, height);
            expX = (int) pos.x + (size - width) / 2;
            expY = (int) pos.y - (size - height) / 2;
            explosion.drawAnimation(g, expX, expY, size, size);

            g2d = (Graphics2D)  g;
            g2d.setComposite(Utility.makeTransparent(1 - explosion.getProgress()));
            renderScore(g);
            g2d.setComposite(Utility.makeTransparent(1f));
        }
    }

    public void renderShadow(Graphics g) {
        if (state != STATE_INACTIVE) {
            g2d = (Graphics2D) g;
            g2d.setComposite(Utility.makeTransparent(0.5f));

            int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
            int sizeOffset = 10;
            int offsetY = 10 + (int) pos.y / 500;
            g.setColor(Color.DARK_GRAY);
            g.fillRect((int) pos.x - offsetX, (int) pos.y + offsetY, width - sizeOffset, height - sizeOffset);

            g2d.setComposite(Utility.makeTransparent(1f));
        }
    }

    // todo: Use render function in Reflection Class
    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));

        reflectionX = reflection.calculateReflectionX(pos.x, getCenterX());
        reflectionY = reflection.calculateReflectionY(pos.y, getCenterY());
        newWidth = (int) (width * reflection.sizeOffset);
        newHeight = (int) (height * reflection.sizeOffset);

        if (state == STATE_ACTIVE) {
            g2d.drawImage(image, reflectionX + (jitter_MULT * jitter), reflectionY + (jitter_MULT * jitter), newWidth, newHeight, null);
        } else if (state == STATE_EXPLODING) {
            explosion.drawAnimation(g2d, reflectionX, reflectionY, newWidth, newHeight);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    @Override
    protected void shoot() {
        resetShootTimer();
        shootDefault();
    }

    protected void shootDefault() {
        bulletsPerShot++;
    }

    protected void moveInScreen() {
        pos.y += velY;
        pos.x += velX;
    }

    public void damage(int damageValue) {
        this.health -= damageValue;
        jitter = jitter_MAX;
    }

    public int getHealth() {
        return health;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    protected void setHealth(int min, int max) {
        health = Utility.randomRange(min, max);
    }

    public boolean isInscreenY() {
        return inscreenY;
    }

    public boolean isBelowDeadZoneTop() {
        int offset = 25;
        return pos.y > VariableHandler.deadzoneTop + offset;
    }

    public void setInscreenY(boolean inscreenY) {
        this.inscreenY = inscreenY;
    }

    protected void resetShootTimer() {
        shootTimer = shootTimerDefault;
    }

//    public ContactID getContactId() {
//        return contactId;
//    }

    public int getScore() {
        return (int) (score * Difficulty.getScoreMultiplier());
    }

    public void setHMove(int direction) {
        if (direction == 1) {
            hMove = HorizontalMovement.LEFT;
            velX = -velX_MIN;
        } else if (direction == -1) {
            hMove = HorizontalMovement.RIGHT;
            velX = velX_MIN;
        } else {
//            velX = 0;
        }
    }

    protected void setHMove(HorizontalMovement hMove) {
        if (hMove == HorizontalMovement.LEFT)
            velX = -velX_MIN;
        else if (hMove == HorizontalMovement.RIGHT)
            velX = velX_MIN;

    }

    public void setLevelHeight(int levelHeight) {
        this.levelHeight = levelHeight;
    }

    public void destroy() {
        state = STATE_EXPLODING;
        velX = 0;
        SoundHandler.playSound(SoundHandler.EXPLOSION);
    }

//    public boolean isAlive() {
//        return state == STATE_ACTIVE;
//    }

    public boolean inRadius(double x, double y, int radius) {
        double aX = pos.x;
        double aY = pos.y;
        if (pos.x < x)
            aX = pos.x + width;
        if (pos.y < y)
            aY = pos.y + height;
        return Math.abs(calculateMagnitude(aX, aY, x,y)) < radius;
    }

    @Override
    public void resurrect(double x, double y) {
        super.resurrect(x, y);
        shootTimer = shootTimerFirst;
        commonInit();
        explosion.playedOnce = false;
        explosion.endAnimation();
        inscreenY = false;
        jitter = 0;
        jitter_MULT = 1;
    }

    public boolean checkDeathAnimationEnd() {
        return explosion.playedOnce;
    }

    protected void setEnemyType() {

    }

    public void setMovementDistance(int distance) {
        this.movementDistance = distance;
    }

    @Override
    public boolean canDisable() {
        return getY() > bottomBounds;
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void clear() {
        super.clear();
        explosion.endAnimation();
//        explosion.playedOnce = false;
    }

    public boolean hasShot() {
//        if (inscreenX)
            return bulletsPerShot > 0;
//        else return false;
    }

    public void decrementShot() {
        bulletsPerShot--;
    }

    public int getTurretX() {
        return ((int) pos.x + width / 2);
    }

    public int getTurretY() {
        return (int) pos.y + Utility.intAtWidth640(2);
    }

    public double getBulletVelocity() {
        return bulletVelocity;
    }

    public double getBulletAngle() {
        return bulletAngle;
    }

    public double calculateShotTrajectory() {
        return getBulletAngle();
    }

    public boolean isExploding() {
        return state == STATE_EXPLODING;
    }

    public int getDamage() {
        return damage;
    }

    // todo: Find better name
    protected void commonInit() {
        setHealth(health_MAX);
        velX = 0;
        velY = explodingVelocity;
        jitter = 0;
    }

    public int getEnemyType() {
        return enemyType;
    }

    // Private Methods

    private double getCenterY() {
        return (pos.y + height / 2 + 2);
    }

    protected void renderScore(Graphics g) {
        if (isExploding()) {
            g.setFont(VariableHandler.customFont.deriveFont(1, 20));
            g.setColor(VariableHandler.scoreColor);
            int offsetX = width / 2 - Utility.intAtWidth640(10);
            double mult = Difficulty.getScoreMultiplier();
            g.drawString(Integer.toString((int) (score * mult)), (int) pos.x + offsetX, (int) pos.y);
        }
    }

    public long getSpawnInterval() {
        return spawnInterval;
    }
}
