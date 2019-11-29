package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.engine.Button;

import java.awt.*;

public class MenuTransition extends Menu{

    public MenuTransition() {
        super(GameState.Transition);
        MAXBUTTON = 6;
        options = new Button[MAXBUTTON];

        // Transition / Shop

        ButtonAction health = new ButtonAction(leftButtonX, topButtonY, buttonSize/2, "Buy Health", GameState.Transition, ActionTag.health);

        ButtonAction ground = new ButtonAction(midLeftButtonX, topButtonY, buttonSize/2, "Ground Bullets", GameState.Transition, ActionTag.ground);

        ButtonAction power = new ButtonAction(rightButtonX, topButtonY, buttonSize/2, "Upgrade Power", GameState.Transition, ActionTag.power);

        ButtonAction go = new ButtonAction(leftButtonX, lowestButtonY, buttonSize, "Go!", GameState.Transition, ActionTag.go);

        ButtonAction control = new ButtonAction(leftButtonX, lowestButtonY + 50, buttonSize/2, "Switch Control Scheme", GameState.Transition, ActionTag.control);

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Help, GameState.Menu);

        options[0] = health;
        options[1] = ground;
        options[2] = power;
        options[3] = go;
        options[4] = control;
        options[5] = backToMenu;
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
