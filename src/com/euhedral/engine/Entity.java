package com.euhedral.engine;

import com.euhedral.game.EntityID;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Entity {

    // todo: DELETE, test only
    protected int initCount = 0;

    protected double x, y;
    protected int width, height;
    protected EntityID id;

    // State Machine
    protected final int STATE_INACTIVE = 0; // no update, no rendering
    protected final int STATE_ACTIVE = 1; // updating and rendering

    protected int state = STATE_ACTIVE;

    protected Rectangle2D bounds;

    protected Color color;
    Color boundColor = Color.green;
    protected BufferedImage image;
    protected BufferedImage images[];

    protected Animation anim;
    protected int animationSpeed = 3;

    public Entity(double x, double y, EntityID id) {
        this.x = x;
        this.y = y;
        this.id = id;

        bounds = new Rectangle();

//        initialize();
    }

    public Entity(int x, int y, EntityID id, BufferedImage image) {
        this(x,y, id);
        this.image = image;
    }

    public Entity(int x, int y, EntityID id, BufferedImage[] images) {
        this(x,y, id);
        this.images = images;

    }

//    protected abstract void initialize();

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
        if (images != null) {
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

    protected void drawAnimation(Graphics g) {
        // todo: ???
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
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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
        bounds.setRect( x, y, width, height);
        return bounds;
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) (x + 0.2*width), (int) y,  (int) (0.6* width),  height/4);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle((int) (x + 0.2*width), (int) y + 3*height/4,  (int) (0.6* width),  height/4);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle((int) x, (int) (y + 0.35*height),  width/4,  (int) (height * 0.3));
    }

    public Rectangle getBoundsRight() {
        return new Rectangle((int) x + 3*width/4, (int) (y + 0.35*height),  width/4,  (int) (height * 0.3));
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

    protected void drawImage(Graphics g, BufferedImage image) {
        g.drawImage(image, (int) x, (int) y, null);
    }

    protected void drawImage(Graphics g, BufferedImage image, int targetWidth, int targetHeight) {
        g.drawImage(image, (int) x, (int) y, targetWidth, targetHeight, null);
    }

    protected void drawImage(Graphics g, BufferedImage image, int targetWidth, int targetHeight, double rotation) {
        rotation = rotation % 90;
        Graphics2D g2d = (Graphics2D) g;
        int tX = 0, tY = 0;
        g2d.translate(-tX, -tY);
        g2d.rotate(Math.toRadians(rotation));
        g2d.drawImage(image, (int) x, (int) y, targetWidth, targetHeight, null);
        g2d.rotate(Math.toRadians(-rotation));
        g2d.translate(tX, tY);
    }

//    protected void drawImage(Graphics g, BufferedImage image, int x, int y, int targetWidth, int targetHeight) {
//        g.drawImage(image, x, y, targetWidth, targetHeight, null);
//    }

    protected void drawRect(Graphics g) {
        g.fillRect((int) x, (int) y, width, height);
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
        this.x = x;
        this.y = y;
    }

    public void resurrect(double x, double y, EntityID id) {
        resurrect(x, y);
    }

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
        System.out.printf("__ at (%d, %d)", x, y);
    }

    public EntityID getID() {
        return id;
    }
}
