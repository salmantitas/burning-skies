package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.game.EntityID;
import com.euhedral.game.TextureHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Airplane extends MobileEntity {
    // Shooting
    protected int shootTimer = 0;
    protected int shootTimerDefault;
    protected double bulletVelocity;

    // Bounds
    protected Rectangle2D boundsVertical, boundsHorizontal;

    // Collisions
    protected boolean collidesVertically, collidesHorizontally;

    // Jitter
    protected int jitter = 0, jitter_MULT = 1, jitter_MAX;

    // Graphics
    protected Graphics2D g2d;
    protected TextureHandler textureHandler;
    protected BufferedImage damageImage;

    public Airplane(double x, double y, EntityID id) {
        super(x, y, id);
    }

    protected void updateShootTimer() {
        shootTimer--;
    }

    protected void jitter() {
        if (jitter > 0) {
            jitter--;
            jitter_MULT *= -1;
        }
    }

    protected void shoot() {

    }

    public boolean checkCollision(Rectangle2D object) {
        boundsVertical = getBoundsVertical();
        boundsHorizontal = getBoundsHorizontal();
        collidesVertically = object.intersects(boundsVertical);
        collidesHorizontally = object.intersects(getBoundsHorizontal());

        return collidesVertically || collidesHorizontally;
    }

    public Rectangle2D getBoundsHorizontal() {
        boundsHorizontal.setRect(pos.x, pos.y, width, 1 * height / 3 + 2);
        return boundsHorizontal;
    }

    public Rectangle2D getBoundsVertical() {
        boundsVertical.setRect(pos.x + (width / 4), pos.y, (2 * width) / 4, height);
        return boundsVertical;
    }

    protected void drawDamageImage() {
        g2d.drawImage(damageImage, (int) pos.x, (int) pos.y, null);
    }
}
