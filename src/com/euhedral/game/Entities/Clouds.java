package com.euhedral.Game.Entities;

import com.euhedral.Engine.MobileEntity;
import com.euhedral.Game.Background;
import com.euhedral.Game.EntityID;
import com.euhedral.Game.GameController;

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
