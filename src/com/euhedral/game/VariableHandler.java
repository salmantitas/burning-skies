package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.Utility;
import com.euhedral.game.UI.UIHandler;

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

    public static Attribute power;
    public static Attribute shield;
    public static Attribute health;

    public static int speedBoostDuration;
    private static int speedBoostX;

    private static Color healthLow = Color.RED;
    private static Color healthMed = Color.ORANGE;
    private static Color healthHigh = Color.GREEN;

    // todo: Boss Health
//    public static Attribute bossHealth;

    // Score
    private static int score = 0;
    private static int scoreX = Utility.percWidth(75);;
    private static int scoreY = Utility.intAtWidth640(20);
    private static int scoreSize = Utility.percWidth(2);
    private static Font scoreFont = UIHandler.customFont.deriveFont(0, scoreSize);

    // High Score
    public static final int HIGH_SCORE_NUMBERS_MAX = 10;
    private static LinkedList<Integer> highScore;
    private static int baseScore;
    private static int highScoreFontSize = 25;
    private static Font highScoreFont = UIHandler.customFont.deriveFont(1, highScoreFontSize);

    private static int lineHeightInPixel = Utility.intAtWidth640(18);

    // Level
    private static int levelX = Utility.percWidth(90);
    private static int levelY;
    private static int levelSize = scoreSize;
    private static Font levelFont = UIHandler.customFont.deriveFont(0, levelSize);

    private static float renderWaveDuration = 1f;

    // Timer
    private static int timerX = Utility.percWidth(75);
    private static int timerY;

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
    private static final int MAXLEVEL = EntityHandler.enemyTypes;
//    public static int difficulty = 1;

    private static int healthBossDef, healthBoss;
    private static int bossScore = 500;
    private static boolean bossLives = false;

    public static HashMap<Color, EntityID> colorMap;

    // todo: ActionTag will be updated here
    private ActionTag action = null;

    private static boolean tutorial = true;

    // Deadzones
    public static int deadzoneWidth = Utility.intAtWidth640(32);
    public static int deadzoneLeftX = 0, deadzoneRightX = Engine.WIDTH - deadzoneWidth - Utility.intAtWidth640(8), deadzoneTop = Utility.intAtWidth640(50);

    // Difficulty

    static int DIFFICULTY_EASY = 0;
    static int DIFFICULTY_NORMAL = 1;
//    static int DIFFICULTY_CUSTOM = DIFFICULTY_NORMAL + 1;
    static int DIFFICULTY_CHALLENGE = DIFFICULTY_NORMAL + 1;

    public static int difficultyType = DIFFICULTY_NORMAL;
    public static int difficultyLevel = 1;

    // Shop Costs

//    public static final int costPower = 1000;

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
        health = new Attribute("Health", 100, false);
        health.setY(Utility.percHeight(5));
        health.setForegroundColor(healthHigh);

        levelY = Utility.percHeight(4);
        timerY = scoreY + Utility.intAtWidth640(25);

        power = new Attribute("Power", 1, false);
        power.setMIN(1);
        power.setMAX(2);
        power.setX(Utility.intAtWidth640(17));
        power.setY(scoreY);
        power.setFontSize(scoreSize);

        shield = new Attribute("Shield", 0, false);
        shield.setMIN(0);
        shield.setMAX(100);
        shield.setY(health.getY() + Utility.percHeight(3));
        shield.setForegroundColor(Color.blue);

        speedBoostDuration = 0;
        speedBoostX = power.getX() + Utility.intAtWidth640(120);
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
        renderWave(g);
        health.renderBar(g);
        if (shield.getValue() > 0)
            shield.renderBar(g);
        if (Engine.stateIs(GameState.Game))
            renderTimer(g);
        if (speedBoostDuration > 0)
            renderSpeedBoost(g);
//            renderFPS(g);
    }

    public static void renderScore(Graphics g) {
        g.setFont(scoreFont);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, scoreX, scoreY);
    }

    public static void renderLevel(Graphics g) {
        g.setFont(levelFont);
        g.setColor(Color.YELLOW);
        g.drawString("Level " + level, timerX, levelY);
    }

    public static void renderWave(Graphics g) {
        if (renderWaveDuration > 0 && (Engine.stateIs(GameState.Game))) {
            g.setFont(levelFont);
            g.setColor(Color.YELLOW);
//            int offsetX = Utility.intAtWidth640(30), offsetY = Utility.intAtWidth640(90);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(Utility.makeTransparent(renderWaveDuration));
            g.drawString("Wave " + level, Engine.WIDTH/2 - Utility.intAtWidth640(40), Utility.intAtWidth640(80));
            g2d.setComposite(Utility.makeTransparent(1f));
            renderWaveDuration -= 0.01f;
        }
    }

    public static void renderTimer(Graphics g) {
        g.setFont(levelFont);
        g.setColor(Color.YELLOW);
        g.drawString("Timer: " + GameController.getCurrentTime(), timerX, timerY);
    }

    public static void renderSpeedBoost(Graphics g) {
        g.setFont(levelFont);
        g.setColor(Color.WHITE);
        g.drawString("Speed Boost: " + speedBoostDuration, speedBoostX, power.getY());
    }

//    public static void renderFPS(Graphics g) {
//        g.setFont(new Font("arial", 1, levelSize));
//        g.setColor(Color.YELLOW);
//        g.drawString("FPS: " + Engine.getFPS(), timerX, shield.getY());
//    }

    protected static void drawBossHealth(Graphics g) {
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

    public static int getHealthBossDef() {
        return healthBossDef;
    }

    public static void setHealthBossDef(int newHealthBossDef) {
        healthBossDef = newHealthBossDef;
    }

    public int getHealthBoss() {
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

    public static boolean isBossLives() {
        return bossLives;
    }

    public static void setBossLives(boolean newBossLives) {
        bossLives = newBossLives;
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

    public static boolean finishedFinalLevel() {
        return level > MAXLEVEL;
    }

    public static void setLevel(int i) {
        renderWaveDuration = 1f;
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

    public static int getDifficultyLevel() {
        if (difficultyType == DIFFICULTY_CHALLENGE)
            return EntityHandler.enemyTypes;
//        todo: Custom Difficulty
//        else if () {
//
//        }
        else
            return (difficultyType + 1);
    }
}
