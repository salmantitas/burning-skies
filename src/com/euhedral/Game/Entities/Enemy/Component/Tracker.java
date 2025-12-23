package com.euhedral.Game.Entities.Enemy.Component;

import com.euhedral.Game.EntityHandler;

public class Tracker {
    public double destinationX, destinationY;

    public void updateDestination() {
        destinationX = EntityHandler.playerPositon.x;
        destinationY = EntityHandler.playerPositon.y;
    }

    public double calculateAngle(double x, double y) {
        return Utility.calculateAngle(x, y, destinationX, destinationY); // stub
    }
}
