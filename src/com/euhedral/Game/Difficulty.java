package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;

import java.awt.*;

public class Difficulty {
    private static int enemyTypes;

    // Difficulty

    public static int DIFFICULTY_NORMAL = 1;
    public static int enemyLevel = DIFFICULTY_NORMAL;

    private static String difficultyName = "";
    private static String text1 = "";
    private static String text2 = "";

    private static int size1 = Utility.intAtWidth640(25);
    private static int size2 = Utility.intAtWidth640(10);
    private static Font font1 = VariableHandler.customFont.deriveFont(1, size1);
    private static Font font2 = VariableHandler.customFont.deriveFont(1, size2);

    private static int lineSpace = Utility.intAtWidth640(25);

    private static int difficultyNameX = 400;
    private static int difficultyNameY = 300;
    private static int text1X = 200;
    private static int text1Y = difficultyNameY + lineSpace;
    private static int text2X = text1X;
    private static int text2Y = text1Y + lineSpace;
    private static int currentButton = 0;

    private static int mult_MIN = 1;
    private static int mult_MAX = 2;
    private static double damageDealtMult = 1;
    private static double damageTakenMult = 1;
    private static boolean firePowerLoss = false;

    public Difficulty(int enemyTypes) {
        this.enemyTypes = enemyTypes;
    }

    public static int getDifficultyLevel() {
        return enemyLevel + 1;
//        if (difficultyType == DIFFICULTY_CHALLENGE)
//            return enemyTypes;
////        todo: Custom Difficulty
////        else if () {
////
////        }
//        else
//            return (difficultyType + 1);
    }

    public static void increaseDifficulty() {
        Difficulty.enemyLevel++;
        if (enemyLevel > enemyTypes)
            enemyLevel = 0;
    }

    public static void decreaseDifficulty() {
        enemyLevel--;
        if (enemyLevel < 0)
            enemyLevel = enemyTypes;
    }

    public static void render(Graphics g) {
        difficultyName = "" + (enemyLevel + 1);
        if (currentButton == 0 ) {
            if (enemyLevel == 0) {
                difficultyName += " - Easy";
                text1 = "Only start with basic enemies.";
                text2 = "Enemies will progressively get more difficult";
            } else if (enemyLevel == 1) {
                difficultyName += " - Normal";
                text1 = "The intended experience.";
                text2 = "Enemies will progressively get more difficult";
            } else if (enemyLevel == enemyTypes) {
                difficultyName += " - Master";
                text1 = "For the experienced player.";
                text2 = "All enemies available from the start";
            } else {
                difficultyName += " - Custom";
                text1 = "For a custom experience";
                text2 = "Enemies from level " + (enemyLevel + 1) + " available from the start";
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
            text1 = "Loss firepower when hit";
            text2 = "Firepower Loss : " + (firePowerLoss ? "ON" : "OFF") + ", Score Multiplier: X" + getFirePowerLossMult();
        } else {
            difficultyName = "";
            text1 = "";
            text2 = "";
        }

        g.setColor(Color.BLACK);
        g.setFont(font1);
        g.drawString(difficultyName, difficultyNameX, difficultyNameY);

        g.setFont(font2);
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
                getFirePowerLossMult();
    }

    private static double getGameSpeedScoreMult() {
//        return Engine.getGameSpeedScaleMult();

        if (Engine.getGameSpeedScaleMult() == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    private static double getFirePowerLossMult() {
        return firePowerLoss ? 2 : 1;
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
}
