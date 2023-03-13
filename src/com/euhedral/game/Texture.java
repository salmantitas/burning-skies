package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class Texture {

    public BufferedImage[] player = new BufferedImage[3];
    public BufferedImage[] enemy = new BufferedImage[1];

    public SpriteSheet ps, es;

    public BufferedImage playerSheet = null;
    public BufferedImage enemySheet = null;

    public Texture() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerSheet = loader.loadImage("/player.png");
            enemySheet = loader.loadImage("/enemy.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ps = new SpriteSheet(playerSheet);
        es = new SpriteSheet(enemySheet);

        initializeTexture();
    }

    private void initializeTexture() {
        int w = Utility.intAtWidth640(32);
        int h = w;
        player[0] = ps.grabImage(1,1, w,h);
        player[1] = ps.grabImage(2,1, w,h);
        player[2] = ps.grabImage(3,1, w,h);

        enemy[0] = es.grabImage(1,1,w,h);
    }


}
