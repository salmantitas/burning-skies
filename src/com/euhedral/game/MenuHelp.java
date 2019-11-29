package com.euhedral.game;

import com.euhedral.engine.Button;
import com.euhedral.engine.Panel;
import com.euhedral.engine.*;

import java.awt.*;

public class MenuHelp extends Menu{

    public MenuHelp() {
        super(GameState.Help);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Engine.perc(buttonSize, 80), "Main Menu", GameState.Help, GameState.Menu);
        options[0] = backToMenu;

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawHelpText(g);
    }

//    public void checkHover(int mx, int my) {
//
//    }

//    public void checkButtonAction(int mx, int my) {
//
//    }
//
//    public void chooseSelected() {
//
//
//    }

//    public GameState getState() {
//
//    }

    /*
    *
    * */

    private void drawHelpText(Graphics g) {
        g.setFont(new Font("arial", 1, 30));
        g.setColor(Color.WHITE);
        String[] help = new String[5];
        int lineHeightInPixel = 80;
        help[0] = "W-A-S-D for movementTimer";
        help[1] = "SPACEBAR to shoot";
        help[2] = "CTRL to switch between air and ground weapons";
        help[3] = "ESC or P in-game to pause or resume";
        help[4] = "ESC from menu to quit";
        int helpX = 200;
        for (int i = 0; i < help.length; i++) {
            g.drawString(help[i], helpX, (i+1)*lineHeightInPixel);
        }
    }
}
