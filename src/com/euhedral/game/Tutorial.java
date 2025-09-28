package com.euhedral.game;

import com.euhedral.engine.Utility;
import com.euhedral.game.UI.MessageBox;

import java.awt.*;

public class Tutorial {

    private MessageBox messageBox;
    private static boolean movedLeft, movedRight, movedUp, movedDown;
    private static boolean shot;
    private static boolean special;

    private static int shotTimer;
    private static int specialTimer;

    int topY=200;
    int bottomY = topY + 50;
    int leftX = 70;

    public Tutorial() {
        messageBox = new MessageBox(230,200, 800, 550);
        messageBox.addText("Use WASD or the Arrow Keys to move.");
        messageBox.addText("");
        messageBox.addText("        SPACEBAR to shoot.");
        messageBox.addText("");
        messageBox.addText("        ESCAPE or P to pause.");
        messageBox.addText("");
        messageBox.addText("        CTRL to use Special Move.");
        messageBox.addText("");
        messageBox.addText("You can disable tutorial in Settings.");
        messageBox.addText("");
        messageBox.addText("Shoot as many enemies as you can.");
        messageBox.addText("");
        messageBox.addText("Survive!");
        messageBox.addText("");
        messageBox.setFontSize(Utility.intAtWidth640(10));

        messageBox.setTransparency(0.8f);

        reset();
    }

    public static void reset() {
        movedLeft = false;
        movedDown = false;
        movedRight = false;
        movedUp = false;
        shot = false;
        special = false;

        shotTimer = 60;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        renderMovement(g2d);
        renderShot(g2d);

//        g.drawString("CTRL", leftX, bottomY + 200);
    }

    private void renderMovement(Graphics2D g) {
        renderTutorialPrompt(g, movedUp, 60, "W", leftX + 50, topY);
        renderTutorialPrompt(g, movedLeft, 60, "A", leftX, bottomY);
        renderTutorialPrompt(g, movedDown, 60, "S", leftX + 50, bottomY);
        renderTutorialPrompt(g, movedRight, 60, "D", leftX + 100, bottomY);
    }

    private void renderShot(Graphics2D g) {
        renderTutorialPrompt(g, shot, shotTimer, "SPACE", leftX, bottomY + 100);

    }

    private void renderTutorialPrompt(Graphics2D g, boolean variable, int timer, String button, int x, int y) {
        if (timer > 0) {
            Color color = variable ? Color.green : Color.red;
            g.setColor(color);

            g.setComposite(Utility.makeTransparent(timer / 60f));
            g.drawString(button, x, y);
            g.setComposite(Utility.makeTransparent(1f));
        }
    }

    public void setTutorial(MessageBox tutorial) {
        this.messageBox = tutorial;
    }

    public static void setMovedLeft(boolean movedLeft) {
        Tutorial.movedLeft = movedLeft;
    }

    public static void setMovedRight(boolean movedRight) {
        Tutorial.movedRight = movedRight;
    }

    public static void setMovedUp(boolean movedUp) {
        Tutorial.movedUp = movedUp;
    }

    public static void setMovedDown(boolean movedDown) {
        Tutorial.movedDown = movedDown;
    }

    public static void setShot(boolean shot) {
        Tutorial.shot = shot;
    }

    public static void setSpecial(boolean special) {
        Tutorial.special = special;
    }

    public void update() {
        if (shot)
            shotTimer--;
    }
}
