package com.euhedral.engine;

import com.euhedral.game.EntityID;

public abstract class MobileEntity extends Entity {

    protected class Physics {
        /**********
         * Physics *
         ***********/

        // this can be completely commented out if the
        // game has no functional use of physics
        public double gravity = 1, terminalVel = 0, acceleration = 0;
        public double friction = 0, friction_MAX, friction_MIN;

        // every object is initialized to be not jumping or affected by gravity
//        protected boolean gravityAffected = false, jumping = false; //, friction = false;

//        public void enableFriction() {
//            friction = true;
//        }
//
//        public void setFrictionalForce(float frictionalForce) {
//            this.frictionalForce = frictionalForce;
//        }
//
//        public void setAcceleration(float acceleration) {
//            this.acceleration = acceleration;
//        }
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

    // Velocities will always be double, because doubles store more decimal points than floats
    protected double velX, velY;
    protected double velX_MIN, velY_MIN;
    protected double velX_MAX, velY_MAX;
    protected boolean moveLeft, moveRight, moveUp, moveDown;
    protected HorizontalMovement hMove = HorizontalMovement.NONE;
    protected VerticalMovement vMove = VerticalMovement.NONE;

    // Angle
    protected double forwardVelocity;
    protected double angle;
    private double angleX;
    private double angleY;
    double vectorABx;
    double vectorABy;
    double magnitudeAB;
    double normX;
    double normY;

    public MobileEntity(int x, int y, EntityID id) {
        super(x, y, id);
    }

    @Override
    protected void initialize() {
        forwardVelocity = 0;
        angle = 0;
    }

    @Override
    public void update() {
        if (isActive()) {
            // todo: active check might be redundant
            move();
        }
    }

//    @Override
//    public void render(Graphics g) {
//        super.render(g);
//    }

    // Add velocity components to coordinates to update screen position
    protected void move() {
        x += velX;
        y += velY;
    }

    // Triangle with vertices A, B, C
    protected double calculateAngle(double aX, double aY, double bX, double bY) {

        // Coordinates
        double cX = aX + 1, cY = aY;

        // Vectors
        double vectorABx = aX - bX;
        double vectorABy = (aY - bY);
        double vectorACx = aX - cX;
        double vectorACy = (aY - cY);

        // Magnitudes
        double magnitudeAB = Math.sqrt(Math.pow(vectorABx,2) + Math.pow(vectorABy, 2));
        double magnitudeAC = Math.sqrt(Math.pow(vectorACx,2) + Math.pow(vectorACy, 2));

        // Dot Product
        double dotProduct = vectorABx * vectorACx + vectorABy * vectorACy;

        // Final
        double fraction = dotProduct/(magnitudeAB*magnitudeAC);
        double returnAngle = Math.toDegrees(Math.acos(fraction));

        if (bY < aY) {
            returnAngle = - returnAngle;
        }

        return returnAngle;
    }

    // Triangle with vertices A, B, C
    protected double calculateAngle(double bX, double bY) {
        return calculateAngle(x, y, bX, bY);
    }

    // Calculate the velX and velY using angle (of direction) and forward velocity
    protected void calculateVelocities() {
        angleX = Math.toRadians(360 - angle);
        angleY = Math.toRadians(angle);

        velX = (float) (forwardVelocity * Math.cos(angleX));
        velY = (float) (forwardVelocity * Math.sin(angleY));
    }

    // Calculate the x and y components based on the forward velocity and normalized vector,
    // using (x,y) as the base and (destinationX, destinationY) as the target angle
    protected void calculateVelocities(double destinationX, double destinationY) {
        // Vector
        vectorABx = destinationX - x;
        vectorABy = destinationY - y;

        // Magnitude
        magnitudeAB = Math.sqrt(Math.pow(vectorABx,2) + Math.pow(vectorABy, 2));

        // Normal Vector
        normX = vectorABx/magnitudeAB;
        normY = vectorABy/magnitudeAB;

        velX = forwardVelocity * normX;
        velY = forwardVelocity * normY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    /*
    * Physics Function
    * */

//    public boolean isGravityAffected() {
//        return physics.gravityAffected;
//    }

//    public void setGravityAffected(boolean gravityAffected) {
//        physics.gravityAffected = gravityAffected;
//    }

//    public boolean isJumping() {
//        return physics.jumping;
//    }

//    public void setJumping(boolean jumping) {
//        physics.jumping = jumping;
//    }

    public void setForwardVelocity(double forwardVelocity) {
        this.forwardVelocity = forwardVelocity;
    }

    public double getForwardVelocity() {
        return forwardVelocity;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    protected double getCenterX() {
        return (x + width / 2 - 2);
    }
}
