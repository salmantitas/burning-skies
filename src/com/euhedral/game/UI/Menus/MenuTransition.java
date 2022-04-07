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

    int optionSize = buttonSize/2;
    int shopSize = buttonSize/2;

    // Shop

    ButtonAction health = new ButtonAction(x0, y1, shopSize, "Buy Health", ActionTag.health);

    ButtonAction ground = new ButtonAction(x0, y2, shopSize, "Ground Bullets", ActionTag.ground);

    ButtonAction power = new ButtonAction(x0, y3, shopSize, "Upgrade Power", ActionTag.power);

    ButtonAction shield = new ButtonAction(x0, y4, shopSize, "Buy Shield", ActionTag.shield);

    // Options

    ButtonAction save = new ButtonAction(x2, y1, optionSize, "Save", ActionTag.save);

    ButtonAction load = new ButtonAction(x2, y2, optionSize, "Load", ActionTag.load);

    ButtonAction tutorial = new ButtonAction(x2, y3, optionSize, "Tutorial", ActionTag.tutorial);

    // Transition

    ButtonAction go = new ButtonAction(xFINAL, y1, buttonSize, "Go!", ActionTag.go);

    ButtonNav backToMenu = new ButtonNav(xFINAL, y2, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

    ButtonNav quit = new ButtonNav(xFINAL, y3, buttonSize, "Quit", GameState.Quit);

    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 10;
        options = new Button[MAXBUTTON];




        options[0] = health;
        options[1] = ground;
        options[2] = power;
        options[3] = shield;
        options[4] = save;
        options[5] = load;
        options[6] = tutorial;
        options[7] = go;
        options[8] = backToMenu;
        options[9] = quit;

    }

    @Override
    public void update() {
        int score = VariableManager.getScore();
        boolean minHealthScore = score > VariableManager.health.getCost();
        boolean fullHealth = VariableManager.health.getValue() >= VariableManager.health.getMAX();
        boolean minPowerScore = score > VariableManager.power.getCost();
        boolean maxPower = VariableManager.getPower() >= VariableManager.power.getMAX();
        boolean minGroundScore = score > VariableManager.ground.getCost();
        boolean minShieldScore = score > VariableManager.shield.getCost();
        boolean fullShield = VariableManager.shield.getValue() >= VariableManager.shield.getMAX();

        checkConditionAndDisable(health, minHealthScore && !fullHealth);
        checkConditionAndDisable(power, minPowerScore && !maxPower);
        checkConditionAndDisable(ground, minGroundScore && !VariableManager.gotGround());
        checkConditionAndDisable(shield, minShieldScore && !fullShield);

//        if (minHealthScore && !fullHealth) {
//            health.enable();
//        } else health.disable();

        // If the score is greater than the cost for ground bullets AND if the player has not already bought them
        // then ground bullets are buyable
//        if (minGroundScore && !VariableManager.gotGround()) {
//            ground.enable();
//        } else ground.disable();

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
        tutorialState(g); // to be deleted

        VariableManager.renderHUD(g);


        // Render Screen Text

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);


        // Shop
        g.drawString("Shop", x0, y0);

        // Level
        g.drawString("Level " + VariableManager.getLevel(), x2, y0);

        // Navigation
        g.drawString("Navigation", xFINAL-10, y0);

        super.postRender(g);
    }

    /*
    * Render Functions
    * */

    public void tutorialState(Graphics g) {
        g.setFont(new Font("arial", 1, Utility.percWidth(1.5)));
        g.setColor(Color.WHITE);
        g.drawString("Tutorial: " + VariableManager.tutorialEnabled(), Utility.percWidth(85), Utility.percHeight(8));

        Color c = VariableManager.tutorialEnabled() ? Color.GREEN : Color.RED;
        int bx = tutorial.getX() + tutorial.getWidth();
        int by = tutorial.getY();
        int bsize = tutorial.getHeight();

        g.setColor(c);
        g.fillOval(10+bx,by,bsize,bsize);
    }

}
