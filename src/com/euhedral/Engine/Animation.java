package com.euhedral.Engine;

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

    int BASIC = 0;
    int INBETWEEN = 1;

    private int mode = BASIC;
    int sign = 1;

    public Animation(int speed, BufferedImage... args) {
        this.speed = speed;
        frames = args.length;

        images = new BufferedImage[frames];
        for(int i = 0; i < frames; i++) {
            images[i] = args[i];
        }
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
        if (mode == 0 ) {
            count++;
            if (count >= frames) {
                count = 0;
                playedOnce = true;
            }
        } else {
            if (count == -1) {
                int a = 1;
            }
            count += sign;

            if (count >= frames) {
                count = frames - 1;
                sign *= -1;
                playedOnce = true;
            } else if (count < 0) {
                count = 0;
                sign += -1;
                playedOnce = true;
            }
        }
    }

    public void drawAnimation(Graphics g, double x, double y) {
        g.drawImage(curr, (int) x, (int) y, null);
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

    public float getProgress() {
        return ((float) count)/ ((float) frames);
    }

    public int getImageWidth() {
        return images[0].getWidth();
    }

    public int getMaxFrames() {
        return frames;
    }

    public void setCurrentFrame(int i) {
        count = i;
    }

    public void setMode(int i) {
        mode = i;
    }
}
