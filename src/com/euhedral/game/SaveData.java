package com.euhedral.game;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SaveData {
    VariableManager variableManager; // todo: Use static variableManager instead

    public SaveData(VariableManager variableManager) {
        this.variableManager = variableManager;
        save();
    }

    private void save() {
        String score = Integer.toString(variableManager.getScore());
        String health = Integer.toString(variableManager.getHealth());
        String ground = Boolean.toString(variableManager.gotGround());
        String level = Integer.toString(variableManager.getLevel());
        String power = Integer.toString(variableManager.getPower());

        List<String> rows = Arrays.asList(score, health, ground, level, power);

        // Code implemented from https://stackabuse.com/reading-and-writing-csvs-in-java/
        try {
            FileWriter csvWriter = new FileWriter("save.csv");

            for (String data : rows) {
                csvWriter.append(data);
                csvWriter.append(",");
//                csvWriter.append(String.join(data));
//                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
