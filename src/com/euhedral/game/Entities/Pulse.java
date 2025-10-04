package com.euhedral.Game.Entities;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Position;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class Pulse {
    int radius;
    int radius_MAX;

    double angle;

    public Pulse() {
        radius = -1;
        radius_MAX = Engine.WIDTH;
        angle = 0;
    }

    public void update(double bulletVelocity) {
        if (radius > -1) {

            radius += radius / 10 + bulletVelocity;

            if (radius >= radius_MAX) {
                radius = -1;
            }
        }

        angle++;
    }

    public void render(Graphics2D g, Position pos, int width, int height, double bulletVelocity) {
        if (radius > -1) {
            g.setColor(Color.YELLOW);

            g.setComposite(Utility.makeTransparent(0.f));
            g.fillOval((int) pos.x - radius, (int) pos.y - radius, width + radius * 2, height + radius * 2);
            g.setComposite(Utility.makeTransparent(1f));

            g.setStroke(new BasicStroke((int) (radius / (bulletVelocity * 3))));
            g.drawOval((int) pos.x - radius, (int) pos.y - radius, width + radius * 2, height + radius * 2);
        }
        else if (VariableHandler.pulse) {
            g.setColor(Color.YELLOW);
            g.setStroke( new BasicStroke(1) );
//            int x = pos.intX();
//            int y = pos.intY();
//            int centerX = x + width/2;
//            int centerY = y + height/2;

            g.drawArc(pos.intX(), pos.intY(), width, height, (int) angle, 30);
        }
    }
}
