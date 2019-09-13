package com.euhedral.engine;

import java.awt.*;

public class Panel extends MenuItem{

    public Panel(int x, int y, int width, int height, GameState state) {
        super(x,y,width, height, state);
    }

    public Panel(int x, int y, int width, int height, GameState state, float transparency, Color color) {
        super(x,y,width,height,state,transparency,color);
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(makeTransparent(transparency));

        g.setColor(backColor);
        g.fillRect(x, y, width, height);

        // Ensure that nothing on top of the transparent panel is rendered transparent
        g2d.setComposite(makeTransparent(1));
    }

}
