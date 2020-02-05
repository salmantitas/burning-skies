package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.Engine;
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

    public void initializeTexture() {
        player[0] = ps.grabImage(3,1, Engine.intAtWidth640(32),Engine.intAtWidth640(32));
    }


}
