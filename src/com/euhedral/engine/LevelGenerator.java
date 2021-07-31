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
    int spawnZone, lastZone, lastLastZone;
    int wave, waves, pattern;

    enum Pattern {
        line,
        v,
    }

    int maxP = 2;

    private GameController gameController;

    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
        colorMap = VariableManager.colorMap;
    }

    // Generate level from an image
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

    // generate a level using procedural generation
    public void generateLevel() {
        int level = VariableManager.getLevel();
        int height = 200;// * Utility.randomRange(1, 1); // stub
        gameController.setLevelHeight(height * 32);

        System.out.printf("Width: %d, Height: %d\n", width, height);

        int remainingHeight = height;
        int numwaves = 3; // testVar

        // spawn Player
        int x = 15 * 32, y = remainingHeight * 32;

        gameController.spawnEntity(x, y, EntityID.Player, Color.BLUE);

        // create distance between player and first wave
        remainingHeight -= Engine.HEIGHT / 32;
        wave = 1;

        while (remainingHeight > 0) {
            // for every wave
            waves = 10; // stub
            nextPattern();

            // for every zone
            lastLastZone = lastZone;
            lastZone = spawnZone;
            spawnZone = 2;// Utility.randomRange(1, 3);
//            while (spawnZone == lastZone && spawnZone == lastLastZone) {
//                spawnZone = Utility.randomRange(1,3);
//            }

            // choose spawn pattern
//            int pattern = Utility.randomRange(1, maxP);
            int num = 1; //Utility.randomRange(1,5);

            int tileSize = Utility.intAtWidth640(32);

            // spawn enemies

            System.out.println(spawnZone + " " + num);

            // from left
            if (spawnZone == 1) {
                spawnLeft(num, remainingHeight, 1);

            }
            // spawn from middle
            else if (spawnZone == 2) {
                spawnMiddle(num, remainingHeight);

            }
            // spawn from right
            else if (spawnZone == 3) {
                spawnRight(num, remainingHeight, Engine.WIDTH/32 - 3);
            }
            // pause between next wave
            remainingHeight -= waves;
//            break;
        }
    }

    /* Recursive Spawning Functions */

    private void spawnLeft(int num, int y, int x) {
        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        // Base Case

        if (num > 1) {
            int x0 = x + 3;
            spawnLeft(num - 1, y, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnRight(int num, int y, int x) {
        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        // Base Case

        if (num > 1) {
            int x0 = x - 3;
            spawnRight(num - 1, y, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnMiddle(int num, int remainingHeight) {
        int tileSize = Utility.intAtWidth640(32);
        int x = (width - 1) / 2;
        int y = remainingHeight;
        Color c = Color.RED; // stub

        // odd numb
        if (num % 2 != 0) {
            spawnHelper(x, y, c);

            if (num > 1) {
                num -= 1;
                num = num / 2;
                spawnLeft(num, y, x + 3);
                spawnRight(num, y, x - 3);
            }
        } else {
            num = num /2;

            spawnLeft(num, y, x + 1);
            spawnRight(num, y, x - 1);

        }
    }

    private void spawnHelper(int x, int y, Color c) {
        EntityID id = colorMap.get(c);
        gameController.spawnEntity(x * 32, y * 32, id, c);
    }

    private void nextPattern() {
        pattern = Utility.randomRange(1, maxP);
    }
}
