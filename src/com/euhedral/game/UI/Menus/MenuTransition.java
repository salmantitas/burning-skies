package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonAction;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.ActionTag;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuTransition extends Menu {

    int optionSize = buttonSize/2;
    int shopSize = buttonSize/2;

    // Shop

    ButtonAction health = new ButtonAction(x0, y30, shopSize, "Buy Health", ActionTag.health);

    ButtonAction ground = new ButtonAction(x0, y40, shopSize, "Ground Bullets", ActionTag.ground);

    ButtonAction power = new ButtonAction(x0, y50, shopSize, "Upgrade Power", ActionTag.power);

    ButtonAction shield = new ButtonAction(x0, y60, shopSize, "Buy Shield", ActionTag.shield);

    // Navigation

    ButtonAction start = new ButtonAction(x3, y20, buttonSize, "Start", ActionTag.go);

    ButtonNav backToMenu = new ButtonNav(x2, y40, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

    ButtonNav quit = new ButtonNav(x3, yFINAL, buttonSize, "Quit", GameState.Quit);

    // Options

    ButtonAction save = new ButtonAction(xFINAL, y30, optionSize, "Save", ActionTag.save);

    ButtonAction load = new ButtonAction(xFINAL, y40, optionSize, "Load", ActionTag.load);


    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 3;
        options = new Button[MAXBUTTON];

        options[0] = start;
        options[1] = backToMenu;
        options[2] = quit;

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
        boolean minGroundScore = score > VariableHandler.ground.getCost();
        boolean minShieldScore = score > VariableHandler.shield.getCost();
        boolean fullShield = VariableHandler.shield.getValue() >= VariableHandler.shield.getMAX();

        checkConditionAndDisable(health, minHealthScore && !fullHealth);
        checkConditionAndDisable(power, minPowerScore && !maxPower);
        checkConditionAndDisable(ground, minGroundScore && !VariableHandler.gotGround());
        checkConditionAndDisable(shield, minShieldScore && !fullShield);

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
        g.drawString(VariableHandler.saveDataNotification, x2, y60);

        super.postRender(g);
    }

}
