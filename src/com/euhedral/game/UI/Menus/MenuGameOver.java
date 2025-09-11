package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;
import com.euhedral.game.UI.UIHandler;
import com.euhedral.game.VariableHandler;

import java.awt.*;

public class MenuGameOver extends Menu {

    Animation explosion;
    int gameOverX = Utility.percWidth(5);
    int scoreX = Utility.percWidth(14);

    Font gameOverFont = VariableHandler.customFont.deriveFont(0, Utility.intAtWidth640(60));
    Font scoreFont = new Font("arial", 1, 40);

    int inScreenMarker, minSize, explodeX, explodeY;

    public MenuGameOver() {
        super(GameState.GameOver);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x33, y70, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(x41, y80, Utility.perc(buttonSize, 90), "Quit", GameState.Quit);

        options[0] = backToMenu;
        options[1] = quit;

        explosion = GameController.getTexture().initExplosion(10);
        minSize = Math.min(Engine.WIDTH, Engine.HEIGHT - VariableHandler.deadzoneTop);
        explodeX = (Engine.WIDTH - minSize) / 2;
        explodeY = inScreenMarker / 2;
    }

    @Override
    public void update() {
        if (!explosion.playedOnce)
            explosion.runAnimation();
    }

    @Override
    public void render(Graphics g) {

        if (!explosion.playedOnce) {
            explosion.drawAnimation(g, explodeX, explodeY, minSize, minSize);
        }

        super.render(g);
        drawGameOverScreen(g);
        drawScore(g);
        super.postRender(g);
    }

    /*
     *
     * */

    public void drawGameOverScreen(Graphics g) {
        g.setFont(gameOverFont);
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", gameOverX, Utility.percHeight(50));
    }

    public void drawScore(Graphics g) {
        g.setFont(scoreFont);
        g.setColor(Color.WHITE);
//        g.drawString("Score: " + VariableHandler.getScore(), scoreX, Utility.percHeight(60));
//        g.drawString("Highest Score: " + VariableHandler.getHighScoreList().get(0), scoreX , Utility.percHeight(66));
    }

    @Override
    public void onSwitch() {
        explosion.playedOnce = false;
        SoundHandler.playSound(SoundHandler.EXPLOSION_PLAYER);
        SoundHandler.playBGMGameOver();
        super.onSwitch();
    }

}
