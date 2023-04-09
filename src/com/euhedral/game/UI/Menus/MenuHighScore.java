package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.Utility;
import com.euhedral.game.VariableHandler;

import java.awt.*;
import java.util.ArrayList;

public class MenuHighScore extends Menu {

    public MenuHighScore() {
        super(GameState.Highscore);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, y80, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        options[0] = backToMenu;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawText(g);

        super.postRender(g);
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("arial", 1, titleSize));
        g.setColor(Color.WHITE);

        g.drawString("High Score", x15, y00);

        VariableHandler.drawHighScore(g, x0, y10);
    }
}
