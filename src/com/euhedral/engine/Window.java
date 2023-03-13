package com.euhedral.engine;/*
 * Do not modify this class
 * */

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {

    // Creates a window for the game with the given width and height. The string title is used as the window's title.
    // Locks the screen at the specified dimension so that it cannot be resized. The game is added to the screen and
    // is started.
    public Window(int width, int height, String title, Engine engine) {
        JFrame frame = new JFrame(title);
        System.out.println("Window created");
        Dimension dimension = new Dimension(width, height);
        System.out.println("Dimensions set");

        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);
        System.out.println("Screen forced to set dimensions");

        frame.setLocationRelativeTo(null);
        System.out.println("Screen centered");

        frame.setResizable(false);
        System.out.println("Disable resize");

        frame.setFocusable(true); // not sure yet

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(engine);
        System.out.println("Engine added to window");

        frame.setVisible(true);
        frame.requestFocus();
        engine.start();
    }
}
