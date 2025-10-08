package com.euhedral.Game;

import com.euhedral.Engine.GameState;
import com.euhedral.Engine.UI.Panel;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.UI.MessageBox;

import java.awt.*;

public class Tutorial {

    private MessageBox messageBox;

    private static boolean moved;
    private static boolean shot;
    private static boolean special;
    private Panel panelMoved, panelShot, panelSpecial;

    private static boolean active;

    private static int movedTimer;
    private static int shotTimer;
    private static int specialTimer;

    int margin = 20;
    int width = 126;
    int heightMoved = 72;
    int textHeight = 22;

    int leftX = VariableHandler.deadzoneLeftX + 2*64 + margin;
    int topYMoved = 600;
    int bottomYMoved = topYMoved + 50;

    int topYShot = topYMoved + heightMoved + 78;
    int heightShot = 23;

    int topYSpecial = topYShot + heightMoved + 28;
    int heightSpecial = 23;

    float mult = 60 * 1.5f;

    public Tutorial() {

        panelMoved = new Panel(
                leftX - margin,
                topYMoved - textHeight - margin,
                width + 2* margin,
                heightMoved + 2* margin,
                GameState.Game);
        panelMoved.setBackColor(Color.BLACK);

        panelShot = new Panel(
                leftX - margin,
                topYShot - textHeight - margin,
                width + 2* margin,
                heightShot + 2* margin,
                GameState.Game);
        panelShot.setBackColor(Color.BLACK);

        panelSpecial = new Panel(
                leftX - margin,
                topYSpecial - textHeight - margin,
                width + 2* margin,
                heightSpecial + 2* margin,
                GameState.Game);
        panelSpecial.setBackColor(Color.BLACK);

//        messageBox = new MessageBox(230,200, 800, 550);
//        messageBox.addText("Use WASD or the Arrow Keys to move.");
//        messageBox.addText("");
//        messageBox.addText("        SPACEBAR to shoot.");
//        messageBox.addText("");
//        messageBox.addText("        ESCAPE or P to pause.");
//        messageBox.addText("");
//        messageBox.addText("        CTRL to use Special Move.");
//        messageBox.addText("");
//        messageBox.addText("You can disable tutorial in Settings.");
//        messageBox.addText("");
//        messageBox.addText("Shoot as many enemies as you can.");
//        messageBox.addText("");
//        messageBox.addText("Survive!");
//        messageBox.addText("");
//        messageBox.setFontSize(Utility.intAtWidth640(10));
//
//        messageBox.setTransparency(0.8f);

        reset();
    }

    public static void reset() {
        moved = false;
        shot = false;
        special = false;

        active = true;

        shotTimer = 60;
        movedTimer = 60;
        specialTimer = 60;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        renderMovement(g2d);
        renderShot(g2d);
        renderSpecial(g2d);
    }


    private void renderMovement(Graphics2D g) {
        if (movedTimer > 0)
            panelMoved.setTransparency( movedTimer / mult);
        panelMoved.render(g);

        renderTutorialPrompt(g, moved, movedTimer, "W", leftX + 50, topYMoved);
        renderTutorialPrompt(g, moved, movedTimer, "A", leftX, bottomYMoved);
        renderTutorialPrompt(g, moved, movedTimer, "S", leftX + 50, bottomYMoved);
        renderTutorialPrompt(g, moved, movedTimer, "D", leftX + 100, bottomYMoved);
    }

    private void renderShot(Graphics2D g) {
        if (shotTimer > 0)
            panelShot.setTransparency(shotTimer / mult);
        panelShot.render(g);

        renderTutorialPrompt(g, shot, shotTimer, "SPACE", leftX, bottomYMoved + 100);
    }

    private void renderSpecial(Graphics2D g) {
        if (specialTimer > 0)
            panelSpecial.setTransparency(specialTimer / mult);
        panelSpecial.render(g);

        renderTutorialPrompt(g, special, specialTimer, "CTRL", leftX, bottomYMoved + 200);
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

    public static void setMoved(boolean moved) {
        Tutorial.moved = moved;
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

        if (moved)
            movedTimer--;

        if (special)
            specialTimer--;

        active = !(moved  && shot && special);
    }
}
