package com.euhedral.engine;/*
 * Do not modify
 * */

import com.euhedral.game.ActionTag;

public class ButtonAction extends Button{
    private ActionTag action;

    public ButtonAction(int x, int y, int size, String text, GameState renderState, ActionTag action) {
        super(x, y, size, text, renderState);
        this.action = action;
    }

    public ActionTag getAction() {
        return action;
    }

    @Override
    public void activate() {
        super.activate();
        // todo: Cause the action to be updated to UIHandler
    }
}
