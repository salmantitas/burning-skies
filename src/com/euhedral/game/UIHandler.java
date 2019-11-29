package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.engine.Button;
import com.euhedral.engine.MenuItem;
import com.euhedral.engine.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class UIHandler {
    /*todo: UIHandler will only have a list of menus which will independently handle their own
       UIItems depending on whichever menu is active at the given time */

    private Menu currentMenu;
    private ArrayList<Menu> menus;
//    private int index = 0;
//    private Menu mainMenu;

//    private LinkedList<MenuItem> menuItems = new LinkedList<>();
//    private LinkedList<ButtonNav> navButtons = new LinkedList<>();
//    private LinkedList<ButtonAction> actButtons = new LinkedList<>();
    ActionTag action = null;

    // Common game variables

    private int selectedButton = -1;



    // Button Variables

    int buttonSize = Engine.percWidth(5);
    int leftButtonX = Engine.percWidth(5);
    int midLeftButtonX = Engine.percWidth(38);
    int midButtonX = Engine.percWidth(45);
    int midRightButtonX = Engine.percWidth(50);
    int rightButtonX = Engine.percWidth(80);
    int topButtonY = Engine.percHeight(30);
    int midHeightButtonY = Engine.percHeight(50);
    int lowestButtonY = Engine.percHeight(70);

    Menu nullMenu = new Menu(GameState.Quit);

    // User Variables

    public boolean ground = false;

    public UIHandler() {

        menus = new ArrayList<>();

        MenuMain mainMenu = new MenuMain();
        MenuHelp helpMenu = new MenuHelp();
        MenuTransition transitionMenu = new MenuTransition();
        MenuPause pauseMenu = new MenuPause();
        MenuGameOver gameOverMenu = new MenuGameOver();

        menus.add(mainMenu);
        menus.add(helpMenu);
        menus.add(transitionMenu);
        menus.add(pauseMenu);
        menus.add(gameOverMenu);

        currentMenu = mainMenu;

//        ButtonNav mainMenuPlay = new ButtonNav(leftButtonX, lowestButtonY, buttonSize, "Play", GameState.Menu, GameState.Transition);
//
//        mainMenuPlay.addOtherState(GameState.GameOver);
//        mainMenuPlay.setFill();
//        addButton(mainMenuPlay);
//
//        ButtonNav help = new ButtonNav(midButtonX, lowestButtonY, buttonSize, "Help", GameState.Menu, GameState.Help);
//        addButton(help);
//
//        ButtonNav mainMenuQuit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Menu, GameState.Quit);
//        mainMenuQuit.setFill();
//        mainMenuQuit.addOtherState(GameState.Transition);
//        mainMenuQuit.addOtherState(GameState.Pause);
//        mainMenuQuit.addOtherState(GameState.GameOver);
//        addButton(mainMenuQuit);

        // In-Game

//        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Pause, GameState.Menu);
//        backToMenu.addOtherState(GameState.Help);
//        backToMenu.addOtherState(GameState.Transition);
//        backToMenu.addOtherState(GameState.GameOver);
//        addButton(backToMenu);
//

        // Game Over

//        Panel gameOverPanel = new Panel(0, Engine.percHeight(60), Engine.WIDTH, Engine.HEIGHT, GameState.Highscore);
//        gameOverPanel.addOtherState(GameState.GameOver);
//        addPanel(gameOverPanel);
    }

//    public void update() {
//
//    }

    public void render(Graphics g) {
        if (currentMenu.getState() != Engine.currentState) {
            findNewCurrent();
        }

        // todo: Engine state change function - notify UIHandler

        currentMenu.render(g);

        drawState(g);
    }

    public void checkHover(int mx, int my) {
        currentMenu.checkHover(mx, my);
    }

    public void checkButtonAction(int mx, int my) {
        currentMenu.checkButtonAction(mx, my);
        action = currentMenu.getAction();
//        checkStateChange();
    }

    public void chooseSelected() {
        currentMenu.chooseSelected();
    }

    private void checkStateChange() {
        if (currentMenu.getState() != Engine.currentState) {
            findNewCurrent();
        }
    }

    private void findNewCurrent() {
        currentMenu = nullMenu;

//        boolean menuChanged = false;


        for (Menu menu: menus) {
            if (menu.getState() == Engine.currentState) {
                currentMenu = menu;
//                menuChanged = true;
                break;
            }
        }

        // sanity check
//        if (!menuChanged) {
//            currentMenu = nullMenu;
//        }
    }

    public void keyboardSelection() {
//        int totalButtons = navButtons.size() + actButtons.size();
//        Button button;
//
//        if (selectedButton < 0) {
//            selectedButton = 0;
//        } else {
//            if (selectedButton < navButtons.size()) {
//                button = navButtons.get(selectedButton);
//                button.deselect();
//            } else if (selectedButton < totalButtons) {
//                button = actButtons.get(navButtons.size() - selectedButton);
//                button.deselect();
//            }
//            selectedButton++;
//        }
//        if (selectedButton < navButtons.size()) {
//            button = navButtons.get(selectedButton);
//            if (button.getRenderState() == Engine.currentState) {
//                button.select();
//            }
//        } else if (selectedButton < totalButtons) {
//            button = actButtons.get(selectedButton);
//            if (button.getRenderState() == Engine.currentState) {
//                button = actButtons.get(navButtons.size() - selectedButton);
//                button.select();
//            }
//        } else {
//            selectedButton = 0;
//        }
    }

    public ActionTag getAction() {
        return action;
    }

    public void endAction() {
        action = null;
    }

    /***************************
     * Render Helper Functions *
     ***************************/



    public void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("arial", 1, 150));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Engine.percWidth(2), Engine.HEIGHT/2);
    }

    /*******************
     * Debug Functions *
     *******************/

    public void drawState(Graphics g) {
        g.setFont(new Font("arial", 1, Engine.percWidth(1.5)));
        g.setColor(Color.WHITE);
        g.drawString("State: " + Engine.currentState, Engine.percWidth(85), Engine.percHeight(8));
    }

}
