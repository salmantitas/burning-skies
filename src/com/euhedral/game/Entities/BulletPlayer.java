package com.euhedral.game.Entities;

import com.euhedral.game.ContactID;
import com.euhedral.game.EntityID;
import com.euhedral.game.SoundHandler;

import java.awt.*;

public class BulletPlayer extends Bullet{

    protected ContactID contactId;

    BulletPlayer(int x, int y, ContactID contactId, double angle) {
        super(x, y, angle);
        this.contactId = contactId;
        width = 5;
        height = width * 3;
        setVel(12);
        SoundHandler.playSound(SoundHandler.BULLET_PLAYER);
    }

//    public void update() {
//
//    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillOval(x,y, width, height);
    }

    public ContactID getContactId() {
        return contactId;
    }

    @Override
    public void resurrect(int x, int y) {
//        this.x = x;
//        this.y = y;
//        this.calculated = false;
        super.resurrect(x, y);
        SoundHandler.playSound(SoundHandler.BULLET_PLAYER);
    }
}
