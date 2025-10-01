package com.euhedral.Engine;

public class Position {
    public double x;
    public double y;

    public Position(double x, double y) {
        setPosition(x, y);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
