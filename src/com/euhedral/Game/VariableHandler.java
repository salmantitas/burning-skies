package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.UI.HUD;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

public class VariableHandler {

    /****************************************
     * Global Game Variables                *
     * Comment Out Whichever is Unnecessary *
     ****************************************/
    private static boolean hudActive = true;
    private static boolean console = false;

    /****************************************
     * Common Game Variables                *
     * Comment Out Whichever is Unnecessary *
     ****************************************/

    // Attributes

    public static Attribute health, shield, firepower;

    public static int pulseVarialbleToBeNamed = 0;

//    public static int shootRateBoostDuration;
    private static int shootRateBoostX;

    public static boolean homing;

    public static boolean pulse;

    private static Color healthLow = Color.RED;
    private static Color healthMed = Color.ORANGE;
    public static Color healthHigh = Color.GREEN;

    // Enemy Types
    public static final int TYPE_BASIC1 = 0;
    public static final int TYPE_BASIC2 = TYPE_BASIC1 + 1;
    public static final int TYPE_HEAVY = TYPE_BASIC2 + 1;
    public static final int TYPE_BASIC3 = TYPE_HEAVY + 1;
    public static final int TYPE_DRONE1 = TYPE_BASIC3 + 1;

    public static final int TYPE_STATIC1 = TYPE_DRONE1 + 1;
    public static final int TYPE_SIDE1 = TYPE_STATIC1 + 1;
    public static final int TYPE_DRONE2 = TYPE_SIDE1 + 1;
    public static final int TYPE_FAST = TYPE_DRONE2 + 1;
    public static final int TYPE_SIDE2 = TYPE_FAST + 1;

    public static final int TYPE_DRONE3 = TYPE_SIDE2 + 1;
    public static final int TYPE_MINE1 = TYPE_DRONE3 + 1;
    public static final int TYPE_SCATTER1 = TYPE_MINE1 + 1;
    public static final int TYPE_DRONE4 = TYPE_SCATTER1 + 1;
    public static final int TYPE_SIDE3 = TYPE_DRONE4 + 1;

    public static final int TYPE_MINE2 = TYPE_SIDE3 + 1;
    public static final int TYPE_DRONE5 = TYPE_MINE2 + 1;
    public static final int TYPE_SCATTER2 = TYPE_DRONE5 + 1;
    public static final int TYPE_LASER = TYPE_SCATTER2 + 1;
    public static final int TYPE_DRONE6 = TYPE_LASER + 1;

    public static final int enemyTypes = TYPE_DRONE6 + 1;

    // Custom Fond
    public static Font customFont;

    // Score
    private static int score = 0;

    // High Score
    public static final int HIGH_SCORE_NUMBERS_MAX = 10;
    private static LinkedList<Integer> highScore;
    private static int baseScore;
    private static int highScoreFontSize = 25;
    private static Font highScoreFont;

    private static int lineHeightInPixel = Utility.intAtWidth640(18);

    // Notifications
    public static String saveText = "Game Saved Successfully.";
    public static String loadText = "Game Loaded Successfully.";
    public static String saveDataNotification = "";
    public static int notificationSet;

    /******************
     * User Variables *
     ******************/

    private static HUD hud;

    private static int STARTLEVEL = 1;
    private static int level;
    private static final int MAXLEVEL = enemyTypes;

    public static int healthBossDefault;
    private static int healthBoss;
    private static int bossScore = 500;
    private static boolean bossAlive = false;

    public static Difficulty difficulty;

    public static HashMap<Color, EntityID> colorMap;

    private static boolean tutorial = true;

    // Deadzones
    public static int deadzoneWidth = Utility.intAtWidth640(32);
    public static int deadzoneLeftX = 0, deadzoneRightX = Engine.WIDTH - deadzoneWidth - Utility.intAtWidth640(8), deadzoneTop = Utility.intAtWidth640(50);

