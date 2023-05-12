package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonAction;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.Utility;
import com.euhedral.game.ActionTag;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuSettings extends Menu {

    int optionSize = buttonSize/2;
    int toggleIconSize = optionSize;

    // Options

    ButtonAction tutorial = new ButtonAction(x40, y56, optionSize, "Tutorial", ActionTag.tutorial);
    ButtonAction volumeDown = new ButtonAction(x36, y62, optionSize, optionSize, "-", ActionTag.volumeDown);
    ButtonAction volume = new ButtonAction(x40, y62, optionSize, "Volume", ActionTag.volume);
    ButtonAction volumeUp = new ButtonAction(x52, y62, optionSize, optionSize,"+", ActionTag.volumeUp);
    ButtonNav backToMenu = new ButtonNav(x40, y80, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

    public MenuSettings() {
        super(GameState.Settings);
        MAXBUTTON = 5;
        options = new Button[MAXBUTTON];

        options[0] = tutorial;
        options[1] = volumeDown;
        options[2] = volumeUp;
        options[3] = volume;
        options[4] = backToMenu;
    }

//    @Override
//    public void update() {
//
//    }

    // Disables button when the conditions are not true
    private void checkConditionAndDisable(Button button, boolean condition) {
        if (!condition) {
            button.disable();
        } else button.enable();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);

        // Render Toggle Icons

        renderIcon(g, tutorial, VariableHandler.isTutorial());
        renderValue(g, volumeUp, SoundHandler.getVolume());

        super.postRender(g);
    }

    /*******************
    * Render Functions *
    ********************/

    private void renderIcon(Graphics g, Button button, boolean bool) {
        int bx = button.getX() + button.getWidth();
        int by = button.getY() + ((button.getHeight() - toggleIconSize)/2);

        Color c = bool ? Color.GREEN : Color.RED;
        g.setColor(c);
        g.fillOval(10 + bx, by ,toggleIconSize,toggleIconSize);
    }

    private void renderValue(Graphics g, Button button, int value) {
        int bx = button.getX() + button.getWidth();
        int by = button.getY() + button.getHeight();

        g.setFont(new Font("arial", 1, 50));

        Color c = SoundHandler.isVolume() ? Color.GREEN : Color.GRAY;
        g.setColor(c);
        g.drawString(Integer.toString(value), 10 + bx, by);
    }

}
