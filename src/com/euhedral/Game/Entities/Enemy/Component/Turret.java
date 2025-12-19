package com.euhedral.Game.Entities.Enemy.Component;

import com.euhedral.Engine.Position;
import com.euhedral.Game.Entities.Enemy.Enemy;

public class Turret {
    private Enemy parent;
    private Position relativePosition;
    private double angle;
    private double velocity;
    private boolean tracking;

    public Turret(double x, double y, double velocity, double angle, boolean tracking, Enemy parent) {
        this.relativePosition = new Position(x, y);
        this.angle = angle;
        this.velocity = 3;
        this.tracking = false;
        this.parent = parent;
    }

    public Turret(double x, double y, Enemy parent) {
        this(x, y, 3, 90, false, parent);
    }

    public double getX() {
        return parent.getPos().x + relativePosition.x;
    }

    public double getY() {
        return parent.getPos().y + relativePosition.y;
    }

    public double getAngle() {
        return angle;
    }
}
