package com.euhedral.Game.Entities;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Camera;
//import com.euhedral.game.ContactID;
import com.euhedral.Game.GameController;

import java.awt.*;

public class Flag {

    protected int x, y;
//    protected ContactID contactId;
    protected float velX, velY;
    protected int width, height;
    protected Color color;
    protected Camera cam;

    public Flag(int x, int y) {
        this.x = x;
        this.y = y;
        width = Utility.intAtWidth640(32);
        height = width;
        velY = Utility.floatAtWidth640(2)/2;
        color = Color.YELLOW;
//        this.contactId = contactId;
        cam = GameController.getCamera();
    }

    public void update() {
        move();

//        System.out.println("com.euhedral.game.Entities.Flag at (" + x + ", " + y + ")");
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.drawRect(x,y,width,height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY =  velY;
    }

//    public ContactID getID() {
//        return contactId;
//    }

    // Private Methods

    private void move() {
        y += velY;
    }

    public void reset() {
        x = Engine.WIDTH/2;
        y = -Engine.HEIGHT/2;
    }

}
