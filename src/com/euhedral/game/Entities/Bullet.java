package com.euhedral.game.Entities;

import com.euhedral.engine.Animation;
import com.euhedral.engine.Engine;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet extends MobileEntity {
//    protected int vel;
    protected boolean collided;
    protected Color color;
    protected Color impactColor;
    protected boolean calculated = false;
    protected Animation impact;
    protected int impactSize = 32;

    protected int initSound = SoundHandler.BULLET_PLAYER;

    // Disabling
    int offset;
    int bottomBounds;
    int rightBounds;

    boolean belowScreen, aboveScreen, leftOfScreen, rightOfScreen;

    // Impact
    protected int impactTimer = 0;
    protected final int impactTimerCheck = 10;
    protected int impactFactor = 2;
    protected MobileEntity entity;
    protected TextureHandler textureHandler;

    // State Machine
    protected final int STATE_IMPACT = 2;

    // Reflection
    protected Reflection reflection;
    int reflectionX, reflectionY, newWidth, newHeight;

    Bullet( double x, double y) {
        super(x ,y, EntityID.Bullet);
        setPos(x,y);
//        this.x = x;
//        this.y = y;
        collided = false;
        width = Utility.intAtWidth640(4);
        height = Utility.intAtWidth640(24)/2;
        forwardVelocity = Utility.intAtWidth640(6);

        textureHandler = GameController.getTexture();

        impact = new Animation(2, GameController.getTexture().impactSmall[0],
                GameController.getTexture().impactSmall[1],
                GameController.getTexture().impactSmall[2],
                GameController.getTexture().impactSmall[3]
        );

        reflection = new Reflection();

        newWidth = (int) (width * reflection.sizeOffset);
        newHeight = (int) (height * reflection.sizeOffset);

        impactSize = Math.max(newWidth, newHeight);

        offset = 64*3;
        bottomBounds = EntityHandler.getLevelHeight() + offset;
        rightBounds = Engine.WIDTH + offset;
    }

    Bullet(double x, double y, double angle) {
        this(x,y);
        this.angle = angle % 360;
    }

    @Override
    protected void calculateVelocities() {
        super.calculateVelocities();
        calculated = true;
    }

    public void update() {
        if (!calculated)
            calculateVelocities();
        move();
    }

    @Override
    public void render(Graphics g) {
        drawDefault(g);
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            drawImage(g, image, width, height);
//            g.setColor(color);
//            g.fillOval((int) x, (int) y, width, height);
        } else if (state == STATE_IMPACT && entity.isActive()) {
//            g.setColor(impactColor);
//            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);

            impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
        }
    }

    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));

        reflectionX = reflection.calculateReflectionX(pos.x, getCenterX());
        reflectionY = reflection.calculateReflectionY(pos.y, getCenterY());

        if (state == STATE_ACTIVE) {
            g2d.drawImage(image, reflectionX, reflectionY, newWidth, newHeight, null);
        } else if (state == STATE_IMPACT) {
//            int impactSize = Math.max(newWidth, newHeight);
            impact.drawAnimation(g2d, reflectionX, reflectionY, impactSize, impactSize);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

    protected void move() {
        super.move();
//        pos.x += velX;
//        pos.y += velY;
    }

    public void mirror(Bullet bullet, double angle) {
        if (angle == 90) {
            velX = -bullet.velX;
            velY = bullet.velY;
        }
    }

    public void setX(int x) {
        super.setX(x);
//        pos.x = x;
    }

    public void setY(int y) {
        super.setY(y);
//        this.y = y;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, width, height);
    }

    @Override
    public void resurrect(double x, double y) {
        super.resurrect(x, y);
        impact.endAnimation();
        impact.playedOnce = false;
    }

    public void destroy(MobileEntity entity) {
        destroy();
        this.entity = entity;
    }

    public void destroy() {
        state = STATE_IMPACT;
    }

    public boolean isImpacting() {
        return state == STATE_IMPACT;
    }

    public boolean checkDeathAnimationEnd() {
        return impact.playedOnce;
    }

    protected double getCenterY() {
        return (pos.y + height / 2);
    }

    @Override
    public boolean canDisable() {
//        if (isActive()) {

            belowScreen = pos.y > bottomBounds;
            aboveScreen = pos.y < 0;
            leftOfScreen = pos.x < 0;
            rightOfScreen = pos.x > rightBounds;

//            if (belowScreen || aboveScreen || leftOfScreen || rightOfScreen)
//                Utility.log("Can disable");

            return belowScreen || aboveScreen || leftOfScreen || rightOfScreen;
//        }
//        else return super.canDisable();
    }
}
