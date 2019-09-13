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

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    protected void move() {
        x += velX;
        y += velY;
    }
}
