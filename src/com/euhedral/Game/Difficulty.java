package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;

import java.awt.*;

public class Difficulty {
    private static int enemyTypes;

    // Difficulty

    public static int DIFFICULTY_NORMAL = 0;
    public static int DIFFICULTY_CHALLENGE = DIFFICULTY_NORMAL + 1;
    public static int difficultyMode = DIFFICULTY_NORMAL;

    private static String difficultyName = "";
    private static String text1 = "";
    private static String text2 = "";

    private static int size1 = Utility.intAtWidth640(25);
    private static int size2 = Utility.intAtWidth640(10);
    private static Font font1 = VariableHandler.customFont.deriveFont(1, size1);
    private static Font font2 = VariableHandler.customFont.deriveFont(1, size2);

    private static int lineSpace = Utility.intAtWidth640(25);

    private static int difficultyNameX = 400;
    private static int difficultyNameY = 200;
    private static int text1X = 200;
    private static int text1Y = difficultyNameY + lineSpace;
    private static int text2X = text1X;
    private static int text2Y = text1Y + lineSpace;
    private static int currentButton = 0;

    private static int mult_MIN = 1;
    private static int mult_MAX = 2;
    private static double damageDealtMult = 1;
    private static double damageTakenMult = 1;
    private static boolean firePowerLoss = true;

    private static double enemyFireRateMult = 1;
    private static double enemyBulletSpeedMult = 1;

    public Difficulty(int enemyTypes) {
        this.enemyTypes = enemyTypes;
    }

    public static int getDifficultyLevel() {
        if (difficultyMode == DIFFICULTY_CHALLENGE)
            return enemyTypes;
        else
            return difficultyMode + 1;
    }

    public static void increaseDifficulty() {
        Difficulty.difficultyMode++;
        if (difficultyMode > DIFFICULTY_CHALLENGE)
            difficultyMode = 0;
    }

    public static void decreaseDifficulty() {
        difficultyMode--;
        if (difficultyMode < 0)
            difficultyMode = DIFFICULTY_CHALLENGE;
    }

    public static void render(Graphics g) {
        if (currentButton == 0 ) {
            if (difficultyMode == 0) {
                difficultyName = " Normal";
                text1 = "The intended experience.";
                text2 = "Enemies will progressively get more difficult";
            } else {
                difficultyName = " Master";
                text1 = "For the experienced player.";
                text2 = "All enemies available from the start";
            }
        } else if (currentButton == 1) {
            difficultyName = "Damage Dealt";
            text1 = "Increase the damage dealt, but get half the score";
            text2 = "Damage Dealt: x" + damageDealtMult + ", Score Multiplier: X" + 1d / damageDealtMult;
        } else if (currentButton == 2) {
            difficultyName = "Damage Taken";
            text1 = "Increase the damage taken, but get half the score";
            text2 = "Damage Taken: x" + damageTakenMult + ", Score Multiplier: X" + damageTakenMult;
        }
        else if (currentButton == 3) {
            difficultyName = "Game Speed";
            text1 = "Risk higher speeds for higher score per enemy";
            text2 = "Game Speed : x" + Engine.getGameSpeedScaleMult() + ", Score Multiplier: X" + Engine.getGameSpeedScaleMult();
        } else if (currentButton == 4) {
            difficultyName = "Firepower Loss";
            text1 = "Lose firepower when hit";
            text2 = "Firepower Loss : " + (firePowerLoss ? "ON" : "OFF") + ", Score Multiplier: X" + getFirePowerLossMult();
        } else if (currentButton == 5) {
            difficultyName = "Enemy Fire Rate";
            text1 = "Faster enemy fire rate for higher the score";
            text2 = "Enemy Fire Rate : x" + enemyFireRateMult + ", Score Multiplier: X" + enemyFireRateMult;
        } else if (currentButton == 6) {
            difficultyName = "Enemy Bullet Speed";
            text1 = "Double the speed of enemy bullets, double the score";
            text2 = "Enemy Bullet Speed : x" + enemyBulletSpeedMult + ", Score Multiplier: X" + enemyBulletSpeedMult;
        } else {
            difficultyName = "";
            text1 = "";
            text2 = "";
        }

        g.setColor(Color.BLACK);
        g.setFont(font1);

        int width = g.getFontMetrics().stringWidth(difficultyName);
        difficultyNameX = (Engine.WIDTH - width)/2;

        g.drawString(difficultyName, difficultyNameX, difficultyNameY);

        g.setFont(font2);

        width = g.getFontMetrics().stringWidth(text1);
        text1X = (Engine.WIDTH - width)/2;

        width = g.getFontMetrics().stringWidth(text2);
        text2X = (Engine.WIDTH - width)/2;

        g.drawString(text1, text1X, text1Y);
        g.drawString(text2, text2X, text2Y);
    }

