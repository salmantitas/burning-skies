package com.euhedral.engine;

import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
import com.euhedral.game.VariableManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class LevelGenerator {

    public HashMap<Color, EntityID> colorMap;
    int width = 31;
    int spawnZone = 3;
    int waves;

    private GameController gameController;

    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
        colorMap = VariableManager.colorMap;
    }

    // generate a level
    public void generateLevel() {
        int level = VariableManager.getLevel();
        int height = 200;// * Utility.randomRange(1, 1); // stub
        gameController.setLevelHeight(height * 32);

        System.out.printf("Width: %d, Height: %d\n", width, height);

        int remainingHeight = height;
        while (remainingHeight > 0) {
            // spawn Player
            int x = 15 * 32, y = remainingHeight * 32;

            gameController.spawnEntity(x, y, EntityID.Player, Color.BLUE);

            // create distance between player and first wave
            remainingHeight -= 25;

            // for every wave
            waves = 10; // stub

            // for every zone
            spawnZone = 2;

            // choose spawn pattern
            String pattern = "line"; // stub
            int num = 1; //Utility.randomRange(1,5);

            // spawn enemies

            // from left
            if (spawnZone == 1) {
                spawnLeft(num, remainingHeight);
            }
            // spawn from middle
            else if (spawnZone == 2) {
                // even num
                if (num % 2 == 0) {

                } // odd num

                else {
                    if (num == 1) {
                        int tileSize = Utility.intAtWidth640(32);

                        // x0 = screenstart + enemywidth
                        int x0 = 1 * tileSize;

                        int j = remainingHeight; // stub
                        Color c = Color.RED; // stub
                        x = (Engine.WIDTH + tileSize) / 2;
                        y = j * 32;

                        EntityID id = colorMap.get(c);
                        gameController.spawnEntity(x, y, id, c);
                    }
                }
            }
            // spawn from right
            else if (spawnZone == 3) {
                spawnRight(num, remainingHeight);
            }
            // pause between next wave
            remainingHeight -= waves;
            break;
        }
    }

    public void loadImageLevel(BufferedImage image) {
        width = image.getWidth();
        int height = image.getHeight();
        gameController.setLevelHeight(height * 32);

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
                    gameController.spawnEntity(x, y, id, c);
                }
            }
        }
    }

    private void spawnLeft(int num, int remainingHeight) {
        int x, y;
        for (int i = 0; i < num; i++) {

            int tileSize = Utility.intAtWidth640(32);

            // x0 = screenstart + enemywidth
            int x0 = 1 * tileSize;

            int j = remainingHeight; // stub
            Color c = Color.RED; // stub
            x = x0 + (i) * (2 * tileSize);
            y = j * 32;

            EntityID id = colorMap.get(c);
            gameController.spawnEntity(x, y, id, c);
        }
    }

    private void spawnRight(int num, int remainingHeight) {
        int x, y;
        for (int i = 0; i < num; i++) {

            int tileSize = Utility.intAtWidth640(32);

            // screenend - 2 * enemywidth
            int xF = Engine.WIDTH - 2 * tileSize;

            int j = remainingHeight; // stub
            Color c = Color.RED; // stub
            x = xF - (i) * (3 * tileSize);
            y = j * 32;

            EntityID id = colorMap.get(c);
            gameController.spawnEntity(x, y, id, c);

        }
    }


}
