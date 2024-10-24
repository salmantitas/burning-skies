package com.euhedral.engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Animation {

    // todo: the whole class desperately needs to be reworked

    private int speed, frames;

    private int index = 0;
    private int count = 0; // the frame currently being displayed

    private BufferedImage[] images;
    private BufferedImage curr;
    public boolean playedOnce = false;

    public Animation(int speed, BufferedImage... args) {
        this.speed = speed;
        images = new BufferedImage[args.length];
        for(int i = 0; i < args.length; i++) {
            images[i] = args[i];
        }
        frames = args.length;
    }

    // todo: change so it only runs once, create a separate function called loopAnimation that loops
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
            playedOnce = true;
        }
    }

    public void drawAnimation(Graphics g, int x, int y) {
        g.drawImage(curr, x, y, null);
    }

    public void drawAnimation(Graphics g, int x, int y, int targetWidth, int targetHeight) {
        g.drawImage(curr, x, y, targetWidth, targetHeight, null);
    }

    public boolean isFrame(int i) {
        return count == i;
    }

    public int getCount() {
        return count;
    }

    public void endAnimation() {
        count = 0;
        curr = null;
    }
}