    public static void increaseGameSpeed() {
        if (Engine.getGameSpeedScaleMult() == mult_MAX) {

        } else {
            Engine.setGameSpeedScaleMult(1.5);
        }
    }

    public static void decreaseGameSpeed() {
        if (Engine.getGameSpeedScaleMult() == mult_MIN) {

        } else {
            Engine.setGameSpeedScaleMult(1);
        }
    }

    public static void increaseDamageDealt() {
        damageDealtMult *= 2;
        if (damageDealtMult > mult_MAX)
            damageDealtMult = mult_MAX;
    }

    public static void decreaseDamageDealt() {
        damageDealtMult = mult_MIN;
//        damageDealtMult /= 2;
//        if (damageDealtMult < 1d / mult_MAX)
//            damageDealtMult = 1d / mult_MAX;
    }

    public static void increaseDamageTaken() {
        damageTakenMult *= 2;
        if (damageTakenMult > mult_MAX)
            damageTakenMult = mult_MAX;
    }

    public static void decreaseTakenDealt() {
        damageTakenMult /= 2;
        if (damageTakenMult < 1d / mult_MAX)
            damageTakenMult = 1d / mult_MAX;
    }

    public static double getScoreMultiplier() {
        return  getGameSpeedScoreMult() *
                (1d / damageDealtMult) *
                (damageTakenMult) *
                getFirePowerLossMult() *
                enemyFireRateMult *
                enemyBulletSpeedMult *
                getKillstreakMult();
    }

    private static double getKillstreakMult() {
        return  1 + StatePlay.getKillstreak() / 100d;
    }

    private static double getGameSpeedScoreMult() {

        if (Engine.getGameSpeedScaleMult() == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    private static double getFirePowerLossMult() {
        return firePowerLoss ? 1 : 0.5;
    }

    public static void setCurrentButton(int index) {
        currentButton = index;
    }

    public static double getDamageDealtMult() {
        return damageDealtMult;
    }

    public static double getDamageTakenMult() {
        return damageTakenMult;
    }

    public static boolean isFirePowerLoss() {
        return firePowerLoss;
    }

    public static void toggleFirePowerLoss() {
        Difficulty.firePowerLoss = !firePowerLoss;
    }

    public static double getEnemyFireRateMult() {
        return enemyFireRateMult;
    }

    public static void increaseEnemyFireRate() {
        double mult_MAX = 1.5;

        enemyFireRateMult += 0.5;
        if (enemyFireRateMult > mult_MAX)
            enemyFireRateMult = mult_MAX;
    }

    public static void decreaseEnemyFireRate() {
        enemyFireRateMult -= 0.5;
        if (enemyFireRateMult < 1d)
            enemyFireRateMult = 1d;
    }

    public static double getEnemyBulletSpeedMult() {
        return enemyBulletSpeedMult;
    }

    public static void increaseEnemyBulletSpeed() {
        enemyBulletSpeedMult *= 2;
        if (enemyBulletSpeedMult > mult_MAX)
            enemyBulletSpeedMult = mult_MAX;
    }

    public static void decreaseEnemyBulletSpeed() {
        enemyBulletSpeedMult /= 2;
        if (enemyBulletSpeedMult < 1d / mult_MAX)
            enemyBulletSpeedMult = 1d / mult_MAX;
    }
}
