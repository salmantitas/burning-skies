package com.euhedral.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class LevelGenerator {

    public HashMap<Color, EntityID> colorMap;

    private GameController gameController;

    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
        colorMap = new HashMap<>();
        colorMap.put(new Color(0,0,255), EntityID.Player );
        colorMap.put(new Color(255,0,0), EntityID.EnemyBasic);
        colorMap.put(new Color(150,0,0), EntityID.EnemyMove);
        colorMap.put(new Color(100,0,0), EntityID.EnemySnake);
        colorMap.put(new Color(200,0,0), EntityID.EnemyFast);
        colorMap.put(new Color(255,150,244), EntityID.EnemyGround);
        colorMap.put(new Color(0,255,0), EntityID.Pickup);
        colorMap.put(new Color(255,216,0), EntityID.Boss);
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

                Color c = new Color(r, g, b);

                if (colorMap.containsKey(c)) {

                    int x = i * 32;
                    int y = j * 32;

                    EntityID id = colorMap.get(c);
//                    gameController.spawnEntity(x, y, id, color);

                    // Game Code//

                    if (id == EntityID.Player) {
                        gameController.spawnPlayer(i * 32, j * 32, height * 32);
                    }

                    // Air Enemies

                    else if (id == EntityID.EnemyBasic) {
                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Basic, ContactID.Air, c);
                    }
                    else if (id == EntityID.EnemyMove) {
                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Move, ContactID.Air, c);
                    }
                    else if (id == EntityID.EnemySnake) {
                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Snake, ContactID.Air, c);
                    }
                    else if (id == EntityID.EnemyFast) {
                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Fast, ContactID.Air, c);
                    }

                    // Ground Enemies

                    else if (id == EntityID.EnemyGround) {
                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Basic, ContactID.Ground, new Color(r, g, b));
                    }

                    // Pickups

                    else if (id == EntityID.Pickup) {
                        gameController.spawnPickup(i * 32, j * 32, PickupID.Health, new Color(r, g, b));
                    }

                    // Boss

                    else if (id == EntityID.Boss) {
                        gameController.spawnBoss(i * 32, j * 32);
                    }

//                    else if (r == 255 && g == 0 && b == 0)
//                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Basic, ContactID.Air, new Color(r, g, b));
//                    else if (r == 150 && g == 0 && b == 0)
//                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Move, ContactID.Air, new Color(r, g, b));
//                    else if (r == 100 && g == 0 && b == 0)
//                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Snake, ContactID.Air, new Color(r, g, b));
//                    else if (r == 200 && g == 0 && b == 0)
//                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Fast, ContactID.Air, new Color(r, g, b));

                        // Ground Enemies

//                    else if (r == 255 && g == 150 && b == 244)
//                        gameController.spawnEnemy(i * 32, j * 32, EnemyID.Basic, ContactID.Ground, new Color(r, g, b));
//
//                        // Pickups
//
//                    else if (r == 0 && g == 255 && b == 0) {
//                        gameController.spawnPickup(i * 32, j * 32, PickupID.Health, new Color(r, g, b));
//                    } else if (r == 255 && g == 216 && b == 0)
//                        gameController.spawnBoss(i * 32, j * 32);
                }
            }

            gameController.spawnFlag();
            gameController.setLevelHeight(height * 32);
        }
    }


}
