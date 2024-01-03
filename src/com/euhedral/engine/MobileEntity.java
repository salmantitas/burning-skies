package com.euhedral.engine;

import com.euhedral.game.EntityID;

import java.awt.*;

public abstract class MobileEntity extends Entity {

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

    protected double WEST = 0;
    protected double SOUTH_WEST = 45;
    protected double SOUTH = 90;
    protected double SOUTH_EAST = 135;
    protected double EAST = 180;
    protected double NORTH_EAST = 225;
    protected double NORTH = 270;
    protected double NORTH_WEST = 315;

//    protected double turn = SOUTH_WEST - WEST;
    protected float forwardVelocity = 0;
    protected double angle = 0;
    protected double velX, velY;
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
        x += velX;
        y += velY;
    }

    // Calculate the x and y components based on the angle and forward velocity of the entity
    protected void calculateVelocities() {
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

    public void setAngle(double angle) {
        this.angle = angle;
    }

//    private double cos(double angle) {
//        double angleD = 0;
//
//        if (angle > 0 && angle <= 90) {
//            angleD = Math.toRadians(angle);
//        } else if (angle > 90 && angle <= 180) {
//            angleD = Math.toRadians(angle);
//        } else if (angle > 180 && angle <= 270) {
//            angleD = 360 - angle;
//            if (angleD == 90) {
//                angleD = Math.toRadians(angleD);
//            } else {
//                double difference = angleD - 90;
//                angleD = angleD - 2*difference;
//                angleD = Math.toRadians(angleD);
//                double cosAngleD = Math.cos(angleD);
//                return -cosAngleD;
//            }
//        } else if (angle > 270 && angle <= 360) {
//            angleD = (360 - angle);
//            angleD = Math.toRadians(angleD);
//            double cosAngleD = Math.cos(angleD);
//            return cosAngleD;
//        }
//
//        return Math.cos(angleD);
//    }
//
//    private double sin(double angle) {
//        double angleD = 0;
//
//        if (angle > 0 && angle <= 90) {
//            angleD = Math.toRadians(angle);
//        } else if (angle > 90 && angle <= 180) {
//            angleD = Math.toRadians(angle);
//        } else if (angle > 180 && angle <= 270) {
//            angleD = angle;
//            angleD = Math.toRadians(angleD);
//        } else if (angle > 270 && angle <= 360) {
//            angleD = angle;
//            angleD = Math.toRadians(angleD);
//        }
//
//        return Math.sin(angleD);
//    }
}
