package com.euhedral.engine;

import java.awt.*;
import java.util.LinkedList;

public class Button {
    protected int x, y, width, height;
    protected int size;
    protected String text;
    protected Font font;
    protected GameState renderState;
    protected boolean selected = false;
    protected Color backColor, textColor, selectedColor;
    protected boolean fill = false;
    protected LinkedList<GameState> otherStates = new LinkedList<>();
    protected float transparency = 1;

    public Button(int x, int y, int size, String text, GameState renderState) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
        this.renderState = renderState;
        font = new Font("arial", 1, size);
        backColor = Color.BLUE;
        selectedColor = Color.GREEN;
        textColor = Color.RED;
    }

    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        if (transparency < 1) {
            g2d.setComposite(makeTransparent(transparency));
        }

        g.setFont(font);
        width = Engine.perc((g.getFontMetrics().stringWidth(text)), 110);
        if (width / size < 3)
            height = Engine.perc(width, 50);
        else height = Engine.perc(width, 25);

        g.setColor(backColor);
        if (fill)
            g.fill3DRect(x,y,width,height, true);
        else g.draw3DRect(x, y, width, height, true);

        if (selected)
            g.setColor(selectedColor);
        else g.setColor(textColor);
        g.drawString(text, x + Engine.perc(width, 5), y + Engine.perc(height, 75));

        if (transparency < 1) {
            g2d.setComposite(makeTransparent(1));
        }
    }

    public boolean mouseOverlap(int mx, int my) {
        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public boolean stateIs(GameState state) {
        boolean temp;
        if (otherStates.isEmpty())
            temp = state == this.renderState;
        else {
            temp = state == this.renderState;
            for (GameState gs: otherStates) {
                temp = temp || (gs == state);
            }
        }
        return temp;
    }

    public void setFill() {
        this.fill = true;
    }

    public GameState getRenderState() {
        return renderState;
    }

    // Not very sure what's happening here
    private AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

    public void addOtherState(GameState state) {
        otherStates.add(state);
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
