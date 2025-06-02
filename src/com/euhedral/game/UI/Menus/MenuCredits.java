package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.engine.Utility;

import java.awt.*;
import java.util.ArrayList;

public class MenuCredits extends Menu {

    public MenuCredits() {
        super(GameState.Credits);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav back = new ButtonNav(x52, Utility.percHeight(85), Utility.perc(buttonSize, 80), "Back", GameState.Menu);
        options[0] = back;

        int panelWidth = Utility.percWidth(40);
        int panelHeight = Utility.percHeight(70);

        Panel backPane = new Panel(Utility.percWidth(4), Utility.percHeight(22), panelWidth, panelHeight, GameState.Game);
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

        g.drawString("Credits", x30, y20);

        int creditFontSize = 20;

        g.setFont(new Font("arial", 1, creditFontSize));
        ArrayList<String> text = new ArrayList<>();

        text.add("Game Development and Design");
        text.add("- Salman Mehedy Titas");
        text.add("");

        text.add("Background Music");
        text.add("- Dar Golan");
        text.add("- Joshua McLean");
        text.add("");

        text.add("Bullet Sound Effects");
        text.add("- The Premiere Edition 10 on videvo.com");
        text.add("- Michel Baradari");
        text.add("Explosion Sound Effect");
        text.add("- Pranus Production's Gun Sound Pack on itch.io");
        text.add("");
        text.add("UI Sound Effect");
        text.add("- JDSherbert");
        text.add("");

        text.add("Sea Texture");
        text.add("- Rafael Matos");
        text.add("");

        text.add("Explosion and Impact Animation");
        text.add("- BDragon1727");
        text.add("");

        int lineHeightInPixel = 30;
        for (int i = 0; i < text.size(); i++)
        {
            String s = text.get(i);
            g.drawString(s, x5, y22 + (i+1)*lineHeightInPixel);
        }
    }
}
