package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonAction;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.Utility;
import com.euhedral.game.ActionTag;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuSettings extends Menu {

    int optionSize = buttonSize/2;
    int toggleIconSize = optionSize;

    // Options


    ButtonAction tutorial = new ButtonAction(x2, y40, optionSize, "Tutorial", ActionTag.tutorial);
    ButtonAction volume = new ButtonAction(x2, y50, optionSize, "Background Music", ActionTag.volume);
    ButtonNav backToMenu = new ButtonNav(x2, y80, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);

    public MenuSettings() {
        super(GameState.Settings);
        MAXBUTTON = 3;
        options = new Button[MAXBUTTON];

        tutorial.setFill();
        volume.setFill();

        options[0] = tutorial;
        options[1] = volume;
        options[2] = backToMenu;
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
        renderIcon(g, volume, VariableHandler.isVolume());

        super.postRender(g);
    }

    /*
    * Render Functions
    * */

    private void renderIcon(Graphics g, Button button, boolean bool) {
        int bx = button.getX() + button.getWidth();
        int by = button.getY() + ((button.getHeight() - toggleIconSize)/2);

        Color c = bool ? Color.GREEN : Color.RED;
        g.setColor(c);
        g.fillOval(10 + bx, by ,toggleIconSize,toggleIconSize);
    }

}
