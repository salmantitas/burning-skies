package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.game.Entities.Shop;
import com.euhedral.game.UI.UIHandler;

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
    public static String gameVersion = "0.7.05";
    private int gameWidth = 1280;
    private double gameRatio = 4 / 3;
    private int gameHeight = Engine.HEIGHT;
    private double gameScale = Engine.SCALE;
    private Color gameBackground = new Color(0,0,128);

    long timer = System.currentTimeMillis();
    public static long timeInSeconds;

    // Management
    private UIHandler uiHandler;
    private VariableHandler variableHandler;
    private EntityHandler entityHandler;
    private static TextureHandler textureHandler;
    private SoundHandler soundHandler;

    ActionTag action;

    boolean endGameCondition;
    boolean activeMessageBoxes;
    boolean tutorialDisabled;
    boolean validCameraRenderState;

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
    private boolean levelLoaded = false; // levels will only loaded when this is true
    boolean reset = true;
    boolean levelEndCondition;
    BufferedImage level;

    // Cheats
    public static boolean godMode = false;

    /************
     * Controls *
     ************/
    public static String UP = "W", DOWN = "S", LEFT = "A", RIGHT = "D", SHOOT = "SPACE",
            BULLET = "CTRL",
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

    // Background Scrolling
//    int timesRenderedIn = 0;
    int timesRenderedOut = 0;
    int currentImage = 0;
//    int imageAnimationSpeed = 1;
    static int maxImage = 7;
    private double backgroundScroll = 0;
