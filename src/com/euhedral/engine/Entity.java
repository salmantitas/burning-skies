package com.euhedral.engine;

import com.euhedral.game.EntityID;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity {
    protected Position pos;
//    protected double x, y;
    protected int width, height;
    protected EntityID id;

    // State Machine
    protected final int STATE_INACTIVE = 0; // no update, no rendering
    protected final int STATE_ACTIVE = 1; // updating and rendering

    protected int state = STATE_ACTIVE;

    protected CollisionBox collisionBox;

    protected Color color;
    Color boundColor = Color.green;
    protected BufferedImage image;
//    protected BufferedImage images[];

    protected Animation anim;
    protected int animationSpeed = 3;

    public Entity(double x, double y, EntityID id) {
        pos = new Position(x,y);
        this.id = id;

        collisionBox = new CollisionBox(this,1);
    }

    public Entity(int x, int y, EntityID id, BufferedImage image) {
        this(x,y, id);
        this.image = image;
    }

    public Entity(int x, int y, EntityID id, BufferedImage[] images) {
        this(x,y, id);
        this.anim = new Animation(animationSpeed, images);
    }

    public abstract void update();

    public void render(Graphics g) {
        if (isActive()) {
            drawDefault(g);
        }
    }

    // Attempts to animate the object.
    // In the absense of an animation, it attempts to draw an image
    // Otherwise, a default color is set and a rectangle is drawn on screen
    protected void drawDefault(Graphics g) {
        if (anim != null) {
            drawAnimation(g);
        }
        else if (image != null) {
            drawImage(g, image);
        }
        else {
            setColor(g);
            drawRect(g);
        }
    }

    protected void renderBounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(boundColor);
        g2d.draw(getBounds());

//        g2d.draw(getBoundsTop());
//        g2d.draw(getBoundsBottom());
//        g2d.draw(getBoundsLeft());
//        g2d.draw(getBoundsRight());
    }

    // Getters & Setters

    public double getX() {
        return pos.x;
    }

    public double getY() {
        return pos.y;
    }

    public Position getPos() {
        return pos;
    }

    public void setX(int x) {
        pos.x = x;
    }

    public void setY(int y) {
        pos.y = y;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setPos(double x, double y) {
        pos.x = x;
        pos.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public EntityID getId() {
        return id;
    }

    public void setId(EntityID id) {
        this.id = id;
    }

    public Rectangle2D getBounds() {
        return collisionBox.getBounds();
    }

    protected void setColor(Graphics g) {
        g.setColor(color);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    protected void drawAnimation(Graphics g) {
        anim.drawAnimation(g, pos.x, pos.y);
    }

    protected void drawImage(Graphics g, BufferedImage image) {
        g.drawImage(image, (int) pos.x, (int) pos.y, null);
    }

    protected void drawImage(Graphics g, BufferedImage image, int targetWidth, int targetHeight) {
        g.drawImage(image, (int) pos.x, (int) pos.y, targetWidth, targetHeight, null);
    }

//    protected void drawImage(Graphics g, BufferedImage image, int targetWidth, int targetHeight, double rotation) {
//        rotation = rotation % 90;
//        Graphics2D g2d = (Graphics2D) g;
//        int tX = 0, tY = 0;
//        g2d.translate(-tX, -tY);
//        g2d.rotate(Math.toRadians(rotation));
//        g2d.drawImage(image, (int) pos.x, (int) pos.y, targetWidth, targetHeight, null);
//        g2d.rotate(Math.toRadians(-rotation));
//        g2d.translate(tX, tY);
//    }

//    protected void drawImage(Graphics g, BufferedImage image, int x, int y, int targetWidth, int targetHeight) {
//        g.drawImage(image, x, y, targetWidth, targetHeight, null);
//    }

    protected void drawRect(Graphics g) {
        g.fillRect((int) pos.x, (int) pos.y, width, height);
    }

    public boolean isActive() {
        return state == STATE_ACTIVE;
    }

    public boolean isInactive() {
        return state == STATE_INACTIVE;
    }

    public void enable() {
        setActive(true);
    }

    public void disable() {
        setActive(false);
    }

    public boolean canDisable() {
        return false;
    }

    public void clear() {
        setActive(false);
    }

    public void resurrect(double x, double y) {
        enable();
        setPos(x,y);
//        pos.x = x;
//        this.y = y;
    }

    public void resurrect(double x, double y, EntityID id) {
        resurrect(x, y);
    }

    // todo: What is going on here and why??
    private void setActive(boolean active) {
        if (active) {
            state = STATE_ACTIVE;
        } else {
            state = STATE_INACTIVE;
        }
    }

    /*
    * Debug
    * */

    protected void printLocation() {
        System.out.printf("__ at (%d, %d)", pos.x, pos.y);
    }

    public EntityID getID() {
        return id;
    }

    public CollisionBox getCollisionBox() {
        return collisionBox;
    }

    public boolean checkCollision(Entity other) {
        return collisionBox.checkCollision(other);
    }
}
