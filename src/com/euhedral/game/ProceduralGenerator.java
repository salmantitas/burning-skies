package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* todo: decrease pause after wave after difficulty rises
* */

public class ProceduralGenerator extends EnemyGenerator {

    // Level
    final int SCALE = 32;
    int height = Engine.HEIGHT/SCALE, width = 35;
    public HashMap<Color, EntityID> colorMap;
    final int incrementMIN = Utility.intAtWidth640(1);
    int spacing = 3;
    int xStart = incrementMIN/2, xEnd = width + incrementMIN/2;
    int xMid = (xEnd - xStart)/2 + xStart;

    int spawnZone, lastZone, lastLastZone;
    int pattern, lastPattern, lastLastPattern;

    final int ENDLESS = -1;

    long lastSpawnTimePickup;

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
    final int MAX_DIFF = 10;

    /*
    * Basic - 2/3
    * Heavy - 1/3
    * Fast - 1/0
    * Move - 1/0
    * Snake - 1/0
    * */

    // Enemy spawning weights
    int WEIGHT_BASIC = 2;
    int WEIGHT_HEAVY = 1;
    int spawnFast = 5;
    int spawnMove = spawnFast * 3;
    int spawnSnake = spawnMove * 2;
    int WEIGHT_TOTAL = WEIGHT_BASIC + WEIGHT_HEAVY;

    // Spawning pick-up, every 50 units of level height
    int spawnPickupRate = 50;
    int spawnPickupChance = 1; // in 10;
    int spawnedPickupCount = 0;

    // Ground enemy spawn rate
    int groundRate = 50;
    int groundChance = 1; // in 10;
    int groundCount = 0;

    int[][][] enemyNumbers; // [Pattern][min/max numbers]
    final int ENEMY_MIN = 0;
    final int ENEMY_MAX = 1;
    int minEnemies, maxEnemies;

    // ground spawns
    int minG = 1;
    int maxG = 2;

    // Patterns
    final int PATTERN_LINE = 0;
    final int PATTERN_VERTICAL = 1;
    final int PATTERN_PINCER = 2;
    final int PATTERN_SLIDE = 3;
    final int PATTERN_V = 4;
    final int PATTERN_SQUARE = 5;
    final int PATTERN_CROSS = 6;
    final int maxPatterns = PATTERN_CROSS + 1;

    // basic enemy movement distance
    final int movementFactor = 6;
    final int MOVEMENT_MAX = spacing * movementFactor;

    // Spawn From Direction
    int LEFT = 1;
    int RIGHT = -1*LEFT;

    public ProceduralGenerator(EntityHandler entityHandler) {
        super(entityHandler);
        colorMap = VariableHandler.colorMap;

        buildMatrix();

        spawnInterval_MIN = 2;
        spawnInterval_MAX = 5;
    }

    private void buildMatrix() {
        enemyNumbers = new int[maxTypes][maxPatterns][2];

        buildMatrixBasic();
        buildMatrixHeavy();
    }

    private void buildMatrixBasic() {
        int minLine = 2;
        int maxLine = 8;

        int minPincer = 4;
        int maxPincer = 8;

        int minV = 3;
        int maxV = 9;

        int minVertical = 2;
        int maxVertical = 4;

        int minSquare = 2;
        int maxSquare = 12; // MAX 8 possible but not fun

        int minEnemiesCross = 1;
        int maxEnemiesCross = 8;

        enemyNumbers[TYPE_BASIC][PATTERN_LINE][ENEMY_MIN] = minLine;
        enemyNumbers[TYPE_BASIC][PATTERN_LINE][ENEMY_MAX] = maxLine;
        enemyNumbers[TYPE_BASIC][PATTERN_PINCER][ENEMY_MIN] = minPincer;
        enemyNumbers[TYPE_BASIC][PATTERN_PINCER][ENEMY_MAX] = maxPincer;
        enemyNumbers[TYPE_BASIC][PATTERN_SLIDE][ENEMY_MIN] = minPincer;
        enemyNumbers[TYPE_BASIC][PATTERN_SLIDE][ENEMY_MAX] = maxPincer;
        enemyNumbers[TYPE_BASIC][PATTERN_V][ENEMY_MIN] = minV;
        enemyNumbers[TYPE_BASIC][PATTERN_V][ENEMY_MAX] = maxV;
        enemyNumbers[TYPE_BASIC][PATTERN_VERTICAL][ENEMY_MIN] = minVertical;
        enemyNumbers[TYPE_BASIC][PATTERN_VERTICAL][ENEMY_MAX] = maxVertical;
        enemyNumbers[TYPE_BASIC][PATTERN_SQUARE][ENEMY_MIN] = minSquare;
        enemyNumbers[TYPE_BASIC][PATTERN_SQUARE][ENEMY_MAX] = maxSquare;
        enemyNumbers[TYPE_BASIC][PATTERN_CROSS][ENEMY_MIN] = minEnemiesCross;
        enemyNumbers[TYPE_BASIC][PATTERN_CROSS][ENEMY_MAX] = maxEnemiesCross;
    }

