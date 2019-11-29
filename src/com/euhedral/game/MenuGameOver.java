package com.euhedral.game;

import com.euhedral.engine.Button;
import com.euhedral.engine.ButtonNav;
import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;

import java.awt.*;

public class MenuGameOver extends Menu{

    public MenuGameOver() {
        super(GameState.GameOver);
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
        drawGameOverScreen(g);
    }


    /*
    *
    * */

    public void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("arial", 1, 150));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Engine.percWidth(2), Engine.HEIGHT/2);
    }

}
