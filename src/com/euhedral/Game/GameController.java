package com.euhedral.Game;

import com.euhedral.Engine.*;
import com.euhedral.Game.EnemyGeneration.EnemyGenerator;
import com.euhedral.Game.Entities.Shop;
import com.euhedral.Game.UI.UIHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

// Manages the game itself, and passes instructions to all other classes under it
public class GameController {

    /********************************************
     * Window Settings - Manually Configurable *
     *******************************************/

    private String gameTitle = "BURNING SKIES";
    public static String gameVersion = "0.7.54";
    private int gameWidth = 1280;
    private double gameRatio = 4 / 3;
    private int gameHeight = Engine.HEIGHT;
    private double gameScale = Engine.SCALE;
    private Color gameBackground = new Color(0,0,128);

    private Renderer renderer;

    private HashMap<GameState, State> stateMachine;
    private State currentState;

    long timer = System.currentTimeMillis();
    public static long timeInSeconds;

    // Management
    private UIHandler uiHandler;
    private static VariableHandler variableHandler;
    private EntityHandler entityHandler;
    private static TextureHandler textureHandler;
    private SoundHandler soundHandler;

    boolean validCameraRenderState;

    public static int screenShake = 0;
    public static int screenShake_MULT = 1;

    // Mouse
    double mxD;
    double myD;

    // Console todo
    public Scanner scanner;
    public static String cmd;

    // Camera
    public static Camera camera;
    //    int offsetHorizontal;
    int offsetVertical;
    float camX, camY;
    int inScreenMarker;

    // Enemy Generation
    private EnemyGenerator enemyGenerator;

    // Levels
    private int levelHeight;
    private static boolean levelLoaded = false; // levels will only loaded when this is true
    boolean reset = true;
    boolean levelEndCondition;
    BufferedImage level;

    // Cheats
    public static boolean godMode = false;

    /************
     * Controls *
     ************/
    public static String UP = "W", DOWN = "S", LEFT = "A", RIGHT = "D", SHOOT = "SPACE",
            SPECIAL = "CTRL",
            PAUSE = "P";

    /******************
     * User variables *
     ******************/

    // Objects
    private Shop shop; // todo: Keep or delete?

    // LevelMap to automate level loading
    private HashMap<Integer, BufferedImage> levelMap;

    private boolean levelSpawned = false;

    // Keyboard Options
    public static boolean rebinding = false;
    private boolean keyboardControl = true; // false means mouse Control

    /************
     * Graphics *
     ************/

    private static Background background;

    public GameController() {

        /******************
         * Window Setting *
         ******************/
        Engine.setTITLE(gameTitle);
//        Engine.setRatio(gameRatio);
        Engine.setWIDTH(gameWidth);
        Engine.setBACKGROUND_COLOR(gameBackground);
        gameHeight = Engine.HEIGHT;
//        Engine.setState(GameState.Test);
        Engine.menuState();

        initializeSettings();
        initializeSound();
        initializeGraphics();
        initializeAnimations();
        uiHandler = new UIHandler();
        initializeGame();
        initializeLevel();
    }

    /*************************
     * Initializer Functions *
     *************************/

    private void initializeSettings() {
        variableHandler = new VariableHandler();

        try {
            SaveLoad.loadSettings();
        } catch (Exception e) {
            Utility.log("Exception");
        }
    }

    private void initializeGraphics() {
        renderer = new Renderer();
        textureHandler = new TextureHandler();

        background = new Background();
        /*************
         * Game Code *
         *************/

    }

    private void initializeGame() {
        /*************
         * Game Code *
         *************/

        stateMachine = new HashMap<>();

        State statePlay = new StatePlay();
        State state = new State();

        stateMachine.put(GameState.Game, statePlay);
        stateMachine.put(GameState.Menu, state);

        currentState = state;

        entityHandler = new EntityHandler();
        shop = new Shop();
        scanner = new Scanner(System.in);
    }

    // Initializes SoundHandler Class and loads the volume settings to allow background music to play
    private void initializeSound() {
        soundHandler = new SoundHandler();
        /*************
         * Game Code *
         *************/
    }

    private void initializeAnimations() {
        /*************
         * Game Code *
         *************/


    }

    private void initializeLevel() {
        /*************
         * Game Code *
         *************/

        // Initialize Manual Levels
        levelMap = new HashMap<>();
//        loadCustomMap();
        enemyGenerator = new EnemyGenerator(entityHandler);
//        enemyGenerator = new LevelGenerator(entityHandler);
//        enemyGenerator = new ProceduralGenerator(entityHandler);
    }