    private void buildMatrixHeavy() {
        int minLine = 1;
        int maxLine = 2;

        int minPincer = 1;
        int maxPincer = 2;

        int minV = 1;
        int maxV = 2;

        int minVertical = 1;
        int maxVertical = 2;

        int minSquare = 1;
        int maxSquare = 2; // MAX 8 possible but not fun

        int minEnemiesCross = 1;
        int maxEnemiesCross = 2;

        enemyNumbers[TYPE_HEAVY][PATTERN_LINE][ENEMY_MIN] = minLine;
        enemyNumbers[TYPE_HEAVY][PATTERN_LINE][ENEMY_MAX] = maxLine;
        enemyNumbers[TYPE_HEAVY][PATTERN_PINCER][ENEMY_MIN] = minPincer;
        enemyNumbers[TYPE_HEAVY][PATTERN_PINCER][ENEMY_MAX] = maxPincer;
        enemyNumbers[TYPE_HEAVY][PATTERN_SLIDE][ENEMY_MIN] = minPincer;
        enemyNumbers[TYPE_HEAVY][PATTERN_SLIDE][ENEMY_MAX] = maxPincer;
        enemyNumbers[TYPE_HEAVY][PATTERN_V][ENEMY_MIN] = minV;
        enemyNumbers[TYPE_HEAVY][PATTERN_V][ENEMY_MAX] = maxV;
        enemyNumbers[TYPE_HEAVY][PATTERN_VERTICAL][ENEMY_MIN] = minVertical;
        enemyNumbers[TYPE_HEAVY][PATTERN_VERTICAL][ENEMY_MAX] = maxVertical;
        enemyNumbers[TYPE_HEAVY][PATTERN_SQUARE][ENEMY_MIN] = minSquare;
        enemyNumbers[TYPE_HEAVY][PATTERN_SQUARE][ENEMY_MAX] = maxSquare;
        enemyNumbers[TYPE_HEAVY][PATTERN_CROSS][ENEMY_MIN] = minEnemiesCross;
        enemyNumbers[TYPE_HEAVY][PATTERN_CROSS][ENEMY_MAX] = maxEnemiesCross;
    }

    // generate a level using procedural generation // YEAH NO SHIT.
//    @Override
//    public void generateLevel() {
//        level = VariableHandler.getLevel();
////        switch (level) {
////            case ENDLESS:
////                height = ENDLESS;
////                break;
////            case 1:
////                height = 150; // 100 = ~4 waves
////                break;
////            case 2:
////                height = 225;
////                break;
////            case 3:
////                height = 275;
////                break;
////            case 4:
////                height = 300;
////                break;
////        }
//
//
//        System.out.printf("Width: %d, Height: %d\n", width, height);
//
//        difficulty = 1;
//        entityHandler.setLevelHeight(getLevelHeight());
//
//        playerY = getLevelHeight();
//
//        entityHandler.spawnPlayer(playerX, playerY);
//
//        // create distance between player and first wave
//        enemiesSpawned = 0;
//        wave = 1;
//        resetWaveSinceHealth();
//        resetWaveSincePower();
//        resetWaveSinceShield();
//        System.out.println("Wave: " + wave);
//
//        // todo: use line for first wave here
//
//        spawnY = (height - Engine.HEIGHT/SCALE) ;//- (wave * MIN_PAUSE);
//        lastSpawnTime = GameController.getCurrentTime();
//        spawnInterval = spawnInterval_MIN;
//        spawnIntervalPickups = spawnInterval_MAX;
//
////        spawnFirstWave();
//    }

    // Spawns an enemy or a pickup depending on how long ago the last one has been spawned
    @Override
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
            enemiesSpawned = 0;
        }
