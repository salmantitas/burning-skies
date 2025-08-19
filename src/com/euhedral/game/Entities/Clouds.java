package com.euhedral.game.Entities;

import com.euhedral.engine.MobileEntity;
import com.euhedral.game.Background;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
import com.euhedral.game.TextureHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Clouds extends MobileEntity {

    public Clouds(double x, double y, EntityID id) {
        super(x, y, id);
        color = Color.WHITE;
        forwardVelocity = 1;
        velY = forwardVelocity;
        setImage(GameController.getTexture().cloud[0]);
    }

    @Override
    public void update() {
        velY = forwardVelocity + Background.getScrollRate();
        super.update();
    }

    @Override
    protected void drawImage(Graphics g, BufferedImage image) {
        drawImage(g, image, width, height);
    }
}
