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

    public MenuHighScore() {
        super(GameState.Highscore);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav back = new ButtonNav(x40, y80, Utility.perc(buttonSize, 80), "Back", GameState.Menu);
        options[0] = back;

        int panelWidth = 300;
        int panelHeight = Utility.percHeight(40);

        com.euhedral.engine.UI.Panel backPane = new Panel(Utility.percWidth(6), Utility.percHeight(25), panelWidth, panelHeight, GameState.Game);
        backPane.setTransparency(0.7f);
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
        g.setColor(Color.WHITE);

        g.drawString("High Score", x5, y20);

        VariableHandler.drawHighScore(g, x10, y26);
    }
}
