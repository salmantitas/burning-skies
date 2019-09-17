package com.euhedral.game.Entities;

import java.awt.*;

public class BulletEnemy extends Bullet{

    BulletEnemy(int x, int y) {
        super(x, y);
    }

    BulletEnemy(int x, int y, double angle) {
        super(x, y, angle);
    }

    BulletEnemy(int x, int y, double angle, int vel) {
        super(x, y, angle);
        this.vel = vel;
    }

//    public void update() {
//
//    }

    public void render(Graphics g) {
        g.setColor(Color.orange);
        g.fillOval(x,y, width, height);
    }
}
