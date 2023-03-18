package com.euhedral.game.Entities.Enemy;

import com.euhedral.game.ContactID;
import com.euhedral.game.GameController;
import com.euhedral.game.TextureHandler;

import java.awt.*;

public class EnemyBasic extends Enemy{

    private TextureHandler textureHandler;

    public EnemyBasic(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, contactID, levelHeight);
    }

    public EnemyBasic(int x, int y, ContactID contactID, Color color, int levelHeight) {
        super(x, y, contactID, color, levelHeight);
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemy[0]);
    }

    @Override
    public void initialize() {
        super.initialize();

        power = 1;
        shootTimerDef = 250;
        velY = 1.8f;
        healthRange(2,4);
        score = 50;
    }

    @Override
    protected void shoot() {
        super.shoot();
        shootDownDefault();
    }

    @Override
    public void update() {
        super.update();
        if (!alive)
            explosion.runAnimation();
    }

    @Override
    public void render(Graphics g) {
        if (alive) {
            super.render(g);
//            renderBounds(g);
        } else {
            explosion.drawAnimation(g, x, y);

            if (explosion.playedOnce) {
                active = false;
            }
        }
    }
}
