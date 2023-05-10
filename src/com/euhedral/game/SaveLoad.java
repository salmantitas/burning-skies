package com.euhedral.game;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SaveLoad {

    private enum index {
        score, health, ground, level, power, shield;
    }

    private static String filename = "save";
    private static String settings = "settings";
    private static String score = "score";

    public SaveLoad() {

    }

    public static void saveGame() {
        /*************
         * Game Code *
         *************/

        // Create string variables of variables that will be saved
        String score = Integer.toString(VariableHandler.getScore());
        String health = Integer.toString(VariableHandler.health.getValue());
        String ground = Boolean.toString(VariableHandler.gotGround());
        String level = Integer.toString(VariableHandler.getLevel());
        String power = Integer.toString(VariableHandler.power.getValue());
        String shield = Integer.toString(VariableHandler.shield.getValue());

        // Turn strings into a string array to be written
        List<String> rows = Arrays.asList(score, health, ground, level, power, shield);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            String pathString = filename + ".csv";
            FileWriter csvWriter = new FileWriter(pathString);

            for (String data : rows) {
                csvWriter.append(data);
                csvWriter.append(",");
            }

            csvWriter.flush();
            csvWriter.close();

            VariableHandler.saveDataNotification = VariableHandler.saveText;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame() throws IOException {
        /***************
         * Engine Code *
         ***************/
        String pathString = filename + ".csv";

        /*************
         * Game Code *
         *************/

        // Create array of string to store variables from save-file
        String[] data = new String[6];

        /***************
         * Engine Code *
         ***************/

        // Code modified and implemented from:
        // https://stackabuse.com/reading-and-writing-csvs-in-java/

        File csvFile = new File(pathString);
        if (csvFile.isFile()) {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathString));
            String row;

            // Assumes there's only one line to parse
            while ((row = csvReader.readLine()) != null) {
                data = row.split(",");

            }

            /*************
             * Game Code *
             *************/

            ArrayList<String> loadData = new ArrayList<>(6);
            for (String s: data) {
                loadData.add(s);
            }

            // Loads the array with 0 in the event that a new variable has been added, to prevent null values
            while (loadData.size() < 6) {
                loadData.add("0");
            }

            // Create variables to store data from save-file
            int score = Integer.parseInt(loadData.get(0));
            int health = Integer.parseInt(loadData.get(1));
            boolean ground = Boolean.parseBoolean(loadData.get(2));
            int level = Integer.parseInt(loadData.get(3));
            int power = Integer.parseInt(loadData.get(4));
            int shield = Integer.parseInt(loadData.get(5));

            // Update variables
            VariableHandler.setScore(score);
            VariableHandler.health.set(health);
            VariableHandler.setGround(ground);
            VariableHandler.setLevel(level);
            VariableHandler.power.set(power);
            VariableHandler.shield.setValue(shield);

            VariableHandler.saveDataNotification = VariableHandler.loadText;
        }
    }

    // Save settings
    public static void saveSettings() {
        /***************
         * Engine Code *
         ***************/
        String pathString = settings + ".csv";

        /*************
         * Game Code *
         *************/

        // Create string for settings that will be saved
        String tutorial = Boolean.toString(VariableHandler.isTutorial());
        String volume = Boolean.toString(SoundHandler.isVolume());

        List<String> rows = Arrays.asList(tutorial, volume);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter(pathString);

            // Write the setting
            for (String data: rows) {
                csvWriter.append(data);
                csvWriter.append(",");
            }

            /***************
             * Engine Code *
             ***************/
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings () throws IOException {
        /***************
         * Engine Code *
         ***************/
        String pathString = settings + ".csv";

        /*************
         * Game Code *
         *************/
        // Create array to store settings
        String[] data = new String[2];

        /***************
         * Engine Code *
         ***************/

        // Code modified and implemented from:
        // https://stackabuse.com/reading-and-writing-csvs-in-java/

        File csvFile = new File(pathString);
        if (csvFile.isFile()) {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathString));
            String row;

            // Assumes there's only one word to parse
            while ((row = csvReader.readLine()) != null) {
                data = row.split(",");
            }

            /*************
             * Game Code *
             *************/

            // Create variables from parsed data
            boolean tutorial = Boolean.parseBoolean(data[0]);
            boolean volume  = Boolean.parseBoolean(data[1]);

            // Update variables
            if (!VariableHandler.isTutorial() == tutorial) {
                VariableHandler.toggleTutorial();
            }
            if (!SoundHandler.isVolume() == volume) {
                SoundHandler.toggleVolume();
            }
        }
    }

    // Save settings
    public static void saveHighScore() {
        /***************
         * Engine Code *
         ***************/
        String pathString = score + ".csv";

        /*************
         * Game Code *
         *************/

        LinkedList<String> rows = VariableHandler.getHighScoreList();

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter(pathString);

            // Write the setting
            for (String data: rows) {
                csvWriter.append(data);
                csvWriter.append(",");
            }

            /***************
             * Engine Code *
             ***************/
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Integer> loadHighScore () throws IOException {
        /***************
         * Engine Code *
         ***************/
        String pathString = score + ".csv";

        /*************
         * Game Code *
         *************/
        // Create array to store settings
        String[] data = new String[VariableHandler.HIGH_SCORE_NUMBERS_MAX];

        /***************
         * Engine Code *
         ***************/

        // Code modified and implemented from:
        // https://stackabuse.com/reading-and-writing-csvs-in-java/

        LinkedList<Integer> scores = new LinkedList<>();

        File csvFile = new File(pathString);
        if (!csvFile.exists()) {
            for (int i = 0; i < VariableHandler.HIGH_SCORE_NUMBERS_MAX; i++) {
                scores.add(0);
            }
        } else if (csvFile.isFile()) {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathString));
            String row;

            // Assumes there's only one word to parse
            while ((row = csvReader.readLine()) != null) {
                data = row.split(",");
            }


            for (int i = 0; i < data.length; i++) {
                scores.add(Integer.parseInt(data[i]));
            }

            /*************
             * Game Code *
             *************/
        }
        return scores;
    }
}
