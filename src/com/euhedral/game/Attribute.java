package com.euhedral.game;

import com.euhedral.engine.Utility;

public class Attribute {

    private int x = Utility.percWidth(2.5);
    private int y = Utility.percHeight(5);
    private int defaultValue;
    private int MIN = 0;
    private int MAX;
    private int value;
    private int cost;

    public Attribute(int defaultValue) {
        this.defaultValue = defaultValue;
        MAX = defaultValue;
        value = defaultValue;
    }

    Attribute(int defaultValue, int cost) {
        this(defaultValue);
        this.cost = cost;
    }

    public void reset() {
        this.value = defaultValue;
    }

    public void increase(int value) {
        this.value += value;
        if (value >= MAX)
            set(MAX);
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
}
