package com.euhedral.Game.Entities.Projectile;

import com.euhedral.Game.EntityHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MissileEnemy extends BulletEnemy{

    double forwardVelocity_MIN;
    double destinationX, destinationY;
    double finalAngle;
    boolean destinationReached = false;

    public MissileEnemy(double x, double y, double angle, double destinationX, double destinationY) {
        super(x, y, angle, 3, false);
        finalAngle = angle;
        this.destinationX = destinationX;
        this.destinationY = destinationY;

        color = Color.GRAY;
        width = 8;
        height = 32;

        damage = 20;

        impactSize = 48;
        reflectedImpactSize = (int) (impactSize * reflection.sizeOffset);

        forwardVelocity_MIN = forwardVelocity;
        physics.acceleration = 0.1;
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);

            Graphics2D g2d = (Graphics2D) g;
            AffineTransform original = g2d.getTransform();

            g2d.rotate(Math.toRadians(angle - 90), pos.x + width/2, pos.y + height/2 );

            g.fillRect(pos.intX(), pos.intY(), width, height);
            drawOutline(g, width, height);
            g2d.setTransform(original);

        } else if (state == STATE_IMPACT) {
            if (entity == null) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            } else if (entity.isActive()) {
                impact.drawAnimation(g, (int) pos.x, (int) pos.y, impactSize, impactSize);
            }
        }
    }

    @Override
    protected void drawOutline(Graphics g, int targetWidth, int targetHeight) {
        if (state == STATE_ACTIVE) {
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawRect(pos.intX(), pos.intY(), targetWidth, targetHeight);
        }
    }

//    @Override
//    public void update() {
//        super.update();
//        if (isActive()) {
//            if (velY != 0) {
//                if (velY > 0) {
//                    velY = Math.abs(velY) + physics.acceleration;
//                } else if (velY < 0) {
//                    velY = Math.abs(velY) - physics.acceleration;
//                }
//            }
//        }
//    }


    @Override
    public void resurrect(double x, double y) {
        super.resurrect(x, y);
        forwardVelocity = forwardVelocity_MIN;
    }

    public void setDestination(double x, double y) {
        destinationX = x;
        destinationY = y;
        destinationReached = false;
    }

    @Override
    protected void move() {
        int offsetPos = 16;
        if (Math.abs(destinationX - pos.x) < offsetPos && Math.abs(destinationY - pos.y) < offsetPos) {
            destinationReached = true;
        }

        if (!destinationReached) {
            finalAngle = calculateAngle(destinationX, destinationY);

            int offsetAngle = 10;

            if (angle - finalAngle < offsetAngle) {
                angle++;
            } else if (angle - finalAngle > offsetAngle )
                angle--;
        }

        forwardVelocity += physics.acceleration;
        calculateVelocities();

        pos.x += velX;
        pos.y += velY;// + EntityHandler.backgroundScrollingSpeed;
    }
}
