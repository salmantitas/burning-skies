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

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Quit);
        quit.setFill();

        options[0] = backToMenu;
        options[1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        drawGameOverScreen(g);
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
