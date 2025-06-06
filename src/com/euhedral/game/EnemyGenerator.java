package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;

import java.util.ArrayList;

public class EnemyGenerator {
    // Level
    int level;
    int cutoffHeight = 0, cutoffWidth = Utility.intAtWidth640(24);
    int height = Engine.HEIGHT - cutoffHeight, width = VariableHandler.deadzoneRightX - cutoffWidth;

    final int SCALE = 64;

    final int incrementMIN = Utility.intAtWidth640(1);
    int xStart = 1, xEnd = width/64;
    int xMid = (xEnd - xStart)/2 + xStart;

    // Player
    int playerX = xMid, playerY;

    // Enemy Spawning
    protected EntityHandler entityHandler;
    int difficulty;
    int minWavesDifficultyIncrease;
    int num;
    long lastSpawnTime;
    long spawnInterval;
    long spawnInterval_MIN = 2;
    long spawnInterval_MAX = 5;
//    int enemiesSpawned; // todo: Why do we need this?
    int spawnX, spawnY;

    // Movement
    int minDifficultyForMovement;
    int movementDistance;
    int movementDirection;
    int MOVE_NONE = 0;
    int MOVE_LEFT = 1;
    int MOVE_RIGHT = -MOVE_LEFT;

    // Pickup
    long spawnIntervalPickups;

    // Enemy Types
    int enemytype;
    final int TYPE_BASIC1 = EntityHandler.TYPE_BASIC1;
    final int TYPE_HEAVY = EntityHandler.TYPE_HEAVY;
    final int TYPE_BASIC2 = EntityHandler.TYPE_BASIC2;
    final int TYPE_BASIC3 = EntityHandler.TYPE_BASIC3;
    final int TYPE_FAST2 = EntityHandler.TYPE_FAST;
    final int TYPE_SIDE1 = EntityHandler.TYPE_SIDE1;
    final int TYPE_DRONE1 = EntityHandler.TYPE_DRONE1;
    final int TYPE_STATIC1 = EntityHandler.TYPE_STATIC1;
    final int TYPE_DRONE2 = EntityHandler.TYPE_DRONE2;
    final int TYPE_SIDE2 = EntityHandler.TYPE_SIDE2;
    final int TYPE_STATIC2 = EntityHandler.TYPE_STATIC2;
    final int TYPE_DRONE3 = EntityHandler.TYPE_DRONE3;
    final int TYPE_STATIC3 = EntityHandler.TYPE_STATIC3;
    int maxTypes = TYPE_STATIC3 + 1;

    boolean difficultyIncreased = false;

    // Wave
    int wave;
    final int firstWave = 1;
    int waveSinceHealth, waveSincePower, waveSinceShield;
    int wavesSinceDifficultyIncrease = 0;
    int waveSinceHeavy = 0;
    final int MINWaveSinceHeavy = 1;

    public EnemyGenerator(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
    }

    // todo: SpawnHealth
    public void update() {
        long timeNowMillis = GameController.getCurrentTime();
        long timeSinceLastSpawnMillis = timeNowMillis - lastSpawnTime;
        boolean canSpawn = spawnInterval <= timeSinceLastSpawnMillis;
        // todo: SpawnInterval for different types

        if (canSpawn) {
            spawnEnemies();
            lastSpawnTime = GameController.getCurrentTime();
//            enemiesSpawned = 0;
        }
    }

    public void generateLevel() {
        level = VariableHandler.getLevel();

        System.out.printf("Width: %d, Height: %d\n", width, height);

        difficulty = VariableHandler.getDifficultyLevel();
        minWavesDifficultyIncrease = 5;
        minDifficultyForMovement = 2;
        entityHandler.setLevelHeight(getLevelHeight());

        playerY = getLevelHeight() / SCALE;

        entityHandler.spawnPlayer(playerX * SCALE, playerY * SCALE);

        // create distance between player and first wave
//        enemiesSpawned = 0;
        wave = firstWave;
        resetWaveSinceHealth();
        resetWaveSincePower();
        resetWaveSinceShield();
//        System.out.println("Wave: " + wave);

        // todo: use line for first wave here

        spawnY = (height - Engine.HEIGHT)/SCALE ;//- (wave * MIN_PAUSE);
        lastSpawnTime = GameController.getCurrentTime();
        spawnInterval = spawnInterval_MIN;
        spawnIntervalPickups = spawnInterval_MAX;

//        spawnFirstWave();
    }

