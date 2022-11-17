package com.euhedral.game;

import com.euhedral.engine.*;
import com.euhedral.game.Entities.Shop;
import com.euhedral.game.UI.UIHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

// Manages the game itself, and passes instructions to all other classes under it
public class GameController {
    /********************************************
     * Window Settings - Manually Configurable *
     *******************************************/

    private int gameWidth = 1024;
    private double gameRatio = 4 / 3;
    private int gameHeight = Engine.HEIGHT;
    private String gameTitle = "Aerial Predator";
    private Color gameBackground = Color.BLUE;

    // Management
    private UIHandler uiHandler;
    private VariableManager variableManager;
    private EntityManager entityManager;

    public Scanner scanner;
    public static String cmd;

    // High Score
//    private LinkedList<Integer> highScore = new LinkedList<>();
//    private int highScoreNumbers = 5;
//    private boolean updateHighScore = false;

    // Objects
    private Shop shop;

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

    // LevelMap to automate level loading
    private HashMap<Integer, BufferedImage> levelMap;

    private boolean levelSpawned = false;
    public static boolean rebinding = false;

    private boolean keyboardControl = true; // false means mouse Control

    public static Texture texture;

    /************
     * Graphics *
     ************/

    private SpriteSheet playerSpriteSheet;
    private BufferedImage[] playerImage;

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
        variableManager = new VariableManager();
        entityManager = new EntityManager(variableManager, camera);
        shop = new Shop();
        scanner = new Scanner(System.in);
    }

    private void initializeGraphics() {
        /*************
         * Game Code *
         *************/
        texture = new Texture();
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
        proceduralGenerator = new ProceduralGenerator(entityManager);
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
            boolean endGameCondition = variableManager.health.getValue() <= 0;

            if (endGameCondition) {
                Engine.gameOverState();
                resetGame();
            }

            /*************
             * Game Code *
             *************/

            else {

                // Game only runs if either tutorials are disabled, or no message boxes are active

                if (uiHandler.noActiveMessageBoxes() || !VariableManager.tutorialEnabled()) {

                    entityManager.update();
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

                renderInCamera(g);

                if (VariableManager.isHud()) {

                    entityManager.renderBossHealth(g);

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
        entityManager.render(g);

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

        if (mouse == MouseEvent.BUTTON1) {
            shootPlayer();
        }
    }

    public void mouseReleased(int mouse) {
        /*************
         * Game Code *
         *************/

        if (mouse == MouseEvent.BUTTON1) {
            stopShootPlayer();
        }
    }

    public void keyPressed(int key) {
        /*************
         * Game Code *
         *************/

        if (VariableManager.isConsole()) {
//            scanner = new Scanner(System.in);
//            cmd = scanner.nextLine();
//            variableManager.console();
        }

        if (key == 192) {
            System.out.println("TILDE PRESSED");
            VariableManager.console();
        }

        if (Engine.currentState == GameState.Help) {
            // todo: Pass it to the menu
            uiHandler.keyPressed(key);
        }

        if (Engine.currentState != GameState.Game) {
            // Keyboard to Navigate buttons

            // Enter/Spacebar to select selected
            if (key == KeyEvent.VK_ENTER || key == KeyInput.getKeyEvent(SHOOT)) {
                uiHandler.chooseSelected();
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyInput.getKeyEvent(RIGHT)) {
                uiHandler.keyboardSelection('r');
            }

            if (key == KeyEvent.VK_LEFT || key == KeyInput.getKeyEvent(LEFT)) {
                uiHandler.keyboardSelection('l');
            }
        }

        if (Engine.currentState != GameState.Pause) {
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
                entityManager.switchPlayerBullet();

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

//    private void setupHighScore() {
//        for (int i = 0; i < highScoreNumbers; i++) {
//            highScore.add(0);
//        }
//    }

    /***************************
     * Game Managing Functions *
     ***************************/

    public void resetGame() {

        Engine.timer = 0;
        variableManager.resetScore();
        variableManager.resetPower();
        VariableManager.shield.reset();
        variableManager.health.reset();

        /*************
         * Game Code *
         *************/

        variableManager.resetLevel();
        levelSpawned = false;
        entityManager.clearEnemies();
        entityManager.clearPickups();
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
                case tutorial: VariableManager.toggleTutorial();
                break;
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

    public void notifyUIHandler(GameState state) {
        uiHandler.updateState(state);
    }

    private void beginSaveLoadResetTimer() {
        VariableManager.notificationSet = Engine.timer;
    }

    /***************************
     * Render Helper Functions *
     ***************************/

//    private void drawScore(Graphics g) {
//        variableManager.renderScore(g);
//    }
//
//    private void drawHealth(Graphics g) {
//        variableManager.renderHealth(g);
//    }
//
//    private void drawPower(Graphics g) {
//        variableManager.renderPower(g);
//    }

    /******************
     * User functions *
     ******************/

    // Shop Functions



    public void movePlayer(char c) {
        if (c == 'l' || c == 'r' || c == 'u' | c == 'd') {
            entityManager.movePlayer(c);
        }
    }

    public void stopMovePlayer(char c) {
        if (c == 'l' || c == 'r' || c == 'u' | c == 'd') {
            entityManager.stopMovePlayer(c);
        }
    }

    private void giveDestination(int mx, int my) {
        if (!keyboardControl)
            entityManager.giveDestination(mx, my);
    }

    public void shootPlayer() {
        entityManager.playerCanShoot();
    }

    public void stopShootPlayer() {
        entityManager.playerCannotShoot();
    }


    /*
     * Creates the level and spawns enemies appropriately
     * Generates a flag to indicate end of level after level generation
     */


    private void spawn() {
        levelSpawned = !levelSpawned;
        Engine.gameState();

        int level = variableManager.getLevel();

        BufferedImage currentLevel = levelMap.get(level);
        if (currentLevel != null) {
//            levelGenerator.loadImageLevel(currentLevel);
//            levelHeight = levelGenerator.getLevelHeight();
        } else {
            proceduralGenerator.generateLevel();
            levelHeight = proceduralGenerator.getLevelHeight();
        }

        entityManager.spawnFlag();
    }

    public static Camera getCamera() {
        return camera;
    }

    // Creates an instance of the player and sets the camera to follow it
    public void setCameraToPlayer() {
        offsetVertical = gameHeight - Utility.intAtWidth640(32)*4;

        // sets the camera's width to center the player horizontally, essentially to 0, and
        // adjust the height so that player is at the bottom of the screen
        camera = new Camera(0,entityManager.getPlayerY() - 615);
        camera.setMarker(entityManager.getPlayerY());
    }

    // checks whether the condition to advance to the next level has been fulfilled
    // if the flag crosses the screen, advance level and if no levels remain, end game
    public void checkLevelStatus() {
        // If the boss is killed, updates the boolean variable
        entityManager.checkBoss();

        boolean condition = entityManager.getFlagY() > levelHeight && !variableManager.isBossLives();

        if (condition) {
            variableManager.nextLevel();
            levelSpawned = false;
            variableManager.setBossLives(false);
            entityManager.clearEnemies();
            entityManager.clearPickups();

            if (variableManager.finishedFinalLevel()) {
                Engine.menuState(); // stub
                resetGame();
            } else {
                Engine.transitionState();
            }
        }
    }

    private void testingCheat() {
        VariableManager.health.set(100);
        VariableManager.setScore(4000);
        variableManager.setLevel(4);
        VariableManager.power.set(5);
        variableManager.setGround(true);
        VariableManager.shield.set(100);
    }

    public static Texture getTexture() {
        return texture;
    }

    private void addLevel(int num, String path) {
        BufferedImage level = Engine.loader.loadImage(path);
        levelMap.put(num, level);
    }

}
