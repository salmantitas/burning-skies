package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.*;
import com.euhedral.game.Entities.Enemy.Enemy;

import java.awt.*;

public class BulletPlayer extends Bullet{

    public BulletPlayer(int x, int y, double angle) {
        super(x, y, angle);
//        this.contactId = contactId;
        width = Utility.intAtWidth640(3); //6
        height = width * 3; // 18
        setImage(textureHandler.bulletPlayer[0]);
//        impactColor = Color.GREEN;
        commonInit();

//        impact = new Animation(10, GameController.getTexture().impactSmall[0],
//                GameController.getTexture().impactSmall[1],
//                GameController.getTexture().impactSmall[2],
//                GameController.getTexture().impactSmall[3]
//        );
    }

    @Override
    public void update() {
        if (state == STATE_ACTIVE) {
            super.update();
        } else if (state == STATE_IMPACT) {
            impact.runAnimation();
//            impactTimer++;
//            Utility.log(state + " " + impactTimer);
            this.velX = entity.getVelX();
            this.velY = entity.getVelY();
            super.update();
        }
    }

//    @Override
//    protected void drawDefault(Graphics g) {
//        if (state == STATE_ACTIVE) {
//            drawImage(g, image, width, height);
////            g.setColor(color);
////            g.fillOval((int) x, (int) y, width, height);
//        } else if (state == STATE_IMPACT && entity.isActive()) {
////            g.setColor(impactColor);
////            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);
//
//            impact.drawAnimation(g, (int) x, (int) y, impactSize, impactSize);
//        }
//    }

//    public ContactID getContactId() {
//        return contactId;
//    }

    @Override
    public void resurrect(double x, double y) {
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
