package com.euhedral.engine;/*
 * Do not modify this class
 * */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.euhedral.game.GameController;
import com.euhedral.game.MouseInput;

public class EngineMouse extends MouseAdapter {
//    private GameController gameController;
    private MouseInput mouseInput;

    private int mxMove, myMove;
    private int mxDrag, myDrag;
    private int mxPressed, myPressed;
    private int mxReleased, myReleased;
    private int buttonPressed, buttonReleased;

    public EngineMouse(GameController gameController) {
//        this.gameController = gameController;
        mouseInput = new MouseInput(this, gameController);
    }

    public void mousePressed(MouseEvent e) {
        mxPressed = e.getX();
        myPressed = e.getY();
        buttonPressed = e.getButton();

        mouseInput.updatePressed();

//        System.out.println("Mouse Pressed at (" + mxPressed + ", " + myPressed + ")");
        }

    public void mouseReleased(MouseEvent e) {
        mxReleased = e.getX();
        myReleased = e.getY();
        buttonReleased = e.getButton();

        mouseInput.updateReleased();

//        System.out.println("Mouse Released at (" + mxReleased + ", " + myReleased + ")");
    }

    public void mouseMoved(MouseEvent e) {
        mxMove = e.getX();
        myMove = e.getY();
        mouseInput.updateMoved();

//        System.out.println("Mouse moved at (" + mxMove + ", " + myMove + ")");
    }

    public void mouseDragged(MouseEvent e) {
        mxDrag = e.getX();
        myDrag = e.getY();
        mouseInput.updateDragged();

//        System.out.println("Mouse dragged at (" + mxMove + ", " + myMove + ")");
    }

    public int getMxMove() {
        return mxMove;
    }

    public int getMyMove() {
        return myMove;
    }

    public int getMxDrag() {
        return mxDrag;
    }

    public int getMyDrag() {
        return myDrag;
    }

    public int getMxPressed() {
        return mxPressed;
    }

    public int getMyPressed() {
        return myPressed;
    }

    public int getMxReleased() {
        return mxReleased;
    }

    public int getMyReleased() {
        return myReleased;
    }

    public int getButtonPressed() {
        return buttonPressed;
    }

    public int getButtonReleased() {
        return buttonReleased;
    }

    public boolean mxIsPressed(int mx) {
        return mx == mxPressed;
    }

    public boolean myIsPressed(int my) {
        return my == myPressed;
    }

    public boolean mxIsReleased(int mx) {
        return mx == mxReleased;
    }

    public boolean myIsReleased(int my) {
        return my == myReleased;
    }

    public boolean buttonIsPressed(int button) {
        return buttonPressed == button;
    }

    public boolean buttonIsReleased(int button) {
        return buttonReleased == button;
    }
}
