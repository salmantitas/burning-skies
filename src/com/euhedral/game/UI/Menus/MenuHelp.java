package com.euhedral.Game.UI.Menus;

import com.euhedral.Engine.UI.Button;
import com.euhedral.Engine.UI.ButtonNav;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Engine.*;
import com.euhedral.Engine.UI.Panel;
import com.euhedral.Game.VariableHandler;

import java.awt.*;
import java.util.ArrayList;

public class MenuHelp extends Menu {

    int helpX = 210;
    int helpY = 200;

    Font textFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(10));
    ArrayList<String> help;
    int lineHeightInPixel = 50;

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

        ButtonNav back = new ButtonNav(x43, y80, optionSize, "Back", GameState.Menu);
        options[0] = back;

        int panelWidth = Utility.percWidth(67);
        int panelHeight = 470;

        float panelTransparency = 0.7f;
        Color panelColor = Color.BLACK;

        Panel backPane = new Panel(helpX - Utility.percWidth(1), helpY + 40, panelWidth, panelHeight, GameState.Game, panelTransparency, panelColor);
        backPane.enableBorder();
        menuItems.add(backPane);

//        createBindable(GameController.UP, 100, 100);

        help = new ArrayList<>();
        help.add("Use WASD or the Arrow Keys to move.");
//        help.add("");
        help.add("        SPACEBAR to shoot.");
//        help.add("");
        help.add("        ESCAPE or P to pause.");
//        help.add("");
        help.add("        CTRL to use Special Move.");
//        help.add("");
        help.add("You can disable tutorial in Settings.");
//        help.add("");
        help.add("Shoot as many enemies as you can.");
//        help.add("");
        help.add("You can toggle tutorial message-boxes");
        help.add("in Settings");
//        help.add("ESC from menu to quit");

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

//    @Override
//    public void checkButtonAction(int mx, int my) {
//        super.checkButtonAction(mx, my);
//        // do the same for bindable keys
////        for (Bindable b: bindables) {
////            b.rebind();
////        }
////        return null;
//    }

    private void drawBindables(Graphics g) {
//        for (Bindable b: bindables) {
//            b.render(g);
//        }
    }

    private void drawHelpText(Graphics g) {
        g.setFont(textFont);
        g.setColor(Color.WHITE);

        for (int i = 0; i < help.size(); i++)
        {
            String s = help.get(i);
            g.drawString(s, helpX + Utility.percWidth(3), (helpY + 50) + (i+1)*lineHeightInPixel);
        }
    }

//    private void createBindable(String str, int x, int y) {
//        Bindable b = new Bindable(x, y, str);
//        bindables.add(b);
//    }
}
