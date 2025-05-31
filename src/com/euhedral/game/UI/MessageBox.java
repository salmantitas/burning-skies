package com.euhedral.game.UI;

import com.euhedral.engine.UI.UIItem;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.ArrayList;

import static com.euhedral.engine.Utility.makeTransparent;

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
            backColor = Color.BLACK,
            textColor = Color.WHITE,
            textColorError = Color.red;
    private boolean enable = true;
    private Close close;
    private int margin = Utility.intAtWidth640(10);
    private int fontSize = Utility.intAtWidth640(10);
    private Font font = new Font("arial", 0, fontSize);
    private float transparency;

    private double SCALE_FACTOR;

    private int widthMax = 500;
    private int heightMax;

    public MessageBox(int x, int y) {
        this.x = x;
        this.y = y;
        texts = new ArrayList<>();
        SCALE_FACTOR = 2.00/2.50;
        width = 300;
        height = (int) (width * SCALE_FACTOR);
        heightMax = (int) (widthMax * SCALE_FACTOR);
        transparency = 1;
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

        if (isEnabled()) {// && texts.size() != 0) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(makeTransparent(transparency));

            g.setColor(backColor);
            g.fillRect(x, y, width, height);

            // Ensure that nothing on top of the transparent panel is rendered transparent
            g2d.setComposite(makeTransparent(1));

//            g.setColor(backColor);
//            g.fillRect(x, y, width, height);
            g.setColor(textColor);
            int yPos = y;
            int textStart = (int) (height*0.2);

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

                int textSpacing = i*18;
                yPos = y + textSpacing + textStart;
                g.drawString(text, x + margin, yPos);
            }

            String dismissalText = "Press ENTER or E to start";
            int textSize = g.getFontMetrics().stringWidth(dismissalText);
            // Dismissal
            g.setColor(Color.RED);
            g.drawString(dismissalText, x + width/2 - textSize/2, yPos + textStart);
            close.render(g);
        }
    }

    public void disable() {
        enable = false;
    }

    public void enable() {
        enable = true;
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

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }
}
