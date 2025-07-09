package com.euhedral.engine;/*
 * Do not modify
 * */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.euhedral.game.GameController;
import com.euhedral.game.KeyInput;

public class  EngineKeyboard extends KeyAdapter {
//    public GameController gameController;
    private KeyInput keyInput;

    private static HashMap<String, Integer> keyMap;

    private int keyPressed;
    private int keyReleased;

    public EngineKeyboard(GameController gameController) {
//        this.gameController = gameController;
        keyInput = new KeyInput(this, gameController);
        keyMap = new HashMap<>();
        initKeyMap();
    }

    private void initKeyMap() {
        keyMap.put("W", KeyEvent.VK_W);
        keyMap.put("A", KeyEvent.VK_A);
        keyMap.put("S", KeyEvent.VK_S);
        keyMap.put("D", KeyEvent.VK_D);
        keyMap.put("SPACE", KeyEvent.VK_SPACE);
    }

    public void keyPressed(KeyEvent e) {
        keyPressed = e.getKeyCode();

        keyInput.updatePressed();

//        System.out.println(keyPressed + " Key Pressed");

    }

    public void keyReleased(KeyEvent e) {
        keyReleased = e.getKeyCode();

        keyInput.updateReleased();

//        System.out.println(keyReleased + " Key Released");
    }

    public int getKeyPressed() {
        return keyPressed;
    }

    public int getKeyReleased() {
        return keyReleased;
    }

    public boolean keyIsPressed(int key) {
        return keyPressed == key;
    }

    public boolean keyIsReleased(int key) {
        return keyReleased == key;
    }

    public static int getKeyEvent(String str) {
        return keyMap.get(str);
    }
}
