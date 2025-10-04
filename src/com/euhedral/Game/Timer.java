package com.euhedral.Game;

public class Timer {
    private int timeLeft;
    private int startTime;
    private boolean running;

    public Timer(int startTime) {
        running = false;
        this.startTime = startTime;
        timeLeft = 0;
    }

    public void update() {
        if (running) {
            timeLeft--;
        }
    }

    public void start() {
        running = true;
        timeLeft = startTime;

    }

    public void stop() {
        running = false;
        timeLeft = 0;
    }

    public float getProgress() {
        return 1f * timeLeft / startTime;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
