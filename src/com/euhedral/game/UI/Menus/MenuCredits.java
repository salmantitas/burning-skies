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

        g.drawString("Credits", x15, y00);

        int creditFontSize = 25;

        g.setFont(new Font("arial", 1, creditFontSize));
        ArrayList<String> text = new ArrayList<>();
        text.add("Background Music");
        text.add("Dar Golan");
        text.add("Joshua McLean");
        text.add("");

        text.add("Bullet Sound Effect");
        text.add("The Premiere Edition 10 on videvo.com");
        text.add("Explosion Sound Effect");
        text.add("Pranus Production's Gun Sound Pack on itch.io");
        text.add("");

        text.add("Sea Texture");
        text.add("Rafael Matos");

        int lineHeightInPixel = 35;
        for (int i = 0; i < text.size(); i++)
        {
            String s = text.get(i);
            g.drawString(s, x0, y10 + (i+1)*lineHeightInPixel);
        }
    }
}
