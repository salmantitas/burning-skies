package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.ArrayList;

public class MenuCredits extends Menu {

    public MenuCredits() {
        super(GameState.Credits);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, yFINAL, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
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

        g.drawString("Credits", x15, y00);

        g.setFont(new Font("arial", 1, 30));
        ArrayList<String> text = new ArrayList<>();
        text.add("Background Music");
        text.add("Dar Golan");
        text.add("");

        text.add("Bullet Sound Effect");
        text.add("The Premiere Edition 10 on videvo.com");
        text.add("");

        text.add("Explosion Sound Effect");
        text.add("Pranus Production's Gun Sound Pack on itch.io");

        int lineHeightInPixel = 40;
        for (int i = 0; i < text.size(); i++)
        {
            String s = text.get(i);
            g.drawString(s, x0, y20 + (i+1)*lineHeightInPixel);
        }
    }
}
