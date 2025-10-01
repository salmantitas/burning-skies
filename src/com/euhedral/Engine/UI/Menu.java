package com.euhedral.Engine.UI;

import com.euhedral.Engine.GameState;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.UI.MessageBox;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// Displays collection of Interactive and Non-Interactive Menu Items
// Each Menu is specific to a gameState
public class Menu {

    protected int spacingY =  Utility.percHeight(9);

    private int index = 0; // for buttons
    private GameState state; // why is this private?
//    protected GameState previous;
    protected LinkedList<MenuItem> menuItems = new LinkedList<>();

    protected Button[] options;
    protected int MAXBUTTON;

    protected ArrayList<MessageBox> messageBoxes;
    protected int activeMessageBoxes;

    // Title Variables

    protected int titleX = Utility.percWidth(10);
    protected int titleSize = Utility.intAtWidth640(37);
    protected int versionSize = Utility.percWidth(2);
    protected Color titleColor = Color.BLACK;

    protected int buttonSize = Utility.intAtWidth640(24);
    protected int optionSize = Utility.intAtWidth640(13);
    protected int toggleIconSize = optionSize;

    protected int x5 = Utility.percWidth(5);
    protected int x10 = Utility.percWidth(10);
    protected int x20 = Utility.percWidth(20);
    protected int x30 = Utility.percWidth(30);
    protected int x35 = Utility.percWidth(35);
    protected int x36 = Utility.percWidth(36);
    protected int x40 = Utility.percWidth(40);
    protected int x41 = Utility.percWidth(41);
    protected int x43 = Utility.percWidth(43) + 10;
    protected int x52 = Utility.percWidth(52);
    protected int x62 = Utility.percWidth(62);
    protected int x70 = Utility.percWidth(70);
    protected int x75 = Utility.percWidth(75);
    protected int xLast = Utility.percWidth(90);
    protected int y20 = Utility.percHeight(20);
    protected int y22 = Utility.percHeight(22);
    protected int y25 = Utility.percHeight(25);
    protected int y32 = Utility.percHeight(32);
    protected int y40 = Utility.percHeight(40);
    protected int y48 = Utility.percHeight(48);
    protected int y56 = Utility.percHeight(56);
    protected int y62 = Utility.percHeight(62);
    protected int y70 = Utility.percHeight(70);
    protected int y71 = Utility.percHeight(71);
    protected int y80 = Utility.percHeight(80);

    protected int headingY = Utility.percHeight(19);
    protected Font headingFont;

    protected int buttonOffsetX, buttonOffsetY;
    protected Font buttonValueFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(18));
    protected Color buttonValueColor;

    Button button;
