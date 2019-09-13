package com.euhedral.engine;/*
 * Do not modify
 * */

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.game.ActionTag;

import java.awt.*;
import java.util.LinkedList;

public class ButtonAction extends Button{
    private ActionTag action;

    public ButtonAction(int x, int y, int size, String text, GameState renderState, ActionTag action) {
        super(x, y, size, text, renderState);
        this.action = action;
    }

    public ActionTag getAction() {
        return action;
    }
}
