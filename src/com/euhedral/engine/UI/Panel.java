package com.euhedral.engine.UI;

import com.euhedral.engine.GameState;
import com.euhedral.engine.Utility;

import java.awt.*;

public class Panel extends MenuItem {

    Graphics2D g2d;

    public Panel(int x, int y, int width, int height, GameState state) {
        super(x,y,width, height, state);
    }

    public Panel(int x, int y, int width, int height, GameState state, float transparency, Color color) {
        super(x,y,width,height,state,transparency,color);
    }

    public void render(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setComposite(Utility.makeTransparent(transparency));

        g.setColor(backColor);
        g.fillRect(x, y, width, height);

        // Ensure that nothing on top of the transparent panel is rendered transparent
        g2d.setComposite(Utility.makeTransparent(1));
    }

}
