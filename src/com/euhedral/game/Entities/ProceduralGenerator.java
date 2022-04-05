package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
import com.euhedral.game.VariableManager;

import java.awt.*;
import java.util.HashMap;

public class ProceduralGenerator {

    public HashMap<Color, EntityID> colorMap;
    int width = 31;
    int xMid = width/2;
    int spawnZone, lastZone, lastLastZone;
    int wave, pauseBetweenWaves, pattern;
    int increment = 3;
    int level;

    enum Pattern {
        line,
        v;

        public static Pattern fromInteger(int x) {
            switch(x) {
                case 0:
                    return line;
                case 1:
                    return v;
            }
            return null;
        }
    }

    int maxP = Pattern.values().length;

    private GameController gameController;

    public ProceduralGenerator(GameController gameController) {
        this.gameController = gameController;
        colorMap = VariableManager.colorMap;
    }

    // generate a level using procedural generation
    public void generateLevel() {
        level = VariableManager.getLevel();
        int height = 200;
        gameController.setLevelHeight(height * 32);

        System.out.printf("Width: %d, Height: %d\n", width, height);

        int remainingHeight = height;
        int numwaves = 3; // testVar

        // spawn Player
        int x = 15 * 32, y = remainingHeight * 32;

        gameController.spawnPlayer(x, y, height*32);

        // create distance between player and first wave
        remainingHeight -= Engine.HEIGHT / 32;
        wave = 1;

        // max spawns in V pattern
        int minV = 3;
        int maxV = 7;

        // max spawns in line pattern
        int minL = 1;
        int maxL = 5;

        // pauses between each waves
        int minPause = 5;
        pauseBetweenWaves = minPause;

        while (remainingHeight > 0) {
            // for every wave
            nextPattern();

            // for every zone
            lastLastZone = lastZone;
            lastZone = spawnZone;
            spawnZone = Utility.randomRange(1, 3);

            while (spawnZone == lastZone && spawnZone == lastLastZone) {
                spawnZone = Utility.randomRange(1,3);
            }

            // choose spawn pattern
            Pattern pattern = Pattern.fromInteger (Utility.randomRange(0, maxP));
            int num = 1;

            if (pattern == Pattern.line) {
                num = Utility.randomRange(1, maxL);
                pauseBetweenWaves = minPause + (num - minL);
            }
            if (pattern == Pattern.v) {
                num = Utility.randomRange(minV, maxV);
                pauseBetweenWaves = 2* minPause + (num - minV) * 4;
            }

            int tileSize = Utility.intAtWidth640(32);

            // spawn enemies

            switch (pattern) {
                case line: spawnLine(num, remainingHeight);
                break;
                case v: spawnV(num, remainingHeight);
                break;
            }

            remainingHeight -= pauseBetweenWaves;
        }

        remainingHeight -= pauseBetweenWaves;
        spawnBoss(xMid, remainingHeight);

    }

    /* Spawn Pattern Functions*/

    private void spawnLine(int num, int remainingHeight) {
        System.out.println(spawnZone + " " + num);

        switch (spawnZone) {
            case 1:
                spawnFromLeft(num, remainingHeight, 1);
                break;
            case 2:
                spawnFromMiddle(num, remainingHeight);
                break;
            case 3:
                spawnFromRight(num, remainingHeight, Engine.WIDTH/32 - 3);
                break;
        }

        // pause between next wave
        remainingHeight -= pauseBetweenWaves;
    }

    private void spawnV(int num, int remainingHeight) {
        int xStart, xMid, xLast;
        xStart = 6;
        xMid = (width - 1) / 2;
        xLast = width - xStart;
        switch (spawnZone) {
            case 1:
                spawnFromMiddleV(xStart, num, remainingHeight);
                break;
            case 2:
                spawnFromMiddleV(xMid, num, remainingHeight);
                break;
            case 3:
                spawnFromMiddleV(xLast, num, remainingHeight);
                break;
        }
    }

    /* Recursive Spawning Functions */

    private void spawnFromLeft(int num, int y, int x) {
//        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x + (increment + skip);
            spawnFromLeft(num - 1, y, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnFromRight(int num, int y, int x) {
        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x - (increment + skip);
            spawnFromRight(num - 1, y, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnFromMiddle(int num, int remainingHeight) {
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
                int skip = calculateSkip();

                spawnFromLeft(num, y, x + (increment + skip));

                skip = calculateSkip();

                spawnFromRight(num, y, x - (increment + skip));
            }
        } else {
            num = num /2;

            spawnFromLeft(num, y, x + 1);
            spawnFromRight(num, y, x - 1);

        }
    }

    private void spawnFromMiddleV(int x, int num, int remainingHeight) {
        int tileSize = Utility.intAtWidth640(32);
        int y = remainingHeight;
        Color c = Color.RED; // stub

        int incrementX = 2;

        int y0 = y - increment;

        // odd numb
        if (num % 2 != 0) {
            spawnHelper(x, y, c);

            if (num > 1) {
                num -= 1;
                num = num / 2;

                int xL = x + (incrementX);

                spawnFromLeftV(num, y0, xL);

                int xR = x - (incrementX);

                spawnFromRightV(num, y0, xR);
            }
        } else {
            num = num /2;

            spawnFromLeftV(num, y0, x + 1);
            spawnFromRightV(num, y0, x - 1);

        }
    }

    private void spawnFromLeftV(int num, int y, int x) {
//        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        // Base Case

        int incrementX = 2;
        int y0 = y - increment;

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x + (incrementX);
            spawnFromLeftV(num - 1, y0, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnFromRightV(int num, int y, int x) {
        int tileSize = Utility.intAtWidth640(32);

        Color c = Color.RED; // stub

        int incrementX = 2;
        int y0 = y - increment;

        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x - (incrementX);
            spawnFromRightV(num - 1, y0, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnHelper(int x, int y, Color c) {
        EntityID id = colorMap.get(c);
        gameController.spawnEntity(x * 32, y * 32, id, c);
    }

    private void spawnBoss(int x, int y) {
        gameController.spawnEntity(x * 32, y * 32, EntityID.Boss, Color.YELLOW);
    }

    private void nextPattern() {
        pattern = Utility.randomRange(1, maxP);
    }

    private int calculateSkip() {
        return Utility.randomRange(0, 2);
    }

    // calculate how long the pause should be between each wave, depending on the pattern
    private int recalculatePause(int num, Pattern pattern) {
        float factor = 1;
        if (pattern == Pattern.v) {
            factor *= 1.2;
        }
        factor += num/10;
        return (int) (10 * factor);
    }

    private void determineSpawn() {
//        if level
    }
}
