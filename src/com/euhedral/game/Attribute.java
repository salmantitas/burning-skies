package com.euhedral.game;

import com.euhedral.engine.Utility;

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

    // Render Properties

    Color backgroundColor = Color.lightGray;
    Color foregroundColor;

    int fontSize;

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
    }

    public void renderBar(Graphics g) {
        int width = Utility.intAtWidth640(2);
        int height = width * 6;
        g.setColor(backgroundColor);
        g.fillRect(x, y, MAX * width, height);
        g.setColor(foregroundColor);
        g.fillRect(x, y, value * width, height);
    }

    public void renderValue(Graphics g) {
        g.setFont(new Font("arial", 1, fontSize));
        g.setColor(Color.WHITE);
        g.drawString("Power: " + value, x, y);
    }
}
