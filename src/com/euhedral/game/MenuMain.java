package com.euhedral.game;

import com.euhedral.engine.Button;
import com.euhedral.engine.Menu;
import com.euhedral.engine.Panel;
import com.euhedral.engine.*;

import java.awt.*;

public class MenuMain extends Menu {

    public MenuMain() {
        super(GameState.Menu);
        MAXBUTTON = 3;
        options = new Button[MAXBUTTON];

        Panel sidePanel = new Panel(0, 0, Engine.percWidth(40), Engine.HEIGHT, GameState.Menu);
        menuItems.add(sidePanel);

        ButtonNav mainMenuPlay = new ButtonNav(leftButtonX, lowestButtonY, buttonSize, "Play", GameState.Menu, GameState.Transition);

        mainMenuPlay.addOtherState(GameState.GameOver);
        mainMenuPlay.setFill();

        ButtonNav help = new ButtonNav(midButtonX, lowestButtonY, buttonSize, "Help", GameState.Menu, GameState.Help);

        ButtonNav mainMenuQuit = new ButtonNav(rightButtonX, lowestButtonY, buttonSize, "Quit", GameState.Menu, GameState.Quit);
        mainMenuQuit.setFill();
        mainMenuQuit.addOtherState(GameState.Transition);
        mainMenuQuit.addOtherState(GameState.Pause);
        mainMenuQuit.addOtherState(GameState.GameOver);

        options[0] = mainMenuPlay;
        options[1] = help;
        options[2] = mainMenuQuit;

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawTitle(g);
    }

    /*
    *
    * */

    private void drawTitle(Graphics g) {
        Font font = new Font("arial", 1, titleSize);
        g.setFont(font);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, titleY);
    }
}
