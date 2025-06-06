package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.engine.Utility;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuHighScore extends Menu {

    int highScoreX = titleX + Utility.percWidth(1);
//    int highScoreY = Utility.percHeight(25);

    public MenuHighScore() {
        super(GameState.Highscore);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav back = new ButtonNav(x43, y80, Utility.perc(buttonSize, 80), "Back", GameState.Menu);
        options[0] = back;

        int panelWidth = 300;
        int panelHeight = Utility.percHeight(41.5);

        Panel backPane = new Panel(highScoreX, y25, panelWidth, panelHeight, GameState.Game);
        backPane.setTransparency(0.8f);
        menuItems.add(backPane);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawText(g);

        super.postRender(g);
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("arial", 1, titleSize));
        g.setColor(Color.BLACK);

        g.drawString("High Score", titleX, headingY);

        g.setColor(Color.WHITE);

        VariableHandler.drawHighScore(g, highScoreX + Utility.percWidth(2), y25 + Utility.percHeight(1));
    }
}
