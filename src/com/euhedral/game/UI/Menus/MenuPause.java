package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuPause extends Menu {

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, y60, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(x3, y80, buttonSize, "Quit", GameState.Quit);

        Panel topPane = new Panel(0,0, Engine.WIDTH, Utility.percHeight(12), GameState.Game);
        topPane.setTransparency(1);
        menuItems.add(topPane);

        options[0] = backToMenu;
        options[1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        VariableHandler.renderHUD(g);
        drawPause(g);
//        VariableHandler.renderLevel(g);

        super.postRender(g);
    }

    /*
    * Renders the text "Pause"
    * */

    public void drawPause(Graphics g) {
        int x = Utility.percWidth(16), y = Utility.percHeight(50);
        g.setFont(new Font("arial", 1, Utility.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", x, y);
    }
}
