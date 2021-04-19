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

    // Shop

    ButtonAction health = new ButtonAction(x0, y1, buttonSize/2, "Buy Health", ActionTag.health);

    ButtonAction ground = new ButtonAction(x0, y2, buttonSize/2, "Ground Bullets", ActionTag.ground);

    ButtonAction power = new ButtonAction(x0, y3, buttonSize/2, "Upgrade Power", ActionTag.power);

    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 9;
        options = new Button[MAXBUTTON];


        // Options

        ButtonAction save = new ButtonAction(x2, y1, buttonSize, "Save", ActionTag.save);

        ButtonAction load = new ButtonAction(x2, y2, buttonSize, "Load", ActionTag.load);

        ButtonAction tutorial = new ButtonAction(x2, y3, buttonSize, "Tutorial", ActionTag.tutorial);

        // Transition

        ButtonAction go = new ButtonAction(x0, yFINAL, buttonSize, "Go!", ActionTag.go);

        ButtonNav backToMenu = new ButtonNav(x2, yFINAL, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

        ButtonNav quit = new ButtonNav(xFINAL, yFINAL, buttonSize, "Quit", GameState.Quit);

        options[0] = health;
        options[1] = ground;
        options[2] = power;
        options[3] = save;
        options[4] = load;
        options[5] = tutorial;
        options[6] = go;
        options[7] = backToMenu;
        options[8] = quit;

    }

    @Override
    public void update() {
        boolean minHealthScore = VariableManager.getScore() > VariableManager.costHealth;
        boolean fullHealth = VariableManager.getHealth() == VariableManager.getHealthMAX();
        boolean minPowerScore = VariableManager.getScore() > VariableManager.costPower;
        boolean minGroundScore = VariableManager.getScore() > VariableManager.costGround;

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

        VariableManager.renderHUD(g);

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);

        // Shop
        g.drawString("Shop", x0, y0);

        // Level
        g.drawString("Level " + VariableManager.getLevel(), x2, y0);
//        drawHealth(g);
//        drawPower(g);

        super.postRender(g);
    }

    /*
    * Render Functions
    * */

}
