package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.Enemy.EnemyGround;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* todo: decrease pause after wave after difficulty rises
* */

public class ProceduralGenerator {

    public HashMap<Color, EntityID> colorMap;
    int height, width = 35;
    final int incrementMIN = Utility.intAtWidth640(1);
    int spacing = 3;
    int xStart = incrementMIN, xEnd = width;
    int xMid = (xEnd - xStart)/2 + xStart;
    int spawnZone, lastZone, lastLastZone;
    int pattern, lastPattern, lastLastPattern;
    int wave, waveSinceHealth, waveSincePower, waveSinceShield;
    int level;
    int playerX = xMid*32, playerY;
    final int ENDLESS = -1;
    int spawnHeight;

    long lastSpawnTime;
    long lastSpawnTimePickup;
    long spawnInterval;
    long spawnIntervalPickups;
    long spawnInterval_MIN = 2;
    long spawnInterval_MAX = 5;

    final int SPAWN_ENEMY = 0;
    final int SPAWN_HEALTH = 10;
    int spawnNext = 0;
    int MIN_WAVE_HEALTH_SPAWN = 15;
    int MIN_WAVE_BETWEEN_HEALTH_SPAWN = 10;
    int MAX_WAVE_BETWEEN_HEALTH_SPAWN = 20;

    int MIN_WAVE_SHIELD_SPAWN = 40;
    int MIN_WAVE_BETWEEN_SHIELD_SPAWN = 30;
    int MAX_WAVE_BETWEEN_SHIELD_SPAWN = 50;

    int MIN_WAVE_POWER_SPAWN = 70;
    int MIN_WAVE_BETWEEN_POWER_SPAWN = 30;
    int MAX_WAVE_BETWEEN_POWER_SPAWN = 80;

    int difficulty = 1;

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
    final int ENEMY_MIN = 0;
    final int ENEMY_MAX = 1;

    // ground spawns
    int minG = 1;
    int maxG = 2;

    final int PATTERN_LINE = 0;
    final int PATTERN_V = 1;
    final int PATTERN_SQUARE = 2;
    final int PATTERN_CROSS = 3;
    final int maxPatterns = 2;

    // basic enemy movement time
    final int TIME_MIN = 20;
    final int TIME_MAX = TIME_MIN * 10;

    private EntityHandler entityHandler;

    public ProceduralGenerator(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        colorMap = VariableHandler.colorMap;

        buildMatrix();
    }

    private void buildMatrix() {
        int minEnemiesV = 3;
        int maxEnemiesV = 9;

        int minEnemiesLine = 2;
        int maxEnemiesLine = 8;

        int minEnemiesSquare = 2;
        int maxEnemiesSquare = 4; // MAX 8 possible but not fun

        int minEnemiesCross = 3;
        int maxEnemiesCross = 3;

        enemyNumbers = new int[maxPatterns][2];
        enemyNumbers[PATTERN_LINE][ENEMY_MIN] = minEnemiesLine;
        enemyNumbers[PATTERN_LINE][ENEMY_MAX] = maxEnemiesLine;
        enemyNumbers[PATTERN_V][ENEMY_MIN] = minEnemiesV;
        enemyNumbers[PATTERN_V][ENEMY_MAX] = maxEnemiesV;
//        enemyNumbers[PATTERN_SQUARE][ENEMY_MIN] = minEnemiesSquare;
//        enemyNumbers[PATTERN_SQUARE][ENEMY_MAX] = maxEnemiesSquare;
//        enemyNumbers[PATTERN_CROSS][ENEMY_MIN] = minEnemiesCross;
//        enemyNumbers[PATTERN_CROSS][ENEMY_MAX] = maxEnemiesCross;
    }

    // generate a level using procedural generation // YEAH NO SHIT.
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

        difficulty = 1;
        entityHandler.setLevelHeight(getLevelHeight());

        playerY = getLevelHeight();

