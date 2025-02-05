package com.euhedral.engine.UI;/*
 * Do not modify
 * */

@FunctionalInterface
interface Function{
    void apply();
}

public class ButtonFunction extends Button{
    private Function func;

    public ButtonFunction(int x, int y, int width, int height, String text, Function func) {
        super(x, y, width, height, text);
        this.func = func;
    }

    public ButtonFunction(int x, int y, int size, String text, Function func) {
        super(x, y, size, text);
        this.func = func;
    }

    @Override
    public Object activate() {
        super.activate();
        // todo: Cause the action to be updated to UIHandler
        runFunction();
        return null;
    }

    public void runFunction() {
        func.apply();
    }
}
