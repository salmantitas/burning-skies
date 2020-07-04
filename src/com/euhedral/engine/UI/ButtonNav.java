package com.euhedral.engine.UI;/*
 * Do not modify
 * */

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;

import java.awt.*;

public class ButtonNav extends Button {
    private GameState targetSate;

    public ButtonNav(int x, int y, int size, String text, GameState targetSate) {
        super(x, y, size, text);
        this.targetSate = targetSate;
    }

    public ButtonNav(int x, int y, int size, String text, GameState targetSate, boolean fill) {
        this(x, y, size, text, targetSate);
        font = new Font("arial", 1, size);
        backColor = Color.BLUE;
        textColor = Color.RED;
        this.fill = fill;
    }

    public ButtonNav(int x, int y, int size, String text, GameState targetSate, Color backColor, Color textColor) {
        this(x, y, size, text, targetSate);
        font = new Font("arial", 1, size);
        this.backColor = backColor;
        this.textColor = textColor;
    }

    public ButtonNav(int x, int y, int size, String text, GameState targetSate, Color backColor, Color textColor, Font font) {
        this(x, y, size, text, targetSate);
        this.font = font;
        this.backColor = backColor;
        this.textColor = textColor;
    }

    public GameState getTargetSate() {
        return targetSate;
    }

    @Override
    public void activate() {
        super.activate();
        Engine.setState(targetSate);
    }
}
