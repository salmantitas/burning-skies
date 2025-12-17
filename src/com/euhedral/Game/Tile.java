package com.euhedral.Game;

import com.euhedral.Engine.Animation;
import com.euhedral.Engine.Position;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public int width, height;
    public Position pos;

    Animation anim;

    public Tile(int x, int y, int size) {
        pos = new Position(x,y);
        width = size;
        height = width;
        BufferedImage[] sea = GameController.getTexture().sea;
        anim = new Animation(9, sea);
//        anim.setMode(1);
    }

    public void update() {
        anim.runAnimation();
        pos.y += Background.getScrollRate();

        if (pos.y > 1000) {
            pos.y = 0;
        }
    }

    public void render(Graphics g) {
        anim.drawAnimation(g, pos.intX(), pos.intY(), width, height);
    }
}
