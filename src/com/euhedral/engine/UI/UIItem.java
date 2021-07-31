package com.euhedral.engine.UI;

// Parent class of UI Items that can be interacted with
// Buttons
public class UIItem {
    protected int x, y, width, height;

    public boolean mouseOverlap(int mx, int my) {
        int maxX = x + width, maxY = y + height;
        return (mx >= x && mx <= maxX && my >= y && my <= maxY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
