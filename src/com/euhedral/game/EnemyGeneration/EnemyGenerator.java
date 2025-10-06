package com.euhedral.Game.EnemyGeneration;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.VariableHandler;

public class EnemyGenerator {
    // Level
    int level;
    int cutoffHeight = 0, cutoffWidth = Utility.intAtWidth640(24);
    int height = Engine.HEIGHT - cutoffHeight;
    int width = VariableHandler.deadzoneRightX - cutoffWidth;

    final int SCALE = 64;

    final int incrementMIN = Utility.intAtWidth640(2);
    int xStart = 1, xEnd = width/64;
    int xMid = (xEnd - xStart)/2 + xStart;

    // Player
    int playerX = xMid, playerY;

    // Enemy Spawning
    protected EntityHandler entityHandler;
    int difficulty;
    int minWavesDifficultyIncrease;
    int num;

    long spawnInterval;
    long spawnInterval_MIN = 60 * 1;
    long spawnInterval_START = 60 * 3;
    long spawnInterval_MAX = spawnInterval_START * 5;
    float spawnIntervalFloat;
    int spawnIntervalDeduction;

    int updatesSinceLastSpawn;

//    int enemiesSpawned; // todo: Why do we need this?
    int spawnX, spawnY;

    long timeNowMillis;
    long timeSinceLastSpawnMillis;
    boolean canSpawn;

    // Movement
    int minDifficultyForMovement;
    int movementDistance;
    int movementDirection;
    int MOVE_NONE = 0;
    int MOVE_LEFT = 1;
    int MOVE_RIGHT = -MOVE_LEFT;

    // Pickup
//    long spawnIntervalPickups;

    // Enemy Types
    int enemytype;
    int maxTypes;// = TYPE_DRONE5 + 1;

    boolean difficultyIncreased = false;

    // Enemy Limits
    int limit = 5;
    int limitSide = 4;

    // Wave
    int wave;
    final int firstWave = 1;
    int waveSinceHealth, waveSincePower, waveSinceShield;
    int wavesSinceDifficultyIncrease = 0;
//    int waveSinceHeavy = 0;
    final int MINWaveSinceHeavy = 1;

    // Zone
    boolean entersFromSide;
    boolean isDrone;
    boolean isSideFlyer;
    boolean movesHorizontally;
    int adjustment = 1;
    int zone;
    private int spawnY_DEFAULT;

    public EnemyGenerator(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        maxTypes = VariableHandler.enemyTypes;
    }

    public void generateLevel() {
        level = VariableHandler.getLevel();

        System.out.printf("levelWidth: %d, levelHeight: %d\n", width, height);

        difficulty = Difficulty.getDifficultyLevel();
        minWavesDifficultyIncrease = 5;
        minDifficultyForMovement = 2;
        entityHandler.setLevelHeight(getLevelHeight());

        playerY = getLevelHeight() / SCALE;

        entityHandler.spawnPlayer(playerX * SCALE, playerY * SCALE);

        // create distance between player and first wave
//        enemiesSpawned = 0;
        wave = firstWave;

        // todo: use line for first wave here

        spawnY_DEFAULT = (height - Engine.HEIGHT) / SCALE;
        spawnY = spawnY_DEFAULT;
        spawnInterval = 0 ; //spawnInterval_START;
        spawnIntervalDeduction = 0;
        updatesSinceLastSpawn = 0;

//        spawnFirstWave();
    }

    public void update() {
        updatesSinceLastSpawn++;
        canSpawn = spawnInterval <= updatesSinceLastSpawn;

        if (canSpawn) {
            spawnEnemies();
            updatesSinceLastSpawn = 0;
        }
    }

    protected void spawnEnemies() {
        System.out.println("Wave: " + wave);
//        increment();
        determinePattern(); // move down later
        determineNum();
        determineType();
        determineZone();
        determineMovement();
        spawnEnemiesHelper();
        incrementWave();
        incrementDifficulty();
        determineSpawnInterval();
    }

    // Increment Wave Count for Non-Enemy Spawns
    protected void increment() {

    }

    protected void determineNum() {
        num = 1; // stub
    }

