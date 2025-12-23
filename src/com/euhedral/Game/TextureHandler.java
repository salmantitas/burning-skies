package com.euhedral.Game;

import com.euhedral.Engine.Animation;
import com.euhedral.Engine.BufferedImageLoader;
import com.euhedral.Engine.SpriteSheet;
import com.euhedral.Engine.Utility;

import java.awt.image.BufferedImage;

public class TextureHandler {

    private BufferedImage
            playerImage,
            enemyDownImage,
            enemyDownDamageImage,
            enemyHeavyImage,
            enemyDroneImage,
            enemyDroneLImage,
            enemySideImage,
            explosionImage,
            impactSmallImage,
            enemyBoss1Image,
            enemyBoss2Image,
            enemyBoss3Image,
            enemyBoss4Image,
            logo,
            seaImage,
            cloudImage,
            pickupImage,
            bulletPlayerImage,
            bulletEnemyImage;

    private SpriteSheet
            playerSS,
            enemyDownSS,
            enemyDownDamageSS,
            enemyHeavySS,
            enemyDroneSS,
            enemyDroneLSS,
            enemySideSS,
            explosionSS,
            impactSmallSS,
            enemyBoss1SS,
            enemyBoss2SS,
            enemyBoss3SS,
            enemyBoss4SS,
            seaSS,
            cloudSS,
            pickupSS,
            bulletPlayerSS,
            bulletEnemySS;


    public BufferedImage[] player = new BufferedImage[9];
    public BufferedImage[] playerDamage = new BufferedImage[3];
    public BufferedImage[] enemy = new BufferedImage[12];
    public BufferedImage[] enemyDamage = new BufferedImage[3];
    public BufferedImage[] enemyHeavy = new BufferedImage[2];
    public BufferedImage[] enemyFast = new BufferedImage[1];
    public BufferedImage[] enemyStatic = new BufferedImage[3];
    public BufferedImage[] enemySide = new BufferedImage[8];
    public BufferedImage[] enemyDrone = new BufferedImage[6];
    public BufferedImage[] enemyDroneL = new BufferedImage[1];
    public BufferedImage[] enemyBoss = new BufferedImage[4];
    public BufferedImage[] explosion = new BufferedImage[4];
    public BufferedImage[] impactSmall = new BufferedImage[4];
    public BufferedImage[] sea = new BufferedImage[8];
    public BufferedImage[] cloud = new BufferedImage[1];
    public BufferedImage[] pickup = new BufferedImage[5];
    public BufferedImage[] bulletPlayer = new BufferedImage[1];
    public BufferedImage[] bulletEnemy = new BufferedImage[1];

