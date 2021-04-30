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
        String score = Integer.toString(VariableManager.getScore());
        String health = Integer.toString(VariableManager.getHealth());
        String ground = Boolean.toString(VariableManager.gotGround());
        String level = Integer.toString(VariableManager.getLevel());
        String power = Integer.toString(VariableManager.getPower());

        List<String> rows = Arrays.asList(score, health, ground, level, power);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter(filename + ".csv");

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
        String[] data = new String[5];
        String pathString = filename + ".csv";

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

            int score = Integer.parseInt(data[0]);
            int health = Integer.parseInt(data[1]);
            boolean ground = Boolean.parseBoolean(data[2]);
            int level = Integer.parseInt(data[3]);
            int power = Integer.parseInt(data[4]);

            VariableManager.setScore(score);
            VariableManager.setHealth(health);
            VariableManager.setGround(ground);
            VariableManager.setLevel(level);
            VariableManager.setPower(power);
        }
    }

    public static void saveSettings() {
        String tutorial = Boolean.toString(VariableManager.tutorialEnabled());

        List<String> rows = Arrays.asList(tutorial);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter(settings + ".csv");

            csvWriter.append(tutorial);
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings () throws IOException {
        String[] data = new String[1];
        String pathString = settings + ".csv";

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

            boolean tutorial = Boolean.parseBoolean(data[0]);

            if (!VariableManager.tutorialEnabled() == tutorial) {
                VariableManager.toggleTutorial();
            }
        }
    }
}