    protected void determineType() {
        // experimental

//        int temp = 1;
//        int total = 0;
//        while (temp <= maxTypes) {
//            total += temp;
//            temp++;
//        }
//        int calculatedDifficulty = difficulty - 1;

        if (difficultyIncreased) {
            difficultyIncreased = false;
            enemytype = difficulty - 1;
        } else {

//        if (waveSinceHeavy > MINWaveSinceHeavy) {
//            calculatedDifficulty -= 1;
//        }

            enemytype = Utility.randomRangeInclusive(0, difficulty - 1);
//            enemytype = rand;
        }

//        enemytype = VariableHandler.TYPE_BASIC1; // stub
//        enemytype = VariableHandler.TYPE_BASIC2; // stub
//        enemytype = VariableHandler.TYPE_HEAVY;
//        enemytype = VariableHandler.TYPE_BASIC3; // stub
//        enemytype = VariableHandler.TYPE_DRONE1; // stub
//        enemytype = VariableHandler.TYPE_STATIC1; // stub
//        enemytype = VariableHandler.TYPE_SIDE1; // stub
//        enemytype = VariableHandler.TYPE_DRONE2; // stub
//        enemytype = VariableHandler.TYPE_FAST; // stub
//        enemytype = VariableHandler.TYPE_SIDE2; // stub
//        enemytype = VariableHandler.TYPE_DRONE3; // stub
//        enemytype = VariableHandler.TYPE_MINE1;
//        enemytype = VariableHandler.TYPE_SCATTER1; // stub
//        enemytype = VariableHandler.TYPE_DRONE4; // stub
//        enemytype = VariableHandler.TYPE_SIDE3; // stub
//        enemytype = VariableHandler.TYPE_MINE2; // stub
//        enemytype = VariableHandler.TYPE_DRONE5; // stub
//        enemytype = VariableHandler.TYPE_SCATTER2; //
//        enemytype = VariableHandler.TYPE_LASER; // stub
//        enemytype = VariableHandler.TYPE_DRONE6; // stub

//        difficulty = maxTypes; // stub

//        Utility.log("Active: " + EntityHandler.getActiveEnemies(enemytype));

        limit = 5;

        switch (enemytype) {
            case VariableHandler.TYPE_MINE1:
                limit = 2;
                break;
            case VariableHandler.TYPE_SIDE1:
                limit = limitSide;
                break;
            case VariableHandler.TYPE_STATIC1:
                limit = limitSide;
                break;
            case VariableHandler.TYPE_SIDE2:
                limit = limitSide - 1;
                break;
            case VariableHandler.TYPE_SIDE3:
                limit = limitSide - 2;
                break;
            case VariableHandler.TYPE_MINE2:
                limit = 1;
                break;
            case VariableHandler.TYPE_SCATTER1:
                limit = limitSide - 1;
                break;
            case VariableHandler.TYPE_DRONE3:
                limit = 1;
                break;
            case VariableHandler.TYPE_SCATTER2:
                limit = limitSide - 2;
                break;
            case VariableHandler.TYPE_DRONE4:
                limit = 2;
                break;
            case VariableHandler.TYPE_DRONE5:
                limit = 1;
                break;
            case VariableHandler.TYPE_LASER:
                limit = 1;
                break;
            case VariableHandler.TYPE_DRONE6:
                limit = 1;
                break;
        }

            if (EntityHandler.getActiveEnemies(enemytype) >= limit) {
                enemytype = VariableHandler.TYPE_BASIC1;
//                rand = Utility.randomRangeInclusive(0, calculatedDifficulty);
//                enemytype = rand;
                // calculate limit
//                if (enemytype == TYPE_BASIC) {
//                    limit = 20;
//                }

//                Utility.log("Type: " + enemytype + " | Limit: " + limit + " | Looping");
            }

//        if (wave == firstWave) {
//            enemytype = TYPE_STATIC; // todo: remove, TEST only
//        } else {
//
//            enemytype++;
//            if (enemytype >= maxTypes) {
//                enemytype = 0;
//            }
//        }

//        int temp = Utility.randomRangeInclusive(0, WEIGHT_TOTAL);
//        enemytype = Utility.randomRangeInclusive(0,1); // type;
    }

