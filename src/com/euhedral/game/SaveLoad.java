package com.euhedral.game;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class SaveLoad {
    private static String filename = "save";
    private static String settings = "settings";

    public SaveLoad() {

    }

    public static void saveGame() {
        /*************
         * Game Code *
         *************/

        // Create string variables of variables that will be saved
        String score = Integer.toString(VariableManager.getScore());
        String health = Integer.toString(VariableManager.getHealth());
        String ground = Boolean.toString(VariableManager.gotGround());
        String level = Integer.toString(VariableManager.getLevel());
        String power = Integer.toString(VariableManager.getPower());

        // Turn strings into a string array to be written
        List<String> rows = Arrays.asList(score, health, ground, level, power);

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
        String[] data = new String[5];

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

            // Create variables to store data from save-file
            int score = Integer.parseInt(data[0]);
            int health = Integer.parseInt(data[1]);
            boolean ground = Boolean.parseBoolean(data[2]);
            int level = Integer.parseInt(data[3]);
            int power = Integer.parseInt(data[4]);

            // Update variables
            VariableManager.setScore(score);
            VariableManager.setHealth(health);
            VariableManager.setGround(ground);
            VariableManager.setLevel(level);
            VariableManager.setPower(power);
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
        String tutorial = Boolean.toString(VariableManager.tutorialEnabled());

        List<String> rows = Arrays.asList(tutorial);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter(pathString);

            // Write the setting
            csvWriter.append(tutorial);

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
        String[] data = new String[1];

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

            // Update variables
            if (!VariableManager.tutorialEnabled() == tutorial) {
                VariableManager.toggleTutorial();
            }
        }
    }
}
