package com.euhedral.game.Entities.Enemy;

import com.euhedral.game.ContactID;
import com.euhedral.game.GameController;
import com.euhedral.game.Texture;

import java.awt.*;

public class EnemyBasic extends Enemy{

    private Texture texture;

    public EnemyBasic(int x, int y, ContactID contactID, int levelHeight) {
        super(x, y, contactID, levelHeight);
    }

    public EnemyBasic(int x, int y, ContactID contactID, Color color, int levelHeight) {
        super(x, y, contactID, color, levelHeight);
        texture = GameController.getTexture();
        setImage(texture.enemy[0]);
    }

    @Override
    public void initialize() {
        super.initialize();

        power = 1;
        shootTimerDef = 250;
        velY = 1.8f;
        healthRange(4,6);
        score = 50;
    }

    @Override
    protected void shoot() {
        super.shoot();
        shootDownDefault();
    }
}
