package com.euhedral.game;

import com.euhedral.engine.Utility;
import com.euhedral.game.UI.UIHandler;

import java.awt.*;

public class Attribute {

    private int x = Utility.percWidth(2.5);
    private int y;
    private int defaultValue;
    private int MIN = 0;
    private int MAX;
    private int value;
    private int cost;
    private boolean active = false, binary;
    Font font;

    // Render Properties

    Color backgroundColor = Color.lightGray;
    Color foregroundColor;
    int fontSize;
    int width;
    int height;

    public Attribute(int defaultValue, boolean binary) {
        setDefaultValue(defaultValue);
        setMAX(defaultValue);
        setValue(defaultValue);
        this.binary = binary;
    }

    Attribute(int defaultValue, boolean binary, int cost) {
        this(defaultValue, binary);
        this.cost = cost;
    }

    public void reset() {
        this.value = defaultValue;
    }

    public void increase(int value) {
        this.value += value;
        if (this.value >= MAX)
            this.value = MAX;
    }

    public void decrease(int value) {
        this.value -= value;
        if (this.value <= MIN) {
            this.value = MIN;
        }
    }

    public void set(int value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getMAX() {
        return MAX;
    }

    public void setMAX(int MAX) {
        this.MAX = MAX;
    }

    public void setMIN(int MIN) {
        this.MIN = MIN;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCost() {
        return cost;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setForegroundColor(Color c) {
        foregroundColor = c;
    }

    public void setFontSize(int size) {
        fontSize = size;
        font = UIHandler.customFont.deriveFont(0, fontSize);
    }

    public void renderBar(Graphics g) {
        width = Utility.intAtWidth640(2);
        height = width * 6;
        g.setColor(backgroundColor);
        g.fillRect(x, y, MAX * width, height);
        g.setColor(foregroundColor);
        g.fillRect(x, y, value * width, height);
    }

    public void renderValue(Graphics g) {
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Power: " + value, x, y);
        Utility.log("Power: " + value);
    }
}
