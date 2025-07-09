package com.euhedral.engine;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage image;
    private BufferedImage grabbedImage;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    public SpriteSheet(String path) {
        image = Engine.loader.loadImage(path);
    }

    // col, row >= 1
    public BufferedImage grabImage(int col, int row, int width, int height) {
        grabbedImage = image.getSubimage(col*width - width, row*height-height, width, height);
        return grabbedImage;
    }
}
