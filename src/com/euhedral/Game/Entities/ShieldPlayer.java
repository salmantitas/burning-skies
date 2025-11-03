package com.euhedral.Game.Entities;

import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Attribute;

import java.awt.*;

public class ShieldPlayer extends Shield {

    private Attribute shield;

    public ShieldPlayer(Attribute shield) {
        this.shield = shield;
        color = Color.blue;
    }

    int shieldTimer = 0;
    int shieldTimer_MAX = 30;

    public void update() {
        if (shieldTimer > 0) {
            shieldTimer--;
        }
    }

    public void render(Graphics2D g2d, Position pos, int width, int height) {
        // Render Shield
        int shieldOffset = 8;
        if (shield.getValue() > 0) {
            float shieldTransparency = 0.2f;
            if (shieldTimer > 0) {
                shieldTransparency *= (float) Math.pow(shieldTimer_MAX / 2 - shieldTimer, 2) / (shieldTimer_MAX * 15);
            }
            g2d.setColor(color);
            g2d.setComposite(Utility.makeTransparent(shieldTransparency));
            g2d.fillOval((int) pos.x - width / shieldOffset, (int) pos.y - height / shieldOffset + 5, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
            g2d.setComposite(Utility.makeTransparent(1f));
            g2d.setStroke(new BasicStroke(3, 1, 1));
            g2d.drawOval((int) pos.x - width / shieldOffset, (int) pos.y - height / shieldOffset + 5, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
        }
    }
}
