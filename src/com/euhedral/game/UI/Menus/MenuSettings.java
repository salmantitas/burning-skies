package com.euhedral.game.UI.Menus;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.UI.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.Utility;
import com.euhedral.game.ActionTag;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuSettings extends Menu {

//    // Functions
//    Function toggleTutorial = () -> VariableHandler.toggleTutorial();

    // Options

    ButtonAction tutorial = new ButtonAction(x40, y40, optionSize, "Tutorial", ActionTag.tutorial);
    ButtonAction volumeMasterDown = new ButtonAction(x36, y48, optionSize, optionSize, "-", ActionTag.volumeMasterDown);
    ButtonAction volumeMaster = new ButtonAction(x40, y48, optionSize, "Master Volume", ActionTag.volumeMaster);
    ButtonAction volumeMasterUp = new ButtonAction(x62, y48, optionSize, optionSize,"+", ActionTag.volumeMasterUp);
//    ButtonAction volumeMusicDown = new ButtonAction(x36, y56, optionSize, optionSize, "-", ActionTag.volumeMusicDown);
//    ButtonAction volumeMusic = new ButtonAction(x40, y56, optionSize, "Music Volume", ActionTag.volumeMusic);
//    ButtonAction volumeMusicUp = new ButtonAction(x62, y56, optionSize, optionSize,"+", ActionTag.volumeMusicUp);
//    ButtonAction volumeEffectsDown = new ButtonAction(x36, y62, optionSize, optionSize, "-", ActionTag.volumeEffectsDown);
//    ButtonAction volumeEffects = new ButtonAction(x40, y62, optionSize, "Effects Volume", ActionTag.volumeEffects);
//    ButtonAction volumeEffectsUp = new ButtonAction(x62, y62, optionSize, optionSize,"+", ActionTag.volumeEffectsUp);
    ButtonNav backToMenu = new ButtonNav(x40, y80, Utility.perc(buttonSize, 80), "Back", GameState.Menu);

    public MenuSettings() {
        super(GameState.Settings);
        MAXBUTTON = 5; //11;
        options = new Button[MAXBUTTON];

        options[0] = tutorial;
        options[1] = volumeMasterDown;
        options[2] = volumeMaster;
        options[3] = volumeMasterUp;
//        options[4] = volumeMusicDown;
//        options[5] = volumeMusic;
//        options[6] = volumeMusicUp;
//        options[7] = volumeEffectsDown;
//        options[8] = volumeEffects;
//        options[9] = volumeEffectsUp;
        options[MAXBUTTON - 1] = backToMenu;
    }

//    @Override
//    public void update() {
//
//    }

    // Disables button when the conditions are not true
    private void checkConditionAndDisable(Button button, boolean condition) {
        if (!condition) {
            button.disable();
        } else button.enable();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);

        // Render Toggle Icons

        renderIcon(g, tutorial, VariableHandler.isTutorial());
        renderValue(g, volumeMasterUp, SoundHandler.getVolumeMaster(), SoundHandler.isVolumeMaster());
//        renderValue(g, volumeMusicUp, SoundHandler.getVolumeMusic(), SoundHandler.isVolumeMusic());
//        renderValue(g, volumeEffectsUp, SoundHandler.getVolumeEffects(), SoundHandler.isVolumeEffects());

        super.postRender(g);
    }

    @Override
    public void onSwitch() {
        super.onSwitch();

//        Utility.log("Previous: " + previous);

        backToMenu.setTargetSate(Engine.previousState);
    }

    /*******************
    * Render Functions *
    ********************/

    private void renderIcon(Graphics g, Button button, boolean bool) {
        int bx = button.getX() + button.getWidth();
        int by = button.getY() + ((button.getHeight() - toggleIconSize)/2);

        Color c = bool ? Color.GREEN : Color.RED;
        g.setColor(c);
        g.fillOval(10 + bx, by ,toggleIconSize,toggleIconSize);
    }

}
