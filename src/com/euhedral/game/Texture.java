package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.Engine;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class Texture {

    public BufferedImage[] player = new BufferedImage[4];

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
        int w = Utility.intAtWidth640(32);
        int h = w;
        player[0] = ps.grabImage(1,1, w,h);
        player[1] = ps.grabImage(2,1, w,h);
        player[2] = ps.grabImage(3,1, w,h);
        player[3] = ps.grabImage(4,1, w,h);
    }


}