    public VariableHandler() {
        try {
            URL fontURL = getClass().getResource("/PublicPixel-rv0pA.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontURL.openStream());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        hud = new HUD();

        highScoreFont = customFont.deriveFont(1, highScoreFontSize);

        colorMap = new HashMap<>();
//        initializeColorMap();
        initializeAttributes();
        try {
            highScore = SaveLoad.loadHighScore();
        } catch (IOException e) {
            highScore = new LinkedList<>();
            for (int i = 0; i < HIGH_SCORE_NUMBERS_MAX; i++) {
                highScore.add(0);
            }
            SaveLoad.saveHighScore();
        }

        difficulty = new Difficulty(enemyTypes);
    }

//    private void initializeColorMap() {
//        /*************
//         * Game Code *
//         *************/
//
//        colorMap.put(new Color(0, 0, 255), EntityID.Player);
//        colorMap.put(new Color(255, 0, 0), EntityID.EnemyBasic);
//        colorMap.put(new Color(150, 0, 0), EntityID.EnemyMove);
//        colorMap.put(new Color(100, 0, 0), EntityID.EnemySnake);
//        colorMap.put(new Color(200, 0, 0), EntityID.EnemyFast);
//        colorMap.put(new Color(255, 150, 244), EntityID.EnemyGround);
//        colorMap.put(new Color(0, 255, 0), EntityID.PickupHealth);
//        colorMap.put(new Color(255, 255, 0), EntityID.PickupShield);
//        colorMap.put(new Color(255, 216, 0), EntityID.Boss);
//    }

    public static void initializeAttributes() {
        health = new Attribute("Health", 100, false);
        hud.setHealthProperties(health);
        health.setForegroundColor(VariableHandler.healthHigh);
        health.setGrid(true);
        health.roundDownInRender();

        firepower = new Attribute("Shoot Rate", 1, false);
        hud.setFirepowerProperties(firepower);

        shield = new Attribute("Shield", 0, false);
        hud.setShieldProperties(shield);

        int iconSpacingX = 148;

//        shootRateBoostIconX = healthIconX + iconSpacingX;

//        firepower.setX(power.getX());

//        shootRateBoostDuration = 0;
//        shootRateBoostX = shootRateBoostIconX + Utility.intAtWidth640(20);

        homing = false;
        pulse = false;

        hud.setPulseProperties(firepower, iconSpacingX);
    }

    public static void console() {
        console = !console;
        System.out.println("Console is " + console);
    }

    public static boolean isConsole() {
        return console;
    }

    public static boolean isHudActive() {
        return hudActive;
    }

    public void resetScore() {
        score = 0;
    }

//    public void resetDifficulty() {
//        difficulty = 1;
//    }

    public static void updateHighScore() {
        if (highScore.size() == 0) {
            highScore.add(score);
            SaveLoad.saveHighScore();
            score = 0;
            return;
        }

        for (int i = 0; i < highScore.size(); i++) {
            baseScore = highScore.get(i);

            if (score >= baseScore) {
                highScore.add(i, score);
                SaveLoad.saveHighScore();
                score = 0;
                return;
            }
        }
    }

    public void resetFirepower() {
        firepower.reset();
    }

    public static void increaseScore(int score) {
        VariableHandler.score += score;
    }

    public static void decreaseScore(int score) {
        VariableHandler.score -= score;
    }

    /**********
     * Render *
     **********/



    /**********************
     * Getters and setters *
     ***********************/

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        VariableHandler.score = score;
    }

//    public int getScoreX() {
//        return scoreX;
//    }

//    public void setScoreX(int scoreX) {
//        VariableHandler.scoreX = scoreX;
//    }

//    public int getScoreY() {
//        return scoreY;
//    }

//    public void setScoreY(int scoreY) {
//        VariableHandler.scoreY = scoreY;
//    }

//    public int getScoreSize() {
//        return scoreSize;
//    }

//    public void setScoreSize(int scoreSize) {
//        VariableHandler.scoreSize = scoreSize;
//    }

    public static void initHealthBoss(int newHealthBoss) {
        setHealthBossDefault(newHealthBoss);
        setHealthBoss(newHealthBoss);
    }

    public static int getHealthBossDefault() {
        return healthBossDefault;
    }

    private static void setHealthBossDefault(int newHealthBossDefault) {
        healthBossDefault = newHealthBossDefault;
    }

    public static int getHealthBoss() {
        return healthBoss;
    }

    public static void setHealthBoss(int newHealthBoss) {
        healthBoss = newHealthBoss;
    }

    public static int getBossScore() {
        return bossScore;
    }

    public void setBossScore(int bossScore) {
        this.bossScore = bossScore;
    }

    public static boolean isBossAlive() {
        return bossAlive;
    }

    public static void setBossAlive(boolean newBossAlive) {
        bossAlive = newBossAlive;
    }

    public static int getLevel() {
        return level;
    }

    public void resetLevel() {
        level = STARTLEVEL;
    }

    public void incrementLevel() {
        level++;
    }

    public static boolean finishedFinalLevel() {
        return level > MAXLEVEL;
    }

    public static void setLevel(int i) {
        hud.renderWaveDuration = 1f;
        level = i;
//        if (finishedFinalLevel())
//            level = MAXLEVEL;
    }

    public static boolean isTutorial() {
        return tutorial;
    }

    public static void initTutorial() {
        tutorial = !tutorial;
    }

    public static void toggleTutorial() {
        tutorial = !tutorial;
        SaveLoad.saveSettings();
    }

    public static void resetSaveDataNotification() {
        saveDataNotification = "";
    }

    public static void setHealthColor() {
        if (health.getValue() > 66) {
            health.setForegroundColor(healthHigh);
        } else if (health.getValue() > 33) {
            health.setForegroundColor(healthMed);
        } else {
            health.setForegroundColor(healthLow);
        }
    }

    public static void drawHighScore(Graphics g, int x0, int y0) {

        g.setFont(highScoreFont);

        for (int i = 0; i < HIGH_SCORE_NUMBERS_MAX; i++) {
            baseScore = highScore.get(i);
            g.drawString(Integer.toString(baseScore), x0, y0 + (i + 1) * lineHeightInPixel);
        }
    }

    public static LinkedList<String> getHighScoreList() {
        // why not just return highscore?
        LinkedList outputList = new LinkedList();
        for (int i = 0; i < HIGH_SCORE_NUMBERS_MAX; i++) {
            outputList.add(highScore.get(i).toString());
        }
        return outputList;
    }
}
