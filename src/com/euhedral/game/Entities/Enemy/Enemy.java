package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.*;
import com.euhedral.game.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import com.euhedral.game.GameController;

/*
 *  Standard Enemies, flies downwards and shoots a missile at intervals
 * */
public class Enemy extends MobileEntity {

    protected int health;
    protected int power = 1;
    protected int enemyType;
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
//    protected int shotNum = 0;
    protected int movementDistance;
    protected int damage;
    protected double explodingVelocity = 1.5f;

    protected TextureHandler textureHandler;
    protected Animation explosion;

    protected int levelHeight;
//    protected boolean alive = true;

    protected int shot = 0;
    protected int bulletVelocity;
    protected double bulletAngle;

    // todo: Experimental
    protected boolean attackEffect;

    // State Machine
    protected final int STATE_EXPLODING = 2;

    private Reflection reflection;

    public Enemy(int x, int y, int levelHeight) {
        super(x, y, EntityID.Enemy);
        enemyType = EntityHandler.TYPE_BASIC;
        offscreenVelY = velY;
        moveRight = false;
        moveLeft = false;
        cam = GameController.getCamera().getMarker();
//        power = 1; // todo: reconsider purpose
        width = Utility.intAtWidth640(32);
        height = width;
        color = Color.red;
//        r = new Random();
        setLevelHeight(levelHeight);
        bulletVelocity = Utility.intAtWidth640(5);
        bulletAngle = 90;
        textureHandler = GameController.getTexture();
        attackEffect = false;
        initialize();
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
            shootTimer--;
            if (!inscreen) {
                inscreen = y > cam + Utility.percHeight(30);
            }
            if (inscreen && isActive()) {
                if (shootTimer <= 0) {
                    shoot();
                }
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
//        x = Utility.clamp(x, 0, Engine.WIDTH - Utility.intAtWidth640(width)); // todo: reconsider usefulness

        if (isActive()) {
            if (inscreen) {
                moveInScreen();
            } else {
                y += offscreenVelY;
            }
        } else if(isExploding()) {
            velY = 1.5f;
            y += velY;
        }
    }

    @Override
    public void initialize() {
//        if (contactId == ContactID.Ground) {
//            width = Utility.intAtWidth640(32);
//            height = 2* width;
//            color = Color.pink;
//            r = new Random();
//            health = r.nextInt(3) + 2;
//            minVelY = 2f;
//            velY = minVelY;
//        }
        explosion = GameController.getTexture().initExplosion(5);
        reflection = new Reflection();
        damage = 30;
    }

    @Override
    public void render(Graphics g) {
        if (isActive()) {
            renderAttackPath(g);
            super.render(g);
            renderBounds(g);
        } else {
            if (!explosion.playedOnce) {
                int size = Math.max(width, height);
                int expX = (int) x + (size - width)/2;
                int expY = (int) y - (size - height)/2;
                explosion.drawAnimation(g, expX, expY, size, size);
            }
        }
    }

    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            boolean secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g.setColor(Color.red);

                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.RED);


                double drawX = x - (0.5) * (double) width;
                double drawY = getTurretY() - (0.5) * (double) height;
//                double drawY = y - (0.5) * (double) height;
                int arcAngle = 20;

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(getBulletAngle()) - arcAngle / 2, arcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    public void renderShadow(Graphics g) {
        if (state != STATE_INACTIVE) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(Utility.makeTransparent(0.5f));

            int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
            int sizeOffset = 10;
            int offsetY = 10 + (int) y / 500;
            g.setColor(Color.DARK_GRAY);
            g.fillRect((int) x - offsetX, (int) y + offsetY, width - sizeOffset, height - sizeOffset);

            g2d.setComposite(Utility.makeTransparent(1f));
        }
    }

    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));

        int reflectionX = reflection.calculateReflectionX(x, getCenterX());
        int reflectionY = reflection.calculateReflectionY(y, getCenterY());
        int newWidth = (int) (width * reflection.sizeOffset);
        int newHeight = (int) (height * reflection.sizeOffset);

        if (state == STATE_ACTIVE) {
            g2d.drawImage(image, reflectionX, reflectionY, newWidth, newHeight, null);
        } else if (state == STATE_EXPLODING) {
            explosion.drawAnimation(g2d, reflectionX, reflectionY, newWidth, newHeight);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    protected void shoot() {
//        shotNum++;
        resetShooter();
    }

    protected void shootDownDefault() {
        shot++;
    }

    protected void moveInScreen() {
        y += velY;
        x += velX;
    }

    public void damage() {
        this.health--;
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

    public boolean isInscreen() {
        return inscreen;
    }

    public void setInscreen(boolean inscreen) {
        this.inscreen = inscreen;
    }

    protected void resetShooter() {
        shootTimer = shootTimerDefault;
    }

//    public ContactID getContactId() {
//        return contactId;
//    }

    public int getScore() {
        return score;
    }

    public void setHMove(String move) {
        if (move == "left") {
            hMove = HorizontalMovement.LEFT;
            velX = -minVelX;
        } else if (move == "right") {
            hMove = HorizontalMovement.RIGHT;
            velX = minVelX;//*1.45f;//1.95f;
        } else {
            velX = 0;
        }
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

    public Rectangle2D getBoundsHorizontal() {
        Rectangle2D bounds = new Rectangle2D.Double(x, y, width, 1*height/3 + 2);
//        Rectangle bounds = new Rectangle(x, y, width, 1*height/3 + 2);
        return bounds;
    }

    public Rectangle2D getBoundsVertical() {
        Rectangle2D bounds = new Rectangle2D.Double(x + (width / 4), y, (2 * width) / 4, height);
//        Rectangle bounds = new Rectangle(x + (width / 4), y, (2 * width) / 4, height);
        return bounds;
    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        Rectangle2D r1 = getBoundsVertical();
        Rectangle2D r2 = getBoundsHorizontal();

        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(r1);
        g2d.draw(r2);

//        g2d.drawRect(r1.x, r1.y, r1.width, r1.height);
//        g2d.drawRect(r2.x, r2.y, r2.width, r2.height);

    }

    @Override
    public void resurrect(int x, int y) {
        super.resurrect(x, y);
        explosion.endAnimation();
        shootTimer = shootTimerDefault;
        inscreen = false;
    }

    public boolean checkDeathAnimationEnd() {
        return explosion.playedOnce;
    }

    public void setMovementDistance(int time) {
        this.movementDistance = time;
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
        return shot > 0;
    }

    public void decrementShot() {
        shot--;
    }

    //stub
    public int getTurretX() {
        return ((int) x + width)/2;
    }

    public int getTurretY() {
        return (int) y + Utility.intAtWidth640(2);
    }

    public int getBulletVelocity() {
        return bulletVelocity;
    }

    public double getBulletAngle() {
        return bulletAngle;
    }

    public boolean isExploding() {
        return state == STATE_EXPLODING;
    }

    public int getDamage() {
        return damage;
    }

    protected void commonInit() {
        velX = 0;
        velY = explodingVelocity;
    }

    public int getEnemyType() {
        return enemyType;
    }

    // Private Methods

    private double getCenterY() {
        return (y + height / 2 + 2);
    }

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D rV = getBoundsVertical();
        Rectangle2D rH = getBoundsHorizontal();
        boolean collidesVertically = object.intersects(rV);
        boolean collidesHorizontally = object.intersects(rH);

        return collidesVertically || collidesHorizontally;
    }
}
