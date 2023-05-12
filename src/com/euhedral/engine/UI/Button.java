package com.euhedral.engine.UI;

import com.euhedral.engine.GameState;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.LinkedList;

// Interactive Buttons to navigate between menus or activate actions
public class Button extends UIItem{
    protected int size;
    protected String text;
    protected Font font;
    protected boolean selected = false;
    protected Color backColor, textColor, selectedColor;
    protected boolean fill = false;
    protected boolean outline = true;
    protected boolean enabled = true;
    protected LinkedList<GameState> otherStates = new LinkedList<>();
    protected float transparency = 1;
    protected boolean fontSizeCalculated = false;

    protected Color disabledColor, disabledTextColor;

    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        font = new Font("arial", 1, width);
        backColor = Color.BLUE;
        selectedColor = Color.GREEN;
        textColor = Color.RED;
        disabledColor = Color.GRAY;
        disabledTextColor = Color.LIGHT_GRAY;
    }

    public Button(int x, int y, int size, String text) {
        this(x, y, size, (size * 11)/10, text);
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        // make it transparent whether the button is enabled or disabled

        if (transparency < 1) {
            g2d.setComposite(makeTransparent(transparency));
        }

        g.setFont(font);

        int stringWidth = (g.getFontMetrics().stringWidth(text));

        if (!fontSizeCalculated) {
            // Adjusts the width and height of the button to fit the text
            width = Utility.perc(stringWidth, 110);;

            if (width < height)
                width = height;
            fontSizeCalculated = true;
        }

        if (!enabled) {
            g.setColor(disabledColor);
            g.fill3DRect(x,y,width,height, true);

            g.setColor(disabledTextColor);
            g.drawString(text, x + Utility.perc(width, 5), y + Utility.perc(height, 75));
        } else {
            g.setColor(backColor);
            if (fill)
                g.fill3DRect(x,y,width,height, true);
            else if (outline) {
                g.setColor(Color.BLACK);
                g.drawRect(x, y, width, height);
            }

            if (selected)
                g.setColor(selectedColor);
            else g.setColor(textColor);
//            int stringX = x + Utility.perc(width, 5);
            int stringX = x + (width/2 - stringWidth/2);
            g.drawString(text, stringX, y + Utility.perc(height, 75));
        }

        if (transparency < 1) {
            g2d.setComposite(makeTransparent(1));
        }
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public void setFill() {
        this.fill = true;
    }

    // Not very sure what's happening here
    private AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

//    // todo: removal
//    public void addOtherState(GameState state) {
//        otherStates.add(state);
//    }

    public void activate() {

    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void select() {
        setSelected(true);
    }

    public void deselect() {
        setSelected(false);
    }

    public boolean isSelected() {
        return selected;
    }

    private void setSelected(boolean b) {
        selected = b;
    }


}

