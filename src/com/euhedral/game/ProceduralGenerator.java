package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.Enemy.EnemyGround;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProceduralGenerator {

    public HashMap<Color, EntityID> colorMap;
    int height, width = 31;
    int xStart = 1, xEnd = width - 3;
    int xMid = width/2;
    int spawnZone, lastZone, lastLastZone;
    int pattern, lastPattern, lastLastPattern;
    int wave, pauseBetweenWaves, waveSinceHealth;
    int spacingHorizontal = 3;
    int level;
    int remainingHeight;
    int playerX = xMid*32, playerY = Engine.HEIGHT;
    final int ENDLESS = -1;

    int SPAWN_ENEMY = 0;
    int SPAWN_HEALTH = 10;
    int spawnNext = 0;
    int MIN_WAVE_HEALTH_SPAWN = 15;
    int MIN_WAVE_BETWEEN_HEALTH_SPAWN = 10;

    /*
    * Basic - 1/1
    * Fast - 1/2
    * Move - 1/3
    * Snake - 1/4
    * */

    // Enemy spawning chances
    int spawnFast = 5;
    int spawnMove = spawnFast * 3;
    int spawnSnake = spawnMove * 2;

    // Spawning pick-up, every 50 units of level height
    int spawnPickupRate = 50;
    int spawnPickupChance = 1; // in 10;
    int spawnedPickupCount = 0;

    // Ground enemy spawn rate
    int groundRate = 50;
    int groundChance = 1; // in 10;
    int groundCount = 0;

    int[][] enemyNumbers; // [Pattern][min/max numbers]
    int ENEMY_MIN = 0;
    int ENEMY_MAX = 1;

    // ground spawns
    int minG = 1;
    int maxG = 2;

    // pauses between each waves
    int minPause = 5;

    int PATTERN_LINE = 0;
    int PATTERN_V = 1;

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

    private EntityHandler entityHandler;

    public ProceduralGenerator(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        colorMap = VariableHandler.colorMap;

        buildMatrix();
    }

    private void buildMatrix() {
        // spawns in V pattern
        int minEnemiesV = 3;
        int maxEnemiesV = 7;

        // spawns in line pattern
        int minEnemiesL = 1;
        int maxEnemiesL = 5;

        enemyNumbers = new int[2][2];
        enemyNumbers[PATTERN_LINE][ENEMY_MIN] = minEnemiesL;
        enemyNumbers[PATTERN_LINE][ENEMY_MAX] = maxEnemiesL;
        enemyNumbers[PATTERN_V][ENEMY_MIN] = minEnemiesV;
        enemyNumbers[PATTERN_V][ENEMY_MAX] = maxEnemiesV;
    }

    // generate a level using procedural generation
    public void generateLevel() {
        level = VariableHandler.getLevel();
        switch (level) {
            case ENDLESS:
                height = ENDLESS;
                break;
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

        remainingHeight = height;

        entityHandler.setLevelHeight(getLevelHeight());
        entityHandler.spawnPlayer(playerX, height * 32);

        // create distance between player and first wave
        remainingHeight -= Engine.HEIGHT / 32;
        wave = 1;

        pauseBetweenWaves = minPause;

//        generateEnemies();
    }

    public void update() {
        spawnNext = Utility.randomRangeInclusive(SPAWN_ENEMY, SPAWN_HEALTH);
        if (wave == MIN_WAVE_HEALTH_SPAWN) {
            spawnHealth();
        } else if (spawnNext == SPAWN_HEALTH && wave >= MIN_WAVE_HEALTH_SPAWN && waveSinceHealth >= MIN_WAVE_BETWEEN_HEALTH_SPAWN)
            spawnHealth();
        else
            generateEnemiesHelper();
//        System.out.println("Spawn Code: " + spawnNext);
        System.out.printf("Remaining Height: %d\n", remainingHeight);
    }

    private void generateEnemiesHelper() {
            // for every wave
        waveSinceHealth++;
        nextPattern();

        // for every zone
        lastLastZone = lastZone;
        lastZone = spawnZone;
        spawnZone = Utility.randomRange(1, 3);

        // determine that no zone spawns more than twice in a row
        while (spawnZone == lastZone && spawnZone == lastLastZone) {
            spawnZone = Utility.randomRange(1, 3);
        }

        // choose spawn pattern
        Pattern pattern = Pattern.fromInteger(Utility.randomRange(0, maxP));
        int num = 1, minEnemies = 1, maxEnemies = 1;

        if (pattern == Pattern.line) {
            minEnemies = enemyNumbers[PATTERN_LINE] [ENEMY_MIN];
            maxEnemies = enemyNumbers[PATTERN_LINE] [ENEMY_MAX];
        }
        if (pattern == Pattern.v) {
            minEnemies = enemyNumbers[PATTERN_V] [ENEMY_MIN];
            maxEnemies = enemyNumbers[PATTERN_V] [ENEMY_MAX];
        }

        num = Utility.randomRange(minEnemies, maxEnemies);

//        System.out.printf("Num: %d\n", num);
//
        pattern = Pattern.v;

        // spawn enemies

        switch (pattern) {
            case line:
                spawnLine(num);
                break;
            case v:
                spawnV(num);
                break;
        }

        wave++;

        int minEnemy = 1;

        if (pattern == Pattern.line) {
            minEnemy = enemyNumbers[PATTERN_LINE][ENEMY_MIN];
            pauseBetweenWaves = minPause + (num - minEnemy);
        }
        if (pattern == Pattern.v) {
            minEnemy = enemyNumbers[PATTERN_V][ENEMY_MIN];
            pauseBetweenWaves = 2 * minPause + (num - minEnemy) * 4;
        }

        remainingHeight -= pauseBetweenWaves;

    }

    private void spawnHealth() {
        spawnZone = Utility.randomRange(1, 3);
        spawnPickupHelper2(remainingHeight, EntityID.PickupHealth);
        wave++;
        waveSinceHealth = 0;
        remainingHeight -= minPause;
    }

    // Pickup Spawner Helped
    private void spawnPickupHelper2(int remainingHeight, EntityID id) {
        entityHandler.spawnPickup(xMid * 32, remainingHeight * 32, id);
    }

    private void generateEnemies() {
        // todo: Every can spawn either of the 3 - Air Enemy, Ground Enemy, Pickup
        while (remainingHeight > 0) {
            // for every wave
            nextPattern();

            // spawn pickups
            if (level > 1) {
                spawnPickupHelper(remainingHeight, 0, 50, EntityID.PickupHealth);
            }

            if (level > 2) {
                spawnPickupHelper(remainingHeight, 1, 100, EntityID.PickupShield);
                spawnPickupHelper(remainingHeight, 2, 150, EntityID.PickupHealth);
            }

            if (level > 3) {
                spawnPickupHelper(remainingHeight, 3, 200, EntityID.PickupShield);
                spawnPickupHelper(remainingHeight, 3, 250, EntityID.PickupHealth);
            }

            // spawn ground
            if (level > 3) {
                spawnGroundLeft(remainingHeight, 75, 0);
                spawnGroundRight(remainingHeight, 150, 1);
                spawnGroundLeft(remainingHeight, 175, 2);
                spawnGroundRight(remainingHeight, 250, 3);
            }

            // for every zone
            lastLastZone = lastZone;
            lastZone = spawnZone;
            spawnZone = Utility.randomRange(1, 3);

            // determine that no zone spawns more than twice in a row
            while (spawnZone == lastZone && spawnZone == lastLastZone) {
                spawnZone = Utility.randomRange(1,3);
            }

            // choose spawn pattern
            Pattern pattern = Pattern.fromInteger (Utility.randomRange(0, maxP));
            int num = 1;

//            if (pattern == Pattern.line) {
//                num = Utility.randomRange(1, maxEnemiesL);
//                pauseBetweenWaves = minPause + (num - minEnemiesL);
//            }
//            if (pattern == Pattern.v) {
//                num = Utility.randomRange(minEnemiesV, maxEnemiesV);
//                pauseBetweenWaves = 2* minPause + (num - minEnemiesV) * 4;
//            }

            // spawn enemies

//            switch (pattern) {
//                case line: spawnLine(num, remainingHeight);
//                break;
//                case v: spawnV(num, remainingHeight);
//                break;
//            }

            remainingHeight -= pauseBetweenWaves;
        }

        remainingHeight -= pauseBetweenWaves;
        spawnBoss(xMid, remainingHeight);

    }

    /* Spawn Pattern Functions*/

    // Pickup Spawner Helped
    private void spawnPickupHelper(int remainingHeight, int num, int spawnHeight, EntityID id) {
        if (spawnedPickupCount == num && height - remainingHeight >= spawnHeight) {
            entityHandler.spawnPickup(xMid * 32, remainingHeight * 32, id);
            spawnedPickupCount++;
        }
    }

    private void spawnLine(int num) {
//        System.out.println(spawnZone + " " + num);

//        int spawnHeight = (height -Engine.HEIGHT/32) - wave*pauseBetweenWaves;
//        System.out.printf("Spawn Height = %d\n", spawnHeight);

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
    }

    private void spawnV(int num) {
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
            int x0 = x + (spacingHorizontal + skip);
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
            int x0 = x - (spacingHorizontal + skip);
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

                spawnFromLeft(num, y, x + (spacingHorizontal + skip));

                skip = calculateSkip();

                spawnFromRight(num, y, x - (spacingHorizontal + skip));
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

        int y0 = y - spacingHorizontal;

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
        int y0 = y - spacingHorizontal;

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
        int y0 = y - spacingHorizontal;

        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x - (incrementX);
            spawnFromRightV(num - 1, y0, x0);
        }

        spawnHelper(x, y, c);
    }

    private void spawnHelper(int x, int y, Color c) {

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

        entityHandler.spawnEntity(x*32, y*32, id, c);
    }

    private void spawnGround(int remainingHeight, int spawnHeight, int num, String spawnFrom) {
        if (groundCount == num && height - remainingHeight >= spawnHeight) {
            EnemyGround eG = new EnemyGround(xStart * 32, remainingHeight * 32, getLevelHeight());
            groundCount++;
            if (spawnFrom == "left") {
                eG.setHMove("right");
            } else if (spawnFrom == "right") {
                eG.setHMove("left");
                eG.setX(xEnd*32);
            }
            entityHandler.addEnemy(eG);
        }
    }

    private void spawnGroundLeft(int remainingHeight, int spawnHeight, int num) {
        spawnGround(remainingHeight, spawnHeight, num, "left");
    }

    private void spawnGroundRight(int remainingHeight, int spawnHeight, int num) {
        spawnGround(remainingHeight, spawnHeight, num, "right");
    }

    private void spawnBoss(int x, int y) {
        entityHandler.spawnBoss(level, x*32, y*32);
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
