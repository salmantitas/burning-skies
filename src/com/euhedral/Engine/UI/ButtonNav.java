package com.euhedral.Engine.UI;/*
 * Do not modify
 * */

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;

public class ButtonNav extends Button {

    public ButtonNav(int x, int y, int size, String text, GameState targetState) {
        super(x, y, size, text);

        activate = () -> { Engine.setState(targetState);};
    }
}
