package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Position;
import com.euhedral.engine.Utility;

import java.awt.*;

public class Pulse {
    int radius;
    int radius_MAX;

    public Pulse() {
        radius = -1;
        radius_MAX = Engine.WIDTH;
    }

    public void update(double bulletVelocity) {
        if (radius > -1) {

            radius += radius / 10 + bulletVelocity;

            if (radius >= radius_MAX) {
                radius = -1;
            }
        }
    }

    public void render(Graphics2D g, Position pos, int width, int height, double bulletVelocity) {
        // Render Ring Of Fire
        if (radius > -1) {
            g.setColor(Color.YELLOW);

            g.setComposite(Utility.makeTransparent(0.f));
            g.fillOval((int) pos.x - radius, (int) pos.y - radius, width + radius * 2, height + radius * 2);
            g.setComposite(Utility.makeTransparent(1f));

            g.setStroke(new BasicStroke((int) (radius / (bulletVelocity * 3))));
            g.drawOval((int) pos.x - radius, (int) pos.y - radius, width + radius * 2, height + radius * 2);
        }
    }
}
