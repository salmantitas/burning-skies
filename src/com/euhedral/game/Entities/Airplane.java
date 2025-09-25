package com.euhedral.game.Entities;

import com.euhedral.engine.CollisionBox;
import com.euhedral.engine.MobileEntity;
import com.euhedral.game.EntityID;
import com.euhedral.game.TextureHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Airplane extends MobileEntity {
    // Shooting
    protected int shootTimer = 0;
    protected int shootTimerDefault;
    protected double bulletVelocity;

    // Jitter
    protected int jitter = 0, jitter_MULT = 1, jitter_MAX;

    // Graphics
    protected Graphics2D g2d;
    protected TextureHandler textureHandler;
    protected BufferedImage damageImage;

    // debug
    protected boolean debug = false;

    public Airplane(double x, double y, EntityID id) {
        super(x, y, id);
        collisionBox = new CollisionBox(this, 2);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void updateBounds() {
        collisionBox.setBounds(0, pos.x, pos.y, width, 1 * height / 3 + 2);
        collisionBox.setBounds(1, pos.x + (width / 4), pos.y, (2 * width) / 4, height);
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

    protected abstract void shoot();

    public Rectangle2D getBoundsHorizontal() {
        return collisionBox.getBounds(0);
    }

    public Rectangle2D getBoundsVertical() {
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
