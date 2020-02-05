package com.euhedral.game.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.Button;
import com.euhedral.engine.Menu;
import com.euhedral.game.ActionTag;

import java.awt.*;

public class MenuTransition extends Menu {

    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 9;
        options = new Button[MAXBUTTON];

        // Transition / Shop

        ButtonAction health = new ButtonAction(leftButtonX, topButtonY, buttonSize/2, "Buy Health", GameState.Transition, ActionTag.health);

        ButtonAction ground = new ButtonAction(midLeftButtonX, topButtonY, buttonSize/2, "Ground Bullets", GameState.Transition, ActionTag.ground);

        ButtonAction power = new ButtonAction(rightButtonX, topButtonY, buttonSize/2, "Upgrade Power", GameState.Transition, ActionTag.power);

        ButtonAction go = new ButtonAction(leftButtonX, lowestButtonY, buttonSize, "Go!", GameState.Transition, ActionTag.go);

        ButtonAction control = new ButtonAction(leftButtonX, lowestButtonY + 50, buttonSize/2, "Switch Control Scheme", GameState.Transition, ActionTag.control);

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Help, GameState.Menu);

        ButtonAction save = new ButtonAction(midLeftButtonX + 30,topButtonY + 140, buttonSize, "Save", GameState.Transition, ActionTag.save);

        ButtonAction load = new ButtonAction(midLeftButtonX + 30,topButtonY + 220, buttonSize, "Load", GameState.Transition, ActionTag.load);

        ButtonNav quit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Menu, GameState.Quit);

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
    public void render(Graphics g) {
        super.render(g);
    }

//    public void checkHover(int mx, int my) {
//
//    }

//    public void checkButtonAction(int mx, int my) {
//
//    }
//
//    public void chooseSelected() {
//
//
//    }

//    public GameState getState() {
//
//    }

    /*
    *
    * */
}
