package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;

import java.awt.image.BufferedImage;

public class Texture {

    public BufferedImage[] player = new BufferedImage[1];

    public SpriteSheet ps;

    public BufferedImage playerSheet = null;

    public Texture() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerSheet = loader.loadImage("/player.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ps = new SpriteSheet(playerSheet);

        initializeTexture();
    }

    private void initializeTexture() {
        player[0] = ps.grabImage(1,1,32,32);
    }


}
