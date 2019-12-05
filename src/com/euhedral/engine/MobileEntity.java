package com.euhedral.engine;

import com.euhedral.game.EntityID;

import java.awt.*;

public class MobileEntity extends Entity {

    protected enum HorizontalMovement{
        LEFT, RIGHT, NONE;
    }

    protected enum VerticalMovement{
        UP, DOWN, NONE;
    }

    protected int WEST = 0;
    protected int SOUTH_WEST = 45;
    protected int SOUTH = 90;
    protected int SOUTH_EAST = 135;
    protected int EAST = 180;
    protected int NORTH_EAST = 225;
    protected int NORTH = 270;
    protected int NORTH_WEST = 315;

    protected float turn = SOUTH_WEST - WEST;
    protected float forwardVelocity = 0;
    protected float facing = 0;
    protected float acceleration, frictionalForce;
    protected float velX, velY;
    protected float minVelX, minVelY;
    protected float maxVelX, maxVelY;
    protected boolean moveLeft, moveRight, moveUp, moveDown;
    protected HorizontalMovement hMove = HorizontalMovement.NONE;
    protected VerticalMovement vMove = VerticalMovement.NONE;

    public MobileEntity(int x, int y, EntityID id) {
        super(x, y, id);
    }

    @Override
    protected void initialize() {

    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    protected void move() {
        x += velX;
        y += velY;
    }

    protected void calculateVelocities() {
        double angle = facing;
        double angleX;
        double angleY;
        angleX = Math.toRadians(360 - angle);
        angleY = Math.toRadians(angle);
        velX = (float) (forwardVelocity * Math.cos(angleX));
        velY = (float) (forwardVelocity * Math.sin(angleY));
    }
}
