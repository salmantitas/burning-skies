package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;

import java.awt.*;

public class MenuGameOver extends Menu {

    public MenuGameOver() {
        super(GameState.GameOver);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, y60, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(x3, yFINAL, buttonSize, "Quit", GameState.Quit);

        options[0] = backToMenu;
        options[1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawGameOverScreen(g);
        super.postRender(g);
    }

    /*
    *
    * */

    public void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("arial", 1, 150));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Utility.percWidth(2), Engine.HEIGHT/2);
    }

}
