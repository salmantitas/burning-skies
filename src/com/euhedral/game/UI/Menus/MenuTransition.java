package com.euhedral.Game.UI.Menus;

import com.euhedral.Engine.*;
import com.euhedral.Engine.UI.*;
import com.euhedral.Engine.UI.Button;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.GameController;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class MenuTransition extends Menu {

    int optionSize = buttonSize/2;
    int shopSize = buttonSize/2;

//    // Shop
//
//    ButtonAction health = new ButtonAction(x5, y48, shopSize, "Buy Health", ActionTag.health);
//
//    ButtonAction ground = new ButtonAction(x5, y56, shopSize, "Ground Bullets", ActionTag.ground);
//
//    ButtonAction power = new ButtonAction(x5, y62, shopSize, "Upgrade Power", ActionTag.power);
//
//    ButtonAction shield = new ButtonAction(x5, y70, shopSize, "Buy Shield", ActionTag.shield);

    // Navigation

    int difficultyY = y48;
    int difficultyX = x10;
    int gameSpeedY = difficultyY + spacingY;
    int startY = gameSpeedY + spacingY;
    int backY = startY  + spacingY;

    ButtonOption difficulty;
    ButtonOption gameSpeed;
    ButtonOption damage;

    Button start;

    ButtonNav back = new ButtonNav(difficultyX, backY, Utility.perc(buttonSize, 80), "Back", GameState.Menu);

//    ButtonNav quit = new ButtonNav(x43, y80, buttonSize, "Quit", GameState.Quit);

    // Options

//    ButtonAction save = new ButtonAction(x75, y48, optionSize, "Save", ActionTag.save);
//
//    ButtonAction load = new ButtonAction(x75, y56, optionSize, "Load", ActionTag.load);


    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 4;
        options = new Button[MAXBUTTON];

        start = new Button(difficultyX, startY, Utility.perc(buttonSize, 80), "Start");
        start.activate = (GameController::setLevelLoadedtoTrue);

        difficulty = new ButtonOption(difficultyX, difficultyY, Utility.perc(buttonSize, 70), "Difficulty");

        difficulty.setIncreaseActivate(() -> {
            Difficulty.increaseDifficulty();
        });

        difficulty.setDecreaseActivate(() -> {
            Difficulty.decreaseDifficulty();
        });

        damage = new ButtonOption(difficultyX, difficultyY + 100, Utility.perc(buttonSize, 70), "Damage");
        damage.setIncreaseActivate(Difficulty::increaseDamage);
        damage.setDecreaseActivate(Difficulty::decreaseDamage);
//        gameSpeed = new ButtonOption(difficultyX, difficultyY + 100, Utility.perc(buttonSize, 70), "Game Speed");
//        gameSpeed.setIncreaseActivate(Difficulty::increaseGameSpeed);
//        gameSpeed.setDecreaseActivate(Difficulty::decreaseGameSpeed);

        options[0] = difficulty;
        options[1] = damage;
        options[2] = start;
        options[3] = back;
//        options[2] = quit;
    }

    @Override
    public void update() {
        int score = VariableHandler.getScore();
        boolean minHealthScore = score > VariableHandler.health.getCost();
        boolean fullHealth = VariableHandler.health.getValue() >= VariableHandler.health.getMAX();
        boolean minPowerScore = score > VariableHandler.firepower.getCost();
        boolean maxPower = VariableHandler.firepower.getValue() >= VariableHandler.firepower.getMAX();
        boolean minShieldScore = score > VariableHandler.shield.getCost();
        boolean fullShield = VariableHandler.shield.getValue() >= VariableHandler.shield.getMAX();

//        checkConditionAndDisable(health, minHealthScore && !fullHealth);
//        checkConditionAndDisable(power, minPowerScore && !maxPower);
//        checkConditionAndDisable(ground, minGroundScore && !VariableHandler.gotGround());
//        checkConditionAndDisable(shield, minShieldScore && !fullShield);

        if (Engine.timer > VariableHandler.notificationSet + 100) {
            VariableHandler.resetSaveDataNotification();
        }
    }

    // Disables button when the conditions are not true
    private void checkConditionAndDisable(Button button, boolean condition) {
        if (!condition) {
            button.disable();
        } else button.enable();
    }

    @Override
    protected void reassignSelected(int reassign) {
        super.reassignSelected(reassign);
        Difficulty.setCurrentButton(getIndex());
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
//        tutorialState(g); // to be deleted

        VariableHandler.renderHUD(g);


        // Render Screen Text

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);


//        // Shop
//        g.drawString("Shop", x0, y0);

        // Level
//        g.drawString("Level " + VariableManager.getLevel(), x2, y0);

//        // Navigation
//        g.drawString("Navigation", xFINAL-10, y0);

        // Notification
        g.setFont(new Font("arial", 1, Utility.percWidth(1)));
        g.drawString(VariableHandler.saveDataNotification, x40, y70);

        Difficulty.render(g);

        super.postRender(g);
    }

    @Override
    public void onSwitch() {
        SoundHandler.playBGMMenu();
        super.onSwitch();
    }

}
