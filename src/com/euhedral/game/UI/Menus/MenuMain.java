package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuMain extends Menu {

    public MenuMain() {
        super(GameState.Menu);
        MAXBUTTON = 6;
        options = new Button[MAXBUTTON];

        int playY = y32;
        int settingsY = playY + Utility.percHeight(12);
        int highScoreY = settingsY + Utility.percHeight(9);
        int helpY = highScoreY + Utility.percHeight(8);
        int creditsY = helpY + Utility.percHeight(11);
        int quitY = y80;

        ButtonNav play = new ButtonNav(x43, playY, buttonSize, "Play", GameState.Transition);
        ButtonNav settings = new ButtonNav(x40, settingsY, buttonSize, "Settings", GameState.Settings);
        ButtonNav highScore = new ButtonNav(x36, highScoreY, buttonSize, "High Score", GameState.Highscore);
        ButtonNav help = new ButtonNav(x43, helpY, buttonSize, "Help", GameState.Help);
        ButtonNav credits = new ButtonNav(x40, creditsY, buttonSize, "Credits", GameState.Credits);
        ButtonNav quit = new ButtonNav(x43, quitY, buttonSize, "Quit", GameState.Quit);

        options[0] = play;
        options[1] = settings;
        options[2] = highScore;
        options[3] = help;
        options[4] = credits;
        options[5] = quit;

//        try {
//            importButtons();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        drawTitle(g);
//        renderLogo(g);

        super.postRender(g);
    }

    @Override
    public void onSwitch() {
        SoundHandler.playBGMMenu();
    }

    /*
    *
    * */

    private void drawTitle(Graphics g) {
        Font font = new Font("arial", 1, titleSize);
        g.setFont(font);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, y26);

        font = new Font("arial", 1, titleSize/10);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("v " + GameController.gameVersion, xLast, y26);
//        BufferedImage image = GameController.getTexture().title;
//        int imageWidth = image.getWidth(), imageHeight = image.getHeight();
//        int imageOffset = 50;
//        int imageX = Engine.WIDTH - imageWidth - imageOffset;
//        int imageY = 0;
//
//        g.drawImage(image, imageX, imageY, null);
    }

    private void renderLogo(Graphics g) {
        BufferedImage logo = GameController.getTexture().logo;
        int imageWidth = logo.getWidth(), imageHeight = logo.getHeight();
        int imageOffset = 20;
        int imageX = Engine.WIDTH - imageWidth - imageOffset;
        int imageY = Engine.HEIGHT - imageHeight - imageOffset;

        g.drawImage(logo, imageX, imageY, null);
    }
}
