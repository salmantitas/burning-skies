package com.euhedral.game;

import com.euhedral.engine.EngineKeyboard;

import java.awt.event.KeyAdapter;

public class KeyInput extends KeyAdapter{

    private static EngineKeyboard keyboard;
    private GameController gameController;

    public KeyInput(EngineKeyboard engineKeyboard, GameController gameController) {
        this.gameController = gameController;
        keyboard = engineKeyboard;

        /*************
         * Game Code *
         *************/


}

    public void updatePressed() {

        /***************
         * Engine Code *
         ***************/

        notifyKeyPress(keyboard.getKeyPressed());

        /*************
         * Game Code *
         *************/
    }

    public void updateReleased() {
        /***************
         * Engine Code *
         ***************/

        notifyKeyRelease(keyboard.getKeyReleased());

        /*************
         * Game Code *
         *************/

    }

    private void notifyKeyPress(int key) {
        gameController.keyPressed(key);
    }

    private void notifyKeyRelease(int key) {
        gameController.keyReleased(key);
    }

    public static int getKeyEvent(String str) {
        return keyboard.getKeyEvent(str);
    }

    /******************
     * User functions *
     ******************/
}
