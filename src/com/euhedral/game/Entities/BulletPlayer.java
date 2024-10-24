package com.euhedral.game.Entities;

import com.euhedral.engine.*;
import com.euhedral.game.ContactID;
import com.euhedral.game.Entities.Enemy.Enemy;
import com.euhedral.game.EntityID;
import com.euhedral.game.GameController;
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

    @Override
    protected void drawDefault(Graphics g) {
        if (state == STATE_ACTIVE) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, width, height);
        } else if (state == STATE_IMPACT && entity.isActive()) {
//            g.setColor(impactColor);
//            g.fillOval((int) x - impactFactor, (int) y - impactFactor, width + impactFactor*2, height + impactFactor*2);

            impact.drawAnimation(g, (int) x, (int) y, 32, 32);
        }
    }

    @Override
    public void renderReflection(Graphics2D g2d, float transparency) {
        g2d.setComposite(Utility.makeTransparent(transparency));
        double sizeOffset = 0.9;
        int xCorrection = 8;
        int yCorrection = 12;
        int offsetX = (int) (Engine.WIDTH / 2 - getCenterX()) / 15;
        int offsetY = (int) (Engine.HEIGHT/2 - getCenterY()) / 15;
        int reflectionX = xCorrection + (int) x - offsetX;
        int reflectionY = yCorrection + (int) y + offsetY;

        if (state == STATE_ACTIVE) {
            g2d.setColor(color);
            g2d.fillOval(reflectionX, reflectionY, (int) (width*sizeOffset) ,  (int) (height*sizeOffset));
        } else if (state == STATE_IMPACT) {
//            g2d.setColor(impactColor);
//            int impactX = (int) x - impactFactor;
//            int impactY = (int) y - impactFactor;
//            int impactWidth = width + impactFactor*2;
//            int impactHeight = height + impactFactor*2;
//            g2d.fillOval(xCorrection + impactX - offsetX, impactY + offsetY, (int) (impactWidth*sizeOffset), (int) (impactHeight*sizeOffset));

            int impactX = (int) x + xCorrection - offsetX;
            int impactY = (int) y + yCorrection + offsetY;
            int impactWidth = (int) ((double )height*sizeOffset);
            int impactHeight = impactWidth;
            impact.drawAnimation(g2d, impactX, impactY, impactWidth, impactHeight);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
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
