package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;

public class State {

    public void update(GameController gameController) {
        Engine.timer++;
        gameController.updateUI();

//        gameController.updateSounds();

        if (Engine.stateIs(GameState.Quit))
            Engine.stop();

        if (!Engine.stateIs(GameState.Pause) && !Engine.stateIs(GameState.Game) && !Engine.stateIs(GameState.Transition))
//            resetGame();



//            uiHandler.update();

        if (Engine.stateIs(GameState.Menu) || Engine.stateIs(GameState.GameOver)) {
            gameController.resetLevelLoaded();

            if (gameController.reset)
                gameController.resetGame();
        }

        if (Engine.stateIs(GameState.Transition)) {
            /*************
             * Game Code *
             *************/

            /*
             * Spawn if the level can loaded and has not already been spawned
             * */

            if (gameController.islevelLoaded()) {
                if (!gameController.isLevelSpawned())
                    gameController.spawn();
            }
        }

//        background.update();
    }
}
