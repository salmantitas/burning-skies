package com.euhedral.game.Entities;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProceduralGenerator {

    public HashMap<Color, EntityID> colorMap;
    int height, width = 31;
    int xMid = width/2;
    int spawnZone, lastZone, lastLastZone;
    int wave, pauseBetweenWaves, pattern;
    int increment = 3;
    int level;

    /*
    * Basic - 1/1
    * Fast - 1/2
    * Move - 1/3
    * Snake - 1/4
    * */

    // Enemy spawning chances
    int spawnFast = 5;
    int spawnMove = spawnFast * 2;
    int spawnSnake = spawnMove * 2;

    // Spawning pick-up, every 50 units of level height
    int spawnPickupRate = 50;
    int spawnPickupChance = 1; // in 10;
    int spawnedPickupCount = 0;

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

    private EntityManager entityManager;

    public ProceduralGenerator(EntityManager entityManager) {
        this.entityManager = entityManager;
        colorMap = VariableManager.colorMap;
    }

    // generate a level using procedural generation
    public void generateLevel() {
        level = VariableManager.getLevel();
        switch (level) {
            case 1:
                height = 150; // 100 = ~4 waves
                break;
            case 2:
                height = 225;
                break;
            case 3:
                height = 275;
                break;
            case 4:
                height = 300;
                break;

        }

        System.out.printf("Width: %d, Height: %d\n", width, height);

        int remainingHeight = height;

        // spawn Player
        int x = 15 * 32, y = remainingHeight * 32;

        entityManager.spawnPlayer(xMid*32, height*32, height*32, VariableManager.power.getValue(), VariableManager.gotGround());
//        gameController.setCameraToPlayer(); // todo: move to Game Controller

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

            // check if pickup can be spawned
            if (level > 1) {
                spawnPickupHelper(remainingHeight, 0, 50, EntityID.PickupHealth);
//                if (spawnedPickupCount == 0 && height - remainingHeight >= 50) {
//                    entityManager.spawnPickup(xMid * 32, remainingHeight * 32, PickupID.Health, Color.green);
//                    spawnedPickupCount++;
//                }
            }

            if (level > 2) {
                spawnPickupHelper(remainingHeight, 1, 100, EntityID.PickupShield);
//                if (spawnedPickupCount == 1 && height - remainingHeight >= 100) {
//                    entityManager.spawnPickup(xMid * 32, remainingHeight * 32, PickupID.Shield, Color.yellow);
//                    spawnedPickupCount++;
//                }

                spawnPickupHelper(remainingHeight, 2, 150, EntityID.PickupHealth);

//                if (spawnedPickupCount == 2 && height - remainingHeight >= 150) {
//                    entityManager.spawnPickup(xMid * 32, remainingHeight * 32, PickupID.Health, Color.green);
//                    spawnedPickupCount++;
//                }
            }

            remainingHeight -= pauseBetweenWaves;
        }

        remainingHeight -= pauseBetweenWaves;
        if (level > 1)
            spawnBoss(xMid, remainingHeight);

    }

    /* Spawn Pattern Functions*/

    // Pickup Spawner Helped
    private void spawnPickupHelper(int remainingHeight, int num, int spawnHeight, EntityID id) {
        if (spawnedPickupCount == num && height - remainingHeight >= spawnHeight) {
            entityManager.spawnPickup(xMid * 32, remainingHeight * 32, id);
            spawnedPickupCount++;
        }
    }

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
        // todo: choose which to spawn depending on level
        // todo: then choose which to spawn depending on spawn chances

        EntityID id = colorMap.get(Color.RED);

        if (level >= 2) {
            int spawnChance = Utility.randomRange(0, spawnFast);
            if (spawnChance == 0)
                id = EntityID.EnemyFast;
        }

        if (level >= 3) {
            int spawnChance = Utility.randomRange(0, spawnMove);
            if (spawnChance == 0)
                id = EntityID.EnemyMove;
        }

        if (level >= 4) {
            int spawnChance = Utility.randomRange(0, spawnSnake);
            if (spawnChance == 0)
                id = EntityID.EnemySnake;
        }

        c = getKey(id);

        entityManager.spawnEntity(x*32, y*32, id, c);
    }

    private void spawnBoss(int x, int y) {
        entityManager.spawnBoss(1, x*32, y*32);
    }

    private void nextPattern() {
        pattern = Utility.randomRange(1, maxP);
    }

    private int calculateSkip() {
        return Utility.randomRange(0, 2);
    }

    private void determineSpawn() {
//        if level
    }

    public int getLevelHeight() {
        return height * 32;
    }

    private Color getKey(EntityID id) {
        for (Map.Entry<Color, EntityID> entry : colorMap.entrySet()) {
            if (Objects.equals(id, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