    private void loadCustomMap() {
        addLevel(1, "/level1.png");
        addLevel(2, "/level2.png");
    }

    public void update() {
        if (stateMachine.containsKey(Engine.currentState)) {
            currentState = stateMachine.get(Engine.currentState);
        } else {
            currentState = stateMachine.get(GameState.Menu);
        }

        currentState.update(this);

        if (screenShake > 0) {
            screenShake -= 1;
            screenShake_MULT *= -1;
        }

//        if (screenShake < 0) {
//            screenShake++;
//        }
    }

    public void render(Graphics g) {
        renderer.render(this, g);
    }

    public void renderInCamera(Graphics g) {
        /***************
         * Engine Code *
         ***************/

        Graphics2D g2d = (Graphics2D) g; // consider writing the whole renderer in g2d

//        screenShake = Utility.randomRangeInclusive(10, 20);

        // Camera start
        // Camera Translation Variables
        camX = camera.getX();
        camY = camera.getY();
        g2d.translate(-camX - screenShake*screenShake_MULT, -camY - screenShake*screenShake_MULT);

        /*************
         * Game Code *
         *************/

        inScreenMarker = (int) camera.getMarker() + 100;


        if (!Engine.stateIs(GameState.GameOver)) {
            entityHandler.render(g);

            g.setColor(Color.RED);
            g.drawLine(0, inScreenMarker, Engine.WIDTH, inScreenMarker);

        } else {

        }

        /*****************
         * Engine Code *
         *****************/

        // Camera end
        g2d.translate(camX + screenShake*screenShake_MULT, camY + screenShake*screenShake_MULT);
    }

    /************************
     * User Input Functions *
     ************************/

    public void mouseMoved(int mx, int my) {
        /*************
         * Game Code *
         *************/

        // Convert Screen Space Coordinates to World Space Coordiantes
        mxD = (double) mx;
        myD = (double) my;

        mxD /= Engine.SCALE;
        myD /= Engine.SCALE;

        uiHandler.checkHover((int) mxD, (int) myD);
//        giveDestination(mx, my);
    }

    public void mouseDragged(int mx, int my) {
        /*************
         * Game Code *
         *************/

        giveDestination(mx, my);
    }

    public void mousePressed(int mouse) {
        /*************
         * Game Code *
         *************/

//        if (mouse == MouseEvent.BUTTON1) {
//            shootPlayer();
//        }
    }

    public void mouseReleased(int mouse) {
        /*************
         * Game Code *
         *************/

//        if (mouse == MouseEvent.BUTTON1) {
//            stopShootPlayer();
//        }
    }

    public void keyPressed(int key) {
        /*************
         * Game Code *
         *************/

        if (VariableHandler.isConsole()) {
//            scanner = new Scanner(System.in);
//            cmd = scanner.nextLine();
//            variableManager.console();
        }

//        if (key == 192) {
//            System.out.println("TILDE PRESSED");
//            VariableManager.console();
//        }

        if (Engine.currentState == GameState.Help) {
            // todo: Pass it to the menu
            uiHandler.keyPressed(key);
        }

        keyboardMenuNavigation(key);
        keyPressedPlayer(key);

        if (key == (KeyEvent.VK_ESCAPE)) {
            if (Engine.currentState == GameState.Menu) {
                System.exit(1);
            }
        }
    }

    public void keyReleased(int key) {
        /*************
         * Game Code *
         *************/

        if (Engine.stateIs(GameState.Game) || Engine.stateIs(GameState.Pause)) {

            // todo: Move all player movement functions to Entity Handler
            if (key == (KeyEvent.VK_LEFT) || key == KeyInput.getKeyEvent(LEFT))
                entityHandler.stopMovePlayer('l');

            if (key == (KeyEvent.VK_RIGHT) || key == KeyInput.getKeyEvent(RIGHT))
                entityHandler.stopMovePlayer('r');

            if (key == (KeyEvent.VK_UP) || key == KeyInput.getKeyEvent(UP))
                entityHandler.stopMovePlayer('u');

            if (key == (KeyEvent.VK_DOWN) || key == KeyInput.getKeyEvent(DOWN))
                entityHandler.stopMovePlayer('d');

            if (key == (KeyInput.getKeyEvent(SHOOT)) || key == (KeyEvent.VK_NUMPAD0))
                entityHandler.playerCannotShoot();

//            if (key == KeyInput.getKeyEvent(SPECIAL))
//                entityHandler.stopPlayerSpecial();
        }

    }

//    private void setupHighScore() {
//        for (int i = 0; i < highScoreNumbers; i++) {
//            highScore.add(0);
//        }
//    }

