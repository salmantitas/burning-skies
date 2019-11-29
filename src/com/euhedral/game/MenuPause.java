package com.euhedral.game;

import com.euhedral.engine.Button;
import com.euhedral.engine.ButtonNav;
import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;

import java.awt.*;

public class MenuPause extends Menu{

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Help, GameState.Menu);
        ButtonNav quit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Menu, GameState.Quit);
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
        g.setFont(new Font("arial", 1, Engine.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", Engine.percWidth(16), Engine.percHeight(25));
    }
}
