package com.euhedral.Game.UI.Menus;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;
import com.euhedral.Engine.UI.*;
import com.euhedral.Engine.UI.Button;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Engine.UI.Panel;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.SaveLoad;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class MenuSettings extends Menu {

    int bgmX, bgmY;
    Font BGMFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(10));

    // Options

    int volOffset = 2;

    Button tutorial;
//    ButtonAction volumeMasterDown = new ButtonAction(x30, y48 + volOffset, optionSize, optionSize, "-", ActionTag.volumeMasterDown);
    ButtonOption volumeMaster = new ButtonOption(x36, y48, optionSize, "Master Volume");
    Button volumeMasterUp = new Button(x70, y48 + volOffset, optionSize,"+");
//    ButtonAction volumeMusicDown = new ButtonAction(x36, y56, optionSize, optionSize, "-", ActionTag.volumeMusicDown);
    Button volumeMusic;
    ButtonOption changeBGM = new ButtonOption(x36, y62, optionSize, "Change BGM");
//    ButtonAction volumeMusicUp = new ButtonAction(x62, y56, optionSize, optionSize,"+", ActionTag.volumeMusicUp);
//    ButtonAction volumeEffectsDown = new ButtonAction(x36, y62, optionSize, optionSize, "-", ActionTag.volumeEffectsDown);
//    ButtonAction volumeEffects = new ButtonAction(x40, y62, optionSize, "Effects Volume", ActionTag.volumeEffects);
//    ButtonAction volumeEffectsUp = new ButtonAction(x62, y62, optionSize, optionSize,"+", ActionTag.volumeEffectsUp);
    ButtonNav back = new ButtonNav(x43, y80, optionSize, "Back", GameState.Menu);

    public MenuSettings() {
        super(GameState.Settings);
        MAXBUTTON = 5; //11;
        options = new Button[MAXBUTTON];

//        tutorial = new ButtonAction(x36, y40, optionSize, "Tutorial", ActionTag.tutorial);
        tutorial = new Button(x36, y40, optionSize, "Tutorial");
        tutorial.setActivate(() -> {
            VariableHandler.toggleTutorial();
            SaveLoad.saveSettings();
        });

        volumeMaster.setActivate((() -> {
            SoundHandler.toggleVolumeMaster();
            SaveLoad.saveSettings();
        }));

//        ButtonAction volumeMusic = new ButtonAction(x36, y56, optionSize, "Music Volume", ActionTag.volumeMusic);
        volumeMusic = new Button(x36, y56, optionSize, "Music Volume");
        volumeMusic.setActivate(() -> {
            SoundHandler.toggleVolumeMusic();
            SaveLoad.saveSettings();
        });

//        volumeMaster.setIncreaseAction(ActionTag.volumeMasterUp);
//        volumeMaster.setDecreaseAction(ActionTag.volumeMasterDown);

        volumeMaster.setIncreaseActivate(() -> {
            SoundHandler.volumeMasterUp();
            SaveLoad.saveSettings();
        });

        volumeMaster.setDecreaseActivate(() -> {
            SoundHandler.volumeMasterDown();
            SaveLoad.saveSettings();
        });

//        changeBGM.setIncreaseAction(ActionTag.BGMUp);
//        changeBGM.setDecreaseAction(ActionTag.BGMDown);

        changeBGM.setIncreaseActivate(SoundHandler::BGMUp);
        changeBGM.setDecreaseActivate(SoundHandler::BGMDown);

        options[0] = tutorial;
//        options[1] = volumeMasterDown;
        options[1] = volumeMaster;
//        options[3] = volumeMasterUp;
//        options[4] = volumeMusicDown;
        options[2] = volumeMusic;
        options[3] = changeBGM;
//        options[6] = volumeMusicUp;
//        options[7] = volumeEffectsDown;
//        options[8] = volumeEffects;
//        options[9] = volumeEffectsUp;
        options[MAXBUTTON - 1] = back;

        int panelX = 300;
        int panelY = 300;
        bgmX = panelX;
        bgmY = panelY - 50;
        int panelWidth = 780;
        int panelHeight = 370;
        float panelTransparency = 0.85f;
        Color panelColor = Color.BLACK;

        Panel backPane = new Panel(panelX, panelY, panelWidth, panelHeight, GameState.Game, panelTransparency, panelColor);
        backPane.enableBorder();
        menuItems.add(backPane);
    }

    @Override
    public void update() {
        checkConditionAndDisable(changeBGM, SoundHandler.gameBGMRunning());
    }

    // Disables button when the conditions are not true
    private void checkConditionAndDisable(Button button, boolean condition) {
        if (!condition) {
            button.disable();
        } else button.enable();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

//        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);
        if (SoundHandler.gameBGMRunning())
            drawBGM(g);

        // Render Toggle Icons

        renderIcon(g, tutorial, VariableHandler.isTutorial());
        renderValue(g, volumeMasterUp, SoundHandler.getVolumeMaster(), SoundHandler.isVolumeMaster());
        renderIcon(g, volumeMusic, SoundHandler.isVolumeMusic());
//        renderValue(g, volumeMusic, SoundHandler.getVolumeMusic(), SoundHandler.isVolumeMusic());
//        renderValue(g, volumeEffectsUp, SoundHandler.getVolumeEffects(), SoundHandler.isVolumeEffects());

        super.postRender(g);
    }

    @Override
    public void onSwitch() {
        super.onSwitch();

        back.setActivate(() -> {
            Engine.setState(Engine.previousState);
        });

    }

    /*******************
    * Render Functions *
    ********************/

    private void renderIcon(Graphics g, Button button, boolean bool) {
        buttonOffsetX = button.getX() + button.getWidth();
        buttonOffsetY = button.getY() + ((button.getHeight() - toggleIconSize)/2);

        buttonValueColor = bool ? Color.GREEN : Color.RED;
        g.setColor(buttonValueColor);

        int gap = button.getSelectedWidth() - button.getWidth();

        g.fillOval(buttonOffsetX + gap, buttonOffsetY ,toggleIconSize,toggleIconSize);
    }

    public void drawBGM(Graphics g) {
        g.setFont(BGMFont);
        g.setColor(Color.WHITE);
        g.drawString("BGM: " + SoundHandler.getSongName(), bgmX, bgmY + 30);
    }

}