    /***************************
     * Game Managing Functions *
     ***************************/

    public void resetGame() {
        System.out.println("Game has been reset");
        reset = false;
        Engine.timeInSeconds = 0; // might be unnecesary
        timeInSeconds = 0;
        variableHandler.resetScore();
//        variableHandler.resetDifficulty(); todo: reset
        variableHandler.resetFirepower();
        VariableHandler.shield.reset();
        variableHandler.health.reset();
        variableHandler.firepower.reset();
        variableHandler.pulse = true;
        VariableHandler.setHealthColor();
        Tutorial.reset();
        StatePlay.resetKillstreak();

        /*************
         * Game Code *
         *************/

        variableHandler.resetLevel();
        levelSpawned = false;
        entityHandler.resetPlayer();
        entityHandler.clearEnemies();
        entityHandler.clearPickups();
        levelSpawned = false;
        uiHandler.resetActiveMessageBoxes();

//        testingCheat();
    }

    public void checkButtonAction(int mx, int my) {
        mxD = (double) mx;
        myD = (double) my;

        mxD /= Engine.SCALE;
        myD /= Engine.SCALE;

        uiHandler.checkButtonAction((int) mxD, (int) myD);
    }

    public void notifyUIHandler(GameState state) {
        uiHandler.updateState(state);

        if (state == GameState.Menu || state == GameState.GameOver) {
            reset = true;
            VariableHandler.updateHighScore();
        }
    }

    public void save() {
        SaveLoad.saveGame();
        System.out.println("Saving");
        beginSaveLoadResetTimer();
    }

    public void load() {
        try {
            SaveLoad.loadGame();
            beginSaveLoadResetTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loading");
    }

    private void beginSaveLoadResetTimer() {
        VariableHandler.notificationSet = Engine.timer;
    }

    /******************
     * User functions *
     ******************/

    /**********************
     * Keyboard Functions *
     **********************/

    private void keyboardMenuNavigation(int key) {
        if (Engine.currentState != GameState.Game) {
            // Keyboard to Navigate buttons

            if (key == KeyEvent.VK_DOWN || key == KeyInput.getKeyEvent(DOWN)) {
                uiHandler.keyboardSelection('d');
            }

            if (key == KeyEvent.VK_UP || key == KeyInput.getKeyEvent(UP)) {
                uiHandler.keyboardSelection('u');
            }

            // Left/Right to increase or decrease something
            if (key == KeyEvent.VK_RIGHT || key == KeyInput.getKeyEvent(RIGHT)) {
                uiHandler.increaseOption();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyInput.getKeyEvent(LEFT)) {
                uiHandler.decreaseOption();
            }
        }

        // Needs to be outside the check to dismiss tutorial prompt
        // Enter/E to select selected
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E ) {
            uiHandler.chooseSelected();
        }

        if (key == KeyEvent.VK_G) {
            godMode = !godMode;
            Utility.log("God Mode " + godMode);
            VariableHandler.setScore(0);
        }
    }