    protected void spawnEnemies() {
        System.out.println("Wave: " + wave);
        increment();
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
        int calculatedDifficulty = difficulty - 1;

        if (difficultyIncreased) {
            difficultyIncreased = false;
            enemytype = calculatedDifficulty;
        } else {

//        if (waveSinceHeavy > MINWaveSinceHeavy) {
//            calculatedDifficulty -= 1;
//        }

            int rand = Utility.randomRangeInclusive(0, calculatedDifficulty);
            enemytype = rand;
        }

//        enemytype = TYPE_BASIC1; // stub
//        enemytype = TYPE_HEAVY; // stub
//        enemytype = TYPE_BASIC2; // stub
//        enemytype = TYPE_BASIC3; // stub
//        enemytype = TYPE_FAST2; // stub
//        enemytype = TYPE_DRONE1; // stub
//        enemytype = TYPE_SIDE1; // stub
//        enemytype = TYPE_STATIC1; // stub
//        enemytype = TYPE_DRONE2; // stub
//        enemytype = TYPE_SIDE2; // stub
//        enemytype = TYPE_STATIC2; // stub
//        enemytype = TYPE_DRONE3; // stub
//        enemytype = TYPE_STATIC3; // stub

//        difficulty = maxTypes; // stub

//        Utility.log("Active: " + EntityHandler.getActiveEnemies(enemytype));

        int limit = 5;
        int limitSide = 4;

        switch (enemytype) {
            case TYPE_SIDE1:
                limit = limitSide;
                break;
            case TYPE_STATIC1:
                limit = limitSide;
                break;
            case TYPE_SIDE2:
                limit = limitSide - 1;
                break;
            case TYPE_STATIC2:
                limit = limitSide - 1;
                break;
            case TYPE_DRONE3:
                limit = 1;
                break;
            case TYPE_STATIC3:
                limit = limitSide - 2;
                break;
        }

            if (EntityHandler.getActiveEnemies(enemytype) >= limit) {
                enemytype = TYPE_BASIC1;
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

        boolean entersFromSide = enemytype == TYPE_DRONE1 || enemytype == TYPE_DRONE2 || enemytype == TYPE_DRONE3 || enemytype == TYPE_SIDE1 || enemytype == TYPE_SIDE2;

        // Vertical Zone
        if (entersFromSide) {
            int adjustment = 1;
            int zone = Utility.randomRangeInclusive(0, 1);
            if (zone == 0) {
                spawnX = -adjustment;
            } else {
                spawnX = xEnd + 2*adjustment;
            }

            boolean isDrone = enemytype == TYPE_DRONE1 || enemytype == TYPE_DRONE2 || enemytype == TYPE_DRONE3;
            boolean isSideFlyer = enemytype == TYPE_SIDE1 || enemytype == TYPE_SIDE2;

            if (isDrone) {
                spawnY = Utility.randomRangeInclusive(0, height / SCALE * 2 / 3);
            } else if (isSideFlyer) {
                spawnY = Utility.randomRangeInclusive(5, 7);
            }
//            else if (enemytype == TYPE_SIDE2) {
//                spawnY = (int) (EntityHandler.playerY/SCALE);
//            }
        }
        // Horizontal Zone
        else {
            boolean movesHorizontally = enemytype == TYPE_HEAVY || enemytype == TYPE_BASIC2 || enemytype == TYPE_STATIC2 || enemytype == TYPE_STATIC3;
            if (movesHorizontally) {
                spawnX = Utility.randomRangeInclusive(xStart + 3, xEnd - 3);

                // check if coordinate is in exclusion list
                ArrayList<Integer> exclusionZones = EntityHandler.getExclusionZones();

                while (exclusionZones.contains(spawnX)) {
                    spawnX = Utility.randomRangeInclusive(xStart + 3, xEnd - 3);
                }
            } else {
                spawnX = Utility.randomRangeInclusive(xStart, xEnd);

                // check if coordinate is in exclusion list
                ArrayList<Integer> exclusionZones = EntityHandler.getExclusionZones();

                while (exclusionZones.contains(spawnX)) {
                    spawnX = Utility.randomRangeInclusive(xStart, xEnd);
                }
            }

            spawnY = (height - Engine.HEIGHT) / SCALE;

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

        if (enemytype != TYPE_HEAVY) {
            waveSinceHeavy++;
        } else {
            waveSinceHeavy = 0;
        }
    }

    protected void incrementDifficulty() {
        if (wavesSinceDifficultyIncrease + 1 >= minWavesDifficultyIncrease) {
            if (difficulty < maxTypes) {
                difficulty++;
                difficultyIncreased = true;
                wavesSinceDifficultyIncrease = 0;
                Utility.log("Diff: " + difficulty);
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
        spawnInterval = spawnInterval_MIN;
        float spawnIntervalFloat = spawnInterval;

        if (enemytype == TYPE_HEAVY) {
//            if (spawnIntervalFloat > spawnInterval_MIN)
                spawnInterval = (long) (spawnIntervalFloat*1.1);
        }
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
//        System.out.println("Enemy spawned");
    }
}
