package com.euhedral.game;

import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class TextureHandler {

    private BufferedImage
            playerImage,
            enemyBasicImage,
            enemyHeavyImage,
            explosionImage,
            impactSmallImage,
            title,
            logo,
            seaImage,
            pickupImage,
            bulletPlayerImage,
            bulletEnemyImage;

    private SpriteSheet
            playerSS,
            enemySS,
            enemyHeavySS,
            explosionSS,
            impactSmallSS,
            seaSS,
            pickupSS,
            bulletPlayerSS,
            bulletEnemySS;


    public BufferedImage[] player = new BufferedImage[9];
    public BufferedImage[] enemy = new BufferedImage[1];
    public BufferedImage[] enemyHeavy = new BufferedImage[1];
    public BufferedImage[] explosion = new BufferedImage[7];
    public BufferedImage[] impactSmall = new BufferedImage[4];
    public BufferedImage[] sea = new BufferedImage[8];
    public BufferedImage[] pickup = new BufferedImage[3];
    public BufferedImage[] bulletPlayer = new BufferedImage[1];
    public BufferedImage[] bulletEnemy = new BufferedImage[1];

    public TextureHandler() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerImage = loader.loadImage("/player.png");
            enemyBasicImage = loader.loadImage("/enemy1.png");
            enemyHeavyImage = loader.loadImage("/enemy2.png");
            explosionImage = loader.loadImage("/explosion.png");
            impactSmallImage = loader.loadImage("/impactsmall.png");
            title = loader.loadImage("/title.png");
            logo = loader.loadImage("/logo.png");
            seaImage = loader.loadImage("/sea.png");
            pickupImage = loader.loadImage("/pickup.png");
            bulletPlayerImage = loader.loadImage("/bulletplayer.png");
            bulletEnemyImage = loader.loadImage("/bulletenemy.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSS = new SpriteSheet(playerImage);
        enemySS = new SpriteSheet(enemyBasicImage);
        enemyHeavySS = new SpriteSheet(enemyHeavyImage);
        explosionSS = new SpriteSheet(explosionImage);
        impactSmallSS = new SpriteSheet(impactSmallImage);
        seaSS = new SpriteSheet(seaImage);
        pickupSS = new SpriteSheet(pickupImage);
        bulletPlayerSS = new SpriteSheet(bulletPlayerImage);
        bulletEnemySS = new SpriteSheet(bulletEnemyImage);

        initializeTexture();
    }

    private void initializeTexture() {
        int w = Utility.intAtWidth640(32);
        int h = w;
        player[0] = playerSS.grabImage(1,1, w,h);
        player[1] = playerSS.grabImage(1,2, w,h);
        player[2] = playerSS.grabImage(1,3, w,h);
        player[6] = playerSS.grabImage(2,1, w,h);
        player[7] = playerSS.grabImage(2,2, w,h);
        player[8] = playerSS.grabImage(2,3, w,h);
        player[3] = playerSS.grabImage(3,1, w,h);
        player[4] = playerSS.grabImage(3,2, w,h);
        player[5] = playerSS.grabImage(3,3, w,h);

        enemy[0] = enemySS.grabImage(1,1,w,h);

        for (int i = 0; i < 5; i ++) {
            explosion[i] = explosionSS.grabImage(i+1, 1, w, h);
        }

        w = 128;

        enemyHeavy[0] = enemyHeavySS.grabImage(1,1,w,h);

        w = 32;
        h = 32;

        for (int i = 0; i < 4; i ++) {
            impactSmall[i] = impactSmallSS.grabImage(i+1, 1, w, h);
        }

        for (int i = 0; i < 8; i++) {
            sea[i] = seaSS.grabImage(i+1, 1, w, h);
        }

        w = Utility.intAtWidth640(16);
        h = w*2;

        pickup[0] = pickupSS.grabImage(1,1, w, h);
        pickup[1] = pickupSS.grabImage(2,1, w, h);
        pickup[2] = pickupSS.grabImage(3,1, w, h);

        w = 4;
        h = 12;
        bulletPlayer[0] = bulletPlayerSS.grabImage(1,1,w,h);

        w = 8;
        h = 48;
        bulletEnemy[0] = bulletEnemySS.grabImage(1,1,w,h);
    }


}
