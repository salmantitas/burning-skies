package com.euhedral.engine;

import com.euhedral.game.Camera;
import com.euhedral.game.GameController;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.euhedral.engine.Engine.HEIGHT;
import static com.euhedral.engine.Engine.WIDTH;

public class Utility {

    /*
     * Returns the given percentage of a given parameter
     * */
    public static int perc(int parameter, double percentage) {
        return  (int) (parameter * percentage/ 100.0);
    }

    /*
     * Returns the given percentage of the screen width
     * */
    public static int percWidth(double percentage) {
        return perc(WIDTH, percentage);
    }

    /*
     * Returns the given percentage of the screen height
     * */
    public static int percHeight(double percentage) {
        return perc(HEIGHT, percentage);
    }

    /*
     * Scales the input integer up from what it would have been at screen width of 640
     * */
    public static int intAtWidth640(int var) {
        float factor = 640/var;
        return (int) (WIDTH/factor);
    }

    /*
     * Scales the input float up from what it would have been at screen width of 640
     * */
    public static float floatAtWidth640(int var) {
        float factor = 640/var;
        return (float) (WIDTH/factor);
    }

    /*
     * Limits the given integer between min and max
     * */
    public static int clamp(int var, int min, int max) {
        if (var <= min)
            return min;
        if (var >= max)
            return max;
        else return var;
    }

    /*
     * Limits the given float between min and max
     * */
    public static float clamp(float var, float min, float max) {
        if (var <= min)
            return min;
        if (var >= max)
            return max;
        else return var;
    }

    /*
     * Limits the given double between min and max
     * */
    public static double clamp(double var, double min, double max) {
        if (var <= min)
            return min;
        if (var >= max)
            return max;
        else return var;
    }

    public static int randomRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int randomRangeInclusive(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
//        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }

    public static boolean oddInt(int number) {
        return (number % 2 != 0);
    }

    public static boolean evenInt(int number) {
        return (number % 2 == 0);
    }

    /*******************
     * Debug Functions *
     *******************/

    private static void debugSettings(Graphics g) {
        g.setFont(new Font("arial", 1, Utility.percWidth(1.5)));
        g.setColor(Color.WHITE);
    }

    public static void drawState(Graphics g) {
        debugSettings(g);
        g.drawString("State: " + Engine.currentState, Utility.percWidth(85), Utility.percHeight(4));
    }

    public static void drawCommand(Graphics g) {
        debugSettings(g);
        g.drawString("Command: " + GameController.cmd, Utility.percWidth(25), Utility.percHeight(45));
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void log(String[] arg) {
        for (String s: arg) {
            log(s);
            log(", ");
        }
    }

}
