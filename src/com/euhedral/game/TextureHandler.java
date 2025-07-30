package com.euhedral.game;

import com.euhedral.engine.Animation;
import com.euhedral.engine.BufferedImageLoader;
import com.euhedral.engine.SpriteSheet;
import com.euhedral.engine.Utility;

import java.awt.image.BufferedImage;

public class TextureHandler {

    private BufferedImage
            playerImage,
            enemyDroneImage,
            enemyDownImage,
            enemySide1Image,
//            enemyHeavyImage,
//            enemyStaticImage,
            enemySide2Image,
            explosionImage,
            impactSmallImage,
            logo,
            seaImage,
            pickupImage,
            bulletPlayerImage,
            bulletEnemyImage;

    private SpriteSheet
            playerSS,
            enemyDroneSS,
            enemyDownSS,
            enemySide1SS,
//            enemyHeavySS,
//            enemyStaticSS,
            enemySide2SS,
            explosionSS,
            impactSmallSS,
            seaSS,
            pickupSS,
            bulletPlayerSS,
            bulletEnemySS;


    public BufferedImage[] player = new BufferedImage[9];
    public BufferedImage[] enemy = new BufferedImage[3];
    public BufferedImage[] enemyHeavy = new BufferedImage[1];
    public BufferedImage[] enemyFast = new BufferedImage[1];
    public BufferedImage[] enemyStatic = new BufferedImage[3];
    public BufferedImage[] enemySide = new BufferedImage[4];
    public BufferedImage[] enemyDrone = new BufferedImage[3];
    public BufferedImage[] explosion = new BufferedImage[4];
    public BufferedImage[] impactSmall = new BufferedImage[4];
    public BufferedImage[] sea = new BufferedImage[8];
    public BufferedImage[] pickup = new BufferedImage[3];
    public BufferedImage[] bulletPlayer = new BufferedImage[1];
    public BufferedImage[] bulletEnemy = new BufferedImage[1];

    public TextureHandler() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerImage = loader.loadImage("/player.png");
            enemyDownImage = loader.loadImage("/enemyDown.png");
            enemyDroneImage = loader.loadImage("/enemyDrone.png");
            enemySide1Image = loader.loadImage("/enemySide1.png");
//            enemyHeavyImage = loader.loadImage("/enemy2.png");
//            enemyStaticImage = loader.loadImage("/enemyStatic.png");
            enemySide2Image = loader.loadImage("/enemySide2.png");
            explosionImage = loader.loadImage("/explosion.png");
            impactSmallImage = loader.loadImage("/impactsmall.png");
            logo = loader.loadImage("/logo.png");
            seaImage = loader.loadImage("/sea.png");
            pickupImage = loader.loadImage("/pickup.png");
            bulletPlayerImage = loader.loadImage("/bulletplayer.png");
            bulletEnemyImage = loader.loadImage("/bulletenemy.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSS = new SpriteSheet(playerImage);
        enemyDownSS = new SpriteSheet(enemyDownImage);
        enemySide1SS = new SpriteSheet(enemySide1Image);
//        enemyHeavySS = new SpriteSheet(enemyHeavyImage);
//        enemyStaticSS = new SpriteSheet(enemyStaticImage);
        enemySide2SS = new SpriteSheet(enemySide2Image);
        enemyDroneSS = new SpriteSheet(enemyDroneImage);
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

        for (int i = 0; i < 3; i ++) {
            enemy[i] = enemyDownSS.grabImage(i + 1, 1, w, h);
//            enemy[0] = enemyBasicDownSS.grabImage(1, 1, w, h);
//            enemy[1] = enemyBasicDownSS.grabImage(2, 1, w, h);
//            enemy[2] = enemyBasicDownSS.grabImage(3, 1, w, h);
        }

//        w = 128;

        enemyHeavy[0] = enemyDownSS.grabImage(1,2,w,h);
        enemyFast[0] = enemyDownSS.grabImage(2,2,w,h);
        enemyStatic[0] = enemyDownSS.grabImage(1,3,w,h);
        enemyStatic[1] = enemyDownSS.grabImage(2,3,w,h);
        enemyStatic[2] = enemyDownSS.grabImage(3,3,w,h);
        enemySide[0] = enemySide1SS.grabImage(1,1,w,h);
        enemySide[1] = enemySide1SS.grabImage(2,1,w,h);
        enemySide[2] = enemySide2SS.grabImage(1,1,w,h);
        enemySide[3] = enemySide2SS.grabImage(2,1,w,h);

        w = 32;
        h = 32;

        for (int i = 0; i < 3; i ++) {
            enemyDrone[i] = enemyDroneSS.grabImage(i+1,1,w,h);
        }

        for (int i = 0; i < 4; i ++) {
            explosion[i] = explosionSS.grabImage(i+1, 1, w, h);
        }

        for (int i = 0; i < 4; i ++) {
            impactSmall[i] = impactSmallSS.grabImage(i+1, 1, w, h);
        }

        for (int i = 0; i < 8; i++) {
            sea[i] = seaSS.grabImage(i+1, 1, w, h);
        }

        w = Utility.intAtWidth640(16);
        h = w;

        pickup[0] = pickupSS.grabImage(1,1, w, h);
        pickup[1] = pickupSS.grabImage(2,1, w, h);
        pickup[2] = pickupSS.grabImage(3,1, w, h);

        w = 4;
        h = 12;
        bulletPlayer[0] = bulletPlayerSS.grabImage(1,1,w,h);

        w = 16;
        h = 16;
        bulletEnemy[0] = bulletEnemySS.grabImage(1,1,w,h);
    }

    public static Animation initExplosion(int speed) {
        return new Animation(speed, GameController.getTexture().explosion[0],
                GameController.getTexture().explosion[1],
                GameController.getTexture().explosion[2],
                GameController.getTexture().explosion[3]
        );
    }


}
