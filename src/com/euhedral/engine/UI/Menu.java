package com.euhedral.engine.UI;

import com.euhedral.engine.GameState;
import com.euhedral.engine.Panel;
import com.euhedral.engine.Utility;
import com.euhedral.game.ActionTag;
import com.euhedral.game.UI.MessageBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Menu {

    protected ActionTag action = null;
    private int index = 0;
    private GameState state;
    protected LinkedList<MenuItem> menuItems = new LinkedList<>();

    protected Button[] options;
    protected int MAXBUTTON;

    protected ArrayList<MessageBox> messageBoxes;

    // Title Variables

    protected int titleX = Utility.percWidth(9);
    protected int titleY = Utility.percHeight(20);
    protected int titleSize = Utility.percWidth(11.5);
    protected Color titleColor = Color.BLACK;

    protected int buttonSize = Utility.percWidth(5);
    protected int leftButtonX = Utility.percWidth(5);
    protected int midLeftButtonX = Utility.percWidth(38);
    protected int midButtonX = Utility.percWidth(45);
    protected int midRightButtonX = Utility.percWidth(50);
    protected int rightButtonX = Utility.percWidth(75);
    protected int topButtonY = Utility.percHeight(30);
    protected int midHeightButtonY = Utility.percHeight(50);
    protected int lowestButtonY = Utility.percHeight(70);

    public Menu(GameState state) {
        this.state = state;
        messageBoxes = new ArrayList<>();
    }

    public void update() {

    }

    public void render(Graphics g) {
        for (MenuItem menuItem: menuItems) {
            menuItem.render(g);
        }

        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            button.render(g);
        }
    }

    /*
    * Selects the button over which the cursor is hovering.
    * Everything else is deselected.
    * */
    public void checkHover(int mx, int my) {
        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            if (button.mouseOverlap(mx, my)) {
                button.select();
            } else button.deselect();
        }
    }

    /*
    * Checks whether the mouse has clicked on a button. If true, the button is activated.
    * */
    public void checkButtonAction(int mx, int my) {
        for (int i = 0; i < messageBoxes.size(); i++) {
            MessageBox messageBox = messageBoxes.get(i);

        }
        for (int i = 0; i < MAXBUTTON; i++) {
            Button button = options[i];
            if (button.mouseOverlap(mx, my)) {
                if (button.isEnabled()) {
                    activateButton(button);
                    break;
                }
//                if (button instanceof ButtonNav) {
//                    ButtonNav navButton = (ButtonNav) button;
//                    Engine.setState(navButton.getTargetSate());
//                    break;
//                } else {
//                    ButtonAction actButton = (ButtonAction) button;
//                    this.action = actButton.getAction();
//                    break;
//                }
            }
        }
    }

    /*
    * If the selected button is a navigation button, the GameState is changed. Otherwise, the ActionTag is applied.
    * */
    public void activateButton(Button button) {
        if (button instanceof ButtonNav) {
            button.activate();
//            ButtonNav navButton = (ButtonNav) button;
//            Engine.setState(navButton.getTargetSate());
        } else {
            ButtonAction actButton = (ButtonAction) button;
            this.action = actButton.getAction();
        }
    }

    /*
    * Activates the button that is selected
    * */
    public void chooseSelected() {
        activateButton(options[index]);
    }

    /*
    * Changes selected button by keypress
    * */
    public void keyboardSelection(char c) {
        if (c == 'r') {
            options[index].deselect();
            index = (index + 1) % MAXBUTTON;
            options[index].select();
        } else {
            options[index].deselect();
            index = (index - 1);
            if (index < 0) index = MAXBUTTON-1;
            options[index].select();
        }
    }

    public GameState getState() {
        return state;
    }

    public ActionTag getAction() {
        return action;
    }

    /****************
     * UI Functions *
     ****************/

    public void addPanel(com.euhedral.engine.Panel panel) {
        menuItems.add(panel);
    }

    public void addPanel(int x, int y, int width, int height, GameState state) {
        menuItems.add(new com.euhedral.engine.Panel(x, y, width, height, state));
    }


    public void addPanel(int x, int y, int width, int height, GameState state, float transparency, Color color) {
        menuItems.add(new Panel(x, y, width, height, state, transparency, color));
    }
}
