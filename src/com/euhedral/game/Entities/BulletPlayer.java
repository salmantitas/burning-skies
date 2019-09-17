package com.euhedral.game.Entities;

import com.euhedral.game.ContactID;

import java.awt.*;

public class BulletPlayer extends Bullet{

    protected ContactID contactId;

    BulletPlayer(int x, int y, ContactID contactId, double angle) {
        super(x, y, angle);
        this.contactId = contactId;
        width = 5;
        height = width * 3;
        setVel(12);
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
}
