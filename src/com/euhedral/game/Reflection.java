package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;

import java.awt.*;

public class Reflection {
    public static final double screenCenterX = Engine.WIDTH / 2, screenCenterY = Engine.HEIGHT/2;
    public static final double sizeOffset = 0.8;
    public static final int xCorrection = 8;
    public static final int yCorrection = 12;

    int reflectionX, reflectionY, newWidth, newHeight;

    double offsetX, offsetY;

    public double calculateOffsetX(double objectCenterX) {
        return offsetX = (screenCenterX - objectCenterX) / 15;
    }

    public double calculateOffsetY(double objectCenterY) {
        return offsetY = (screenCenterY - objectCenterY) / 15;
    }

    public int calculateReflectionX(double x, double centerX) {
        return (int) (xCorrection + x + calculateOffsetX(centerX));
    }

    public int calculateReflectionY(double y, double centerY) {
        return (int) (yCorrection + y + calculateOffsetY(centerY));
    }

    public void render(Graphics2D g, Image image, double x, double centerX, double y, double centerY, int width, int height, float transparency) {
        g.setComposite(Utility.makeTransparent(transparency));

        reflectionX = calculateReflectionX(x, centerX);
        reflectionY = calculateReflectionY(y, centerY);
        newWidth = (int) (width * sizeOffset);
        newHeight = (int) (height * sizeOffset);

        g.drawImage(image, reflectionX, reflectionY, newWidth, newHeight, null);

        g.setComposite(Utility.makeTransparent(1f));
    }
}
