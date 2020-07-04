package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;

import java.awt.*;

public class MenuPause extends Menu {

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Quit);
        quit.setFill();

        options[0] = backToMenu;
        options[1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawPause(g);
    }

    /*
    *
    * */

    public void drawPause(Graphics g) {
        g.setFont(new Font("arial", 1, Utility.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", Utility.percWidth(16), Utility.percHeight(25));
    }
}