        entityHandler.spawnPlayer(playerX, playerY);

        // create distance between player and first wave
        wave = 1;
        System.out.println("Wave: " + wave);

        // todo: use line for first wave here

        spawnHeight = (height - Engine.HEIGHT/32) ;//- (wave * MIN_PAUSE);
        lastSpawnTime = GameController.getCurrentTime();
        spawnInterval = spawnInterval_MIN;
        spawnIntervalPickups = spawnInterval_MAX;

//        spawnFirstWave();
    }

    // Spawns an enemy or a pickup depending on how long ago the last one has been spawned
    public void update() {
        long timeNowMillis = GameController.getCurrentTime();
        long timeSinceLastSpawnMillis = timeNowMillis - lastSpawnTime;
        boolean canSpawn = spawnInterval <= timeSinceLastSpawnMillis;

        // Pickup or Enemies
        // Can spawn multiple pickups
        // todo: Improve the logic
        if (canSpawn) {
            boolean canSpawnPickups = spawnIntervalPickups <= timeSinceLastSpawnMillis;
//            if (wave == (MIN_WAVE_SHIELD_SPAWN - 1))
//                spawnShield();
//            else if (wave == (MIN_WAVE_POWER_SPAWN - 1))
//                spawnPower();
//            else
                if (wave == (MIN_WAVE_HEALTH_SPAWN - 1))
                spawnHealth();
            else {
                /*
                * todo:
                *  If (canSpawnPickups) {
                *  wrap everything below
                *  } else spawnEnemies
                * */
//                if (waveSinceShield >= (MAX_WAVE_BETWEEN_SHIELD_SPAWN - 1))
//                    spawnShield();
//                else if (waveSincePower >= (MAX_WAVE_BETWEEN_POWER_SPAWN - 1) && VariableHandler.power.getValue() <= 2)
//                    spawnPower();
//                else
                    if (waveSinceHealth >= (MAX_WAVE_BETWEEN_HEALTH_SPAWN - 1))
                    spawnHealth();
                else
                    spawnEnemies();
            }
            lastSpawnTime = GameController.getCurrentTime();
        }
//        System.out.println("Spawn Code: " + spawnNext);
    }

    private void spawnFirstWave() {
        lastLastPattern = lastPattern;
        lastPattern = pattern;
        pattern = PATTERN_LINE;

        int minEnemies = enemyNumbers[pattern][ENEMY_MIN];
        int maxEnemies = enemyNumbers[pattern][ENEMY_MAX];
        int currentMax = Math.min(minEnemies + difficulty, maxEnemies);

        int num = Utility.randomRangeInclusive(minEnemies, currentMax);

        spawnLine(num);
        wave++;
        calculateSpawnInterval(num, maxEnemies);
    }

    private void spawnEnemies() {
            // for every wave
        waveSinceHealth++;
        waveSincePower++;
        waveSinceShield++;

        // for every zone
        lastLastZone = lastZone;
        lastZone = spawnZone;
        spawnZone = Utility.randomRangeInclusive(1, 3);

        // determine that no zone spawns more than twice in a row
        while (spawnZone == lastZone && spawnZone == lastLastZone) {
            spawnZone = Utility.randomRangeInclusive(1, 3);
        }

//        spawnZone = 3; // stub

        // choose spawn pattern
        if (wave == 1) {
            lastLastPattern = lastPattern;
            lastPattern = pattern;
            pattern = PATTERN_LINE;
        } else {
            nextPattern();
        }
//        pattern = PATTERN_V; // stubs

        int minEnemies = enemyNumbers[pattern][ENEMY_MIN];
        int maxEnemies = enemyNumbers[pattern][ENEMY_MAX];
        int currentMax = Math.min(minEnemies + difficulty, maxEnemies);

        int num = Utility.randomRangeInclusive(minEnemies, currentMax);

//        num = maxEnemies; // stub

        // spawn enemies
        switch (pattern) {
            case PATTERN_LINE:
                spawnLine(num);
                break;
            case PATTERN_V:
                spawnV(num);
                break;
            case PATTERN_SQUARE:
                spawnSquare(num);
                break;
            case PATTERN_CROSS:
                spawnCross(num);
                break;
        }

        if (wave % 10 == 0) {
            difficulty++;
        }

//        System.out.printf("Wave: %d, LastHeight: %d\n", wave, spawnHeight);

        wave++;
        System.out.println("Wave: " + wave);
        calculateSpawnInterval(num, maxEnemies);
    }

