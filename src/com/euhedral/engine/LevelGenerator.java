package com.euhedral.engine;

import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
import com.euhedral.game.VariableManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class LevelGenerator {

    public HashMap<Color, EntityID> colorMap;
    int width = 31;

    private GameController gameController;

    public LevelGenerator(GameController gameController) {
        this.gameController = gameController;
        colorMap = VariableManager.colorMap;
    }

    // Generate level from an image
    public void loadImageLevel(BufferedImage image) {
        width = image.getWidth();
        int height = image.getHeight();
        gameController.setLevelHeight(height * 32);

        System.out.printf("Width: %d, Height: %d\n", width, height);

        for (int j = height - 1; j >= 0; j--) {
            for (int i = width - 1; i >= 0; i--) {
                int pixel = image.getRGB(i, j);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                Color c = new Color(r, g, b);

                if (colorMap.containsKey(c)) {

                    int x = i * 32;
                    int y = j * 32;

                    EntityID id = colorMap.get(c);
                    gameController.spawnEntity(x, y, id, c);
                }
            }
        }
    }


}
