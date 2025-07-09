package com.euhedral.game.UI.Menus;

import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.engine.*;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.UI.MessageBox;

import java.awt.*;

public class MenuMain extends Menu {

//    Font titleFont = new Font("arial", 1, titleSize);
    Font versionFont = new Font("arial", 1, titleSize/8);

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
        start.setFontSize(61);

        addMessageBox(start);

//        try {
//            importButtons();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Custom Font
//        try {
////            URL fontURL = new URL("file:///D:/Programming/burning-skies/res/magz.otf");
//            URL fontURL = getClass().getResource("/Magz.otf");//"/mags.otf");
//            titleFont = Font.createFont(Font.TRUETYPE_FONT, fontURL.openStream());
//            titleFont = titleFont.deriveFont(1, titleSize);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        catch (FontFormatException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

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
        g.setFont(headingFont);
        g.setColor(titleColor);
        g.drawString(Engine.TITLE, titleX, headingY);

        g.setFont(versionFont);
        g.setColor(Color.BLACK);
        g.drawString("v " + GameController.gameVersion, xLast - Utility.percWidth(2), headingY);
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
