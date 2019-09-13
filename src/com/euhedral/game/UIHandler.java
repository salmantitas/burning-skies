package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.engine.Button;
import com.euhedral.engine.MenuItem;
import com.euhedral.engine.Panel;

import java.awt.*;
import java.util.LinkedList;

public class UIHandler {
    private LinkedList<MenuItem> menuItems = new LinkedList<>();
    private LinkedList<ButtonNav> navButtons = new LinkedList<>();
    private LinkedList<ButtonAction> actButtons = new LinkedList<>();
    ActionTag action = null;

    // Common game variables

    private int selectedButton = -1;

    // Title Variables

    int titleX = Engine.percWidth(9);
    int titleY = Engine.percHeight(20);
    int titleSize = Engine.percWidth(11.5);
    Color titleColor = Color.BLACK;

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

    // User Variables

    public boolean ground = false;

    public UIHandler() {

        // Game Backdrop

        // Main Menu
        Panel mainMenu = new Panel(0, 0, Engine.percWidth(40), Engine.HEIGHT, GameState.Menu);
        addPanel(mainMenu);

        ButtonNav mainMenuPlay = new ButtonNav(leftButtonX, lowestButtonY, buttonSize, "Play", GameState.Menu, GameState.Transition);

        mainMenuPlay.addOtherState(GameState.GameOver);
        mainMenuPlay.setFill();
        addButton(mainMenuPlay);

        ButtonNav help = new ButtonNav(midButtonX, lowestButtonY, buttonSize, "Help", GameState.Menu, GameState.Help);
        addButton(help);

        ButtonNav mainMenuQuit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Menu, GameState.Quit);
        mainMenuQuit.setFill();
        mainMenuQuit.addOtherState(GameState.Transition);
        mainMenuQuit.addOtherState(GameState.Pause);
        mainMenuQuit.addOtherState(GameState.GameOver);
        addButton(mainMenuQuit);

        // In-Game

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Pause, GameState.Menu);
        backToMenu.addOtherState(GameState.Help);
        backToMenu.addOtherState(GameState.Transition);
        backToMenu.addOtherState(GameState.GameOver);
        addButton(backToMenu);

        // Transition / Shop

        ButtonAction health = new ButtonAction(leftButtonX, topButtonY, buttonSize/2, "Buy Health", GameState.Transition, ActionTag.health);
        addButton(health);

        ButtonAction ground = new ButtonAction(midLeftButtonX, topButtonY, buttonSize/2, "Ground Bullets", GameState.Transition, ActionTag.ground);
        addButton(ground);

        ButtonAction power = new ButtonAction(rightButtonX, topButtonY, buttonSize/2, "Upgrade Power", GameState.Transition, ActionTag.power);
        addButton(power);

        ButtonAction go = new ButtonAction(leftButtonX, lowestButtonY, buttonSize, "Go!", GameState.Transition, ActionTag.go);
        addButton(go);

//        ButtonAction control = new ButtonAction(leftButtonX, lowestButtonY, buttonSize/2, "Switch Control Scheme", GameState.Pause, ActionTag.control);
//        addButton(control);

        // Game Over

        Panel gameOverPanel = new Panel(0, Engine.percHeight(60), Engine.WIDTH, Engine.HEIGHT, GameState.Highscore);
        gameOverPanel.addOtherState(GameState.GameOver);
        addPanel(gameOverPanel);
    }

