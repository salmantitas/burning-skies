package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class TextureHandler {

    public BufferedImage
            playerImage,
            enemyImage,
            explosionImage,
            title,
            logo,
            seaImage,
            pickupImage;

    public SpriteSheet
            playerSS,
            enemySS,
            explosionSS,
            seaSS,
            pickupSS;


    public BufferedImage[] player = new BufferedImage[3];
    public BufferedImage[] enemy = new BufferedImage[1];
    public BufferedImage[] explosion = new BufferedImage[7];
    public BufferedImage[] sea = new BufferedImage[8];
    public BufferedImage[] pickup = new BufferedImage[2];

    public TextureHandler() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerImage = loader.loadImage("/player.png");
            enemyImage = loader.loadImage("/enemy.png");
            explosionImage = loader.loadImage("/explosion.png");
            title = loader.loadImage("/title.png");
            logo = loader.loadImage("/logo.png");
            seaImage = loader.loadImage("/sea.png");
            pickupImage = loader.loadImage("/health.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSS = new SpriteSheet(playerImage);
        enemySS = new SpriteSheet(enemyImage);
        explosionSS = new SpriteSheet(explosionImage);
        seaSS = new SpriteSheet(seaImage);
        pickupSS = new SpriteSheet(pickupImage);

        initializeTexture();
    }

    private void initializeTexture() {
        int w = Utility.intAtWidth640(32);
        int h = w;
        player[0] = playerSS.grabImage(1,1, w,h);
        player[1] = playerSS.grabImage(1,2, w,h);
        player[2] = playerSS.grabImage(1,3, w,h);

        enemy[0] = enemySS.grabImage(1,1,w,h);

        explosion[0] = explosionSS.grabImage(1,1,w,h);
        explosion[1] = explosionSS.grabImage(2,1,w,h);
        explosion[2] = explosionSS.grabImage(3,1,w,h);
        explosion[3] = explosionSS.grabImage(4,1,w,h);
        explosion[4] = explosionSS.grabImage(5,1,w,h);

        w = 32;
        h = 32;

        for (int i = 0; i < 8; i++) {
            sea[i] = seaSS.grabImage(i+1, 1, w, h);
        }

        w = Utility.intAtWidth640(16);
        h = w*2;

        pickup[0] = pickupSS.grabImage(1,1, w, h);
    }


}
