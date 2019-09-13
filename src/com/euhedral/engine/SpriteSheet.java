package com.euhedral.engine;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage image;

    public SpriteSheet(BufferedImage image) {
        this.image = image;
    }

    public SpriteSheet(String path) {
        image = Engine.loader.loadImage(path);
    }

    public BufferedImage grabImage(int col, int row, int width, int height) {
        BufferedImage img = image.getSubimage(col*width - width, row*height-height, width, height);
        return img;
    }
}
