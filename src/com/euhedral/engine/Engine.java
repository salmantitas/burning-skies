package com.euhedral.engine;/*
* Do not modify
* */

import java.awt.*;
import java.awt.image.BufferStrategy;
import com.euhedral.game.GameController;

public class Engine extends Canvas implements Runnable{

    /*
     * By Default:
     * VERSION = x.x
     * TITLE = "Euhedral Engine x.x"
     * SCREEN_RATIO = 4.0/3.0
     * WIDTH = 640
     * HEIGHT = 480
     * BACKGROUND_COLOR = Color.BLACK
     */

    public static double VERSION = 0.245;
    public static String TITLE = "Simian Framework";
    public static double SCREEN_RATIO = 4.0/3.0;
    public static int WIDTH = 640;
    public static int HEIGHT = (int) (WIDTH / SCREEN_RATIO);
    public static double SCALE = 1;
    public static Color BACKGROUND_COLOR = Color.BLACK;
    private static Window window;

    private final double UPDATE_CAP = 1.0/60.0; // determines the frequency of game-updates. 1/60 means once every 60 seconds
    private static boolean running = false;
    public static int timeInSeconds = 0;
    public static int timer = 0;

    private static int fps = 0;

    public static GameController gameController;
    public static BufferedImageLoader loader;

    // todo: change to private
    public static GameState currentState = GameState.Game;
    public static GameState previousState;

    public EngineKeyboard keyInput;
    public EngineMouse mouseInput;

    /*
     * Initializes variables:
     *   gameController
     *   keyInput
     *   mouseInput
     * Adds the input methods to the appropriate listeners
     * Creates the window
     */
    public Engine() {
        loader = new BufferedImageLoader();
        gameController = new GameController();

        keyInput = new EngineKeyboard(gameController);
        mouseInput = new EngineMouse(gameController);

        addKeyListener(keyInput);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        addMouseWheelListener(mouseInput);
        System.out.println("Game initialized");
        window = new Window(WIDTH, HEIGHT, TITLE, this);
    }

    public void start() {
        if (running) return;
        running = true;
        new Thread(this, "EngineMain-Thread").start();
    }

    public static void stop() {
        if (!running) return;
        running = false;
    }

    @Override
    public void run() {
        requestFocus();
        double target = 60.0;
        double millisecondsPerCycle =  1000000.0;
        double nanosecondsPerCycle =  1000000000.0;
        double drawInterval = nanosecondsPerCycle/target;
        double nextDrawTime = System.nanoTime() + drawInterval;
        long timer = System.currentTimeMillis();
        int tps = 0;

        // Sleep Based Game Loop
        // todo: Replace with timestep based loop
        while (running) {
            update();
            tps++;
            render();
            fps++;

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/millisecondsPerCycle;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (System.currentTimeMillis() - 1000 > timer) {
                timer += 1000;
                timeInSeconds++;
                System.out.printf("FPS: %d | TPS: %d | Timer: %d\n", fps, tps, timeInSeconds);
                fps = 0;
                tps = 0;
            }

        }

        System.exit(0);
    }

    /*
     * Called every frame to update the game behavior.
     */
    public void update() {
        gameController.update();
    }

    /*
     * Called every frame to render graphics on the window.
     * Pre-renders images using triple buffers and renders them on-screen
     * Fills the screen with the background color and calls the gameController's render function
     * */
    public void render() {
        BufferStrategy bs = getBufferStrategy(); // BufferStrategy loads the upcoming frames in memory (prerenders them)
        if (bs == null) {
            createBufferStrategy(3); // pre-renders three frames
            return;
        }

        Graphics g = bs.getDrawGraphics(); //

        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        Graphics2D g2d = (Graphics2D) g;

        g2d.scale(SCALE, SCALE);
        gameController.render(g);

        g.dispose();
        bs.show(); //
    }

    public static void main(String[] args) {
        System.out.println(TITLE + " " + VERSION + " Started");
        new Engine();
    }

    /***********************************************
     * Utility functions To Adjust Game Properties *
     ***********************************************/

    /*
     * Manually sets the width to the given value and updates the height according to the
     * aspect ratio
     * */
    public static void setWIDTH(int width) {
        WIDTH = width;
        updateHeight();
    }

    /*
     * Manually updates the aspect ratio of the game using a double
     * */
    // HEIGHT = WIDTH / SCREEN_RATIO, that is WIDTH * numerator / denominator
    public static void setRatio(double ratio) {
        SCREEN_RATIO = ratio;
        updateHeight();
    }

    /*
     * Manually updates the aspect ratio of the game using the numerator and denominator
     * */
    // HEIGHT = WIDTH / SCREEN_RATIO, that is WIDTH * numerator / denominator
    public static void setRatio(double denominator, double numerator) {
        SCREEN_RATIO = (1.0 * denominator) / (1.0 * numerator);
        updateHeight();
    }

    /*
     * Updates the width using the aspect ratio and the height
     * */
    private static void updateWidth() {
        WIDTH = (int) (HEIGHT * SCREEN_RATIO);
    }

    /*
     * Updates the height using the aspect ratio and the height
     * */
    private static void updateHeight() {
        HEIGHT = (int) (WIDTH / SCREEN_RATIO);
    }

    /*
     * Sets the title of the window
     * */
    public static void setTITLE(String title) {
        TITLE = title;
    }

    /*
     * Updates the background color of the window to the given color
     * */
    public static void setBACKGROUND_COLOR(Color color) {
        BACKGROUND_COLOR = color;
    }

    /*
     * Updates the background color of the window using the rgb values
     * */
    public static void setBACKGROUND_COLOR(int red, int green, int blue) {
        BACKGROUND_COLOR = new Color(red, green, blue);
    }

    public static void printTimer() {
        System.out.println(timer);
    }

    /***********************
     * GameState Functions *
     ***********************/

    /*
     * Notify UI Handler about the change of state
     * */
    private static void notifyUIHandler() {
        if (gameController != null)
            gameController.notifyUIHandler(currentState);
    }

    /*
     * Manually set the GameState
     * */
    public static void setState(GameState state) {
        previousState = currentState;
        currentState = state;
        notifyUIHandler();
    }

    /*
     * Set the state to Game
     * */
    public static void gameState() {
        setState(GameState.Game);
    }

    /*
     * Set the state to Transition
     * */
    public static void transitionState() {
        setState(GameState.Transition);
    }

    /*
     * Set the state to Menu
     * */
    public static void menuState() {
        setState(GameState.Menu);
    }

    /*
     * Set the state to GameOver
     * */
    public static void gameOverState() {
        setState(GameState.GameOver);
    }

    /*
     * Set the state to Pause
     * */
    public static void pauseState() {
        setState(GameState.Pause);
    }

    public static boolean stateIs(GameState state) {
        return currentState == state;
    }

    public static int getFPS() {
        return 0; // stub
    }
}
