package com.euhedral.engine.UI;

public class UIItem {
    protected int x, y, width, height;

    public boolean mouseOverlap(int mx, int my) {
        int maxX = x + width, maxY = y + height;
        return (mx >= x && mx <= maxX && my >= y && my <= maxY);
    }
}