//    public void update() {
//
//    }

    public void render(Graphics g) {
        if (Engine.currentState == GameState.Help) {
            drawHelpText(g);
        }

        if (Engine.currentState == GameState.Pause) {
            drawPause(g);
        }

        if (Engine.currentState == GameState.GameOver) {
            drawGameOverScreen(g);
        }

        for (MenuItem menuItem: menuItems) {
            if (menuItem.stateIs(Engine.currentState))
                menuItem.render(g);
        }

        for (ButtonNav buttonNav : navButtons) {
            if (buttonNav.stateIs(Engine.currentState))
                buttonNav.render(g);
        }

        for (ButtonAction actButton : actButtons) {
            if (actButton.stateIs(Engine.currentState)) {
                if (!ground && actButton.getAction() == ActionTag.ground) {
                    actButton.render(g);
                } else
                    actButton.render(g);
            }
        }

        if (Engine.currentState == GameState.Menu) {
            drawTitle(g);
        }
    }

    public void checkHover(int mx, int my) {
        for (ButtonNav button: navButtons) {
            if (button.stateIs(Engine.currentState)) {
                if (button.mouseOverlap(mx, my)) {
                    button.select();
                } else button.deselect();
            }
        }

        for (ButtonAction button: actButtons) {
            if (button.stateIs(Engine.currentState)) {
                if (button.mouseOverlap(mx, my)) {
                    button.select();
                } else button.deselect();
            }
        }
    }

    public void checkButtonAction(int mx, int my) {
        for (ButtonNav buttonNav : navButtons) {
            if (buttonNav.stateIs(Engine.currentState))
                if (buttonNav.mouseOverlap(mx, my)) {
                    Engine.setState(buttonNav.getTargetSate());
                    break;
                }
        }

        for (ButtonAction actButton : actButtons) {
            if (actButton.stateIs(Engine.currentState)) {
                if (actButton.mouseOverlap(mx, my)) {
                    this.action = actButton.getAction();
                    break;
                }
            }
        }
    }

    public void chooseSelected() {
        Button selected = null;

        for (ButtonNav buttonNav : navButtons) {
            if (buttonNav.stateIs(Engine.currentState))
                if (buttonNav.isSelected()) {
                    Engine.setState(buttonNav.getTargetSate());
                    break;
                }
        }

        if (selected == null) {

            for (ButtonAction actButton : actButtons) {
                if (actButton.stateIs(Engine.currentState)) {
                    if (actButton.isSelected()) {
                        this.action = actButton.getAction();
                    }
                }
            }
        }
    }

    public void keyboardSelection() {
        int totalButtons = navButtons.size() + actButtons.size();
        Button button;

        if (selectedButton < 0) {
            selectedButton = 0;
        } else {
            if (selectedButton < navButtons.size()) {
                button = navButtons.get(selectedButton);
                button.deselect();
            } else if (selectedButton < totalButtons) {
                button = actButtons.get(navButtons.size() - selectedButton);
                button.deselect();
            }
            selectedButton++;
        }
        if (selectedButton < navButtons.size()) {
            button = navButtons.get(selectedButton);
            if (button.getRenderState() == Engine.currentState) {
                button.select();
            }
        } else if (selectedButton < totalButtons) {
            button = actButtons.get(selectedButton);
            if (button.getRenderState() == Engine.currentState) {
                button = actButtons.get(navButtons.size() - selectedButton);
                button.select();
            }
        } else {
            selectedButton = 0;
        }
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

    private void drawTitle(Graphics g) {
        Font font = new Font("arial", 1, titleSize);
        g.setFont(font);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, titleY);
    }

    public void drawPause(Graphics g) {
        g.setFont(new Font("arial", 1, Engine.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", Engine.percWidth(16), Engine.percHeight(25));
    }

    public void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("arial", 1, 200));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Engine.percWidth(2), Engine.HEIGHT/2);
    }

    private void drawHelpText(Graphics g) {
        g.setFont(new Font("arial", 1, 30));
        g.setColor(Color.WHITE);
        String[] help = new String[5];
        int lineHeightInPixel = 80;
        help[0] = "W-A-S-D for movementTimer";
        help[1] = "SPACEBAR to shoot";
        help[2] = "CTRL to switch between air and ground weapons";
        help[3] = "ESC or P in-game to pause or resume";
        help[4] = "ESC from menu to quit";
        int helpX = 200;
        for (int i = 0; i < help.length; i++) {
            g.drawString(help[i], helpX, (i+1)*lineHeightInPixel);
        }
    }

    /****************
     * UI Functions *
     ****************/

    public void addButton(ButtonNav buttonNav) {
        navButtons.add(buttonNav);
    }

    public void addButton(ButtonAction actButton) {
        actButtons.add(actButton);
    }

    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState) {
        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState));
    }

    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState, Color borderColor, Color textColor) {
        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState, borderColor, textColor));
    }

    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState, Color borderColor, Color textColor, Font font) {
        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState, borderColor, textColor, font));
    }

    public void addPanel(Panel panel) {
        menuItems.add(panel);
    }

    public void addPanel(int x, int y, int width, int height, GameState state) {
        menuItems.add(new Panel(x, y, width, height, state));
    }


    public void addPanel(int x, int y, int width, int height, GameState state, float transparency, Color color) {
        menuItems.add(new Panel(x, y, width, height, state, transparency, color));
    }

}
