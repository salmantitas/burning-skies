package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class TextureHandler {

    public BufferedImage[] player = new BufferedImage[3];
    public BufferedImage[] enemy = new BufferedImage[1];
    public BufferedImage[] explosion = new BufferedImage[7];
    public BufferedImage[] sea = new BufferedImage[8];

    public SpriteSheet playerSS, enemySS, explosionSS, seaSS;

    public BufferedImage playerImage = null;
    public BufferedImage enemyImage = null;
    public BufferedImage explosionImage = null;
    public BufferedImage title = null;
    public BufferedImage logo = null;
    public BufferedImage seaImage = null;

    public TextureHandler() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerImage = loader.loadImage("/player.png");
            enemyImage = loader.loadImage("/enemy.png");
            explosionImage = loader.loadImage("/explosion.png");
            title = loader.loadImage("/title.png");
            logo = loader.loadImage("/logo.png");
            seaImage = loader.loadImage("/sea.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSS = new SpriteSheet(playerImage);
        enemySS = new SpriteSheet(enemyImage);
        explosionSS = new SpriteSheet(explosionImage);
        seaSS = new SpriteSheet(seaImage);

        initializeTexture();
    }

    private void initializeTexture() {
        int w = Utility.intAtWidth640(32);
        int h = w;
        player[0] = playerSS.grabImage(1,1, w,h);
        player[1] = playerSS.grabImage(2,1, w,h);
        player[2] = playerSS.grabImage(3,1, w,h);

        enemy[0] = enemySS.grabImage(1,1,w,h);

        explosion[0] = explosionSS.grabImage(1,1,w,h);
        explosion[1] = explosionSS.grabImage(2,1,w,h);
        explosion[2] = explosionSS.grabImage(3,1,w,h);
        explosion[3] = explosionSS.grabImage(4,1,w,h);
        explosion[4] = explosionSS.grabImage(5,1,w,h);

        for (int i = 0; i < 8; i++) {
            sea[i] = seaSS.grabImage(i+1, 1, 32, 32);
        }
    }


}
