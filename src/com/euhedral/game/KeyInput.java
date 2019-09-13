package com.euhedral.game;

import com.euhedral.engine.EngineKeyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeyInput extends KeyAdapter{

    private EngineKeyboard keyboard;
    private GameController gameController;
    private ArrayList<Integer> legalKeysPress;
    private ArrayList<Integer> legalKeysRelease;

    public KeyInput(EngineKeyboard engineKeyboard, GameController gameController) {
        this.gameController = gameController;
        keyboard = engineKeyboard;

        legalKeysPress = new ArrayList<>();
        legalKeysRelease = new ArrayList<>();

        /*************
         * Game Code *
         *************/

        legalKeysPress.add(KeyEvent.VK_LEFT);
        legalKeysPress.add(KeyEvent.VK_A);
        legalKeysPress.add(KeyEvent.VK_RIGHT);
        legalKeysPress.add(KeyEvent.VK_D);
        legalKeysPress.add(KeyEvent.VK_UP);
        legalKeysPress.add(KeyEvent.VK_W);
        legalKeysPress.add(KeyEvent.VK_DOWN);
        legalKeysPress.add(KeyEvent.VK_S);
        legalKeysPress.add(KeyEvent.VK_SPACE);
        legalKeysPress.add(KeyEvent.VK_NUMPAD0);
        legalKeysPress.add(KeyEvent.VK_ESCAPE);
        legalKeysPress.add(KeyEvent.VK_CONTROL);
        legalKeysPress.add(KeyEvent.VK_P);

        for (int i: legalKeysPress)
            if (i != KeyEvent.VK_ESCAPE)
                legalKeysRelease.add(i);


}

    public void updatePressed() {

        /***************
         * Engine Code *
         ***************/

        for (int lk: legalKeysPress)
            if (keyIsPressed(lk))
                notifyKeyPress(lk);

        /*************
         * Game Code *
         *************/
    }

    public void updateReleased() {
        /***************
         * Engine Code *
         ***************/

        for (int lk: legalKeysRelease)
            if (keyIsReleased(lk))
                notifyKeyRelease(lk);

        /*************
         * Game Code *
         *************/

    }

    private boolean keyIsPressed(int key) {
        return keyboard.keyIsPressed(key);
    }

    private boolean keyIsReleased(int key) {
        return keyboard.keyIsReleased(key);
    }

    private void notifyKeyPress(int key) {
        gameController.keyPressed(key);
    }

    private void notifyKeyRelease(int key) {
        gameController.keyReleased(key);
    }

    /******************
     * User functions *
     ******************/
}
