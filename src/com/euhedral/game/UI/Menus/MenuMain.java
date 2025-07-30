package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.UI.MessageBox;
import com.euhedral.game.UI.UIHandler;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MenuMain extends Menu {

    Font titleFont, versionFont;
    int versionX, versionY;

    public MenuMain() {
        super(GameState.Menu);
        MAXBUTTON = 6;
        options = new Button[MAXBUTTON];

        int playY = y32;
        int settingsY = playY + spacingY;
        int highScoreY = settingsY + spacingY;
        int helpY = highScoreY + spacingY;
        int creditsY = helpY + spacingY;
        int quitY = creditsY + spacingY;

        versionX = xLast - 228;// Utility.intAtWidth640(106);
        versionY = headingY + Utility.intAtWidth640(20);

        ButtonNav play = new ButtonNav(x10, playY, buttonSize, "Play", GameState.Transition);
        ButtonNav settings = new ButtonNav(x10, settingsY, buttonSize, "Settings", GameState.Settings);
        ButtonNav highScore = new ButtonNav(x10, highScoreY, buttonSize, "High Score", GameState.Highscore);
        ButtonNav help = new ButtonNav(x10, helpY, buttonSize, "Help", GameState.Help);
        ButtonNav credits = new ButtonNav(x10, creditsY, buttonSize, "Credits", GameState.Credits);
        ButtonNav quit = new ButtonNav(x10, quitY, buttonSize, "Quit", GameState.Quit);

        options[0] = play;
        options[1] = settings;
        options[2] = highScore;
        options[3] = help;
        options[4] = credits;
        options[5] = quit;

        MessageBox start = new MessageBox( 100, 400, 1070, 300);
//        start.addText("");
        start.addText("");
//        start.addText("This game requires a keyboard to play");
        start.addText("");
//        start.addText("");
//        start.addText("");
        start.setFontSize(Utility.intAtWidth640(16));

        addMessageBox(start);

//        try {
//            importButtons();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        titleFont = UIHandler.customFont.deriveFont(1, titleSize);
        versionFont = UIHandler.customFont.deriveFont(1, versionSize);
    }

    @Override
    public void render(Graphics g) {
        if (activeMessageBoxes == 0)
            super.render(g);

        drawTitle(g);
//        renderLogo(g);

        postRender(g);
    }

    protected void postRender(Graphics g) {
        for (MessageBox messageBox : messageBoxes) {
                messageBox.render(g);
            }
    }

    @Override
    public void onSwitch() {
        SoundHandler.playBGMMenu();
        super.onSwitch();
    }

    /*
    *
    * */

    private void drawTitle(Graphics g) {
        g.setFont(titleFont);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, headingY);

        g.setFont(versionFont);
        g.setColor(Color.BLACK);
        g.drawString("v " + GameController.gameVersion, versionX, versionY);
//        BufferedImage image = GameController.getTexture().title;
//        int imageWidth = image.getWidth(), imageHeight = image.getHeight();
//        int imageOffset = 50;
//        int imageX = Engine.WIDTH - imageWidth - imageOffset;
//        int imageY = 0;
//
//        g.drawImage(image, imageX, imageY, null);
    }

    private void renderLogo(Graphics g) {
//        BufferedImage logo = GameController.getTexture().logo;
//        int imageWidth = logo.getWidth(), imageHeight = logo.getHeight();
//        int imageOffset = 20;
//        int imageX = Engine.WIDTH - imageWidth - imageOffset;
//        int imageY = Engine.HEIGHT - imageHeight - imageOffset;
//
//        g.drawImage(logo, imageX, imageY, null);
    }
}
