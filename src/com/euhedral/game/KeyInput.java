package com.euhedral.game;

import com.euhedral.engine.EngineKeyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class KeyInput extends KeyAdapter{

    private EngineKeyboard keyboard;
    private GameController gameController;
    private ArrayList<Integer> legalKeysPress;
    private ArrayList<Integer> legalKeysRelease;

    public KeyInput(EngineKeyboard engineKeyboard, GameController gameController) {
        this.gameController = gameController;
        keyboard = engineKeyboard;

        legalKeysRelease = new ArrayList<>();

        /*************
         * Game Code *
         *************/

        legalKeysPress = new ArrayList<> (Arrays.asList(
                KeyEvent.VK_LEFT,
                KeyEvent.VK_A,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_D,
                KeyEvent.VK_UP,
                KeyEvent.VK_W,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_S,
                KeyEvent.VK_SPACE,
                KeyEvent.VK_NUMPAD0,
                KeyEvent.VK_ESCAPE,
                KeyEvent.VK_CONTROL,
                KeyEvent.VK_P,
                192
        ));

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
