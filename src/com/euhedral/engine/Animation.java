package com.euhedral.engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {

    private int speed, frames;

    private int index = 0;
    private int count = 0; // the frame currently being displayed

    private BufferedImage[] images;
    private BufferedImage curr;

    public Animation(int speed, BufferedImage... args) {
        this.speed = speed;
        images = new BufferedImage[args.length];
        for(int i = 0; i < args.length; i++) {
            images[i] = args[i];
        }
        frames = args.length;
    }

    public void runAnimation() {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
        }
    }

    private void nextFrame() {
        curr = images[count];
        count++;
        if (count >= frames) {
            count = 0;
        }
    }

    public void drawAnimation(Graphics g, int x, int y) {
        g.drawImage(curr, x, y, null);
    }

    public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
        g.drawImage(curr, x, y, scaleX, scaleY, null);
    }
}
