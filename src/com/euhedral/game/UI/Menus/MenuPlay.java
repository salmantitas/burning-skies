package com.euhedral.game.UI.Menus;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.UI.Panel;
import com.euhedral.engine.Utility;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.Tutorial;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuPlay extends Menu {

//    public static boolean moved = false;
//    public static boolean shot = false;

    private Tutorial tutorial;

    public MenuPlay() {
        super(GameState.Game);
        MAXBUTTON = 0;
//        options = new Button[MAXBUTTON];

        tutorial = new Tutorial();

        addMessageBox(tutorial.getMessageBox());

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
    }

//    @Override
//    public void update() {
//        super.update();
//        tutorial.update();
//    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        VariableHandler.renderHUD(g);
//        tutorial.render(g);
        super.postRender(g);
    }

    @Override
    public void onSwitch() {
        super.onSwitch();
        SoundHandler.playBGMPlay();
    }
}
