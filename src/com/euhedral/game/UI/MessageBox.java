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
            setX(x);
            this.y = y;
            this.width = margin;
            height = width;
            color = Color.red;
            selectColor = Color.green;
        }

        public void setX(int x) {
            this.x = x;
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
    private Color
            backColor = Color.GRAY,
            textColor = Color.BLACK,
            textColorError = Color.red;
    private boolean enable = true;
    private Close close;
    private int margin = Utility.intAtWidth640(20);
    private int fontSize = Utility.intAtWidth640(10);
    private Font font = new Font("arial", 0, fontSize);

    private double SCALE_FACTOR;

    private int widthMax = 500;
    private int heightMax;

    public MessageBox(int x, int y) {
        this.x = x;
        this.y = y;
        texts = new ArrayList<>();
        SCALE_FACTOR = 2.00/3.00;
        width = 300;
        height = (int) (width * SCALE_FACTOR);
        heightMax = (int) (widthMax * SCALE_FACTOR);
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

        if (texts.size() != 0 && isEnabled()) {
            g.setColor(backColor);
            g.fillRect(x, y, width, height);
            g.setColor(textColor);
            for (int i = 0; i < texts.size(); i++) {
                String text = texts.get(i);
                int textSize = Utility.perc((g.getFontMetrics().stringWidth(text)), 110);
                if (textSize > width - margin) {
                    if (textSize > widthMax - margin) {
                        g.setColor(textColorError);
                        text += "Text too long, please reformat.";
                    }
                    else {
                        width = textSize + margin;
                        close.setX(x+width);
                    }
                }
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
