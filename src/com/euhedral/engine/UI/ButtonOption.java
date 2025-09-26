package com.euhedral.engine.UI;/*
 * Do not modify
 * */

import com.euhedral.game.SoundHandler;

import java.awt.*;

public class ButtonOption extends Button {
    private Button increase, decrease;

    public ButtonOption(int x, int y, int size, String text) {
        super(x, y, size, text);
        increase = new Button(x + width + 2* size,y, size, ">");
        decrease = new Button(x - (int) (2.5 * size),y, size, "<");
    }

    @Override
    public void increaseOption() {
        if (increase != null)
            SoundHandler.playSound(SoundHandler.UI2);
//        return increase.getAction();
    }

    @Override
    public void decreaseOption() {
        if (decrease != null)
            SoundHandler.playSound(SoundHandler.UI2);
//        return decrease.getAction();
    }

//    public void setIncreaseAction(ActionTag increaseAction) {
//            this.increase.setAction(increaseAction);
//    }
//
//    public void setDecreaseAction(ActionTag decreaseAction) {
//            this.decrease.setAction(decreaseAction);
//    }

    public void setIncreaseActivate(Runnable runnable) {
        increase.setActivate(runnable);
    }

    public void setDecreaseActivate(Runnable runnable) {
        decrease.setActivate(runnable);
    }

    @Override
    public void enable() {
        enabled = true;
        increase.enable();
        decrease.enable();
    }

    @Override
    public void disable() {
        enabled = false;
        increase.disable();
        decrease.disable();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        int newX = x + width + size;

        // todo: put inside font calculation function
        increase.setX(newX);

        if (enabled) {
            increase.render(g);
            decrease.render(g);
        }

//        g2d = (Graphics2D) g;
//
//        // make it transparent whether the button is enabled or disabled
//
//        if (transparency < 1) {
//            g2d.setComposite(Utility.makeTransparent(transparency));
//        }
//
//        g.setFont(font);
//
//        stringWidth = (g.getFontMetrics().stringWidth(text));
//
//        if (!fontSizeCalculated) {
//            // Adjusts the width and height of the button to fit the text
//            width = Utility.perc(stringWidth, 110);;
//
//            if (width < height)
//                width = height;
//            fontSizeCalculated = true;
//        }
//
//        if (!enabled) {
//            g.setColor(disabledColor);
//            g.fill3DRect(x,y,width,height, true);
//
//            g.setColor(disabledTextColor);
//            g.drawString(text, x + Utility.perc(width, 5), y + Utility.perc(height, 75));
//        } else {
//            g.setColor(backColor);
//            if (fill)
//                g.fill3DRect(x,y,width,height, true);
//            else if (outline) {
//                g.setColor(Color.BLACK);
//                g.drawRect(x, y, width, height);
//            }
//
//            if (selected) {
//                g.setColor(selectedColor);
//            }
//            else g.setColor(textColor);
////            int stringX = x + Utility.perc(width, 5);
//            stringXSelected = x + (width/2 - stringWidth/2);
//            g.drawString(text, stringXSelected, y + Utility.perc(height, 75));
//        }
//
//        if (transparency < 1) {
//            g2d.setComposite(Utility.makeTransparent(1));
//        }
    }

//    @Override
//    public boolean mouseOverlap(int mx, int my) {
//        boolean value =
//                increase.mouseOverlap(mx, my);
//        if (value)
//            return value;
//        else {
//            value = decrease.mouseOverlap(mx, my);
//            if (value)
//                return value;
//            else
//                return super.mouseOverlap(mx, my);
//        }
//    }

    @Override
    public void select() {
        super.select();
        increase.select();
        decrease.select();
    }

    @Override
    public void deselect() {
        super.deselect();
        increase.deselect();
        decrease.deselect();
    }

    @Override
    public void activateIncrease() {
        increase.activate();
    }

    @Override
    public void activateDecrease() {
        decrease.activate();
    }
}