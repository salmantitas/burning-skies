package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonAction;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.ActionTag;
import com.euhedral.game.UI.UIHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuTransition extends Menu {

    int optionSize = buttonSize/2;
    int shopSize = buttonSize/2;

    String difficultyName = "";
    String text1 = "";
    String text2 = "";
    int size1 = Utility.intAtWidth640(25);
    int size2 = Utility.intAtWidth640(10);
    Font font1 = UIHandler.customFont.deriveFont(1, size1);
    Font font2 = UIHandler.customFont.deriveFont(1, size2);

    int lineSpace = Utility.intAtWidth640(25);
    int difficultyNameX = 500;
    int difficultyNameY = 300;
    int text1X = 200;
    int text1Y = difficultyNameY + lineSpace;
    int text2X = text1X;
    int text2Y = text1Y + lineSpace;

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
    int startY = difficultyY + spacingY;

    ButtonAction difficulty = new ButtonAction(difficultyX, difficultyY, Utility.perc(buttonSize, 70), "Difficulty", ActionTag.toggleDifficulty);

    ButtonAction start = new ButtonAction(difficultyX, startY, Utility.perc(buttonSize, 80), "Start", ActionTag.go);

    ButtonNav back = new ButtonNav(difficultyX, y70, Utility.perc(buttonSize, 80), "Back", GameState.Menu);

//    ButtonNav quit = new ButtonNav(x43, y80, buttonSize, "Quit", GameState.Quit);

    // Options

    ButtonAction save = new ButtonAction(x75, y48, optionSize, "Save", ActionTag.save);

    ButtonAction load = new ButtonAction(x75, y56, optionSize, "Load", ActionTag.load);


    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 3;
        options = new Button[MAXBUTTON];

        options[0] = difficulty;
        options[1] = start;
        options[2] = back;
//        options[2] = quit;

//        options[4] = health;
//        options[5] = ground;
//        options[6] = power;
//        options[7] = shield;
//
//        options[8] = save;
//        options[9] = load;


    }

    @Override
    public void update() {
        int score = VariableHandler.getScore();
        boolean minHealthScore = score > VariableHandler.health.getCost();
        boolean fullHealth = VariableHandler.health.getValue() >= VariableHandler.health.getMAX();
        boolean minPowerScore = score > VariableHandler.power.getCost();
        boolean maxPower = VariableHandler.power.getValue() >= VariableHandler.power.getMAX();
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

        renderDifficulty(g);

        super.postRender(g);
    }

    private void renderDifficulty(Graphics g) {
        if (VariableHandler.difficultyType == 0) {
            difficultyName = "Easy";
            text1 = "Only start with basic enemies.";
            text2 = "Enemies will progressively get more difficult";
        } else if (VariableHandler.difficultyType == 1) {
            difficultyName = "Normal";
            text1 = "The intended experience.";
            text2 = "Enemies will progressively get more difficult";
        } else {
            difficultyName = "Master";
            text1 = "For the experienced player.";
            text2 = "All enemies available from the start";
        }

        g.setColor(Color.BLACK);
        g.setFont(font1);
        g.drawString(difficultyName, difficultyNameX, difficultyNameY);

//        g.setColor(Color.BLACK);
        g.setFont(font2);
        g.drawString(text1, text1X, text1Y);

//        g.setColor(Color.BLACK);
//        g.setFont(font2);
        g.drawString(text2, text2X, text2Y);
    }

}