    public TextureHandler() {
        BufferedImageLoader loader = new BufferedImageLoader();

        try {
            playerImage = loader.loadImage("/player.png");
            enemyDownImage = loader.loadImage("/enemyDown.png");
            enemyDownDamageImage = loader.loadImage("/enemyDamage.png");
            enemyHeavyImage = loader.loadImage("/enemyHeavy.png");
            enemyDroneImage = loader.loadImage("/enemyDrone.png");
            enemyDroneLImage = loader.loadImage("/enemyDroneL.png");
            enemySideImage = loader.loadImage("/enemySide.png");
            enemyBoss1Image = loader.loadImage("/enemyBoss1.png");
            enemyBoss2Image = loader.loadImage("/enemyBoss2.png");
            enemyBoss3Image = loader.loadImage("/enemyBoss3.png");
            enemyBoss4Image = loader.loadImage("/enemyBoss4.png");
            explosionImage = loader.loadImage("/explosion.png");
            impactSmallImage = loader.loadImage("/impactsmall.png");
            logo = loader.loadImage("/logo.png");
            seaImage = loader.loadImage("/sea.png");
            cloudImage = loader.loadImage("/cloud.png");
            pickupImage = loader.loadImage("/pickup.png");
            bulletPlayerImage = loader.loadImage("/bulletPlayer.png");
            bulletEnemyImage = loader.loadImage("/bulletEnemy.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSS = new SpriteSheet(playerImage);
        enemyDownSS = new SpriteSheet(enemyDownImage);
        enemyDownDamageSS = new SpriteSheet(enemyDownDamageImage);
        enemyHeavySS = new SpriteSheet(enemyHeavyImage);
        enemySideSS = new SpriteSheet(enemySideImage);
        enemyDroneSS = new SpriteSheet(enemyDroneImage);
        enemyDroneLSS = new SpriteSheet(enemyDroneLImage);
        enemyBoss1SS = new SpriteSheet(enemyBoss1Image);
        enemyBoss2SS = new SpriteSheet(enemyBoss2Image);
        enemyBoss3SS = new SpriteSheet(enemyBoss3Image);
        enemyBoss4SS = new SpriteSheet(enemyBoss4Image);
        explosionSS = new SpriteSheet(explosionImage);
        impactSmallSS = new SpriteSheet(impactSmallImage);
        seaSS = new SpriteSheet(seaImage);
        cloudSS = new SpriteSheet(cloudImage);
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

        playerDamage[0] = playerSS.grabImage(1,4, w,h);
        playerDamage[1] = playerSS.grabImage(2,4, w,h);
        playerDamage[2] = playerSS.grabImage(3,4, w,h);

        for (int i = 0; i < 4; i ++) {
            enemy[i] = enemyDownSS.grabImage(i + 1, 1, w, h);
        }

        for (int i = 0; i < 4; i ++) {
            enemy[i + 4] = enemyDownSS.grabImage(i + 1, 2, w, h);
        }

        for (int i = 0; i < 4; i ++) {
            enemy[i + 8] = enemyDownSS.grabImage(i + 1, 3, w, h);
        }

        enemyDamage[0] = enemyDownDamageSS.grabImage(1,1, w, h);
        enemyDamage[1] = enemyDownDamageSS.grabImage(1,2, w, h);
        enemyDamage[2] = enemyDownDamageSS.grabImage(2,2, w, h);

//        w = 128;

//        enemyHeavy[0] = enemyDownSS.grabImage(1,2,w,h);
        enemyFast[0] = enemyDownSS.grabImage(2,2,w,h);
        enemyStatic[0] = enemyDownSS.grabImage(1,3,w,h);
        enemyStatic[1] = enemyDownSS.grabImage(2,3,w,h);
        enemyStatic[2] = enemyDownSS.grabImage(3,3,w,h);

        enemySide[0] = enemySideSS.grabImage(1,1,w,h);
        enemySide[1] = enemySideSS.grabImage(2,1,w,h);
        enemySide[2] = enemySideSS.grabImage(1,2,w,h);
        enemySide[3] = enemySideSS.grabImage(2,2,w,h);
        enemySide[4] = enemySideSS.grabImage(1,3,w,h);
        enemySide[5] = enemySideSS.grabImage(2,3,w,h);
        enemySide[6] = enemySideSS.grabImage(1,4,w,h);
        enemySide[7] = enemySideSS.grabImage(2,4,w,h);

        enemyDroneL[0] = enemyDroneLSS.grabImage(1,1,w,h);

        w = 8;
        h = 8;

        for (int i = 0; i < 2; i ++) {
            enemyHeavy[i] = enemyHeavySS.grabImage(i + 1, 1, w, h);
        }

        w = 32;
        h = 32;

        for (int i = 0; i < 3; i ++) {
            enemyDrone[i] = enemyDroneSS.grabImage(i+1,1,w,h);
        }
        for (int i = 0; i < 3; i ++) {
            enemyDrone[3 + i] = enemyDroneSS.grabImage(i + 1,2,w,h);
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

        for (int i = 0; i < 5; i++) {
            pickup[i] = pickupSS.grabImage(i + 1,1, w, h);
        }

        w = 4;
        h = 8;
        bulletPlayer[0] = bulletPlayerSS.grabImage(1,1,w,h);

        w = 16;
        h = 16;
        bulletEnemy[0] = bulletEnemySS.grabImage(1,1,w,h);

        w = 48;
        h = 54;
        cloud[0] = cloudSS.grabImage(1,1,w,h);

        w = 16;
        h = 8;
        enemyBoss[0] = enemyBoss1SS.grabImage(1,1,w,h);

        w = 8;
        h = 8;
        enemyBoss[1] = enemyBoss2SS.grabImage(1,1,w,h);
        enemyBoss[2] = enemyBoss3SS.grabImage(1,1,w,h);

        h = 16;
        enemyBoss[3] = enemyBoss4SS.grabImage(1,1,w,h);
    }

    public static Animation initExplosion(int speed) {
        return new Animation(speed, GameController.getTexture().explosion[0],
                GameController.getTexture().explosion[1],
                GameController.getTexture().explosion[2],
                GameController.getTexture().explosion[3]
        );
    }


}
