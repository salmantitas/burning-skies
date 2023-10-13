package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.game.ActionTag;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuPause extends Menu {

    int volY = y62;
    ButtonAction volumeMasterDown = new ButtonAction(x36, volY, optionSize, optionSize, "-", ActionTag.volumeMasterDown);
    ButtonAction volumeMaster = new ButtonAction(x40, volY, optionSize, "Master Volume", ActionTag.volumeMaster);
    ButtonAction volumeMasterUp = new ButtonAction(x62, volY, optionSize, optionSize,"+", ActionTag.volumeMasterUp);

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 5;
        options = new Button[MAXBUTTON];
        ButtonNav backToMenu = new ButtonNav(x40, y70, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(x43, y80, buttonSize, "Quit", GameState.Quit);

        Panel topPane = new Panel(0,0, Engine.WIDTH, Utility.percHeight(12), GameState.Game);
        topPane.setTransparency(1);
        menuItems.add(topPane);

        options[0] = volumeMasterDown;
        options[1] = volumeMaster;
        options[2] = volumeMasterUp;

        options[3] = backToMenu;
        options[MAXBUTTON - 1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        VariableHandler.renderHUD(g);
        drawPause(g);
//        VariableHandler.renderLevel(g);
        renderValue(g, volumeMasterUp, SoundHandler.getVolumeMaster(), SoundHandler.isVolumeMaster());

        super.postRender(g);
    }

    /*
    * Renders the text "Pause"
    * */

    public void drawPause(Graphics g) {
        int x = Utility.percWidth(16), y = Utility.percHeight(50);
        g.setFont(new Font("arial", 1, Utility.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", x, y);
    }
}
