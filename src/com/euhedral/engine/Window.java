package com.euhedral.engine;/*
 * Do not modify this class
 * */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends Canvas {

    JFrame frame;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double widthTest = screenSize.getWidth();
    double heightTest = screenSize.getHeight();

    // Creates a window for the game with the given width and height. The string title is used as the window's title.
    // Locks the screen at the specified dimension so that it cannot be resized. The game is added to the screen and
    // is started.
    public Window(int width, int height, String title, Engine engine) {
        frame = new JFrame(title);
        System.out.println("Window created");

        Engine.SCALE = heightTest / Engine.HEIGHT;

//        Dimension dimension = new Dimension((int) (width*scale), (int) (height * scale));
        Dimension dimension = new Dimension((int) (width * Engine.SCALE), (int) heightTest);
        System.out.println("Dimensions set");

//        frame.setUndecorated(true);
//        System.out.println("Window undecorated");

        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);
        System.out.println("Screen forced to set dimensions");

        frame.setResizable(false);
        System.out.println("Disable resize");

//        frame.setCursor( frame.getToolkit().createCustomCursor(
//                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
//                new Point(),
//                null ) );
//        System.out.println("Cursor invisible");

        frame.setFocusable(true); // not sure yet

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(engine, BorderLayout.CENTER);
        System.out.println("Engine added to window");

//        frame.requestFocus();
        frame.pack();

        frame.setLocationRelativeTo(null);
        System.out.println("Screen centered");

        frame.setVisible(true);

        // Test
//        Engine.SCALE = widthTest / Engine.WIDTH;


        engine.start();
    }
}
