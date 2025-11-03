package com.euhedral.Game.Entities;

import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;

import java.awt.*;

public class ShieldEnemy extends Shield {
    public ShieldEnemy() {
        color = Color.YELLOW;
    }

    public void render(Graphics2D g2d, Position pos, int width, int height) {
        // Render Shield
        int shieldOffset = 8;
        int drawY = (int) pos.y - height / shieldOffset;// + 5;

        float shieldTransparency = 0.2f;

            g2d.setColor(color);
            g2d.setComposite(Utility.makeTransparent(shieldTransparency));
            g2d.fillOval((int) pos.x - width / shieldOffset, drawY, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
            g2d.setComposite(Utility.makeTransparent(1f));
            g2d.setStroke(new BasicStroke(3, 1, 1));
            g2d.drawOval((int) pos.x - width / shieldOffset, drawY, width + 2 * width / shieldOffset, height + 2 * height / shieldOffset);
    }


}
