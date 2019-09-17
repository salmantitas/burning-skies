package com.euhedral.game.Entities;

import com.euhedral.game.ContactID;

import java.awt.*;

public class BulletPlayerGround extends BulletPlayer{

    BulletPlayerGround(int x, int y, double dir) {
        super(x, y, ContactID.Ground, dir);
        color = Color.GREEN;
    }
}
