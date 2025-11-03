package com.euhedral.Game.UI.Menus;

import com.euhedral.Engine.*;
import com.euhedral.Engine.UI.*;
import com.euhedral.Engine.UI.Button;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Engine.UI.Panel;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.UI.HUD;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class MenuPause extends Menu {

    int pauseX = Utility.percWidth(15), pauseY = Utility.percHeight(40);
    int buttonX = pauseX + Utility.percWidth(1);

    Font textFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(90));
    Font BGMFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(10));

//    ButtonAction volumeMasterDown = new ButtonAction(x36, volY, optionSize, optionSize, "-", ActionTag.volumeMasterDown);
//    ButtonAction volumeMaster = new ButtonAction(x40, volY, optionSize, "Master Volume", ActionTag.volumeMaster);
//    ButtonAction volumeMasterUp = new ButtonAction(x62, volY, optionSize, optionSize,"+", ActionTag.volumeMasterUp);

    public MenuPause() {
        super(GameState.Pause);
        MAXBUTTON = 4;
        options = new Button[MAXBUTTON];

        int spacingY =  Utility.percHeight(9);
        int resumeY = pauseY + spacingY;
        int settingsY = resumeY + spacingY;
        int backY = settingsY + spacingY;
        int quitY = backY + spacingY;

        ButtonNav resume = new ButtonNav(buttonX, resumeY, Utility.perc(buttonSize, 80), "Resume", GameState.Game);
        ButtonNav settings = new ButtonNav(buttonX, settingsY, Utility.perc(buttonSize, 80), "Settings", GameState.Settings);
        ButtonNav back = new ButtonNav(buttonX, backY, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(buttonX, quitY, Utility.perc(buttonSize, 95), "Quit", GameState.Quit);

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

        options[0] = resume;
        options[1] = settings;
//        options[2] = volumeMasterUp;

        options[2] = back;
        options[MAXBUTTON - 1] = quit;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        HUD.render(g);
        drawPause(g);
        drawBGM(g);
//        VariableHandler.renderLevel(g);
//        renderValue(g, volumeMasterUp, SoundHandler.getVolumeMaster(), SoundHandler.isVolumeMaster());

        super.postRender(g);
    }

    /*
    * Renders the text "Pause"
    * */

    public void drawPause(Graphics g) {
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString("PAUSE", pauseX, pauseY);
    }

    public void drawBGM(Graphics g) {
        g.setFont(BGMFont);
        g.setColor(Color.WHITE);
        g.drawString("BGM: " + SoundHandler.getSongName(), pauseX, pauseY + 30);
    }
}
