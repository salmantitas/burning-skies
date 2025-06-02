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

    int pauseX = Utility.percWidth(16), pauseY = Utility.percHeight(50);
    int buttonX = pauseX + Utility.percWidth(1);

    int volY = y62;

//    ButtonAction volumeMasterDown = new ButtonAction(x36, volY, optionSize, optionSize, "-", ActionTag.volumeMasterDown);
//    ButtonAction volumeMaster = new ButtonAction(x40, volY, optionSize, "Master Volume", ActionTag.volumeMaster);
//    ButtonAction volumeMasterUp = new ButtonAction(x62, volY, optionSize, optionSize,"+", ActionTag.volumeMasterUp);

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 3;
        options = new Button[MAXBUTTON];
        ButtonNav settings = new ButtonNav(buttonX, volY, Utility.perc(buttonSize, 80), "Settings", GameState.Settings);
        ButtonNav backToMenu = new ButtonNav(buttonX, y71, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(buttonX, y80, buttonSize, "Quit", GameState.Quit);

        float hudTransparencyTop = 1f;
        float hudTransparencySide = 0.2f;
        int deadzoneLeftWidth = Utility.intAtWidth640(32), deadzoneRightX = VariableHandler.deadzoneRightX;

        Panel topPane = new Panel(0,0, Engine.WIDTH, Utility.percHeight(12), GameState.Game);
        topPane.setTransparency(hudTransparencyTop);
        menuItems.add(topPane);

        Panel leftPane = new Panel(0,Utility.percHeight(12), deadzoneLeftWidth, Engine.HEIGHT, GameState.Game);
        leftPane.setTransparency(hudTransparencySide);
        menuItems.add(leftPane);

        Panel rightPane = new Panel(deadzoneRightX,Utility.percHeight(12), Utility.intAtWidth640(32), Engine.HEIGHT, GameState.Game);
        rightPane.setTransparency(hudTransparencySide);
        menuItems.add(rightPane);

        options[0] = settings;
//        options[1] = volumeMaster;
//        options[2] = volumeMasterUp;

        options[1] = backToMenu;
        options[MAXBUTTON - 1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        VariableHandler.renderHUD(g);
        drawPause(g);
//        VariableHandler.renderLevel(g);
//        renderValue(g, volumeMasterUp, SoundHandler.getVolumeMaster(), SoundHandler.isVolumeMaster());

        super.postRender(g);
    }

    /*
    * Renders the text "Pause"
    * */

    public void drawPause(Graphics g) {
        g.setFont(new Font("arial", 1, Utility.percWidth(20)));
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", pauseX, pauseY);
    }
}
