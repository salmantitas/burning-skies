package com.euhedral.Game.UI;

import com.euhedral.Engine.*;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Game.UI.Menus.*;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.util.ArrayList;

public class UIHandler {

    private Menu currentMenu;
    private ArrayList<Menu> menus;
//    ActionTag action = null;
    boolean menuChanged = false;

    // Common game variables

    Menu nullMenu;

    // User Variables

    public UIHandler() {

        nullMenu = new Menu(GameState.Quit);
        menus = new ArrayList<>();

        MenuMain mainMenu = new MenuMain();
        MenuSettings settingsMenu = new MenuSettings();
        MenuHighScore highScoreMenu = new MenuHighScore();
        MenuHelp helpMenu = new MenuHelp();
        MenuCredits creditsMenu = new MenuCredits();
        MenuTransition transitionMenu = new MenuTransition();
        Menu difficultyMenu = new MenuDifficulty();
        MenuPause pauseMenu = new MenuPause();
        MenuGameOver gameOverMenu = new MenuGameOver();

        Menu testMenu = new Menu(GameState.Test); //stub

        MenuPlay playMenu = new MenuPlay();

        menus.add(mainMenu);
        menus.add(settingsMenu);
        menus.add(highScoreMenu);
        menus.add(helpMenu);
        menus.add(creditsMenu);
        menus.add(transitionMenu);
        menus.add(difficultyMenu);
        menus.add(pauseMenu);
        menus.add(gameOverMenu);
        menus.add(playMenu);

//        currentMenu = testMenu;

        findNewCurrent(Engine.currentState);
    }

    public void update() {
        currentMenu.update();
    }

    /*
    * Renders the current menu
    * */
    public void render(Graphics g) {
        currentMenu.render(g);

        // Debug/Console
//        Utility.drawState(g);
        if (VariableHandler.isConsole())
            Utility.drawCommand(g);
    }

    public void checkHover(int mx, int my) {
        currentMenu.checkHover(mx, my);
    }

    public void checkButtonAction(int mx, int my) {
        currentMenu.checkButtonAction(mx, my);
    }

    public void chooseSelected() {
        currentMenu.chooseSelected();
    }

    public void increaseOption() {
        currentMenu.increaseOption();
    }

    public void decreaseOption() {
        currentMenu.decreaseOption();
    }

    /*
    * Changes the menu to match the current gameState. If no matching menu is found, it's set to an empty menu.
    * */
    private void findNewCurrent(GameState state) {
        menuChanged = false;

        for (Menu menu: menus) {
            if (menu.getState() == state) {

                // resets the notification for
                if (currentMenu instanceof MenuTransition) {
                    // todo: Move to onSwitch
                    VariableHandler.resetSaveDataNotification();
                }

                currentMenu = menu;
                menu.onSwitch();
                menuChanged = true;
                break;
            }
        }

        // I no longer remember the purpose of this, or my own
        if (!menuChanged)
            currentMenu = nullMenu;
    }

    public void keyboardSelection(char c) {
        currentMenu.keyboardSelection(c);
    }

//    public ActionTag getAction() {
//        return action;
//    }
//
//    public void endAction() {
//        action = null;
//    }

    public void updateState(GameState state) {
        findNewCurrent(state);
    }

    public boolean noActiveMessageBoxes() {
        return currentMenu.getActiveMessageBoxes() == 0;
    }

    public void keyPressed(int key) {
        currentMenu.keyPressed(key);
    }

    public void resetActiveMessageBoxes() {
        menus.getLast().resetMessageBoxes();
    }

    /***************************
     * Render Helper Functions *
     ***************************/



}