//        System.out.println("Spawn Code: " + spawnNext);
    }

    private void spawnFirstWave() {
        lastLastPattern = lastPattern;
        lastPattern = pattern;
        pattern = PATTERN_LINE;

        int minEnemies = enemyNumbers[enemytype][pattern][ENEMY_MIN];
        int maxEnemies = enemyNumbers[enemytype][pattern][ENEMY_MAX];
        int currentMax = Math.min(minEnemies + difficulty, maxEnemies);
        Utility.log("Current Max: " + currentMax);

        num = Utility.randomRangeInclusive(minEnemies, currentMax);

        spawnLine(num);
        wave++;
        determineSpawnInterval();
    }

    @Override
    protected void spawnEnemies() {
        // for every wave
        increment();
        determineNum();
        determineType();
        determineZone();
        determinePattern();

        // spawn enemies
        switch (pattern) {
            case PATTERN_LINE:
                spawnLine(num);
                break;
            case PATTERN_PINCER:
                spawnPincer(num);
                break;
            case PATTERN_SLIDE:
                spawnSlide(num);
                break;
            case PATTERN_V:
                spawnV(num);
                break;
            case PATTERN_VERTICAL:
                spawnVertical(num);
                break;
            case PATTERN_SQUARE:
                spawnSquare(num);
                break;
            case PATTERN_CROSS:
                spawnCross(num);
                break;
        }

        incrementDifficulty();

//        System.out.printf("Wave: %d, LastHeight: %d\n", wave, spawnHeight);

        wave++;
        System.out.println("Wave: " + wave);
        determineSpawnInterval();
    }

    @Override
    protected void increment() {
        waveSinceHealth++;
        waveSincePower++;
        waveSinceShield++;
    }

    @Override
    protected void determineNum() {
        minEnemies = enemyNumbers[enemytype][pattern][ENEMY_MIN];
        maxEnemies = enemyNumbers[enemytype][pattern][ENEMY_MAX];

        int currentMax = Math.min(minEnemies + difficulty, maxEnemies);

        num = Utility.randomRangeInclusive(minEnemies, currentMax);
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
        resetWaveSinceHealth();
    }

    private void spawnPower() {
        spawnPickupHelper(EntityID.PickupPower);
        System.out.println("Power-Up Spawned");
        resetWaveSincePower();
    }

    private void spawnShield() {
        spawnPickupHelper(EntityID.PickupShield);
        System.out.println("Shield Spawned");
        resetWaveSinceShield();
    }

    // Pickup Spawner Helped
    private void spawnPickupHelper(EntityID id) {
        spawnZone = Utility.randomRange(1, 3);
        entityHandler.spawnPickup(playerX, spawnY * SCALE, id);
        wave++;
//        System.out.println("Wave: " + wave);
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
                spawnFromDirection(num, spawnY, xStart, "", distance, dispersal, LEFT);
                break;
            case 2:
                spawnFromMiddle(num, spawnY, "", "", distance, dispersal);
                break;
            case 3:
                spawnFromDirection(num, spawnY, xEnd, "", distance, dispersal, RIGHT);
                break;
        }
    }

    private void spawnLineBasic(int num) {

    }

    private void spawnLineHeavy(int num) {
        
    }

    // todo: zone 2 and 3
    private void spawnPincer(int num) {
        // Determine Number
        double numD = num;
        double numD1 = numD/2;
//        Utility.log("Num2: " + numD1);
        num = (int) Math.floor(numD1);

        // Determine Distance
//        System.out.println(spawnZone + " " + num);
        int distance = (MOVEMENT_MAX);

        int dispersal = Utility.randomRangeInclusive(-1,1);
        dispersal = 0; // stub

        distance = (MOVEMENT_MAX) - (num-1)*4;

        spawnFromDirection(num, spawnY, xStart, "right", distance, dispersal, LEFT);
        spawnFromDirection(num, spawnY, xEnd, "left", distance, dispersal, RIGHT);
//
//        switch (spawnZone) {
//            case 1:
////                num = 1; // 18
////                num = 2; // 14
////                num = 3; // 10
////                num = 4; // 6
////                distance = (MOVEMENT_MAX) - (num-1)*4;
////
////                spawnFromDirection(num, spawnHeight, xStart, "right", distance, dispersal, LEFT);
////                spawnFromDirection(num, spawnHeight, xEnd, "left", distance, dispersal, RIGHT);
//                break;
//            case 2:
////                int offset = spacing * 2;
////                num = 1; // 10
////                num = 2; // 7
////                num = 3; // 3
////                num = 4; // 0
////
////                distance = (MOVEMENT_MAX) - 17;
////                Utility.log(distance+"");
////                spawnFromDirection(num, spawnHeight, xStart + offset, "right", distance, dispersal, LEFT);
////                spawnFromDirection(num, spawnHeight, xEnd - offset, "left", distance, dispersal, RIGHT);
////                break;
//            case 3:
////                timeFactor = 5.0;
////                time = (int) (time/timeFactor);
////                offset = spacing * 3;
////                spawnFromDirection(num, spawnHeight, xStart + offset, "", time, dispersal, LEFT);
////                spawnFromDirection(num, spawnHeight, xEnd - offset, "", time, dispersal, RIGHT);
////                break;
//        }
    }

    // todo: zone 2 and 3
    private void spawnSlide(int num) {
//        System.out.println(spawnZone + " " + num);

        // Determine Number
        double numD = num;
        double numD1 = numD/2;
//        Utility.log("Num2: " + numD1);
        num = (int) Math.floor(numD1);

        // Determine Distance
        int distance = (MOVEMENT_MAX);

        int dispersal = Utility.randomRangeInclusive(-1,1);
        dispersal = 0; // stub

        int yDiff = spacing;
        int first = Utility.randomRangeInclusive(0,1);

        if (first == 0) {
            spawnFromDirection(num, spawnY - yDiff, xStart, "right", distance, dispersal, LEFT);
            spawnFromDirection(num, spawnY, xEnd, "left", distance, dispersal, RIGHT);
        } else {
            spawnFromDirection(num, spawnY, xStart, "right", distance, dispersal, LEFT);
            spawnFromDirection(num, spawnY - yDiff, xEnd, "left", distance, dispersal, RIGHT);
        }
    }

    private void spawnV(int num) {
        int xStart = this.xStart + num/2 * spacing;
        int xEnd = this.xEnd - (num/2 + 1) * spacing;

        switch (spawnZone) {
            case 1:
                spawnFromMiddleV(xStart, num, spawnY);
                break;
            case 2:
                spawnFromMiddleV(xMid, num, spawnY);
                break;
            case 3:
                spawnFromMiddleV(xEnd, num, spawnY);
                break;
        }
    }

    private void spawnVertical(int num) {
//        System.out.println(spawnZone + " " + num);

        int distance = MOVEMENT_MAX - (num - 1)*2;
        int dispersal = Utility.randomRangeInclusive(-1,1);
        int x = 0;
        int direction = 0;

        switch (spawnZone) {
            case 1:
                x = xStart;
                direction = LEFT;
                break;
            case 2:
                x = xMid;
                direction = Utility.randomRangeInclusive(-1, 1);
                break;
            case 3:
                x = xEnd;
                direction = RIGHT;
                break;
        }
        spawnFromBottom(num, spawnY, x, distance, direction);
    }

    private void spawnSquare(int num) {
//        Utility.log("Num: " + num);
        double numD = num;
        double numD1 = (numD - 1)/4;
//        Utility.log("Num2: " + numD1);
        num = (int) Math.ceil(numD1);

//        num--;
        int x0 = 0;
        int y0 = spawnY - num* spacing;
        int xFin = 0;
        switch (spawnZone) {
            case 1:
                x0 = xStart;
                xFin = x0 + num* spacing;
                y0 = spawnY - num* spacing;
                break;
            case 2:
                x0 = xMid;
                xFin = x0 + num* spacing;
                y0 = spawnY - num* spacing;
                break;
            case 3:
                x0 = xEnd - num* spacing;
                xFin = x0 + num* spacing;
                y0 = spawnY - num* spacing;
                break;
        }

        spawnFromDirection(num, y0, x0, "", 0, 0, LEFT);
//        spawnFromLeft(num, y0, x0, "", 0, 0);
        spawnFromTop(num, y0, xFin);
        spawnFromBottom(num, spawnY, x0, 0, 0);
        spawnFromDirection(num, spawnY, xFin, "", 0, 0, RIGHT);
    }

    private void spawnCross(int num) {
        // todo: Remove duplication
        //        Utility.log("Num: " + num);
        double numD = num;
        double numD1 = (numD)/4;
//        Utility.log("Num2: " + numD1);
        num = (int) Math.ceil(numD1);

        int interval = spacing;
        int xLeft = 0;
        int xRight = 0;
        int yMid = spawnY - interval*num;
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

        spawnFromDirection(num, yMid, xLeft, "", 0, 0, LEFT);
        spawnFromDirection(num, yMid, xRight, "", 0, 0, RIGHT);
        spawnFromTop(num, yTop, x0);
        spawnFromBottom(num, yBottom, x0, 0, 0);
    }

    private String calculateMoveDirection(String move, int direction) {
        if (move == "") {
            if (Utility.randomRangeInclusive(0,1) == 0) {
                String result = "";
                if (direction == -1*LEFT)
                    result = "left";
                else if (direction == -1*RIGHT)
                    result = "right";
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
        enemiesSpawned++;
        Utility.log("Spawned in Wave: " + enemiesSpawned);
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

                if (num == enemyNumbers[enemytype][pattern][ENEMY_MAX]) {
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

                if (num == enemyNumbers[enemytype][pattern][ENEMY_MAX]) {
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

    private void spawnFromBottom(int num, int y, int x, int distance, int direction) {
        String move = calculateMoveDirection("", direction);
        // Base Case

        if (num > 1) {
            int y0 = y - (spacing);
            spawnFromBottom(num - 1, y0, x, distance, direction);
        }

        spawnHelper(x, y, move, distance);
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

        int spawnX = x*SCALE, spawnY = y*SCALE, time = distance*SCALE;

//        if (enemytype == 0) {
//            id = EntityID.EnemyBasic;
//        } else if (enemytype == 1) {
//            id = EntityID.EnemyMove;
//        }

        entityHandler.spawnEntity(spawnX, spawnY, enemytype, move, time);
//        System.out.println("Enemy spawned");
    }

    private void spawnHeavyHelper(int x, int y) {
        entityHandler.spawnEntity(x*SCALE, y*SCALE, EntityHandler.TYPE_HEAVY,  "", 0);
    }

//    private void spawnGround(int remainingHeight, int spawnHeight, int num, String spawnFrom) {
//        if (groundCount == num && height - remainingHeight >= spawnHeight) {
//            EnemyGround eG = new EnemyGround(xStart * SCALE, remainingHeight * SCALE, getLevelHeight());
//            groundCount++;
//            if (spawnFrom == "left") {
//                eG.setHMove("right");
//            } else if (spawnFrom == "right") {
//                eG.setHMove("left");
//                eG.setX(xEnd*SCALE);
//            }
//            entityHandler.addEnemy(eG);
//        }

//    }
//    pg

    private void spawnBoss(int x, int y) {
        entityHandler.spawnBoss(level, x*SCALE, y*SCALE);
    }

    private void determinePattern() {
        if (wave == 1) {
            lastLastPattern = lastPattern;
            lastPattern = pattern;
            pattern = PATTERN_LINE;

//            spawnHeavyHelper(xMid, spawnHeight);
        } else {
            lastLastPattern = lastPattern;
            lastPattern = pattern;
            int max = maxPatterns;
            if (enemytype == TYPE_HEAVY) {
                max = 2;
            }
            pattern = Utility.randomRange(0, max);

            while (pattern == lastPattern && pattern == lastLastPattern) {
                pattern = Utility.randomRange(0,max);
            }
        }
    }

    @Override
    protected void determineSpawnInterval() {
        float minPause = (float) spawnInterval_MIN;
        float maxPause = (float) spawnInterval_MAX;

        float spawnIntervalFloat = (maxPause - minPause) * (num - 1)/(maxEnemies - 1) + minPause;
        spawnInterval = (long) spawnIntervalFloat;

        if (enemytype == TYPE_DRONE && spawnIntervalFloat > spawnInterval_MIN) {
            spawnInterval = (long) (spawnIntervalFloat*0.9);
        }

        if (enemytype == TYPE_HEAVY && spawnIntervalFloat > spawnInterval_MIN) {
            spawnInterval = (long) (spawnIntervalFloat*1.1);
        }

        // todo: adjust
        if (pattern == PATTERN_SLIDE) {
            spawnInterval *= 1.5;
        }

        // todo: adjust
        if (pattern == PATTERN_PINCER) {
            spawnInterval *= 1.5;
        }

        // todo: adjust
        if (pattern == PATTERN_LINE) {
            spawnInterval *= 1.5;
        }

        // todo: adjust
        if (pattern == PATTERN_V) {
            spawnInterval *= 1.5;
        }

        // todo: adjust
        if (pattern == PATTERN_SQUARE) {
            spawnInterval *= 2;
        }

        // todo: adjust
        if (pattern == PATTERN_CROSS) {
            spawnInterval *= 1.5;
        }
    }

    private void determineSpawn() {
//        if level
    }

    @Override
    protected void determineZone() {
        lastLastZone = lastZone;
        lastZone = spawnZone;
        spawnZone = Utility.randomRangeInclusive(1, 3);

        // determine that no zone spawns more than twice in a row
        while (spawnZone == lastZone && spawnZone == lastLastZone) {
            spawnZone = Utility.randomRangeInclusive(1, 3);
        }
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
