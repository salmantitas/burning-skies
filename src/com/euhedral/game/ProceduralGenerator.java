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

    final int SCALE = 32;
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
    int playerX = xMid*SCALE, playerY;
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
    final int PATTERN_PINCER = 1;
    final int PATTERN_V = 2;
    final int PATTERN_SQUARE = 3;
    final int PATTERN_CROSS = 4;
    final int maxPatterns = 3;

    // basic enemy movement distance
    final int movementFactor = 6;
    final int MOVEMENT_MAX = spacing * movementFactor;

    // Spawn From Direction
    int LEFT = 1;
    int RIGHT = -1;

    private EntityHandler entityHandler;

    public ProceduralGenerator(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
        colorMap = VariableHandler.colorMap;

        buildMatrix();
    }

    private void buildMatrix() {
        int minLine = 2;
        int maxLine = 8;

        int minPincer = 2;
        int maxPincer = 4;

        int minV = 3;
        int maxV = 9;

        int minEnemiesSquare = 2;
        int maxEnemiesSquare = 4; // MAX 8 possible but not fun

        int minEnemiesCross = 3;
        int maxEnemiesCross = 3;

        enemyNumbers = new int[maxPatterns][2];
        enemyNumbers[PATTERN_LINE][ENEMY_MIN] = minLine;
        enemyNumbers[PATTERN_LINE][ENEMY_MAX] = maxLine;
        enemyNumbers[PATTERN_PINCER][ENEMY_MIN] = minPincer;
        enemyNumbers[PATTERN_PINCER][ENEMY_MAX] = maxPincer;
        enemyNumbers[PATTERN_V][ENEMY_MIN] = minV;
        enemyNumbers[PATTERN_V][ENEMY_MAX] = maxV;
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

        spawnHeight = (height - Engine.HEIGHT/SCALE) ;//- (wave * MIN_PAUSE);
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

//        spawnZone = 2; // stub

        // choose spawn pattern
        if (wave == 1) {
            lastLastPattern = lastPattern;
            lastPattern = pattern;
            pattern = PATTERN_LINE;
        } else {
            nextPattern();
        }

//        pattern = PATTERN_V; // stub

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
            case PATTERN_PINCER:
                spawnPincer(num);
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
        entityHandler.spawnPickup(xMid * SCALE, spawnHeight * SCALE, id);
        wave++;
        System.out.println("Wave: " + wave);
        spawnInterval = spawnInterval_MIN;
        lastSpawnTimePickup = GameController.getCurrentTime();
    }

    /* Spawn Pattern Functions*/

    private void spawnLine(int num) {
//        System.out.println(spawnZone + " " + num);

        int distance = MOVEMENT_MAX - (num - 1)*2;
        int dispersal = Utility.randomRangeInclusive(-1,1);

        switch (spawnZone) {
            case 1:
                spawnFromDirection(num, spawnHeight, xStart, "", distance, dispersal, LEFT);
                break;
            case 2:
                spawnFromMiddle(num, spawnHeight, "", "", distance, dispersal);
                break;
            case 3:
                spawnFromDirection(num, spawnHeight, xEnd, "", distance, dispersal, RIGHT);
                break;
        }
    }

    // todo: zone 2 and 3
    private void spawnPincer(int num) {
//        System.out.println(spawnZone + " " + num);



        int distance = (MOVEMENT_MAX);

        int dispersal = Utility.randomRangeInclusive(-1,1);
        dispersal = 0; // stub

        distance = (MOVEMENT_MAX) - (num-1)*4;

        spawnFromDirection(num, spawnHeight, xStart, "right", distance, dispersal, LEFT);
        spawnFromDirection(num, spawnHeight, xEnd, "left", distance, dispersal, RIGHT);
//
        switch (spawnZone) {
            case 1:
//                num = 1; // 18
//                num = 2; // 14
//                num = 3; // 10
//                num = 4; // 6
                distance = (MOVEMENT_MAX) - (num-1)*4;

                spawnFromDirection(num, spawnHeight, xStart, "right", distance, dispersal, LEFT);
                spawnFromDirection(num, spawnHeight, xEnd, "left", distance, dispersal, RIGHT);
                break;
            case 2:
//                int offset = spacing * 2;
//                num = 1; // 10
//                num = 2; // 7
//                num = 3; // 3
//                num = 4; // 0
//
//                distance = (MOVEMENT_MAX) - 17;
//                Utility.log(distance+"");
//                spawnFromDirection(num, spawnHeight, xStart + offset, "right", distance, dispersal, LEFT);
//                spawnFromDirection(num, spawnHeight, xEnd - offset, "left", distance, dispersal, RIGHT);
//                break;
            case 3:
//                timeFactor = 5.0;
//                time = (int) (time/timeFactor);
//                offset = spacing * 3;
//                spawnFromDirection(num, spawnHeight, xStart + offset, "", time, dispersal, LEFT);
//                spawnFromDirection(num, spawnHeight, xEnd - offset, "", time, dispersal, RIGHT);
//                break;
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

//        spawnFromLeft(num, y0, x0, "", 0, 0);
        spawnFromTop(num, y0, xFin);
        spawnFromBottom(num, spawnHeight, x0);
//        spawnFromRight(num, spawnHeight, xFin, "", 0, 0);
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

//        spawnFromLeft(num, yMid, xLeft, "", TIME_MAX, 0);
//        spawnFromRight(num, yMid, xRight, "", TIME_MAX, 0);
        spawnFromTop(num, yTop, x0);
        spawnFromBottom(num, yBottom, x0);
    }

    private String calculateMoveDirection(String move, int direction) {
        if (move == "") {
            if (Utility.randomRangeInclusive(0,1) == 0) {
                String result = "";
                if (direction == -1*LEFT)
                    result = "left";
                else result = "right";
                return result;
            }
        }
        return move;
    }

    private void spawnFromDirection(int num, int y, int x, String move, int distance, int dispersal, int direction) {
        move = calculateMoveDirection(move, direction);

        if (num > 1) {
            int x0 = x + direction * (spacing);
            spawnFromDirection(num - 1, y, x0, move, distance, dispersal, direction);
        }

        distance += dispersal*num;
        spawnHelper(x, y, move, distance);

    }

    private void spawnFromMiddle(int num, int spawnHeight, String moveLeft, String moveRight, int time, int dispersal) {
        int x = xMid;
        int y = spawnHeight;

        boolean odd = num% 2 != 0;
        if (odd) {

            String moveDir = "";
            int move = Utility.randomRange(0, 2);
            if (move == 0) {
                moveLeft = "right";
                moveDir = moveLeft;
            } else if (move == 1) {
                moveRight = "left";
                moveDir = moveRight;
            }

            spawnHelper(x, y, moveDir, time);

            if (num > 1) {
                num -= 1;
                num = num / 2;

                spawnFromDirection(num, y, x + (spacing), moveLeft, time, dispersal, LEFT);
                spawnFromDirection(num, y, x - (spacing), moveRight, time, dispersal, RIGHT);
            }
        } else {
            num = num /2;

            // todo: minimum difference between 2 planes should be 3 * incrementMIN
            // consider changing xMid to (20-17)/2

            int centerOffset = 1;

            int x0Left = x + incrementMIN/2 + centerOffset;
            int x0Right = x0Left - 3;

            moveLeft = calculateMoveDirection(moveLeft, LEFT);
            moveRight = calculateMoveDirection(moveRight, RIGHT);

            spawnFromDirection(num, y, x0Left, moveLeft, time, dispersal, LEFT);
            spawnFromDirection(num, y, x0Right, moveRight, time, dispersal, RIGHT);

        }
    }

    private void spawnPincerMiddle(int num, int spawnHeight, String moveLeft, String moveRight, int time, int dispersal) {
        int x = xMid;
        int y = spawnHeight;

        boolean odd = num% 2 != 0;
        if (odd) {

            int move = Utility.randomRange(0, 2);
            if (move == 0) {
                moveLeft = "right";
                spawnHelper(x, y, moveLeft, time);
            } else if (move == 1) {
                moveRight = "left";
                spawnHelper(x, y, moveRight, time);
            }

//            spawnHelper(x, y, c, moveLeft);

            if (num > 1) {
                num -= 1;
                num = num / 2;

//                spawnFromLeft(num, y, x + (spacing), moveLeft, time, dispersal);

//                spawnFromRight(num, y, x - (spacing), moveRight, time, dispersal);
            }
        } else {
            num = num /2;

            int x0Left = x + incrementMIN;
            int x0Right = x - incrementMIN;

            int pincer = Utility.randomRangeInclusive(0, 1);
            if (pincer == 0) {
                moveLeft = "left";
                moveRight = "right";

                // 2 - 16, 4 - 12, 6 - 8, 8 - 3
                int factor = 16 - 2*(num - 2);
                time = time/factor;

                if (num == enemyNumbers[pattern][ENEMY_MAX]) {
                    time -= 1;
                }
                dispersal = 0;

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
            }

//            spawnFromLeft(num, y, x0Left, moveLeft, time, dispersal);
//            spawnFromRight(num, y, x0Right, moveRight, time, dispersal);

        }
    }

    private void spawnPincerSides(int num, int spawnHeight, String moveLeft, String moveRight, int time, int dispersal) {
        int x = xMid;
        int y = spawnHeight;

        boolean odd = num% 2 != 0;
        if (odd) {

            // todo: decide whether to move left or right
            int move = Utility.randomRange(0, 2);
            if (move == 0) {
                moveLeft = "right";
                spawnHelper(x, y, moveLeft, time);
            } else if (move == 1) {
                moveRight = "left";
                spawnHelper(x, y, moveRight, time);
            }

//            spawnHelper(x, y, c, moveLeft);

            if (num > 1) {
                num -= 1;
                num = num / 2;

//                spawnFromLeft(num, y, x + (spacing), moveLeft, time, dispersal);
//                spawnFromRight(num, y, x - (spacing), moveRight, time, dispersal);
            }
        } else {
            num = num /2;

            int x0Left = x + incrementMIN;
            int x0Right = x - incrementMIN;

            int pincer = Utility.randomRangeInclusive(0, 1);
            if (pincer == 0) {
                moveLeft = "left";
                moveRight = "right";

                // 2 - 16, 4 - 12, 6 - 8, 8 - 3
                int factor = 16 - 2*(num - 2);
                time = time/factor;

                if (num == enemyNumbers[pattern][ENEMY_MAX]) {
                    time -= 1;
                }
                dispersal = 0;

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
            }

//            spawnFromLeft(num, y, x0Left, moveLeft, time, dispersal);
//            spawnFromRight(num, y, x0Right, moveRight, time, dispersal);

        }
    }

    private void spawnFromMiddleV(int x, int num, int spawnHeight) {
        int y = spawnHeight;

        int incrementX = 2*incrementMIN;

        int newY = y - spacing;

        int distance = 0;
        int dispersal = Utility.randomRangeInclusive(-1, 0);
        dispersal = -1;

        boolean odd = (num % 2) != 0;
        if (odd) {
            spawnHelper(x, y, "", 0);

            distance = incrementMIN;

            if (num > 1) {
                num -= 1;
                num = num / 2;

                int xLeft = x + (incrementX);
                spawnFromLeftV(num, newY, xLeft, dispersal, distance);

                int xRight = x - (incrementX);
                spawnFromRightV(num, newY, xRight, dispersal, distance);
            }
        } else {
            distance = incrementMIN;
            num = num /2;

            incrementX = incrementMIN;
            int xLeft = x + (incrementX);
            int xRight = x - (incrementX);

            spawnHelper(xLeft, y, "left", distance/2);
            spawnHelper(xRight, y, "right", distance/2);

            xLeft += incrementX;
            xRight -= incrementX;
            num -= 1;

            spawnFromLeftV(num, newY, xLeft, dispersal, distance);
            spawnFromRightV(num, newY, xRight, dispersal, distance);
        }
    }

    private void spawnFromLeftV(int num, int y, int x, int dispersal, int distance) {
        int incrementX = 2*LEFT*incrementMIN;
        String move = "right";
        if (dispersal == -1) {
            move = "left";
        }
        spawnVHelper(num, y, x, incrementX, move, dispersal, distance, 1);
    }

    private void spawnFromRightV(int num, int y, int x, int dispersal, int distance) {
        int incrementX = 2*RIGHT*incrementMIN;
        String move = "left";
        if (dispersal == -1) {
            move = "right";
        }
        spawnVHelper(num, y, x, incrementX, move, dispersal, distance, 1);
    }

    private void spawnVHelper(int num, int y, int x, int increment, String move, int dispersal, int distance, int spawnCount) {
        // Base Case
//        int spawnCount = 1;

        int y0 = y - spacing;

        if (num > 1) {
            int x0 = x + (increment);
            spawnVHelper(num - 1, y0, x0, increment, move, dispersal, distance + incrementMIN * spawnCount, spawnCount++);
        }

        spawnHelper(x, y, move, distance);
    }

    private void spawnFromTop(int num, int y, int x) {
        // Base Case

        if (num > 1) {
            int y0 = y + (spacing);
            spawnFromTop(num - 1, y0, x);
        }

        spawnHelper(x, y, "", 250);
    }

    private void spawnFromBottom(int num, int y, int x) {
        // Base Case

        if (num > 1) {
            int y0 = y - (spacing);
            spawnFromBottom(num - 1, y0, x);
        }

        spawnHelper(x, y, "", 250);
    }

    private void spawnHelper(int x, int y, String move, int distance) {

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

        Color c = getKey(id);

        entityHandler.spawnEntity(x*SCALE, y*SCALE, id, c, move, distance*SCALE);
//        System.out.println("Enemy spawned");
    }

    private void spawnGround(int remainingHeight, int spawnHeight, int num, String spawnFrom) {
        if (groundCount == num && height - remainingHeight >= spawnHeight) {
            EnemyGround eG = new EnemyGround(xStart * SCALE, remainingHeight * SCALE, getLevelHeight());
            groundCount++;
            if (spawnFrom == "left") {
                eG.setHMove("right");
            } else if (spawnFrom == "right") {
                eG.setHMove("left");
                eG.setX(xEnd*SCALE);
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
        entityHandler.spawnBoss(level, x*SCALE, y*SCALE);
    }

    private void nextPattern() {
        lastLastPattern = lastPattern;
        lastPattern = pattern;
        pattern = Utility.randomRange(0, maxPatterns);

        while (pattern == lastPattern && pattern == lastLastPattern) {
            pattern = Utility.randomRange(0, maxPatterns);
        }
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
        return height * SCALE;
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
