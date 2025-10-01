package com.euhedral.Engine;/*
 * Do not modify
 * */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.euhedral.Game.GameController;

public class KeyInput extends KeyAdapter {
    public GameController gameController;

    private static HashMap<String, Integer> keyMap;

    private int keyPressed;
    private int keyReleased;

    public KeyInput(GameController gameController) {
        this.gameController = gameController;
        keyMap = new HashMap<>();
        initKeyMap();
    }

    private void initKeyMap() {
        keyMap.put("W", KeyEvent.VK_W);
        keyMap.put("A", KeyEvent.VK_A);
        keyMap.put("S", KeyEvent.VK_S);
        keyMap.put("D", KeyEvent.VK_D);
        keyMap.put("SPACE", KeyEvent.VK_SPACE);
        keyMap.put("CTRL", KeyEvent.VK_CONTROL);
    }

    public void keyPressed(KeyEvent e) {
        keyPressed = e.getKeyCode();
        gameController.keyPressed(keyPressed);
    }

    public void keyReleased(KeyEvent e) {
        keyReleased = e.getKeyCode();
        gameController.keyReleased(keyReleased);
    }

    public static int getKeyEvent(String str) {
        return keyMap.get(str);
    }
}
