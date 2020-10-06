package com.euhedral.engine.UI;

import com.euhedral.engine.GameState;

import java.awt.*;
import java.util.LinkedList;

public abstract class MenuItem {

    protected int x, y, width, height;
    protected float transparency = 0.7f;
    protected Color backColor = Color.darkGray;
    protected GameState renderState;
    protected LinkedList<GameState> otherStates = new LinkedList<>();

    public MenuItem(int x, int y, int width, int height, GameState renderState) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.renderState = renderState;
    }

    public MenuItem(int x, int y, int width, int height, GameState renderState, float transparency, Color backColor) {
        this(x, y, width, height, renderState);
        this.transparency = transparency;
        this.backColor = backColor;
    }

    public abstract void render(Graphics g);

    protected void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    protected void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public void addOtherState(GameState state) {
        otherStates.add(state);
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

    // Not very sure what's happening here
    protected AlphaComposite makeTransparent(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }

}
