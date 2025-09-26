package com.euhedral.engine.UI;/*
 * Do not modify
 * */

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;

import java.awt.*;

public class ButtonNav extends Button {

    public ButtonNav(int x, int y, int size, String text, GameState targetState) {
        super(x, y, size, text);

        activate = () -> { Engine.setState(targetState);};
    }
}