    protected void determineZone() {

        isDrone = enemytype == VariableHandler.TYPE_DRONE1 || enemytype == VariableHandler.TYPE_DRONE2 ||
                enemytype == VariableHandler.TYPE_DRONE3 || enemytype == VariableHandler.TYPE_DRONE4 ||
                enemytype == VariableHandler.TYPE_DRONE5 || enemytype == VariableHandler.TYPE_DRONE6;
        isSideFlyer = enemytype == VariableHandler.TYPE_SIDE1 || enemytype == VariableHandler.TYPE_SIDE2 || enemytype == VariableHandler.TYPE_SIDE3 || enemytype == VariableHandler.TYPE_MINE2 || enemytype == VariableHandler.TYPE_DRONE4;

        entersFromSide = isDrone || isSideFlyer;

        // Vertical Zone
        if (entersFromSide) {
            adjustment = 1;
            zone = Utility.randomRangeInclusive(0, 1);
            if (zone == 0) {
                spawnX = -adjustment;
            } else {
                spawnX = xEnd + 2*adjustment;
            }

            if (isDrone) {
                int var = height * 1 /3;
                spawnY = Utility.randomRangeInclusive(0, var / SCALE);
            } else if (isSideFlyer) {
                spawnY = Utility.randomRangeInclusive(5, 7);
            }
//            else if (enemytype == TYPE_SIDE2) {
//                spawnY = (int) (EntityHandler.playerY/SCALE);
//            }
        }
        // Horizontal Zone
        else {
            movesHorizontally = enemytype == VariableHandler.TYPE_HEAVY || enemytype == VariableHandler.TYPE_BASIC2 || enemytype == VariableHandler.TYPE_SCATTER1 || enemytype == VariableHandler.TYPE_SCATTER2;
            if (movesHorizontally) {
                spawnX = Utility.randomRangeInclusive(xStart + 3, xEnd - 3);

//                // check if coordinate is in exclusion list
//                ArrayList<Integer> exclusionZones = EntityHandler.getExclusionZones();

                while (EntityHandler.exclusionZonesContains(spawnX)) {
                    spawnX = Utility.randomRangeInclusive(xStart + 3, xEnd - 3);
                }
            } else if (enemytype == VariableHandler.TYPE_MINE1) {

                if (EntityHandler.exclusionZonesContains(xStart)) {
                    spawnX = xEnd;
                } else if (EntityHandler.exclusionZonesContains(xEnd)) {
                    spawnX = xStart;
                } else {
                    spawnX = Utility.randomRangeInclusive(xStart, xStart + 1);
                    if (spawnX > xStart) {
                        spawnX = xEnd;
                    }
                }

            } else {
                spawnX = Utility.randomRangeInclusive(xStart, xEnd);

//                // check if coordinate is in exclusion list
//                ArrayList<Integer> exclusionZones = EntityHandler.getExclusionZones();

                while (EntityHandler.exclusionZonesContains(spawnX)) {
                    spawnX = Utility.randomRangeInclusive(xStart, xEnd);
                }
            }

            spawnY = spawnY_DEFAULT;

//            if (wave == firstWave) {
//                spawnX = xStart;
//            } else {
//                spawnX += 64;
//            }

//            spawnX = xStart; // stub
//            spawnX = xEnd; // stub

        }
    }

    protected void determineMovement() {
        movementDistance = xMid;

        if (spawnX < xMid) {
            movementDirection = MOVE_RIGHT;
        } else {
            movementDirection = MOVE_LEFT;
        }
    }

    protected void determinePattern() {

    }

    protected void spawnEnemiesHelper() {
        spawnOneEnemy();

//        if (enemytype != VariableHandler.TYPE_HEAVY) {
//            waveSinceHeavy++;
//        } else {
//            waveSinceHeavy = 0;
//        }
    }

    protected void incrementDifficulty() {
        if (wavesSinceDifficultyIncrease + 1 >= minWavesDifficultyIncrease) {
            if (difficulty < maxTypes) {
                difficulty++;
                difficultyIncreased = true;
                wavesSinceDifficultyIncrease = 0;
                Utility.log("Diff: " + difficulty);
            } else {
                spawnIntervalDeduction += 5;
                wavesSinceDifficultyIncrease = 0;
            }
//            minWavesDifficultyIncrease += 5;
            if (wave / minWavesDifficultyIncrease + 1 > level) {
                level++;
                Utility.log("Level: " + (level));
                VariableHandler.setLevel(level);
            }
        } else {
            wavesSinceDifficultyIncrease++;
        }
    }

    protected void incrementWave() {
//        System.out.printf("Wave: %d, LastHeight: %d\n", wave, spawnHeight);
        wave++;
    }

    protected void determineSpawnInterval() {
        spawnInterval = entityHandler.getSpawnInterval();
        spawnInterval = Math.max(spawnInterval - spawnIntervalDeduction, spawnInterval_MIN );

        Utility.log("Spawn Interval: " + spawnInterval);
//        spawnIntervalFloat = (float) spawnInterval;

//        if (enemytype == VariableHandler.TYPE_HEAVY) {
//            if (spawnIntervalFloat > spawnInterval_MIN)
//                spawnInterval = (long) (spawnIntervalFloat*1.1);
//        }
    }

    public int getLevelHeight() {
        return height;
    }

    // Helpers
    protected void resetWaveSinceHealth() {
        waveSinceHealth = 0;
    }

    protected void resetWaveSincePower() {
        waveSincePower = 0;
    }

    protected void resetWaveSinceShield() {
        waveSinceShield = 0;
    }

    protected void spawnOneEnemy() {
        entityHandler.spawnEntity(spawnX * (64 + incrementMIN), spawnY * (64 + incrementMIN), enemytype, movementDistance * SCALE, movementDirection);
    }
}
