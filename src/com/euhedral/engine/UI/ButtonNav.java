package com.euhedral.engine.UI;/*
 * Do not modify
 * */

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;

import java.awt.*;

public class ButtonNav extends Button {
    private GameState targetState;

    public ButtonNav(int x, int y, int size, String text, GameState targetState) {
        super(x, y, size, text);
        this.targetState = targetState;

        activate = () -> { Engine.setState(targetState);};
    }

    public void setTargetState(GameState targetSate) {
        this.targetState = targetSate;
    }

    public GameState getTargetState() {
        return targetState;
    }

    @Override
    public void activate() {
        super.activate();
//        activate.run();
    }
}
