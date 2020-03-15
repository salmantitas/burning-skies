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

    // Vitality
//    private int lives = 3;

    private int healthX = Utility.percWidth(2.5);
    private int healthY = 5 * healthX;
    private final int healthDefault = 100;
    private int healthMAX = healthDefault;
    private int health = healthDefault;

    // Score
    private int score = 0;
    private int scoreX = Utility.percWidth(2.5);
    private int scoreY = Utility.percHeight(15);
    private int scoreSize = Utility.percWidth(2);

    // Power
    private int powerX = Utility.percWidth(24);
    private int powerY = scoreY;
    private int powerSize = scoreSize;
    private final int maxPower = 5;
    private int power = 1;

    /*
    * User Variables
    * */

    private static int STARTLEVEL = 1;
    private static int level;
    private final int MAXLEVEL = 2;

    private int healthBossDef, healthBoss;
    private int bossScore = 500;
    private boolean bossLives = false;

    public static HashMap<Color, EntityID> colorMap;

    // todo: ActionTag will be updated here
    private ActionTag action = null;

    public VariableManager() {
        // Engine Code
        colorMap = new HashMap<>();
        initializeColorMap();
    }

    private void initializeColorMap() {
        // Game Code

        colorMap.put(new Color(0,0,255), EntityID.Player );
        colorMap.put(new Color(255,0,0), EntityID.EnemyBasic);
        colorMap.put(new Color(150,0,0), EntityID.EnemyMove);
        colorMap.put(new Color(100,0,0), EntityID.EnemySnake);
        colorMap.put(new Color(200,0,0), EntityID.EnemyFast);
        colorMap.put(new Color(255,150,244), EntityID.EnemyGround);
        colorMap.put(new Color(0,255,0), EntityID.Pickup);
        colorMap.put(new Color(255,216,0), EntityID.Boss);
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

    public void resetHealth() {
        health = healthDefault;
    }

    public void resetScore() {
        score = 0;
    }

    public void resetPower() {
        power = 1;
    }

    public void increaseHealth(int health) {
        this.health += health;
        if (health >= healthMAX)
            setHealth(healthMAX);
    }

    public void decreaseHealth(int health) {
        this.health -= health;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public void decreaseScore(int score) {
        this.score -= score;
    }

    public void increasePower(int power) {
        this.power += power;
    }

    public void decreasePower(int power) {
        this.power -= power;
    }

    /*
    * Render
    * */

    public void renderHealth(Graphics g) {
        int width = Utility.intAtWidth640(2);
        int height = width * 6;
        Color backColor = Color.lightGray;
        Color healthColor = Color.GREEN;
        g.setColor(backColor);
        g.fillRect(healthX, healthY, healthDefault * width, height);
        g.setColor(healthColor);
        g.fillRect(healthX, healthY, health * width, height);
    }

    public void renderScore(Graphics g) {
        g.setFont(new Font("arial", 1, scoreSize));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, scoreX, scoreY);
    }

    public void renderPower(Graphics g) {
        g.setFont(new Font("arial", 1, powerSize));
        g.setColor(Color.WHITE);
        g.drawString("Power: " + power, powerX, powerY);
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


    /**/

    public int getHealthX() {
        return healthX;
    }

    public void setHealthX(int healthX) {
        this.healthX = healthX;
    }

    public int getHealthY() {
        return healthY;
    }

    public void setHealthY(int healthY) {
        this.healthY = healthY;
    }

    public int getHealthDefault() {
        return healthDefault;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreX() {
        return scoreX;
    }

    public void setScoreX(int scoreX) {
        this.scoreX = scoreX;
    }

    public int getScoreY() {
        return scoreY;
    }

    public void setScoreY(int scoreY) {
        this.scoreY = scoreY;
    }

    public int getScoreSize() {
        return scoreSize;
    }

    public void setScoreSize(int scoreSize) {
        this.scoreSize = scoreSize;
    }

    public int getPowerX() {
        return powerX;
    }

    public void setPowerX(int powerX) {
        this.powerX = powerX;
    }

    public int getPowerY() {
        return powerY;
    }

    public void setPowerY(int powerY) {
        this.powerY = powerY;
    }

    public int getPowerSize() {
        return powerSize;
    }

    public void setPowerSize(int powerSize) {
        this.powerSize = powerSize;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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

    public int getLevel() {
        return level;
    }

    public void resetLevel() {
        level = STARTLEVEL;
    }

    public void nextLevel() {
        level++;
    }

    public boolean finishedFinalLevel() {
        return level > MAXLEVEL;
    }

    public void setLevel(int i) {
        level = i;
        if (finishedFinalLevel())
            level = MAXLEVEL;
    }
}
