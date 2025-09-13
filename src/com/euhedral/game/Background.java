package com.euhedral.game;

import com.euhedral.engine.Engine;
import com.euhedral.engine.GameState;
import com.euhedral.engine.Pool;
import com.euhedral.engine.Utility;
import com.euhedral.game.Entities.Clouds;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {
    // Background Scrolling
//    int timesRenderedIn = 0;
    int timesRenderedOut = 0;
    int currentImage = 0;
    //    int imageAnimationSpeed = 1;
    static int maxImage = 7;
    private double backgroundScroll = 0;
    //    private float backgroundScrollAcc = 0;
    private static final double maxScroll = 64;
    public static final double scrollRateGame = maxScroll/33;  // MAX = 0.04
    public static final double scrollRateTransition = scrollRateGame/4;
    private static double scrollRate;

    BufferedImage imageSea; // = GameController.getTexture().sea[currentImage];
    int imageScrollinginterval; // = imageSea.getHeight() * 2;

    int minX = 0;
    int minY = (int) -maxScroll;

    Pool backgroundObjects;
    int cloudSpawnCount, cloudSpawnCount_MAX;

    int cloudScale = 3;

    /************
     * Test *
     ************/

    int testWaveSpeed = 1;
    Color testColor = new Color(128/3, 128/2, 128);

    public Background() {
        imageSea = GameController.getTexture().sea[currentImage];
        imageScrollinginterval = imageSea.getHeight() * 2 - 2;

        backgroundObjects = new Pool();
        cloudSpawnCount = 0;
        cloudSpawnCount_MAX = 54*cloudScale;
    }

    public void update() {
        backgroundObjects.update();
        cloudSpawnCount++;
        if (cloudSpawnCount > cloudSpawnCount_MAX) {
            spawnNewCloud();
            cloudSpawnCount = 0;
            cloudSpawnCount_MAX = Utility.randomRangeInclusive(54*cloudScale, 54*(cloudScale + 1));
        }
//        backgroundObjects.disableIfOutsideBounds(1);
    }

    public void render(Graphics g) {
        renderScrollingBackground(g);
        backgroundObjects.render(g);
    }

    // todo: speed up 'animation'
    public void renderScrollingBackground(Graphics g) {

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
                scrollRate = scrollRateGame;
//                backgroundScrollAcc += scrollRate;
            } else if (Engine.stateIs(GameState.Transition)) {
                scrollRate = scrollRateTransition;
            } else {
                scrollRate = 0;
            }

            backgroundScroll += scrollRate;

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

    private void spawnNewCloud() {
        Clouds cloud = new Clouds(Utility.randomRangeInclusive(0, Engine.WIDTH), -100, EntityID.Background);
        cloud.setWidth(48*cloudScale);
        cloud.setHeight(54*cloudScale);
        backgroundObjects.add(cloud);
    }

    public static double getScrollRate() {
        return scrollRate;
    }
}
