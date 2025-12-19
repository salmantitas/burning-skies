package com.euhedral.Game;

import com.euhedral.Engine.Animation;
import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Position;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public int width, height;
    public Position pos;

    int imageScrollinginterval;
    Animation anim;

    public Tile(int x, int y, int size) {
        pos = new Position(x,y);
        width = size;
        height = width;
        imageScrollinginterval = height * 2 - 2;

        BufferedImage[] sea = GameController.getTexture().sea;
        anim = new Animation(9, sea);
//        anim.setMode(1);
    }

    public void update() {
        anim.runAnimation();
        pos.y += Background.getScrollRate();

        int factor = Engine.HEIGHT / height;

        if (pos.y > height * factor) {
            pos.y = -height + 3;
        }
    }

    public void render(Graphics g, int screenshake) {
        anim.drawAnimation(g, pos.intX() + screenshake, pos.intY() + screenshake, width, height);
    }
}
