package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;

import java.awt.*;

public class EnemyGenerator {
    // Level
    int level;
    int cutoffHeight = 0, cutoffWidth = 0;
    int height = Engine.HEIGHT - cutoffHeight, width = Engine.WIDTH - cutoffWidth;

    final int incrementMIN = Utility.intAtWidth640(1);
    int xStart = incrementMIN, xEnd = width;
    int xMid = (xEnd - xStart)/2 + xStart;

    // Wave
    int wave, waveSinceHealth, waveSincePower, waveSinceShield;

    // Player
    int playerX = xMid, playerY;

    // Enemy Spawning
    protected EntityHandler entityHandler;
    int difficulty;
    int num;
    long lastSpawnTime;
    long spawnInterval;
    long spawnInterval_MIN = 2;
    long spawnInterval_MAX = 5;
    int enemiesSpawned; // todo: Why do we need this?
    int spawnX, spawnY;

    // Pickup
    long spawnIntervalPickups;

    // Enemy Types
    int enemytype;
    final int TYPE_BASIC = EntityHandler.TYPE_BASIC;
    final int TYPE_HEAVY = EntityHandler.TYPE_HEAVY;
    final int maxTypes = TYPE_HEAVY + 1;

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

        difficulty = 0;
        entityHandler.setLevelHeight(getLevelHeight());

        playerY = getLevelHeight();

        entityHandler.spawnPlayer(playerX, playerY);

        // create distance between player and first wave
        enemiesSpawned = 0;
        wave = 1;
        resetWaveSinceHealth();
        resetWaveSincePower();
        resetWaveSinceShield();
        System.out.println("Wave: " + wave);

        // todo: use line for first wave here

        spawnY = (height - Engine.HEIGHT) ;//- (wave * MIN_PAUSE);
        lastSpawnTime = GameController.getCurrentTime();
        spawnInterval = spawnInterval_MIN;
        spawnIntervalPickups = spawnInterval_MAX;

//        spawnFirstWave();
    }

    protected void spawnEnemies() {
        increment();
        determineNum();
        determineType();
        determineZone();
        spawnHelper();

        incrementDifficulty();
        wave++;
    }

    // Increment Wave Count for Non-Enemy Spawns
    protected void increment() {

    }

    protected void determineNum() {
        num = 1; // stub
    }

    protected void determineType() {
        int rand = Utility.randomRangeInclusive(0, difficulty);
        enemytype = rand;

        // determine type
//        enemytype = TYPE_BASIC; // stub
//        enemytype = TYPE_HEAVY; // stub
//        int temp = Utility.randomRangeInclusive(0, WEIGHT_TOTAL);
//        enemytype = Utility.randomRangeInclusive(0,1); // type;
    }

    protected void determineZone() {
        spawnX = Utility.randomRangeInclusive(xStart, xEnd);
    }

    protected void spawnHelper() {
        entityHandler.spawnEntity(spawnX, spawnY, enemytype);
//        System.out.println("Enemy spawned");
    }

    protected void incrementDifficulty() {
        int inc = 10;
        if (wave % inc == 0) {
            difficulty = Math.min(difficulty + 1, maxTypes - 1);
            Utility.log("Diff: " + difficulty);
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
}
