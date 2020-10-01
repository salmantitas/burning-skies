package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;
import com.euhedral.engine.UI.UIItem;
import com.euhedral.game.GameController;
import com.euhedral.game.UI.MessageBox;

import java.awt.*;
import java.util.ArrayList;

public class MenuHelp extends Menu {

    private class Bindable extends UIItem{
        String str;
        boolean selected = false;
        Color color, selectColor;

        public Bindable(int x, int y, String str) {
            this.str = str;
            this.x = x;
            this.y = y;
            this.width = 30;
            height = width;
            color = Color.red;
            selectColor = Color.green;
        }

        public void render(Graphics g) {
            if (!selected)
                g.setColor(color);
            else g.setColor(selectColor);
            g.drawRect(x,y,width,height);

            // todo: resize width with font-metrics

            g.drawString(str, x, y + height );
        }

        @Override
        public boolean mouseOverlap(int mx, int my) {
            int minX = x, minY = y;
            int maxX = minX + width, maxY = minY + height;
            return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
        }

        public void select() {
            selected = true;
        }

        public void deselect() {
            selected = false;
        }

        public void rebind() {
            GameController.rebinding = true;
        }
    }

    private ArrayList<Bindable> bindables = new ArrayList<>();

    public MenuHelp() {
        super(GameState.Help);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(midLeftButtonX, lowestButtonY, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        options[0] = backToMenu;

        createBindable(GameController.UP, 100, 100);

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawHelpText(g);
        drawBindables(g);

        super.postRender(g);
    }

    @Override
    public void checkHover(int mx, int my) {
        super.checkHover(mx, my);

        for (Bindable b: bindables) {
            if (b.mouseOverlap(mx, my)) {
                b.select();
            } else
                b.deselect();
        }
    }

    @Override
    public void checkButtonAction(int mx, int my) {
        super.checkButtonAction(mx, my);
        // do the same for bindable keys
        for (Bindable b: bindables) {
            b.rebind();
        }
    }

    private void drawBindables(Graphics g) {
        for (Bindable b: bindables) {
            b.render(g);
        }
    }

    private void drawHelpText(Graphics g) {
        g.setFont(new Font("arial", 1, 30));
        g.setColor(Color.WHITE);
        String[] help = new String[5];
        int lineHeightInPixel = 80;
        help[0] = GameController.UP + "-" + GameController.LEFT + "-" + GameController.DOWN + "-"
                + GameController.RIGHT + " for movement";
        help[1] = GameController.SHOOT + " to shoot";
        help[2] = GameController.BULLET + " to switch between air and ground weapons";
        help[3] = "ESC or " + GameController.PAUSE + " in-game to pause or resume";
        help[4] = "ESC from menu to quit";
        int helpX = 200;
        for (int i = 0; i < help.length; i++) {
            g.drawString(help[i], helpX, (i+1)*lineHeightInPixel);
        }
    }

    private void createBindable(String str, int x, int y) {
        Bindable b = new Bindable(x, y, str);
        bindables.add(b);
    }
}
