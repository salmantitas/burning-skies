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

                    // Game Code

                    // todo: move spawnPlayer under spawnEntity
                    if (id == EntityID.Player) {
                        gameController.spawnPlayer(x, y,height * 32);
                    }
                    else
                        gameController.spawnEntity(x, y, id, c);
                }
            }

            gameController.spawnFlag();
            gameController.setLevelHeight(height * 32);
        }
    }


}
