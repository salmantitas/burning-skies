package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonAction;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.ActionTag;
import com.euhedral.game.VariableManager;

import java.awt.*;

public class MenuTransition extends Menu {

    boolean changeControl = false;

    ButtonAction health = new ButtonAction(leftButtonX, topButtonY, buttonSize/2, "Buy Health", ActionTag.health);

    ButtonAction ground = new ButtonAction(midLeftButtonX, topButtonY, buttonSize/2, "Ground Bullets", ActionTag.ground);

    ButtonAction power = new ButtonAction(rightButtonX, topButtonY, buttonSize/2, "Upgrade Power", ActionTag.power);

    ButtonAction control = new ButtonAction(leftButtonX, lowestButtonY + 50, buttonSize/2, "Switch Control Scheme", ActionTag.control);

    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 9;
        options = new Button[MAXBUTTON];

        // Transition / Shop

        ButtonAction go = new ButtonAction(leftButtonX, lowestButtonY, buttonSize, "Go!", ActionTag.go);

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

        ButtonAction save = new ButtonAction(midLeftButtonX + 30,topButtonY + 140, buttonSize, "Save", ActionTag.save);

        ButtonAction load = new ButtonAction(midLeftButtonX + 30,topButtonY + 220, buttonSize, "Load", ActionTag.load);

        ButtonNav quit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Quit);

        options[0] = health;
        options[1] = ground;
        options[2] = power;
        options[3] = go;
        options[4] = control;
        options[5] = save;
        options[6] = load;
        options[7] = backToMenu;
        options[8] = quit;
    }

    @Override
    public void update() {
        boolean minHealthScore = VariableManager.getScore() > VariableManager.costHealth;
        boolean fullHealth = VariableManager.getHealth() == VariableManager.getHealthMAX();
        boolean minPowerScore = VariableManager.getScore() > VariableManager.costPower;
        boolean minGroundScore = VariableManager.getScore() > VariableManager.costGround;

        if (!changeControl)
            control.disable();

        if (minHealthScore && !fullHealth) {
            health.enable();
        } else health.disable();

        // If the score is greater than the cost for ground bullets AND if the player has not already bought them
        // then ground bullets are buyable
        if (minGroundScore && !VariableManager.gotGround()) {
            ground.enable();
        } else ground.disable();

        checkConditionAndDisable(power, minPowerScore);
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
    }

    /*
    *
    * */
}
