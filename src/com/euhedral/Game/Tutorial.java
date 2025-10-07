package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;
import com.euhedral.Engine.UI.Panel;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.UI.MessageBox;

import java.awt.*;

public class Tutorial {

    private MessageBox messageBox;

    private Panel panel;
    private static boolean movedLeft, movedRight, movedUp, movedDown;
    private static boolean shot;
    private static boolean special;

    private static boolean active;

    private static int panelTimer;
    private static int movedLeftTimer, movedRightTimer, movedUpTimer, movedDownTimer;
    private static int shotTimer;
    private static int specialTimer;

    int margin = 20;
    int width = 126;
    int height = 272;
    int textHeight = 22;

    int leftX = VariableHandler.deadzoneLeftX + 2*64 + margin;
    int topY= 600;
    int bottomY = topY + 50;

    public Tutorial() {

        panel = new Panel(
                leftX - margin,
                topY - textHeight - margin,
                width + 2* margin,
                height + 2* margin,
                GameState.Game);
        panel.setTransparency(0.4f);

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

        active = true;

        shotTimer = 60;
        movedDownTimer = 60;
        movedLeftTimer = 60;
        movedUpTimer = 60;
        movedRightTimer = 60;
        specialTimer = 60;
        panelTimer = 60;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        renderPanel(g);
        renderMovement(g2d);
        renderShot(g2d);
        renderSpeial(g2d);
    }

    private void renderPanel(Graphics g) {
        if (panelTimer > 0)
            panel.setTransparency(panelTimer / 120f);
        panel.render(g);
    }

    private void renderMovement(Graphics2D g) {
        renderTutorialPrompt(g, movedUp, movedUpTimer, "W", leftX + 50, topY);
        renderTutorialPrompt(g, movedLeft, movedLeftTimer, "A", leftX, bottomY);
        renderTutorialPrompt(g, movedDown, movedDownTimer, "S", leftX + 50, bottomY);
        renderTutorialPrompt(g, movedRight, movedRightTimer, "D", leftX + 100, bottomY);
    }

    private void renderShot(Graphics2D g) {
        renderTutorialPrompt(g, shot, shotTimer, "SPACE", leftX, bottomY + 100);
    }

    private void renderSpeial(Graphics2D g) {
        renderTutorialPrompt(g, special, specialTimer, "CTRL", leftX, bottomY + 200);
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

        if (movedRight)
            movedRightTimer--;

        if (movedLeft)
            movedLeftTimer--;

        if (movedUp)
            movedUpTimer--;

        if (movedDown)
            movedDownTimer--;

        if (special)
            specialTimer--;

        if (!active) {
            panelTimer--;
        }

        active = !(movedLeft && movedRight && movedUp && movedDown && shot && special);g
    }
}
