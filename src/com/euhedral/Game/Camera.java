package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Entity;

public class Camera {

    private float x,y;
    private float marker;
    private boolean fixed = true; // fixed camera stays in one place, dynamic follows an entity
    private Entity e;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Camera(float x, float y, Entity e) {
        this(x, y);
        this.e = e;
        fixed = false;
    }

    public void update() {
        // Do something with Entity
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getMarker() {
        return marker;
    }

    public void setMarker(float marker) {
        this.marker = marker - Engine.HEIGHT;
    }
}
