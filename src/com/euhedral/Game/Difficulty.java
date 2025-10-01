package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;

import java.awt.*;

public class Difficulty {
    private static int enemyTypes;

    // Difficulty

    public static int DIFFICULTY_EASY = 0;
    public static int DIFFICULTY_NORMAL = 1;
    //    public static int DIFFICULTY_CUSTOM = DIFFICULTY_NORMAL + 1;
    public static int DIFFICULTY_CHALLENGE = DIFFICULTY_NORMAL + 1;

    public static int difficultyType = DIFFICULTY_NORMAL;
    public static int difficultyLevel = 1;

    private static String difficultyName = "";
    private static String text1 = "";
    private static String text2 = "";

    private static int size1 = Utility.intAtWidth640(25);
    private static int size2 = Utility.intAtWidth640(10);
    private static Font font1 = VariableHandler.customFont.deriveFont(1, size1);
    private static Font font2 = VariableHandler.customFont.deriveFont(1, size2);

    private static int lineSpace = Utility.intAtWidth640(25);

    private static int difficultyNameX = 500;
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

    public Difficulty(int enemyTypes) {
        this.enemyTypes = enemyTypes;
    }

    public static int getDifficultyLevel() {
        if (difficultyType == DIFFICULTY_CHALLENGE)
            return enemyTypes;
//        todo: Custom Difficulty
//        else if () {
//
//        }
        else
            return (difficultyType + 1);
    }

    public static void increaseDifficulty() {
        Difficulty.difficultyType++;
        if (difficultyType > DIFFICULTY_CHALLENGE)
            difficultyType = 0;
    }

    public static void decreaseDifficulty() {
        difficultyType--;
        if (difficultyType < 0)
            difficultyType = DIFFICULTY_CHALLENGE;
    }

    public static void render(Graphics g) {
        if (currentButton == 0 ) {
            if (difficultyType == 0) {
                difficultyName = "Easy";
                text1 = "Only start with basic enemies.";
                text2 = "Enemies will progressively get more difficult";
            } else if (difficultyType == 1) {
                difficultyName = "Normal";
                text1 = "The intended experience.";
                text2 = "Enemies will progressively get more difficult";
            } else {
                difficultyName = "Master";
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
//        else if (currentButton == 1) {
//            difficultyName = "Game Speed";
//            text1 = "Risk higher speeds for higher score per enemy";
//            text2 = "Game Speed : x" + Engine.getGameSpeedScaleMult() + ", Score Multiplier: X" + Engine.getGameSpeedScaleMult();
//        }

        g.setColor(Color.BLACK);
        g.setFont(font1);
        g.drawString(difficultyName, difficultyNameX, difficultyNameY);

        g.setFont(font2);
        g.drawString(text1, text1X, text1Y);
        g.drawString(text2, text2X, text2Y);
    }

    public static void increaseGameSpeed() {
//        int mult_MIN = 1;
        int mult_MAX = 2;
        Engine.setGameSpeedScaleMult(mult_MAX);
    }

    public static void decreaseGameSpeed() {
        Engine.setGameSpeedScaleMult(mult_MIN);
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
        return  Engine.getGameSpeedScaleMult() * (1d / damageDealtMult) * (damageTakenMult);
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
}
