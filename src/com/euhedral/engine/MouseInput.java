package com.euhedral.engine;/*
 * Do not modify this class
 * */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.euhedral.game.GameController;
//import com.euhedral.game.MouseInput;

public class MouseInput extends MouseAdapter {
    private GameController gameController;

    private int mxMove, myMove;
    private int mxDrag, myDrag;
    private int mxPressed, myPressed;
    private int mxReleased, myReleased;
    private int buttonPressed, buttonReleased;

    public MouseInput(GameController gameController) {
        this.gameController = gameController;
    }

    public void mousePressed(MouseEvent e) {
        mxPressed = e.getX();
        myPressed = e.getY();
        buttonPressed = e.getButton();
        gameController.mousePressed(buttonPressed);

//        System.out.println("Mouse Pressed at (" + mxPressed + ", " + myPressed + ")");
        }

    public void mouseReleased(MouseEvent e) {
        mxReleased = e.getX();
        myReleased = e.getY();
        buttonReleased = e.getButton();
        gameController.mouseReleased(buttonReleased);
        gameController.checkButtonAction(mxReleased, myReleased);

//        System.out.println("Mouse Released at (" + mxReleased + ", " + myReleased + ")");
    }

    public void mouseMoved(MouseEvent e) {
        mxMove = e.getX();
        myMove = e.getY();
        gameController.mouseMoved(mxMove, myMove);

//        System.out.println("Mouse moved at (" + mxMove + ", " + myMove + ")");
    }

    public void mouseDragged(MouseEvent e) {
        mxDrag = e.getX();
        myDrag = e.getY();
        gameController.mouseDragged(mxDrag, myDrag);

//        System.out.println("Mouse dragged at (" + mxMove + ", " + myMove + ")");
    }
}
