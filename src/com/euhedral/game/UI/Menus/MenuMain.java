package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;

import java.awt.*;

public class MenuMain extends Menu {

    public MenuMain() {
        super(GameState.Menu);
        MAXBUTTON = 4;
        options = new Button[MAXBUTTON];

        ButtonNav play = new ButtonNav(x3, y20, buttonSize, "Play", GameState.Transition);

//        play.addOtherState(GameState.GameOver);
        play.setFill();

        ButtonNav help = new ButtonNav(x3, y40, buttonSize, "Help", GameState.Help);
        ButtonNav credits = new ButtonNav(x2, y60, buttonSize, "Credits", GameState.Credits);

        ButtonNav quit = new ButtonNav(x3, yFINAL, buttonSize, "Quit", GameState.Quit);
        quit.setFill();
        quit.addOtherState(GameState.Transition);
        quit.addOtherState(GameState.Pause);
        quit.addOtherState(GameState.GameOver);

        options[0] = play;
        options[1] = help;
        options[2] = credits;
        options[3] = quit;

//        try {
//            importButtons();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawTitle(g);

        super.postRender(g);
    }

    /*
    *
    * */

    private void drawTitle(Graphics g) {
        Font font = new Font("arial", 1, titleSize);
        g.setFont(font);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, y10);
    }
}
