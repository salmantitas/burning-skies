package com.euhedral.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelGenerator {

    private GameController gameController;

    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
    }

    public void loadImageLevel(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        System.out.printf("Width: %d, Height: %d\n", width, height);

        for (int j = height - 1; j >= 0; j--) {
            for (int i = width - 1; i >= 0; i--) {
                int pixel = image.getRGB(i, j);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                // Game Code//

                if (r == 0 && g == 0 && b == 255)
                    gameController.spawnPlayer(i*32, j*32, height*32);

                // Air Enemies

                else if (r == 255 && g == 0 && b == 0)
                    gameController.spawnEnemy(i*32, j*32, EnemyID.Basic, ContactID.Air, new Color(r,g,b));
                else if (r == 150 && g == 0 && b == 0)
                    gameController.spawnEnemy(i*32, j*32, EnemyID.Move, ContactID.Air, new Color(r,g,b));
                else if (r == 100 && g == 0 && b == 0)
                    gameController.spawnEnemy(i*32, j*32, EnemyID.Snake, ContactID.Air, new Color(r,g,b));
                else if (r == 200 && g == 0 && b == 0)
                    gameController.spawnEnemy(i*32, j*32, EnemyID.Fast, ContactID.Air, new Color(r,g,b));

                // Ground Enemies

                else if (r == 255 && g == 150 && b == 244)
                    gameController.spawnEnemy(i*32, j*32, EnemyID.Basic, ContactID.Ground, new Color(r,g,b));

                // Pickups

                else if (r == 0 && g == 255 && b == 0) {
                    gameController.spawnPickup(i*32, j*32, PickupID.Health, new Color(r,g,b));
                }

                else if (r == 255 && g == 216 && b == 0)
                    gameController.spawnBoss(i*32, j*32);
            }
        }

        gameController.spawnFlag();
        gameController.setLevelHeight(height*32);
    }


}