//    private void spawnPickup() {
//        /*
//        * - start with health only
//        * check if pi
//        * */
//    }

    private void spawnHealth() {
        spawnPickupHelper(EntityID.PickupHealth);
        System.out.println("Health Spawned");
        waveSinceHealth = 0;
    }

    private void spawnPower() {
        spawnPickupHelper(EntityID.PickupPower);
        System.out.println("Power-Up Spawned");
        waveSincePower = 0;
    }

    private void spawnShield() {
        spawnPickupHelper( EntityID.PickupShield);
        System.out.println("Shield Spawned");
        waveSinceShield = 0;
    }

    // Pickup Spawner Helped
    private void spawnPickupHelper(EntityID id) {
        spawnZone = Utility.randomRange(1, 3);
        entityHandler.spawnPickup(xMid * 32, spawnHeight * 32, id);
        wave++;
        System.out.println("Wave: " + wave);
        spawnInterval = spawnInterval_MIN;
        lastSpawnTimePickup = GameController.getCurrentTime();
    }

    /* Spawn Pattern Functions*/

    private void spawnLine(int num) {
//        System.out.println(spawnZone + " " + num);

        double numD = num;
        double factor = ((enemyNumbers[PATTERN_LINE][ENEMY_MAX] + 1) - numD) / (enemyNumbers[PATTERN_LINE][ENEMY_MAX] - enemyNumbers[PATTERN_LINE][ENEMY_MIN]);
        int time = (int) (factor * TIME_MAX);

        int dispersal = Utility.randomRangeInclusive(0,1);
//        int dispersal = 0; // stub

        switch (spawnZone) {
            case 1:
                spawnFromLeft(num, spawnHeight, xStart, "", time, dispersal);
                break;
            case 2:
                spawnFromMiddle(num, spawnHeight, "", "", time/2, dispersal);
                break;
            case 3:
                spawnFromRight(num, spawnHeight, xEnd, "", time, dispersal);
                break;
        }
    }

    private void spawnV(int num) {
        int xStart = this.xStart + num/2 * spacing;
        int xEnd = this.xEnd - (num/2 + 1) * spacing;

        switch (spawnZone) {
            case 1:
                spawnFromMiddleV(xStart, num, spawnHeight);
                break;
            case 2:
                spawnFromMiddleV(xMid, num, spawnHeight);
                break;
            case 3:
                spawnFromMiddleV(xEnd, num, spawnHeight);
                break;
        }
    }

    private void spawnSquare(int num) {
        num--;
        int x0 = 0;
        int y0 = spawnHeight - num* spacing;
        int xFin = 0;
        switch (spawnZone) {
            case 1:
                x0 = xStart;
                xFin = x0 + num* spacing;
                y0 = spawnHeight - num* spacing;
                break;
            case 2:
                x0 = xMid;
                xFin = x0 + num* spacing;
                y0 = spawnHeight - num* spacing;
                break;
            case 3:
                x0 = xEnd - num* spacing;
                xFin = x0 + num* spacing;
                y0 = spawnHeight - num* spacing;
                break;
        }

        spawnFromLeft(num, y0, x0, "", 0, 0);
        spawnFromTop(num, y0, xFin);
        spawnFromBottom(num, spawnHeight, x0);
        spawnFromRight(num, spawnHeight, xFin, "", 0, 0);
    }

    private void spawnCross(int num) {
        int interval = spacing;
        int xLeft = 0;
        int xRight = 0;
        int yMid = spawnHeight - interval*num;
        int yTop = 0;
        int yBottom = 0;
        int x0 = 0;

        switch (spawnZone) {
            case 1:
                x0 = (xMid - xStart)/2;
                xLeft = x0 + interval;
                xRight = x0 - interval;
                yTop = yMid + interval;
                yBottom = yMid - interval;
                break;
            case 2:
                x0 = xMid;
                xLeft = x0 + interval;
                xRight = x0 - interval;
                yTop = yMid + interval;
                yBottom = yMid - interval;
                break;
            case 3:
                x0 = (xEnd - xMid)/2;
                xLeft = x0 + interval;
                xRight = x0 - interval;
                yTop = yMid + interval;
                yBottom = yMid - interval;
                break;
        }

        spawnFromLeft(num, yMid, xLeft, "", TIME_MAX, 0);
        spawnFromRight(num, yMid, xRight, "", TIME_MAX, 0);
        spawnFromTop(num, yTop, x0);
        spawnFromBottom(num, yBottom, x0);
    }

    /* Recursive Spawning Functions */

    private void spawnFromLeft(int num, int y, int x, String move, int time, int dispersal) {
        if (move == "") {
            if (Utility.randomRangeInclusive(0,1) == 0) {
                move = "right";
            }
        }

        // todo: determine whether to move synchronized or asynchronized

        if (num > 1) {
            // time depends on distance of x and xEnd.
//            double xD = x;
//            double factors = xD/ (xEnd - xStart);
//            int newTime = (int) (TIME_MAX * factors);

//            double xD = x;
//            double factors = xD/ (xEnd - xStart);
//            int newTime = (int) (TIME_MAX * factors);

            int skip = calculateSkip();
            int x0 = x + (spacing + skip);
            spawnFromLeft(num - 1, y, x0, move, time + dispersal * TIME_MIN, dispersal);
        }

        // Base Case
        Color c = Color.RED; // stub
        spawnHelper(x, y, c, move, time);
    }

    private void spawnFromRight(int num, int y, int x, String move, int time, int dispersal) {
        if (move == "") {
            if (Utility.randomRangeInclusive(0,1) == 0) {
                move = "left";
            }
        }

        if (num > 1) {
            int skip = calculateSkip();
            int x0 = x - (spacing + skip);
            spawnFromRight(num - 1, y, x0, move, time + TIME_MIN*dispersal, dispersal);
        }

//        // time depends on distance of x and xStart.
//        int time = 300;

        // Base Case
        Color c = Color.RED; // stub
        spawnHelper(x, y, c, move, time);
    }

    private void spawnFromMiddle(int num, int spawnHeight, String moveLeft, String moveRight, int time, int dispersal) {
        int tileSize = Utility.intAtWidth640(32);
        int x = xMid;
        int y = spawnHeight;
        Color c = Color.RED; // stub

        boolean odd = num% 2 != 0;
        if (odd) {

            // todo: decide whether to move left or right
            int move = Utility.randomRange(0, 2);
            if (move == 0) {
                moveLeft = "right";
                spawnHelper(x, y, c, moveLeft, time);
            } else if (move == 1) {
                moveRight = "left";
                spawnHelper(x, y, c, moveRight, time);
            }

//            spawnHelper(x, y, c, moveLeft);

            if (num > 1) {
                num -= 1;
                num = num / 2;
                int skip = calculateSkip();

                spawnFromLeft(num, y, x + (spacing + skip), moveLeft, time, dispersal);

                skip = calculateSkip();

                spawnFromRight(num, y, x - (spacing + skip), moveRight, time, dispersal);
            }
        } else {
            if (moveLeft == "") {
                if (Utility.randomRangeInclusive(0, 1) == 0) {
                    moveLeft = "right";
                }
            }

            if (moveRight == "") {
                if (Utility.randomRangeInclusive(0, 1) == 0) {
                    moveRight = "left";
                }
            }

            num = num /2;

            spawnFromLeft(num, y, x + incrementMIN, moveLeft, time, dispersal);
            spawnFromRight(num, y, x - incrementMIN, moveRight, time, dispersal);

        }
    }

    private void spawnFromMiddleV(int x, int num, int spawnHeight) {
        int y = spawnHeight;
        Color c = Color.RED; // stub

        int incrementX = 2*incrementMIN;

        int newY = y - spacing;

        boolean odd = (num % 2) != 0;
        if (odd) {
            spawnHelper(x, y, c, "", 250);

            if (num > 1) {
                num -= 1;
                num = num / 2;

                int xLeft = x + (incrementX);
                spawnFromLeftV(num, newY, xLeft);

                int xRight = x - (incrementX);
                spawnFromRightV(num, newY, xRight);
            }
        } else {
            num = num /2;

            incrementX = incrementMIN;
            int xLeft = x + (incrementX);
            spawnFromLeftV(num, newY, xLeft);

            int xRight = x - (incrementX);
            spawnFromRightV(num, newY, xRight);


        }
    }

    private void spawnFromTop(int num, int y, int x) {
        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int y0 = y + (spacing + skip);
            spawnFromTop(num - 1, y0, x);
        }

        Color c = Color.RED; // stub
        spawnHelper(x, y, c, "", 250);
    }

    private void spawnFromBottom(int num, int y, int x) {
        // Base Case

        if (num > 1) {
            int skip = calculateSkip();
            int y0 = y - (spacing + skip);
            spawnFromBottom(num - 1, y0, x);
        }

        Color c = Color.RED; // stub
        spawnHelper(x, y, c, "", 250);
    }

    private void spawnVHelper(int num, int y, int x, int increment) {
        Color c = Color.RED; // stub

        // Base Case

        int y0 = y - spacing;

        if (num > 1) {
            int x0 = x + (increment);
            spawnVHelper(num - 1, y0, x0, increment);
        }

        spawnHelper(x, y, c, "", 250);
    }

    private void spawnFromLeftV(int num, int y, int x) {
        int incrementX = 2 * incrementMIN;
        spawnVHelper(num, y, x, incrementX);
    }

    private void spawnFromRightV(int num, int y, int x) {
        int incrementX = -2 * incrementMIN;
        spawnVHelper(num, y, x, incrementX);
    }

    private void spawnHelper(int x, int y, Color c, String move, int time) {

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

        entityHandler.spawnEntity(x*32, y*32, id, c, move, time);
//        System.out.println("Enemy spawned");
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
        lastLastPattern = lastPattern;
        lastPattern = pattern;
        pattern = Utility.randomRange(0, maxPatterns);

        while (pattern == lastPattern && pattern == lastLastPattern) {
            pattern = Utility.randomRange(0, maxPatterns);
        }
    }

    // todo: redundant
    private int calculateSkip() {
        int skip = Utility.randomRange(0, 2);
        if (pattern == PATTERN_SQUARE || pattern == PATTERN_CROSS) {
            skip = 0;
        }

        return 0;
//        return skip;
    }

    private void calculateSpawnInterval(int num, int maxEnemies) {
        float minPause = (float) spawnInterval_MIN;
        float maxPause = (float) spawnInterval_MAX;

        float spawnIntervalFloat = (maxPause - minPause) * (num - 1)/(maxEnemies - 1) + minPause;
        spawnInterval = (long) spawnIntervalFloat;

        if (pattern == PATTERN_V) {
            spawnInterval *= 1.5;
        }

        // todo: adjust
        if (pattern == PATTERN_SQUARE) {
            spawnInterval *= 1.8;
        }

        // todo: adjust
        if (pattern == PATTERN_CROSS) {
            spawnInterval *= 1.5;
        }
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