//    private float backgroundScrollAcc = 0;
    private static double maxScroll = 64;
    private static double scrollRate = maxScroll/20; // MAX = 0.04

    BufferedImage imageSea; // = GameController.getTexture().sea[currentImage];
    int imageScrollinginterval; // = imageSea.getHeight() * 2;

    int minX = 0;
    int minY = (int) -maxScroll;

    /************
     * Test *
     ************/

    int testWaveSpeed = 1;
    Color testColor = new Color(128/3, 128/2, 128);

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

    private void initializeGame() {
        /*************
         * Game Code *
         *************/

        variableHandler = new VariableHandler();
        entityHandler = new EntityHandler();
        shop = new Shop();
        scanner = new Scanner(System.in);

        try {
            SaveLoad.loadSettings();
        } catch (Exception e) {
            Utility.log("Exception");
        }
    }

    private void initializeGraphics() {
        textureHandler = new TextureHandler();

        imageSea = GameController.getTexture().sea[currentImage];
        imageScrollinginterval = imageSea.getHeight() * 2;
        /*************
         * Game Code *
         *************/

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
        Engine.timer++;

        soundHandler.update(); // runs in all states

        if (Engine.stateIs(GameState.Quit))
            Engine.stop();

        if (!Engine.stateIs(GameState.Pause) && !Engine.stateIs(GameState.Game) && !Engine.stateIs(GameState.Transition))
//            resetGame();

            uiHandler.update();

        if (Engine.stateIs(GameState.Menu)) {
            levelLoaded = false;

            if (reset)
                resetGame();
        }

        if (Engine.stateIs(GameState.Transition)) {
            /*************
             * Game Code *
             *************/

            /*
             * Spawn if the level can loaded and has not already been spawned
             * */

            if (levelLoaded) {
                if (!levelSpawned)
                    spawn();
            }
        }

        /*
         * Disable the level load permission, as the level is already running
         * */
        if (Engine.stateIs(GameState.Game)
//                && !VariableManager.isConsole()
        ) {
//            if (System.currentTimeMillis() - 1000 > timer) {
//                timer = System.currentTimeMillis();
//                timeInSeconds++;
//                System.out.printf("Timer: %d\n", timeInSeconds);
//            }

            levelLoaded = false;
            endGameCondition = variableHandler.health.getValue() <= 0;

            if (endGameCondition) {

                Engine.gameOverState();

                System.out.println("Game Over");
                VariableHandler.updateHighScore();
            }

            /*************
             * Game Code *
             *************/

            else {

                // Game only runs if either tutorials are disabled, or no message boxes are active
                // Player can move when the tutorial box is up.

                activeMessageBoxes = !uiHandler.noActiveMessageBoxes();
                tutorialDisabled = !VariableHandler.isTutorial();

                // if tutorial is disabled or there are no active message boxes, just run the game

                if (tutorialDisabled || !activeMessageBoxes) {
                    if (activeMessageBoxes) {
//                        System.out.println("Active Message Box");
                    }
                    enemyGenerator.update();
                    entityHandler.update();
                    checkLevelStatus();

                    if (System.currentTimeMillis() - 1000 > timer) {
                        timer = System.currentTimeMillis();
                        timeInSeconds++;
//                        System.out.printf("Timer: %d\n", timeInSeconds);
                    }
                }

                else if (activeMessageBoxes) {
                        entityHandler.updatePlayer();
                    }
            }
        }

        if (Engine.stateIs(GameState.GameOver)) {

        }
    }

    public void render(Graphics g) {

        renderScrollingBackground(g);

        if (Engine.currentState == GameState.Transition) {
            /*************
             * Game Code *
             *************/
        }

            /*************
             * Game Code *
             *************/

            validCameraRenderState = Engine.currentState == GameState.Game || Engine.currentState == GameState.Pause || Engine.stateIs(GameState.GameOver);

            if (validCameraRenderState) {
                    renderInCamera(g);

                    if (VariableHandler.isHud()) {

                        entityHandler.renderBossHealth(g);

                    }

            }

        /***************
         * Engine Code *
         ***************/

        uiHandler.render(g);
    }

    private void renderInCamera(Graphics g) {
        /***************
         * Engine Code *
         ***************/

        Graphics2D g2d = (Graphics2D) g; // consider writing the whole renderer in g2d

        // Camera start
        // Camera Translation Variables
        camX = camera.getX();
        camY = camera.getY();
        g2d.translate(-camX, -camY);

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
        g2d.translate(camX, camY);
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
                stopMovePlayer('l');

            if (key == (KeyEvent.VK_RIGHT) || key == KeyInput.getKeyEvent(RIGHT))
                stopMovePlayer('r');

            if (key == (KeyEvent.VK_UP) || key == KeyInput.getKeyEvent(UP))
                stopMovePlayer('u');

            if (key == (KeyEvent.VK_DOWN) || key == KeyInput.getKeyEvent(DOWN))
                stopMovePlayer('d');

            if (key == (KeyInput.getKeyEvent(SHOOT)) || key == (KeyEvent.VK_NUMPAD0))
                stopShootPlayer();
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
        variableHandler.resetPower();
        VariableHandler.shield.reset();
        variableHandler.health.reset();
        VariableHandler.setHealthColor();

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
        performAction();
    }

    public void performAction() {
        action = uiHandler.getAction();
        if (action != null) {
            switch (action) {
                case go:
                    levelLoaded = true;
                    break;
                case tutorial:
                    VariableHandler.toggleTutorial();
                    break;
                case volumeMaster: {
                    soundHandler.toggleVolumeMaster();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeMasterUp: {
                    soundHandler.volumeMasterUp();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeMasterDown: {
                    soundHandler.volumeMasterDown();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeMusic: {
                    soundHandler.toggleVolumeMusic();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeMusicUp: {
                    soundHandler.volumeMusicUp();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeMusicDown: {
                    soundHandler.volumeMusicDown();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeEffects: {
                    soundHandler.toggleVolumeEffects();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeEffectsUp: {
                    soundHandler.volumeEffectsUp();
                    SaveLoad.saveSettings();
                    break;
                }
                case volumeEffectsDown: {
                    soundHandler.volumeEffectsDown();
                    SaveLoad.saveSettings();
                    break;
                }
                case toggleDifficulty: {
                    VariableHandler.difficultyType++;
                    if (VariableHandler.difficultyType > VariableHandler.DIFFICULTY_CHALLENGE)
                        VariableHandler.difficultyType = 0;
                    break;
                }
                case health:
                    shop.buyHealth();
                    break;
                case power:
                    shop.buyPower();
                    break;
                case shield:
                    shop.buyShield();
                    break;
                case save:
                    save();
                    break;
                case load:
                    load();
                    break;
            }
            uiHandler.endAction();
        }
    }

    public void notifyUIHandler(GameState state) {
        uiHandler.updateState(state);

        if (state == GameState.Menu) {
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

            if (key == KeyEvent.VK_RIGHT || key == KeyInput.getKeyEvent(RIGHT) || key == KeyEvent.VK_DOWN || key == KeyInput.getKeyEvent(DOWN)) {
                uiHandler.keyboardSelection('r');
            }

            if (key == KeyEvent.VK_LEFT || key == KeyInput.getKeyEvent(LEFT) || key == KeyEvent.VK_UP || key == KeyInput.getKeyEvent(UP)) {
                uiHandler.keyboardSelection('l');
            }
        }

        // Enter/Spacebar to select selected
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E ) {
            uiHandler.chooseSelected();
            performAction();
        }

        if (key == KeyEvent.VK_G) {
            godMode = !godMode;
            Utility.log("God Mode " + godMode);
        }
    }

    private void keyPressedPlayer(int key) {
        if (Engine.stateIs(GameState.Game) || Engine.stateIs(GameState.Pause)) {
            if (key == (KeyEvent.VK_LEFT) || key == KeyInput.getKeyEvent(LEFT))
                movePlayer('l');

            if (key == (KeyEvent.VK_RIGHT) || key == KeyInput.getKeyEvent(RIGHT))
                movePlayer('r');

            if (key == (KeyEvent.VK_UP) || key == KeyInput.getKeyEvent(UP))
                movePlayer('u');

            if (key == (KeyEvent.VK_DOWN) || key == KeyInput.getKeyEvent(DOWN))
                movePlayer('d');

            if (key == (KeyInput.getKeyEvent(SHOOT)) || key == (KeyEvent.VK_NUMPAD0))
                entityHandler.playerCanShoot();

//            if (key == KeyEvent.VK_CONTROL)
//                entityHandler.switchPlayerBullet();

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


    private void spawn() {
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
        entityHandler.checkBoss();

        levelEndCondition = entityHandler.getFlagY() > levelHeight && !variableHandler.isBossLives();

        if (levelEndCondition) {
            variableHandler.nextLevel();
            levelSpawned = false;
            variableHandler.setBossLives(false);
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
        VariableHandler.power.set(3);
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

    // todo: speed up 'animation'
    private void renderScrollingBackground(Graphics g) {
//        scrollRate = 0.005f;


        if (!Engine.stateIs(GameState.Test)) {

            for (int i = minX; i < Engine.WIDTH; i += imageScrollinginterval) {
                for (int j = minY; j < Engine.HEIGHT; j += imageScrollinginterval) {

                    g.drawImage(imageSea, i, j + (int) (backgroundScroll), imageScrollinginterval + 1, imageScrollinginterval + 1, null);

//                    if (backgroundScrollAcc >= maxScroll/2) {
//                        backgroundScrollAcc = 0f;
//                        currentImage = (currentImage - 1);
//                        if (currentImage < 0) {
//                            currentImage = maxImage + currentImage;
//                        }
//                    }

//
//
//                        if ((timesRenderedIn % 16) == 0) {
//                    currentImage = (currentImage + 1) % (maxImage + 1);
//                        }

//                    Utility.log("CurrentImage: " + currentImage);
//
                    imageSea = GameController.getTexture().sea[currentImage];
//                    }

                }

            }

            if (Engine.stateIs(GameState.Game)) {
                backgroundScroll += scrollRate;
//                backgroundScrollAcc += scrollRate;
            } else if (Engine.stateIs(GameState.Transition)) {
                    backgroundScroll += scrollRate/4;
                }

            if (backgroundScroll >= maxScroll) {
                backgroundScroll = 0;
            }
//
//                Utility.log("Scroll: " + backgroundScroll);
//                        timesRenderedIn++;
//
//                        if ((timesRenderedIn % 16) == 0) {
//                            currentImage = (currentImage - 1);
//                            if (currentImage < 0) {
//                                currentImage = maxImage + currentImage;
//                            }
//                        }
//            }

//            if (!Engine.stateIs(GameState.Game)) {
                timesRenderedOut++;

//            if (!Engine.stateIs(GameState.Game)) {
                if ((timesRenderedOut % 20) == 0) {
                    currentImage = (currentImage + 1) % (maxImage + 1);
//                    currentImage = (currentImage - 1);
//                    if (currentImage < 0) {
//                        currentImage = maxImage + currentImage;
//                    }
                }
//            } else {
//                if (backgroundScrollAcc >= maxScroll) {
//                    backgroundScrollAcc = 0f;
//                    currentImage = (currentImage - 1);
//                    if (currentImage < 0) {
//                        currentImage = maxImage + currentImage;
//                    }
//                }
//            }


//            Utility.log("CurrentImage: " + currentImage);
            }
//        }

        // Test todo: delete

//        if (Engine.stateIs(GameState.Menu)) {
//            testFunction(g, 50);
//        }
    }

    public static long getCurrentTime() {
        return timeInSeconds;
    }

    // Test Functions

    private void testFunction(Graphics g, int gap) {
        g.setColor(testColor);
        int yInterval = gap;
        for (int j = 0; j < Engine.HEIGHT; j += yInterval )
                testSineLine(g, j);

        testWaveSpeed++;
    }

    private void testDrawHorizontalLine(Graphics g, int j) {
        for (int i = 0; i < Engine.WIDTH; i++)
            g.fillRect(i, (j + testWaveSpeed) % Engine.HEIGHT, 4, 4);
    }

    private void testSineLine(Graphics g, int j) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(Utility.makeTransparent(0.75f));

        for (int i = 0; i < Engine.WIDTH; i++) {
            double iSin = Math.toDegrees(Math.sin(i));
            double yCor = (j + iSin) + testWaveSpeed;
            g.fillRect(i, (int) yCor % Engine.HEIGHT, 6, 2);
        }
        g2d.setComposite(Utility.makeTransparent(1f));
    }

}
