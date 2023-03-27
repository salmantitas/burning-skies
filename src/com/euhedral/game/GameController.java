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

    private String gameTitle = "Burning Skies";
    private int gameWidth = 1024;
    private double gameRatio = 4 / 3;
    private int gameHeight = Engine.HEIGHT;
    private Color gameBackground = Color.BLUE;

    // Management
    private UIHandler uiHandler;
    private VariableHandler variableHandler;
    private EntityHandler entityHandler;
    private static TextureHandler textureHandler;
    private static SoundHandler soundHandler;

    public Scanner scanner;
    public static String cmd;

    // High Score
//    private LinkedList<Integer> highScore = new LinkedList<>();
//    private int highScoreNumbers = 5;
//    private boolean updateHighScore = false;

    // Camera
    public static Camera camera;
//    int offsetHorizontal;
    int offsetVertical;

    // Level Generation
//    private LevelGenerator levelGenerator;
    private ProceduralGenerator proceduralGenerator;

    // Levels
    private int levelHeight;
    private boolean loadMission = false; // levels will only loaded when this is true

    int count = 0;

    /************
     * Controls *
     ************/
    public static String UP = "W", DOWN = "S", LEFT = "A", RIGHT = "D", SHOOT  = "SPACE",
            BULLET = "CTRL",
            PAUSE = "P";

    /******************
     * User variables *
     ******************/

    // Objects
    private Shop shop;

    // LevelMap to automate level loading
    private HashMap<Integer, BufferedImage> levelMap;

    private boolean levelSpawned = false;
    public static boolean rebinding = false;

    private boolean keyboardControl = true; // false means mouse Control

    /************
     * Graphics *
     ************/

    private float backgroundScroll = 0;
    private static float scrollRate = 0.005f;
    private float maxScroll = 62f + 1;

    public GameController() {

        /******************
         * Window Setting *
         ******************/
        Engine.setTITLE(gameTitle);
//        Engine.setRatio(gameRatio);
        Engine.setWIDTH(gameWidth);
        Engine.setBACKGROUND_COLOR(gameBackground);
        gameHeight = Engine.HEIGHT;
        uiHandler = new UIHandler();

        initializeGraphics();
        initializeAnimations();
        initializeGame();
        initializeSound();
        initializeLevel();
    }

    /*************************
     * Initializer Functions *
     *************************/

    private void initializeGame() {
        /*************
         * Game Code *
         *************/

        Engine.menuState();
        variableHandler = new VariableHandler();
        entityHandler = new EntityHandler(variableHandler);
        shop = new Shop();
        scanner = new Scanner(System.in);
        try {
            SaveLoad.loadSettings();
        } catch (Exception e) {

        }
    }

    private void initializeGraphics() {
        /*************
         * Game Code *
         *************/
        textureHandler = new TextureHandler();
    }

    private void initializeSound() {
        /*************
         * Game Code *
         *************/
        soundHandler = new SoundHandler();
        soundHandler.playBGMMenu();
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
//        levelGenerator = new LevelGenerator(entityManager);
        proceduralGenerator = new ProceduralGenerator(entityHandler);
    }

    private void loadCustomMap() {
        addLevel(1, "/level1.png");
        addLevel(2, "/level2.png");
    }

    public void update() {
        Engine.timer++;

        if (Engine.stateIs(GameState.Quit))
            Engine.stop();

        if (!Engine.stateIs(GameState.Pause) && !Engine.stateIs(GameState.Game) && !Engine.stateIs(GameState.Transition))
            resetGame();

        uiHandler.update();

        if (Engine.stateIs(GameState.Menu)) {
            loadMission = false;
        }

        if (Engine.stateIs(GameState.Transition)) {
            /*************
             * Game Code *
             *************/

            /*
            * Spawn if the level can loaded and has not already been spawned
            * */

            if (loadMission) {
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
            loadMission = false;
            boolean endGameCondition = variableHandler.health.getValue() <= 0;

            if (endGameCondition) {
                soundHandler.playSound(soundHandler.EXPLOSION);
                // todo: play explosion animation first
                Engine.gameOverState();
                resetGame();
            }

            /*************
             * Game Code *
             *************/

            else {

                // Game only runs if either tutorials are disabled, or no message boxes are active

                if (uiHandler.noActiveMessageBoxes() || !VariableHandler.isTutorial()) {

                    proceduralGenerator.update();
                    entityHandler.update();
                    checkLevelStatus();
                }
            }
        }
    }

    public void render(Graphics g) {

        if (Engine.currentState == GameState.Highscore) {
//            drawHighScore(g);
        }

        if (Engine.currentState == GameState.Transition) {
            /*************
             * Game Code *
             *************/
        }

        if (Engine.currentState == GameState.Game || Engine.currentState == GameState.Pause ||
                Engine.currentState == GameState.GameOver) {

            /*************
             * Game Code *
             *************/

            if (Engine.currentState == GameState.Game || Engine.currentState == GameState.Pause ) {

                renderScrollingBackground(g);
                renderInCamera(g);

                if (VariableHandler.isHud()) {

                    entityHandler.renderBossHealth(g);

                }

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

        Graphics2D g2d = (Graphics2D) g;

        // Camera start
        // Camera Translation Variables
        float camX = camera.getX(), camY = camera.getY();
        g2d.translate(-camX, -camY);

        /*************
         * Game Code *
         *************/
        entityHandler.render(g);

        int inScreenMarker = (int) camera.getMarker() + 100;

        g.setColor(Color.RED);
        g.drawLine(0, inScreenMarker, Engine.WIDTH, inScreenMarker);

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

        uiHandler.checkHover(mx, my);
        giveDestination(mx, my);
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

        if (Engine.currentState != GameState.Game) {
            // Keyboard to Navigate buttons

            // Enter/Spacebar to select selected
            if (key == KeyEvent.VK_ENTER || key == KeyInput.getKeyEvent(SHOOT)) {
                uiHandler.chooseSelected();
                performAction();
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyInput.getKeyEvent(RIGHT) || key == KeyEvent.VK_DOWN || key == KeyInput.getKeyEvent(DOWN)) {
                uiHandler.keyboardSelection('r');
            }

            if (key == KeyEvent.VK_LEFT || key == KeyInput.getKeyEvent(LEFT) || key == KeyEvent.VK_UP || key == KeyInput.getKeyEvent(UP)) {
                uiHandler.keyboardSelection('l');
            }
        }

        if (Engine.currentState == GameState.Game) {
            if (key == (KeyEvent.VK_LEFT) || key == KeyInput.getKeyEvent(LEFT))
                movePlayer('l');

            if (key == (KeyEvent.VK_RIGHT) || key == KeyInput.getKeyEvent(RIGHT))
                movePlayer('r');

            if (key == (KeyEvent.VK_UP) || key == KeyInput.getKeyEvent(UP))
                movePlayer('u');

            if (key == (KeyEvent.VK_DOWN) || key == KeyInput.getKeyEvent(DOWN))
                movePlayer('d');

            if (key == (KeyInput.getKeyEvent(SHOOT)) || key == (KeyEvent.VK_NUMPAD0))
                shootPlayer();

            if (key == KeyEvent.VK_CONTROL)
                entityHandler.switchPlayerBullet();

            if (Engine.currentState == GameState.Game) {
                if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    Engine.pauseState();
                }
            }

        } else {

            if (Engine.currentState == GameState.Pause) {
                if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
                    Engine.gameState();
                }
            }

            if (key == (KeyEvent.VK_ESCAPE)) {
                if (Engine.currentState == GameState.Menu) {
                    System.exit(1);
                }
            }
        }

    }

    public void keyReleased(int key) {
        /*************
         * Game Code *
         *************/

        if (Engine.stateIs(GameState.Game)) {

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

        Engine.timeInSeconds = 0;
        variableHandler.resetScore();
        variableHandler.resetPower();
        VariableHandler.shield.reset();
        variableHandler.health.reset();

        /*************
         * Game Code *
         *************/

        variableHandler.resetLevel();
        levelSpawned = false;
        entityHandler.clearEnemies();
        entityHandler.clearPickups();
        levelSpawned = false;

//        testingCheat();
    }

    public void checkButtonAction(int mx, int my) {
        uiHandler.checkButtonAction(mx, my);
        performAction();
    }

    public void performAction() {
        ActionTag action = uiHandler.getAction();
        if (action != null) {
            switch (action) {
                case go:
                    loadMission = true;
                    break;
                case tutorial: VariableHandler.toggleTutorial();
                break;
                case volume: {
                    VariableHandler.toggleVolume();
                    if (VariableHandler.isVolume()) {
                        soundHandler.setVolume(1);
                    } else soundHandler.setVolume(0);
                    break;
                }
                case health: shop.buyHealth();
                break;
                case power: shop.buyPower();
                break;
                case ground: shop.buyGround();
                break;
                case shield: shop.buyShield();
                break;
                case save: save();
                break;
                case load: load();
                break;
            }
            uiHandler.endAction();
        }
    }

    public void notifyUIHandler(GameState state) {
        uiHandler.updateState(state);
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

    // Shop Functions



    public void movePlayer(char c) {
        if (c == 'l' || c == 'r' || c == 'u' | c == 'd') {
            entityHandler.movePlayer(c);
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
        entityHandler.playerCanShoot();
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

        int level = variableHandler.getLevel();

        BufferedImage currentLevel = levelMap.get(level);
        if (currentLevel != null) {
//            levelGenerator.loadImageLevel(currentLevel);
//            levelHeight = levelGenerator.getLevelHeight();
        } else {
            proceduralGenerator.generateLevel();
            levelHeight = proceduralGenerator.getLevelHeight();
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

        boolean condition = entityHandler.getFlagY() > levelHeight && !variableHandler.isBossLives();

        if (condition) {
            variableHandler.nextLevel();
            levelSpawned = false;
            variableHandler.setBossLives(false);
            entityHandler.clearEnemies();
            entityHandler.clearPickups();

            if (variableHandler.finishedFinalLevel()) {
                Engine.menuState(); // stub
                resetGame();
            } else {
                Engine.transitionState();
            }
        }
    }

    private void testingCheat() {
        VariableHandler.health.set(100);
        VariableHandler.setScore(4000);
        variableHandler.setLevel(4);
        VariableHandler.power.set(5);
        variableHandler.setGround(true);
        VariableHandler.shield.set(100);
    }

    public static TextureHandler getTexture() {
        return textureHandler;
    }
    public static SoundHandler getSound() {
        return soundHandler;
    }

    private void addLevel(int num, String path) {
        BufferedImage level = Engine.loader.loadImage(path);
        levelMap.put(num, level);
    }

    // todo: speed up 'animation'
    private void renderScrollingBackground(Graphics g) {
        count = 0;
        BufferedImage imageSea = GameController.getTexture().sea[count];
        int interval = imageSea.getHeight()*2;

        int minX = 0;
        int minY = (int) -maxScroll;

        for (int i = minX; i < Engine.WIDTH; i += interval) {
            for (int j = minY; j < Engine.HEIGHT; j += interval) {
                g.drawImage(imageSea, i, j + (int) (backgroundScroll), interval + 1, interval + 1, null);

                if (Engine.stateIs(GameState.Game)) {
                    backgroundScroll += scrollRate;

                    if (backgroundScroll >= maxScroll) {
                        backgroundScroll = 0;
                    }

                    count++;
                    if (count > 7) {
                        count = 0;
                    }

                    imageSea = GameController.getTexture().sea[count];
                }


            }
        }
    }

    public static float getScrollRate() {
        return scrollRate;
    }

}
