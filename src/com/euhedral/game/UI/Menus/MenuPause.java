package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.VariableManager;

import java.awt.*;

public class MenuPause extends Menu {

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, yFINAL, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(xFINAL, yFINAL, buttonSize, "Quit", GameState.Quit);
        quit.setFill();

        options[0] = backToMenu;
        options[1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        VariableManager.renderHUD(g);
        drawPause(g);

        super.postRender(g);
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
