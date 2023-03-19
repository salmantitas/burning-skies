package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;
import com.euhedral.game.ActionTag;
import com.euhedral.game.GameController;

import java.awt.*;
import java.util.ArrayList;

public class MenuHelp extends Menu {

//    private class Bindable extends UIItem{
//        String str;
//        boolean selected = false;
//        Color color, selectColor;
//
//        public Bindable(int x, int y, String str) {
//            this.str = str;
//            this.x = x;
//            this.y = y;
//            this.width = 30;
//            height = width;
//            color = Color.red;
//            selectColor = Color.green;
//        }
//
//        public void render(Graphics g) {
//            if (!selected)
//                g.setColor(color);
//            else g.setColor(selectColor);
//            g.drawRect(x,y,width,height);
//
//            // todo: resize width with font-metrics
//
//            g.drawString(str, x, y + height );
//        }
//
//        @Override
//        public boolean mouseOverlap(int mx, int my) {
//            int minX = x, minY = y;
//            int maxX = minX + width, maxY = minY + height;
//            return (mx >= minX && mx <= maxX && my >= minY && my <= maxY);
//        }
//
//        public void select() {
//            selected = true;
//        }
//
//        public void deselect() {
//            selected = false;
//        }
//
//        public void rebind() {
//            GameController.rebinding = true;
//        }
//    }
//
//    private ArrayList<Bindable> bindables = new ArrayList<>();

    public MenuHelp() {
        super(GameState.Help);
        MAXBUTTON = 1;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x2, y80, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        options[0] = backToMenu;

//        createBindable(GameController.UP, 100, 100);

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

//        for (Bindable b: bindables) {
//            if (b.mouseOverlap(mx, my)) {
//                b.select();
//            } else
//                b.deselect();
//        }
    }

    @Override
    public ActionTag checkButtonAction(int mx, int my) {
        super.checkButtonAction(mx, my);
        // do the same for bindable keys
//        for (Bindable b: bindables) {
//            b.rebind();
//        }
        return null;
    }

    private void drawBindables(Graphics g) {
//        for (Bindable b: bindables) {
//            b.render(g);
//        }
    }

    private void drawHelpText(Graphics g) {
        g.setFont(new Font("arial", 1, 30));
        g.setColor(Color.WHITE);
        ArrayList<String> help = new ArrayList<>();
        help.add(GameController.UP + "-" + GameController.LEFT + "-" + GameController.DOWN + "-"
                + GameController.RIGHT + " for movement");
        help.add(GameController.SHOOT + " to shoot");
        help.add("ESC or " + GameController.PAUSE + " in-game to pause or resume");
        help.add("");
        help.add("You can use the Tutorial button to permanently");
        help.add("enable or disable the tutorial message-boxes");
//        help.add("ESC from menu to quit");
        int lineHeightInPixel = 80;
        int helpX = 200;
        for (int i = 0; i < help.size(); i++)
        {
            String s = help.get(i);
            g.drawString(s, helpX, (i+1)*lineHeightInPixel);
        }
    }

//    private void createBindable(String str, int x, int y) {
//        Bindable b = new Bindable(x, y, str);
//        bindables.add(b);
//    }
}