    private void keyPressedPlayer(int key) {
        if (Engine.stateIs(GameState.Game)) {
            if (key == (KeyEvent.VK_LEFT) || key == KeyInput.getKeyEvent(LEFT)) {
                movePlayer('l');
                Tutorial.setMoved(true);
            }

            if (key == (KeyEvent.VK_RIGHT) || key == KeyInput.getKeyEvent(RIGHT)) {
                movePlayer('r');
                Tutorial.setMoved(true);
            }

            if (key == (KeyEvent.VK_UP) || key == KeyInput.getKeyEvent(UP)) {
                movePlayer('u');
                Tutorial.setMoved(true);
            }

            if (key == (KeyEvent.VK_DOWN) || key == KeyInput.getKeyEvent(DOWN)) {
                movePlayer('d');
                Tutorial.setMoved(true);
            }

            if (key == (KeyInput.getKeyEvent(SHOOT)) || key == (KeyEvent.VK_NUMPAD0)) {
                entityHandler.playerCanShoot();
                Tutorial.setShot(true);
            }

            if (key == KeyInput.getKeyEvent(SPECIAL)) {
                entityHandler.playerSpecial();
                Tutorial.setSpecial(true);
            }

            if (Engine.stateIs(GameState.Game)) {
                if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    Engine.pauseState();
                }
            } else if (Engine.currentState == GameState.Pause) {
                if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    Engine.gameState();
                }
            }
        }
    }

    // Shop Functions
    public void movePlayer(char c) {
        if (c == 'l' || c == 'r' || c == 'u' | c == 'd') {
            entityHandler.movePlayer(c);

//            if (!MenuPlay.moved) {
//                MenuPlay.moved = true;
//            }
        }
    }

    public void stopMovePlayer(char c) {
        if (c == 'l' || c == 'r' || c == 'u' | c == 'd') {
            entityHandler.stopMovePlayer(c);
        }
    }

    private void giveDestination(int mx, int my) {
        if (!keyboardControl)
            entityHandler.giveDestination(mx, my);
    }

    public void shootPlayer() {


//        if (!MenuPlay.shot) {
//            MenuPlay.shot = true;
//        }
    }

    public void stopShootPlayer() {
        entityHandler.playerCannotShoot();
    }


    /*
     * Creates the level and spawns enemies appropriately
     * Generates a flag to indicate end of level after level generation
     */


    public void spawn() {
        levelSpawned = !levelSpawned;
        Engine.gameState();

//        int level = variableHandler.getLevel();

        BufferedImage currentLevel = levelMap.get(variableHandler.getLevel());
        if (currentLevel != null) {
//            enemyGenerator.loadImageLevel(currentLevel);
//            levelHeight = enemyGenerator.getLevelHeight();
        } else {
            enemyGenerator.generateLevel();
            levelHeight = enemyGenerator.getLevelHeight();
        }

//        entityHandler.spawnFlag();
    }

    public static Camera getCamera() {
        return camera;
    }

    // checks whether the condition to advance to the next level has been fulfilled
    // if the flag crosses the screen, advance level and if no levels remain, end game
    public void checkLevelStatus() {
        // If the boss is killed, updates the boolean variable
//        entityHandler.checkBoss();

        levelEndCondition = entityHandler.getFlagY() > levelHeight && !variableHandler.isBossAlive();

        if (levelEndCondition) {
            variableHandler.incrementLevel();
            levelSpawned = false;
            variableHandler.setBossAlive(false);
            entityHandler.clearEnemies();
            entityHandler.clearPickups();

            if (variableHandler.finishedFinalLevel()) {
                Engine.menuState(); // stub
//                resetGame();
            } else {
                Engine.transitionState();
            }
        }
    }

    private void testingCheat() {
        VariableHandler.health.set(100);
        VariableHandler.setScore(4000);
        variableHandler.setLevel(4);
        VariableHandler.firepower.set(20);
        VariableHandler.shield.set(100);
    }

    public static TextureHandler getTexture() {
        return textureHandler;
    }

//    public static SoundHandler getSound() {
//        return soundHandler;
//    }

    private void addLevel(int num, String path) {
        level = Engine.loader.loadImage(path);
        levelMap.put(num, level);
    }

    public static long getCurrentTime() {
        return timeInSeconds;
    }

    public void updateUI() {
        uiHandler.update();
    }

    public void resetLevelLoaded() {
        levelLoaded = false;
    }

    public boolean islevelLoaded() {
        return  levelLoaded;
    }

    public boolean isLevelSpawned() {
        return levelSpawned;
    }

    public boolean noActiveMessageBoxes() {
        return uiHandler.noActiveMessageBoxes();
    }

    public void updateEnemyGenerator() {
        enemyGenerator.update();
    }

    public void updateEntityHandler() {
        entityHandler.update();
    }

    public void updatePlayer() {
        entityHandler.updatePlayer();
    }

    public static void renderSea(Graphics g) {
        background.renderSea(g, screenShake);
    }

    public static void renderClouds(Graphics g) {
//        background.renderClouds(g, screenShake);
    }

//    public void renderBackground(Graphics g) {
//        background.render(g, screenShake*screenShake_MULT);
//    }

    public void renderUI(Graphics g) {
        uiHandler.render(g);
    }

    public static void setLevelLoadedtoTrue() {
        levelLoaded = true;
    }

    public void updateBackground() {
        background.update();
    }
}
