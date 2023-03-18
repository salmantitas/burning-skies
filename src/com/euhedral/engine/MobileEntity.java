package com.euhedral.engine;

import com.euhedral.game.EntityID;

import java.awt.*;

public class MobileEntity extends Entity {

    protected class Physics {
        /**********
         * Physics *
         ***********/

        // this can be completely commented out if the
        // game has no functional use of physics
        protected float gravity = 1f, terminalVel = 0;
        protected float acceleration = 0, frictionalForce = 0;

        // every object is initialized to be not jumping or affected by gravity
        protected boolean gravityAffected = false, jumping = false, friction = false;

        public void enableFriction() {
            friction = true;
        }

        public void setFrictionalForce(float frictionalForce) {
            this.frictionalForce = frictionalForce;
        }

        public void setAcceleration(float acceleration) {
            this.acceleration = acceleration;
        }
    }

    protected Physics physics = new Physics();

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

    protected void move() {
//        updateX(velX);
//        updateY(velY);
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

    /*
    * Physics Function
    * */

    public boolean isGravityAffected() {
        return physics.gravityAffected;
    }

    public void setGravityAffected(boolean gravityAffected) {
        physics.gravityAffected = gravityAffected;
    }

    public boolean isJumping() {
        return physics.jumping;
    }

    public void setJumping(boolean jumping) {
        physics.jumping = jumping;
    }
}
