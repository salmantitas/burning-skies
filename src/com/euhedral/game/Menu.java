package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.engine.Button;
import com.euhedral.engine.MenuItem;
import com.euhedral.engine.Panel;

import java.awt.*;
import java.util.LinkedList;

public class Menu {

    protected ActionTag action = null;

    // Title Variables

    int titleX = Engine.percWidth(9);
    int titleY = Engine.percHeight(20);
    int titleSize = Engine.percWidth(11.5);
    Color titleColor = Color.BLACK;

    private GameState state;
    protected LinkedList<MenuItem> menuItems = new LinkedList<>();
//    private LinkedList<ButtonNav> navButtons = new LinkedList<>();
//    private LinkedList<ButtonAction> actButtons = new LinkedList<>();

    protected int MAXBUTTON;
    protected Button[] options;
    private int currentSelection;

    int buttonSize = Engine.percWidth(5);
    int leftButtonX = Engine.percWidth(5);
    int midLeftButtonX = Engine.percWidth(38);
    int midButtonX = Engine.percWidth(45);
    int midRightButtonX = Engine.percWidth(50);
    int rightButtonX = Engine.percWidth(80);
    int topButtonY = Engine.percHeight(30);
    int midHeightButtonY = Engine.percHeight(50);
    int lowestButtonY = Engine.percHeight(70);

    public Menu(GameState state) {
        this.state = state;
    }

    public void render(Graphics g) {
        for (MenuItem menuItem: menuItems) {
            menuItem.render(g);
        }

        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            button.render(g);
        }

//        for (ButtonAction actButton : actButtons) {
//            if (actButton.stateIs(Engine.currentState)) {
//                if (!ground && actButton.getAction() == ActionTag.ground) {
//                    actButton.render(g);
//                } else
//                    actButton.render(g);
//            }
//        }

    }

    public void checkHover(int mx, int my) {
        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            if (button.mouseOverlap(mx, my)) {
                button.select();
            } else button.deselect();
        }

//        for (ButtonNav button: navButtons) {
//            if (button.stateIs(Engine.currentState)) {
//                if (button.mouseOverlap(mx, my)) {
//                    button.select();
//                } else button.deselect();
//            }
//        }
//
//        for (ButtonAction button: actButtons) {
//            if (button.stateIs(Engine.currentState)) {
//                if (button.mouseOverlap(mx, my)) {
//                    button.select();
//                } else button.deselect();
//            }
//        }
    }

    public void checkButtonAction(int mx, int my) {
        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            if (button.mouseOverlap(mx, my)) {
                if (button instanceof ButtonNav) {
                    ButtonNav navButton = (ButtonNav) button;
                    Engine.setState(navButton.getTargetSate());
                    break;
                } else {
                    ButtonAction actButton = (ButtonAction) button;
                    this.action = actButton.getAction();
                    break;
                }
            }
        }
    }

    public void chooseSelected() {
        Button selected = null;

//        for (ButtonNav buttonNav : navButtons) {
//            if (buttonNav.stateIs(Engine.currentState))
//                if (buttonNav.isSelected()) {
//                    Engine.setState(buttonNav.getTargetSate());
//                    break;
//                }
//        }
//
//        if (selected == null) {
//
//            for (ButtonAction actButton : actButtons) {
//                if (actButton.stateIs(Engine.currentState)) {
//                    if (actButton.isSelected()) {
//                        this.action = actButton.getAction();
//                    }
//                }
//            }
//        }
    }

    public GameState getState() {
        return state;
    }

    public ActionTag getAction() {
        return action;
    }

    /*
    *
    * */

    /****************
     * UI Functions *
     ****************/

//    public void addButton(ButtonNav buttonNav) {
//        navButtons.add(buttonNav);
//    }
//
//    public void addButton(ButtonAction actButton) {
//        actButtons.add(actButton);
//    }
//
//    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState) {
//        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState));
//    }
//
//    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState, Color borderColor, Color textColor) {
//        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState, borderColor, textColor));
//    }
//
//    public void addButton(int x, int y, int size, String text, GameState renderState, GameState targetState, Color borderColor, Color textColor, Font font) {
//        navButtons.add(new ButtonNav(x, y, size, text, renderState, targetState, borderColor, textColor, font));
//    }

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
