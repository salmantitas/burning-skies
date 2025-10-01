package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;

import java.awt.*;

public class Renderer {

    public void render(GameController gameController, Graphics g) {
        gameController.renderBackground(g);

        if (Engine.currentState == GameState.Transition) {
            /*************
             * Game Code *
             *************/
        }

        /*************
         * Game Code *
         *************/

        boolean validCameraRenderState = Engine.currentState == GameState.Game || Engine.currentState == GameState.Pause || Engine.stateIs(GameState.GameOver);

        if (validCameraRenderState) {
            gameController.renderInCamera(g);

            if (VariableHandler.isHud()) {

//                entityHandler.renderBossHealth(g);

            }

        }

        /***************
         * Engine Code *
         ***************/

        gameController.renderUI(g);
//        uiHandler.render(g);
    }
}
