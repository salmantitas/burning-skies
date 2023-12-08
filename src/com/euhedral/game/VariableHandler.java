package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class VariableHandler {

    /****************************************
     * Global Game Variables                *
     * Comment Out Whichever is Unnecessary *
     ****************************************/
    private static boolean hud = true;
    private static boolean console = false;

    /****************************************
     * Common Game Variables                *
     * Comment Out Whichever is Unnecessary *
     ****************************************/

    // Attributes

    // Vitality
//    private int lives = 3;

    public static Attribute power;
    public static Attribute ground;
    public static Attribute shield;
    public static Attribute health;

    private static Color healthLow = Color.RED;
    private static Color healthMed = Color.ORANGE;
    private static Color healthHigh = Color.GREEN;

    // todo: Boss Health
//    public static Attribute bossHealth;

    // Score
    private static int score = 0;
    private static int scoreX = Utility.percWidth(2.5);
    private static int scoreY = Utility.percHeight(4);
    private static int scoreSize = Utility.percWidth(2);

    // High Score
    public static final int HIGH_SCORE_NUMBERS_MAX = 10;
    private static LinkedList<Integer> highScore;

    // Level
    private static int levelX = Utility.percWidth(90);
    private static int levelY;
    private static int levelSize = scoreSize;

    // Timer
    private static int timerX = Utility.percWidth(85);

    // Notifications
    public static String saveText = "Game Saved Successfully.";
    public static String loadText = "Game Loaded Successfully.";
    public static String saveDataNotification = "";
    public static int notificationSet;

    /******************
     * User Variables *
     ******************/

    private static int STARTLEVEL = 1;
    private static int level;
    private static final int MAXLEVEL = 1;

    private int healthBossDef, healthBoss;
    private int bossScore = 500;
    private boolean bossLives = false;

    public static HashMap<Color, EntityID> colorMap;

    // todo: ActionTag will be updated here
    private ActionTag action = null;

//    // todo: Ground will be updated here
//    private static boolean ground = false;

    private static boolean tutorial = true;

    // Shop Costs

//    public static final int costPower = 1000;
//    public static final int costGround = 1000;

    public VariableHandler() {
        colorMap = new HashMap<>();
        initializeColorMap();
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
    }

    private void initializeColorMap() {
        /*************
         * Game Code *
         *************/

        colorMap.put(new Color(0, 0, 255), EntityID.Player);
        colorMap.put(new Color(255, 0, 0), EntityID.EnemyBasic);
        colorMap.put(new Color(150, 0, 0), EntityID.EnemyMove);
        colorMap.put(new Color(100, 0, 0), EntityID.EnemySnake);
        colorMap.put(new Color(200, 0, 0), EntityID.EnemyFast);
        colorMap.put(new Color(255, 150, 244), EntityID.EnemyGround);
        colorMap.put(new Color(0, 255, 0), EntityID.PickupHealth);
        colorMap.put(new Color(255, 255, 0), EntityID.PickupShield);
        colorMap.put(new Color(255, 216, 0), EntityID.Boss);
    }

    public static void initializeAttributes() {
        health = new Attribute(100, false, 500);
        health.setY(Utility.percHeight(5));
        health.setForegroundColor(healthHigh);

        levelY = Utility.percHeight(7);

        power = new Attribute(1, false, 1000);
        power.setMAX(5);
        power.setX(Utility.percWidth(24));
        power.setY(scoreY);
        power.setFontSize(scoreSize);

        ground = new Attribute(0, true, 1500);
        shield = new Attribute(0, false, 2000);
        shield.setMAX(100);
        shield.setY(health.getY() + Utility.percHeight(3));
        shield.setForegroundColor(Color.yellow);
    }

    public static void console() {
        console = !console;
        System.out.println("Console is " + console);
    }

    public static boolean isConsole() {
        return console;
    }

    public static boolean isHud() {
        return hud;
    }

    public void resetScore() {
        score = 0;
    }

    public static void updateHighScore() {
        if (highScore.size() == 0) {
            highScore.add(score);
            SaveLoad.saveHighScore();
            score = 0;
            return;
        }

        for (int i = 0; i < highScore.size(); i++) {
            int baseScore = highScore.get(i);

            if (score >= baseScore) {
                highScore.add(i, score);
                SaveLoad.saveHighScore();
                score = 0;
                return;
            }
        }
    }

    public void resetPower() {
        power.reset();
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

    public static void renderHUD(Graphics g) {
        renderScore(g);
        power.renderValue(g);
        health.renderBar(g);
        if (shield.getValue() > 0)
            shield.renderBar(g);
        if (Engine.stateIs(GameState.Game))
            renderTimer(g);
    }

    public static void renderScore(Graphics g) {
        g.setFont(new Font("arial", 1, scoreSize));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, scoreX, scoreY);
    }

    public static void renderLevel(Graphics g) {
        g.setFont(new Font("arial", 1, levelSize));
        g.setColor(Color.WHITE);
        g.drawString("Level " + level, levelX, levelY);
    }

    public static void renderTimer(Graphics g) {
        g.setFont(new Font("arial", 1, levelSize));
        g.setColor(Color.YELLOW);
        g.drawString("Timer: " + GameController.getCurrentTime(), timerX, levelY);
    }

    protected void drawBossHealth(Graphics g) {
        int startX = Utility.percWidth(35);
        int endX = Utility.percWidth(65);
        int diffX = endX - startX;

        int y = Utility.percHeight(28);
        int width = diffX / healthBossDef;
        int height = width;
        Color backColor = Color.lightGray;
        Color healthColor = Color.RED;
        g.setColor(backColor);
        g.fillRect(startX, y, healthBossDef * width, height);
        g.setColor(healthColor);
        g.fillRect(startX, y, healthBoss * width, height);
    }

    /**********************
     * Getters and setters *
     ***********************/

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        VariableHandler.score = score;
    }

    public int getScoreX() {
        return scoreX;
    }

    public void setScoreX(int scoreX) {
        VariableHandler.scoreX = scoreX;
    }

    public int getScoreY() {
        return scoreY;
    }

    public void setScoreY(int scoreY) {
        VariableHandler.scoreY = scoreY;
    }

    public int getScoreSize() {
        return scoreSize;
    }

    public void setScoreSize(int scoreSize) {
        VariableHandler.scoreSize = scoreSize;
    }

    public int getHealthBossDef() {
        return healthBossDef;
    }

    public void setHealthBossDef(int healthBossDef) {
        this.healthBossDef = healthBossDef;
    }

    public int getHealthBoss() {
        return healthBoss;
    }

    public void setHealthBoss(int healthBoss) {
        this.healthBoss = healthBoss;
    }

    public int getBossScore() {
        return bossScore;
    }

    public void setBossScore(int bossScore) {
        this.bossScore = bossScore;
    }

    public boolean isBossLives() {
        return bossLives;
    }

    public void setBossLives(boolean bossLives) {
        this.bossLives = bossLives;
    }

    public static int getLevel() {
        return level;
    }

    public void resetLevel() {
        level = STARTLEVEL;
    }

    public void nextLevel() {
        level++;
    }

    public static void setGround(boolean active) {
        ground.setActive(active);
    }

    public static boolean gotGround() {
        return ground.isActive();
    }

    public static boolean finishedFinalLevel() {
        return level > MAXLEVEL;
    }

    public static void setLevel(int i) {
        level = i;
        if (finishedFinalLevel())
            level = MAXLEVEL;
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
        int fontSize = 25;
        g.setFont(new Font("arial", 1, fontSize));

        int lineHeightInPixel = 35;
        for (int i = 0; i < highScore.size(); i++) {
            int val = highScore.get(i);
            g.drawString(Integer.toString(val), x0, y0 + (i + 1) * lineHeightInPixel);
        }
    }

    public static LinkedList<String> getHighScoreList() {
        LinkedList outputList = new LinkedList();
        for (int i = 0; i < HIGH_SCORE_NUMBERS_MAX; i++) {
            outputList.add(highScore.get(i).toString());
        }
        return outputList;
    }
}
