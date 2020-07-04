package com.euhedral.game.UI;

import com.euhedral.engine.UI.UIItem;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.ArrayList;

public class MessageBox extends UIItem {

    private class Close extends UIItem{
        Color color, selectColor;
        boolean selected = false;

        public Close(int x, int y, int margin) {
            this.x = x;
            this.y = y;
            this.width = margin;
            height = width;
            color = Color.red;
            selectColor = Color.green;
        }

        public void render(Graphics g) {
            if (!selected)
                g.setColor(color);
            else g.setColor(selectColor);
            g.fillRect(x - this.width,y,width,height);
        }

        @Override
        public boolean mouseOverlap(int mx, int my) {
            int minX = x - margin, minY = y;
            int maxX = minX + width, maxY = minY + height;
            return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
        }

        public void select() {
            selected = true;
        }

        public void deselect() {
            selected = false;
        }
    }

    private ArrayList<String> texts;
    private Color backColor = Color.GRAY;
    private boolean enable = true;
    private Close close;
    private int margin = Utility.intAtWidth640(20);
    private int fontSize = Utility.intAtWidth640(10);
    private Font font = new Font("arial", 0, fontSize);

    public MessageBox(int x, int y) {
        this.x = x;
        this.y = y;
        texts = new ArrayList<>();
        width = 300;
        height = 200;
        close = new Close(x+width,y, margin);
    }

    public void addText(String text) {
        texts.add(text);
    }

    private void resizeText() {
    }

    public void update() {
        boolean clicked = false;
        if (clicked)
            disable();
    }

    public void render(Graphics g) {
        g.setFont(font);

        // todo: make text fit to box
        if (texts.size() != 0 && isEnabled()) {
            g.setColor(backColor);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            for (int i = 0; i < texts.size(); i++) {
                String text = texts.get(i);
                g.drawString(text, x + margin, y + (i*20) + height / 3);
            }
            close.render(g);
        }
    }

    private void disable() {
        enable = false;
    }

    public boolean isEnabled() {
        return enable;
    }

    public void checkButtonAction(int mx, int my) {
        if (close.mouseOverlap(mx, my))
            disable();
    }

    public void checkHover(int mx, int my) {
        if (close.mouseOverlap(mx, my)) {
            close.select();
        } else {
            close.deselect();
        }
    }
}
