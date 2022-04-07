package com.euhedral.game;

import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.HashMap;

public class VariableManager {

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

    public static Attribute health;
    public static Attribute power;
    public static Attribute ground;
    public static Attribute shield;

    // todo: Boss Health
//    public static Attribute bossHealth;

    // Score
    private static int score = 0;
    private static int scoreX = Utility.percWidth(2.5);
    private static int scoreY = Utility.percHeight(4);
    private static int scoreSize = Utility.percWidth(2);

    // Level
    private static int levelX = Utility.percWidth(50);
    private static int levelY = scoreY;
    private static int levelSize = scoreSize;

    // Shop Costs

//    public static final int costPower = 1000;
//    public static final int costGround = 1000;

    /******************
     * User Variables *
     ******************/

    private static int STARTLEVEL = 1;
    private static int level;
    private static final int MAXLEVEL = 3;

    private int healthBossDef, healthBoss;
    private int bossScore = 500;
    private boolean bossLives = false;

    public static HashMap<Color, EntityID> colorMap;

    // todo: ActionTag will be updated here
    private ActionTag action = null;

//    // todo: Ground will be updated here
//    private static boolean ground = false;

    private static boolean tutorial = false;

    public VariableManager() {
        colorMap = new HashMap<>();
        initializeColorMap();
        initializeAttributes();
    }

    private void initializeColorMap() {
        /*************
         * Game Code *
         *************/

        colorMap.put(new Color(0,0,255), EntityID.Player );
        colorMap.put(new Color(255,0,0), EntityID.EnemyBasic);
        colorMap.put(new Color(150,0,0), EntityID.EnemyMove);
        colorMap.put(new Color(100,0,0), EntityID.EnemySnake);
        colorMap.put(new Color(200,0,0), EntityID.EnemyFast);
        colorMap.put(new Color(255,150,244), EntityID.EnemyGround);
        colorMap.put(new Color(0,255,0), EntityID.PickupHealth);
        colorMap.put(new Color(255,255,0), EntityID.PickupShield);
        colorMap.put(new Color(255,216,0), EntityID.Boss);
    }

    public static void initializeAttributes() {
        health = new Attribute(100, false, 500);
        health.setY(Utility.percHeight(5));
        health.setBodyColor(Color.green);

        power = new Attribute(1, false, 1000);
        power.setMAX(5);
        power.setX(Utility.percWidth(24));
        power.setY(scoreY);
        power.setFontSize(scoreSize);

        ground = new Attribute(0, true, 1500);
        shield = new Attribute(0, false, 2000);
        shield.setMAX(100);
        shield.setY(health.getY() + Utility.percHeight(3));
        shield.setBodyColor(Color.yellow);
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

    public void resetPower() {
        power.reset();
    }

    public static void increaseScore(int score) {
        VariableManager.score += score;
    }

    public static void decreaseScore(int score) {
        VariableManager.score -= score;
    }

    /**********
     * Render *
     **********/

    public static void renderHUD(Graphics g) {
        renderScore(g);
        power.renderValue(g);
        health.renderBar(g);
        shield.renderBar(g);
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
        VariableManager.score = score;
    }

    public int getScoreX() {
        return scoreX;
    }

    public void setScoreX(int scoreX) {
        VariableManager.scoreX = scoreX;
    }

    public int getScoreY() {
        return scoreY;
    }

    public void setScoreY(int scoreY) {
        VariableManager.scoreY = scoreY;
    }

    public int getScoreSize() {
        return scoreSize;
    }

    public void setScoreSize(int scoreSize) {
        VariableManager.scoreSize = scoreSize;
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

    public static boolean tutorialEnabled() {
        return tutorial;
    }

    public static void toggleTutorial() {
        tutorial = !tutorial;
        SaveLoad.saveSettings();
    }
}
