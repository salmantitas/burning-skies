package com.euhedral.game.Entities;

import com.euhedral.engine.Entity;
import com.euhedral.engine.MobileEntity;
import com.euhedral.engine.Utility;
import com.euhedral.game.ContactID;
import com.euhedral.game.Entities.Enemy.Enemy;
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
        impactColor = Color.GREEN;
        commonInit();
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
        } else if (state == STATE_IMPACT) {
            impactTimer++;
//            Utility.log(state + " " + impactTimer);
            this.velX = entity.getVelX();
            this.velY = entity.getVelY();
            super.update();
        }
    }

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);
            g.fillOval(x, y, width, height);
        } else if (state == STATE_IMPACT && entity.isActive()) {
            g.setColor(impactColor);
            g.fillOval(x - impactFactor, y - impactFactor, width + impactFactor*2, height + impactFactor*2);
        }
    }

    public ContactID getContactId() {
        return contactId;
    }

    @Override
    public void resurrect(int x, int y) {
//        this.x = x;
//        this.y = y;
        this.calculated = false;
        super.resurrect(x, y);
        commonInit();
    }

    private void commonInit() {
        state = STATE_ACTIVE;
        SoundHandler.playSound(initSound);
    }

    @Override
    public void disable() {
        state = STATE_INACTIVE;
    }
}
