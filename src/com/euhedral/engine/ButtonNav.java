package com.euhedral.engine;/*
 * Do not modify
 * */

import java.awt.*;

public class ButtonNav extends Button {
    private GameState targetSate;

    public ButtonNav(int x, int y, int size, String text, GameState renderState, GameState targetSate) {
        super(x, y, size, text, renderState);
        this.targetSate = targetSate;
    }

    public ButtonNav(int x, int y, int size, String text, GameState renderState, GameState targetSate, boolean fill) {
        this(x, y, size, text, renderState, targetSate);
        font = new Font("arial", 1, size);
        backColor = Color.BLUE;
        textColor = Color.RED;
        this.fill = fill;
    }

    public ButtonNav(int x, int y, int size, String text, GameState renderState, GameState targetSate, Color backColor, Color textColor) {
        this(x, y, size, text, renderState, targetSate);
        font = new Font("arial", 1, size);
        this.backColor = backColor;
        this.textColor = textColor;
    }

    public ButtonNav(int x, int y, int size, String text, GameState renderState, GameState targetSate, Color backColor, Color textColor, Font font) {
        this(x, y, size, text, renderState, targetSate);
        this.font = font;
        this.backColor = backColor;
        this.textColor = textColor;
    }

    public GameState getTargetSate() {
        return targetSate;
    }
}
