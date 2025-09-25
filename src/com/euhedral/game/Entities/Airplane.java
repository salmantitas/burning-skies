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
//    protected Rectangle2D boundsVertical, boundsHorizontal;

    // Collisions
    protected boolean collidesVertically, collidesHorizontally;

    // Jitter
    protected int jitter = 0, jitter_MULT = 1, jitter_MAX;

    // Graphics
    protected Graphics2D g2d;
    protected TextureHandler textureHandler;
    protected BufferedImage damageImage;

    // debug
    protected boolean debug = true;

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
        Rectangle2D boundsVertical = getBoundsVertical();
        Rectangle2D boundsHorizontal = getBoundsHorizontal();
        collidesVertically = object.intersects(boundsVertical);
        collidesHorizontally = object.intersects(boundsHorizontal);

        return collidesVertically || collidesHorizontally;
    }

    public Rectangle2D getBoundsHorizontal() {
        collisionBox.setBounds(0, pos.x, pos.y, width, 1 * height / 3 + 2);
        return collisionBox.getBounds(0);
//        boundsHorizontal.setRect();
//        return boundsHorizontal;
    }

    public Rectangle2D getBoundsVertical() {
        collisionBox.setBounds(1, pos.x + (width / 4), pos.y, (2 * width) / 4, height);
        return collisionBox.getBounds(1);
    }

    // Render


    @Override
    public void render(Graphics g) {
        super.render(g);
        if (debug) {
            renderBounds(g);
        }
    }

    protected void renderDamageImage() {
        g2d.drawImage(damageImage, (int) pos.x, (int) pos.y, null);
    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        g2d = (Graphics2D) g;

        g2d.draw(getBoundsVertical());
        g2d.draw(getBoundsHorizontal());

    }
}
