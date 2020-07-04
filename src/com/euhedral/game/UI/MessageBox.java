package com.euhedral.game.UI;

import com.euhedral.engine.UI.UIItem;

import java.awt.*;

public class MessageBox extends UIItem {

    private String text;
    private Color backColor = Color.GRAY;
    private boolean enable = true;

    public MessageBox(int x, int y) {
        this.x = x;
        this.y = y;
        width = 300;
        height = 200;
    }

    public void setText(String text) {
        if (text != null) {
            this.text = text;
            resizeText();
        }
    }

    private void resizeText() {
    }

    public void update() {
        boolean clicked = false;
        if (clicked)
            disable();
    }

    public void render(Graphics g) {
        if (text!=null && isEnabled()) {
            g.setColor(backColor);
            g.fillRect((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.drawString(text, (int) x, (int) y);
        }
    }

    private void disable() {
        enable = false;
    }

    public boolean isEnabled() {
        return enable;
    }
}
