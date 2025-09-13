package com.euhedral.game.UI.Menus;

import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.engine.Utility;
import com.euhedral.game.UI.UIHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;
import java.util.ArrayList;

public class MenuCredits extends Menu {
    ArrayList<String> assets, testers;
    int creditFontSize = Utility.intAtWidth640(6);
    int leftPaneX = Utility.percWidth(3), rightPaneX = Utility.percWidth(56);
    int lineHeightInPixel = 30;

    Font creditFont = VariableHandler.customFont.deriveFont(0, creditFontSize);

    public MenuCredits() {
        super(GameState.Credits);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav back = new ButtonNav(x43, Utility.percHeight(86), optionSize, "Back", GameState.Menu);
        options[0] = back;

        int panelWidth = Utility.percWidth(40);
        int panelHeight = Utility.percHeight(70);

        float panelTransparency = 0.7f;
        Color panelColor = Color.BLACK;

        Panel leftPane = new Panel(leftPaneX, Utility.percHeight(22), panelWidth, panelHeight, GameState.Game, panelTransparency, panelColor);
        leftPane.enableBorder();
        menuItems.add(leftPane);

        Panel rightPane = new Panel(rightPaneX, Utility.percHeight(22), panelWidth, panelHeight, GameState.Game, panelTransparency, panelColor);
        rightPane.enableBorder();
        menuItems.add(rightPane);

        assets = new ArrayList<>();

        assets.add("Game Development and Design");
        assets.add("- Salman Mehedy Titas");
        assets.add("");

        assets.add("Background Music");
        assets.add("- Dar Golan");
        assets.add("- Joshua McLean");
        assets.add("- Abstraction");
        assets.add("");

//        assets.add("Bullet Sound Effects");
        assets.add("Sound Effects");
        assets.add("- The Premiere Edition 10 on videvo.com");
        assets.add("- Michel Baradari");
        assets.add("- Pranus Production's Gun Sound Pack");
        assets.add("- JDSherbert");
        assets.add("- Pranus Production's Gun Sound Pack");
        assets.add("- Shapeforms Audio Free Sound Effects");
        assets.add("");

        assets.add("Sea Texture");
        assets.add("- Rafael Matos");
        assets.add("");

        assets.add("Explosion and Impact Animation");
        assets.add("- BDragon1727");
        assets.add("");

        testers = new ArrayList<>();
        testers.add("Invaluable Playtesters");
        testers.add("");
        testers.add("- Shadman Hossain");
        testers.add("- Mourud Ishmam Ahmed");
        testers.add("- Akif Ali");
        testers.add("- Saquib Al Mamun");
        testers.add("- Shabab Kabir");
        testers.add("- Atif Murtaza Mahmud");
        testers.add("- Christina Ly");
        testers.add("- Andres Prieto");
        testers.add("- RobotAsking");
        testers.add("- The people from Full Indie");
        testers.add("- And all the other wonderful people I");
        testers.add("might have missed. Sorry :(");
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawText(g);

        super.postRender(g);
    }

    private void drawText(Graphics g) {
        g.setFont(headingFont);
        g.setColor(Color.BLACK);
        g.drawString("Credits", x30, headingY);

        g.setFont(creditFont);

        g.setColor(Color.WHITE);

        for (int i = 0; i < assets.size(); i++)
        {
            String s = assets.get(i);
            g.drawString(s, leftPaneX + Utility.percWidth(1), y22 + (i+1)*lineHeightInPixel);
        }

        for (int i = 0; i < testers.size(); i++)
        {
            String s = testers.get(i);
            g.drawString(s, rightPaneX + Utility.percWidth(1), y22 + (i+1)*lineHeightInPixel);
        }
    }
}