//    ActionTag returnAction;
    MessageBox messageBox;

    public Menu(GameState state) {
        this.state = state;
        messageBoxes = new ArrayList<>();
        activeMessageBoxes = 0;

        headingFont = VariableHandler.customFont.deriveFont(1, titleSize);
//        previous = null;
    }

    public void update() {

    }

    public void render(Graphics g) {
        for (MenuItem menuItem: menuItems) {
            menuItem.render(g);
        }

        for (int i = 0; i < MAXBUTTON; i++) {
            button = options[i];
            button.render(g);
        }
    }

    // UIItems here will be rendered on top of everything else
    protected void postRender(Graphics g) {
        if (VariableHandler.isTutorial()) {
            for (MessageBox messageBox : messageBoxes) {
                messageBox.render(g);
            }
        }
    }

    /*
    * Selects the button over which the cursor is hovering.
    * Everything else is deselected.
    * */
    public void checkHover(int mx, int my) {
//        for (int i = 0; i < messageBoxes.size(); i++) {
//            MessageBox messageBox = messageBoxes.get(i);
//            if (messageBox.mouseOverlap(mx, my)) {
//                messageBox.checkHover(mx, my);
//            }
//        }

        if (activeMessageBoxes == 0) {
            for (int i = 0; i < MAXBUTTON; i++) {
                button = options[i];
                if (button.mouseOverlap(mx, my)) {
                    if (!button.isSelected()) {
                        options[index].deselect();
                        button.select();
                        index = i;
                    }
                }
//                else button.deselect();
            }
        }
    }

    /*
     * Changes selected button by keypress
     * */
    public void keyboardSelection(char c) {
        if (activeMessageBoxes == 0) {
            if (c == 'd') {
                int nextButton = (index + 1) % MAXBUTTON;
                while (!options[nextButton].isEnabled()) {
                    Utility.log("" + nextButton);
                    nextButton = (nextButton + 1) % MAXBUTTON;
                }
                reassignSelected(nextButton);
            } else if (c == 'u') {
                int nextButton = (index - 1) % MAXBUTTON;
                if (nextButton < 0) {
                    nextButton += MAXBUTTON;
                }

                while (!options[nextButton].isEnabled()) {
                    Utility.log("" + nextButton);
                    nextButton = (nextButton - 1) % MAXBUTTON;
                }

                reassignSelected(nextButton);
            }
        }
    }

    /*
    * Checks whether the mouse has clicked on a button. If true, the button is activated.
    * */
    public void checkButtonAction(int mx, int my) {
//        returnAction = null;
        for (int i = 0; i < messageBoxes.size(); i++) {
            messageBox = messageBoxes.get(i);
//            if (messageBox.mouseOverlap(mx, my)) {
//                if (messageBox.isEnabled()) {
//                    messageBox.checkButtonAction(mx, my);
//                    if (!messageBox.isEnabled())
//                        activeMessageBoxes--;
//                    // todo: else drag
//                }
//            }
        }
        for (int i = 0; i < MAXBUTTON; i++) {
            if (activeMessageBoxes == 0) {
                button = options[i];
                if (button.mouseOverlap(mx, my)) {
                    if (button.isEnabled()) {
                        activateButton(button);
                        break;
                    }
                }
            }
        }
//        return returnAction;
    }

    public void increaseOption() {
        button = options[index];
        button.activateIncrease();
//        Utility.log("Increase Attempted");
    }

    public void decreaseOption() {
        button = options[index];
        button.activateDecrease();
//        Utility.log("Decrease Attempted");
    }

    /*
    * If the selected button is a navigation button, the GameState is changed. Otherwise, the ActionTag is applied.
    * */
    public void activateButton(Button button) {
        if (activeMessageBoxes == 0) {
//            previous = Engine.currentState;
            if (button.getText() == "Back") {
                Utility.log("Back");
            }
            button.activate();
        }

//        return returnAction;
    }

    /*
    * Activates the button that is selected
    * */
    public void chooseSelected() {
//        returnAction = null;
        if (activeMessageBoxes > 0) {
            messageBoxes.getFirst().disable();
            activeMessageBoxes--;
        } else if (options != null)
            activateButton(options[index]);

//        return returnAction;
    }

    public GameState getState() {
        return state;
    }

    public int getActiveMessageBoxes() {
        return activeMessageBoxes;
    }

    public void keyPressed(int key) {

    }

    public void onSwitch() {
        if (MAXBUTTON > 0) {
            reassignSelected(0);
        }

//        resetMessageBoxes();
    }

    private void reassignSelected(int reassign) {
        options[index].deselect();
        index = reassign;
        options[index].select();
    }

    public void resetMessageBoxes() {
        if (activeMessageBoxes == 0) {
            for (MessageBox m : messageBoxes) {
                m.enable();
                activeMessageBoxes++;
            }
        }
    }

    // todo: Experimental feature
    // Editor Feature
    // Save buttons' location
    protected void exportButtons() {
        List<List<Integer>> rows = new ArrayList<>();

        for (int i =0; i < MAXBUTTON; i++) {
            Button b = options[i];
            List<Integer> data = Arrays.asList(b.x, b.y);
            rows.add(data);
        }

        try {
            String filename = this.getClass().getSimpleName();
            FileWriter csvWriter = new FileWriter(filename + ".csv");

            for (List<Integer> data : rows) {
                for (Integer i : data) {

                    csvWriter.append(Integer.toString(i));
                    csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // todo: Experimental feature
    // Editor Feature
    // Load buttons' location
    protected void importButtons() throws IOException{
        String filename = this.getClass().getSimpleName();
        String pathString = filename + ".csv";

        List<Integer[]> rows = new ArrayList<>();
        Integer[] data = new Integer[2];

        // Code modified and implemented from:
        // https://stackabuse.com/reading-and-writing-csvs-in-java/

        File csvFile = new File(pathString);
        if (csvFile.isFile()) {
            BufferedReader csvReader = new BufferedReader(new FileReader(pathString));
            String row;

            // Assumes there's only one line to parse
            while ((row = csvReader.readLine()) != null) {
                String[] sData = row.split(",");
//                List<Integer> iData = Arrays.asList(Integer.parseInt(sData[0]), Integer.parseInt(sData[1]));
                Integer[] iData = new Integer[2];
                for (int i = 0; i < 2; i++) {
                    iData[i] = Integer.parseInt(sData[i]);
                }
                rows.add(iData);
            }
        }

        for (int i = 0; i < MAXBUTTON; i++) {
            Button b = options[i];
            b.x = rows.get(i)[0];
            b.y = rows.get(i)[1];
        }
    }

    /****************
     * UI Functions *
     ****************/

    public void addPanel(Panel panel) {
        menuItems.add(panel);
    }

    public void addPanel(int x, int y, int width, int height, GameState state) {
        menuItems.add(new Panel(x, y, width, height, state));
    }


    public void addPanel(int x, int y, int width, int height, GameState state, float transparency, Color color) {
        menuItems.add(new Panel(x, y, width, height, state, transparency, color));
    }

    public void addMessageBox(MessageBox messageBox) {
        messageBoxes.add(messageBox);
        activeMessageBoxes++;
    }

    /*****************
     * Render Helper *
     *****************/

    protected void renderValue(Graphics g, Button button, int value, boolean condition) {
        buttonOffsetX = button.getX() + button.getWidth();
        buttonOffsetY = button.getY() + button.getHeight() + Utility.intAtWidth640(2);

        g.setFont(buttonValueFont);

        buttonValueColor = condition ? Color.GREEN : Color.GRAY;
        g.setColor(buttonValueColor);
        g.drawString(Integer.toString(value), 10 + buttonOffsetX, buttonOffsetY);
    }

}
