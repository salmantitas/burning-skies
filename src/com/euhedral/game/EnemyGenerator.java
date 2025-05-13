package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;

import java.util.ArrayList;

public class EnemyGenerator {
    // Level
    int level;
    int cutoffHeight = 0, cutoffWidth = 64*2;
    int height = Engine.HEIGHT - cutoffHeight, width = Engine.WIDTH - cutoffWidth;

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
    int enemiesSpawned; // todo: Why do we need this?
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
    final int TYPE_BASIC_DOWN = EntityHandler.TYPE_BASIC_DOWN;
    final int TYPE_BASIC_SIDE = EntityHandler.TYPE_BASIC_SIDE;
    final int TYPE_HEAVY = EntityHandler.TYPE_HEAVY;
    final int TYPE_DRONE = EntityHandler.TYPE_DRONE;
    final int TYPE_STATIC = EntityHandler.TYPE_STATIC;
    final int TYPE_SIDE = EntityHandler.TYPE_SIDE;
    final int TYPE_DRONE2 = EntityHandler.TYPE_DRONE2;
    int maxTypes = TYPE_DRONE2 + 1;

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
            enemiesSpawned = 0;
        }
    }

    public void generateLevel() {
        level = VariableHandler.getLevel();

        System.out.printf("Width: %d, Height: %d\n", width, height);

        difficulty = 1;
        minWavesDifficultyIncrease = 5;
        minDifficultyForMovement = 2;
        entityHandler.setLevelHeight(getLevelHeight());

        playerY = getLevelHeight() / SCALE;

        entityHandler.spawnPlayer(playerX * SCALE, playerY * SCALE);

        // create distance between player and first wave
        enemiesSpawned = 0;
        wave = firstWave;
        resetWaveSinceHealth();
        resetWaveSincePower();
        resetWaveSinceShield();
        System.out.println("Wave: " + wave);

        // todo: use line for first wave here

        spawnY = (height - Engine.HEIGHT)/SCALE ;//- (wave * MIN_PAUSE);
        lastSpawnTime = GameController.getCurrentTime();
        spawnInterval = spawnInterval_MIN;
        spawnIntervalPickups = spawnInterval_MAX;

//        spawnFirstWave();
    }

    protected void spawnEnemies() {
        increment();
        determinePattern(); // move down later
        determineNum();
        determineType();
        determineZone();
        determineMovement();
        spawnEnemiesHelper();
        incrementDifficulty();
        incrementWave();
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

        int temp = 1;
        int total = 0;
        while (temp <= maxTypes) {
            total += temp;
            temp++;
        }

        int calculatedDifficulty = difficulty - 1;

//        if (waveSinceHeavy > MINWaveSinceHeavy) {
//            calculatedDifficulty -= 1;
//        }

        int rand = Utility.randomRangeInclusive(0, calculatedDifficulty);
        enemytype = rand;

//        enemytype = TYPE_BASIC_DOWN; // stub
//        enemytype = TYPE_BASIC_SIDE; // stub
//        enemytype = TYPE_HEAVY; // stub
//        enemytype = TYPE_DRONE; // stub
//        enemytype = TYPE_STATIC; // stub
//        enemytype = TYPE_SIDE; ga// stub
        enemytype = TYPE_DRONE2; // stub

//        Utility.log("Active: " + EntityHandler.getActiveEnemies(enemytype));

        int limit = 10;

        if (enemytype == TYPE_STATIC) {
            limit = 5;
        }

        if (enemytype == TYPE_SIDE) {
            limit = 3;
        }

//        if (enemytype == TYPE_BASIC) {
//            limit = 20;
//        }

            while (EntityHandler.getActiveEnemies(enemytype) >= limit) {
                rand = Utility.randomRangeInclusive(0, calculatedDifficulty);
                enemytype = rand;
                // calculate limit
//                if (enemytype == TYPE_BASIC) {
//                    limit = 20;
//                }
                Utility.log("Type: " + enemytype + " | Limit: " + limit + " | Looping");
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
        // Vertical Zone
        if (enemytype == TYPE_DRONE || enemytype == TYPE_SIDE) {
            int adjustment = 1;
            int zone = Utility.randomRangeInclusive(0, 1);
            if (zone == 0) {
                spawnX = -adjustment;
            } else {
                spawnX = xEnd + adjustment;
            }
            if (enemytype == TYPE_DRONE) {
                spawnY = Utility.randomRangeInclusive(0, height / SCALE * 2 / 3);
            } else if (enemytype == TYPE_SIDE) {
                spawnY = Utility.randomRangeInclusive(5, 7);
//                spawnY = 7;
            }
        }
        // Horizontal Zone
        else {
            spawnX = Utility.randomRangeInclusive(xStart, xEnd);

            // check if coordinate is in exclusion list
            ArrayList<Integer> exclusionZones = EntityHandler.getExclusionZones();

            while (exclusionZones.contains(spawnX)) {
                spawnX = Utility.randomRangeInclusive(xStart, xEnd);
            }

            spawnY = (height - Engine.HEIGHT) / SCALE;

//            if (wave == firstWave) {
//                spawnX = xStart;
//            } else {
//                spawnX += 64;
//            }

        }
    }

    protected void determineMovement() {
        if (difficulty < minDifficultyForMovement) {
            movementDistance = 0;
            movementDirection = MOVE_NONE;
            return;
        }

        int chance = Utility.randomRangeInclusive(0, 1);

        if (chance == 0) {

            movementDistance = xMid;

            if (spawnX < xMid) {
                movementDirection = MOVE_RIGHT;
            } else {
                movementDirection = MOVE_LEFT;
            }
        } else {
            movementDistance = 0;
            movementDirection = MOVE_NONE;
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
        if (wavesSinceDifficultyIncrease >= minWavesDifficultyIncrease) {
            difficulty = Math.min(difficulty + 1, maxTypes);
//            difficulty = Math.min(difficulty + 1, 8);
            minWavesDifficultyIncrease += 5;
            wavesSinceDifficultyIncrease = 0;
            Utility.log("Diff: " + difficulty);
            Utility.log("Level: " + (wave / minWavesDifficultyIncrease + 1));
        } else {
            wavesSinceDifficultyIncrease++;
        }
    }

    protected void incrementWave() {
//        System.out.printf("Wave: %d, LastHeight: %d\n", wave, spawnHeight);
        wave++;
        System.out.println("Wave: " + wave);
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
