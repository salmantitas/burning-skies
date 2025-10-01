package com.euhedral.game;

import com.euhedral.engine.Utility;

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

        g.setColor(Color.BLACK);
        g.setFont(font1);
        g.drawString(difficultyName, difficultyNameX, difficultyNameY);

        g.setFont(font2);
        g.drawString(text1, text1X, text1Y);
        g.drawString(text2, text2X, text2Y);
    }
}
