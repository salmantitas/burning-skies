package com.euhedral.game.UI.Menus;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.engine.Utility;
import com.euhedral.game.UI.MessageBox;
import com.euhedral.game.VariableManager;

import java.awt.*;

public class MenuPlay extends Menu {

    public MenuPlay() {
        super(GameState.Game);
        MAXBUTTON = 0;
        options = new Button[MAXBUTTON];

        MessageBox a = new MessageBox(500,100);
        a.addText("You can use the WASD or Arrow Keys for movement.");
        a.addText("SPACEBAR or NUM0 can be used for shooting.");
        a.addText("CTRL is used to switch to ground bullets.");
        a.addText("CTRL is used to switch to ground bullets.");
        a.addText("Enjoy!");
        addMessageBox(a);

        Panel topPane = new Panel(0,0, Engine.WIDTH, Utility.percHeight(10), GameState.Game);
        menuItems.add(topPane);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        VariableManager.renderHUD(g);
        VariableManager.renderLevel(g);

        super.postRender(g);
    }
}
