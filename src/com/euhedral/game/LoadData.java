package com.euhedral.game;

import java.io.*;

public class LoadData {
    VariableManager variableManager; // todo: Use static variableManager instead

    public LoadData(VariableManager variableManager) throws IOException{
        this.variableManager = variableManager;
        load();
    }

    private void load() throws IOException{
        String pathString = "save.csv";

        String[] data = new String[5];

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

            variableManager.setScore(score);
            variableManager.setHealth(health);
            variableManager.setGround(ground);
            variableManager.setLevel(level);
            variableManager.setPower(power);
        }
    }
}
