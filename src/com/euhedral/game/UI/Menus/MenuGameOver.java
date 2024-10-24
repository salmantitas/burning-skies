package com.euhedral.game.UI.Menus;

import com.euhedral.engine.*;
import com.euhedral.engine.UI.Button;
import com.euhedral.engine.UI.ButtonNav;
import com.euhedral.engine.UI.Menu;
import com.euhedral.game.GameController;
import com.euhedral.game.SoundHandler;

import java.awt.*;

public class MenuGameOver extends Menu {

    Animation explosion;

    public MenuGameOver() {
        super(GameState.GameOver);
        MAXBUTTON = 2;
        options = new Button[MAXBUTTON];

        ButtonNav backToMenu = new ButtonNav(x40, y70, Utility.perc(buttonSize, 80), "Main Menu", GameState.Menu);
        ButtonNav quit = new ButtonNav(x43, y80, buttonSize, "Quit", GameState.Quit);

        options[0] = backToMenu;
        options[1] = quit;

        explosion = new Animation(5, GameController.getTexture().explosion[0],
                GameController.getTexture().explosion[1],
                GameController.getTexture().explosion[2],
                GameController.getTexture().explosion[3],
                GameController.getTexture().explosion[4]
        );
    }

    @Override
    public void update() {
        if (!explosion.playedOnce)
        explosion.runAnimation();
    }

    @Override
    public void render(Graphics g) {

        int inScreenMarker = (int) GameController.getCamera().getMarker() + 100;
        int minSize = Math.min(Engine.WIDTH, Engine.HEIGHT - inScreenMarker);
        int explodeX = (Engine.WIDTH - minSize)/2, explodeY = inScreenMarker/2;

        explosion.drawAnimation(g, explodeX, explodeY, minSize, minSize);

        super.render(g);
        drawGameOverScreen(g);
        super.postRender(g);
    }

    /*
    *
    * */

    public void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("arial", 1, 150));
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", Utility.percWidth(13), Engine.HEIGHT/2);
    }

    @Override
    public void onSwitch() {
        SoundHandler.playSound(SoundHandler.EXPLOSION);
//        GameController.getSound().bgm_Main.stop();
        SoundHandler.playBGMGameOver();
    }

}
