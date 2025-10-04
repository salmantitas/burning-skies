package com.euhedral.Game;

import com.euhedral.Engine.Engine;

public class StatePlay extends State {

    public static Timer killTimer;
    private static int killstreak;

    public StatePlay() {
        super();
        killTimer = new Timer(5 * 60);
        killstreak = 0;
    }

    @Override
    public void update(GameController gameController) {
        super.update(gameController);

        /*
         * Disable the level load permission, as the level is already running
         * */

        gameController.resetLevelLoaded();
        boolean gameEndCondition = VariableHandler.health.getValue() <= 0;

        if (gameEndCondition) {

            Engine.gameOverState();

            System.out.println("Game Over");
            VariableHandler.updateHighScore();
        }

        /*************
         * Game Code *
         *************/

        else {

            // Game only runs if either tutorials are disabled, or no message boxes are active
            // Player can move when the tutorial box is up.

            boolean activeMessageBoxes = !gameController.noActiveMessageBoxes();
            boolean tutorialDisabled = !VariableHandler.isTutorial();

            // if tutorial is disabled or there are no active message boxes, just run the game

            if (tutorialDisabled || !activeMessageBoxes) {
                if (activeMessageBoxes) {
//                        System.out.println("Active Message Box");
                }
                runGame(gameController);

                if (System.currentTimeMillis() - 1000 > gameController.timer) {
                    gameController.timer = System.currentTimeMillis();
                    gameController.timeInSeconds++;
//                        System.out.printf("Timer: %d\n", timeInSeconds);
                }
            } else if (activeMessageBoxes) {
                gameController.updatePlayer();
            }
        }

        killTimer.update();
        if (killTimer.getProgress() <= 0) {
            killstreak = 0;
            killTimer.stop();
        }
    }

    private void runGame(GameController gameController) {
        gameController.updateEnemyGenerator();
        gameController.updateEntityHandler();
        gameController.checkLevelStatus();
    }

    public static void increaseKillstreak() {
        killstreak++;
        killTimer.start();
    }

    public static void resetKillstreak() {
        killTimer.stop();
        killstreak = 0;
    }

    public static int getKillstreak() {
        return killstreak;
    }
}
