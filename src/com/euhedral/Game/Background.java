package com.euhedral.Game;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;
import com.euhedral.Engine.Pool;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Clouds;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.Color.RED;

public class Background {
    // Background Scrolling
//    int timesRenderedIn = 0;
    int timesRenderedOut = 0;
    int currentImage = 0;
    //    int imageAnimationSpeed = 1;
    static int maxImage = 7;
    private double backgroundScrollSea = 0;
    private double backgroundScrollCloud = 0;
    //    private float backgroundScrollAcc = 0;
    private static final double maxScroll = 64;
    public static final double scrollRateGame = maxScroll/32;  // MAX = 0.04
    public static final double scrollRateTransition = scrollRateGame/4;
    private static double scrollRate;

    int minX = -64;
    int minY = (int) -maxScroll;

    BufferedImage imageCloud; // = GameController.getTexture().sea[currentImage];
    Pool backgroundObjects;

    public ArrayList<Tile> tiles;
//    int cloudSpawnCount, cloudSpawnCount_MAX;

//    int cloudScale = 3;

    /************
     * Test *
     ************/

    int testWaveSpeed = 1;
    Color testColor = new Color(128/3, 128/2, 128);

    public Background() {
        int imageSize = 48;
        tiles = new ArrayList<>();
        Tile tile = new Tile(0,0, imageSize);

        for (int j = minY; j < Engine.HEIGHT; j += imageSize) {
           for (int i = minX; i < Engine.WIDTH; i += imageSize) {
                tile = new Tile(i, j, imageSize);
                tiles.add(tile);
            }
        }

        int maxFrame = tile.anim.getMaxFrames();
        int index = 0;

        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).anim.setCurrentFrame(index);

            index += 1;
            if (index >= maxFrame) {
                index = 0;
            }
        }

//        imageCloud = GameController.getTexture().cloud[0];
//
//        backgroundObjects = new Pool();
//        cloudSpawnCount = 0;
//        cloudSpawnCount_MAX = 54*cloudScale;
    }

    public void update() {

        updateSea();
//        updateClouds();
    }

    private void updateClouds() {
        backgroundObjects.update();
//        cloudSpawnCount++;
//        if (cloudSpawnCount > cloudSpawnCount_MAX) {
//            spawnNewCloud();
//            cloudSpawnCount = 0;
//            cloudSpawnCount_MAX = Utility.randomRangeInclusive(54*cloudScale, 54*(cloudScale + 1));
//        }
        backgroundObjects.disableIfOutsideBounds(1);
    }

    private void updateSea() {

        for (Tile tile : tiles) {
            tile.update();
        }

        if (Engine.stateIs(GameState.Game)) {
            scrollRate = scrollRateGame;
//                backgroundScrollAcc += scrollRate;
        } else if (Engine.stateIs(GameState.Transition)) {
            scrollRate = scrollRateTransition;
        } else {
            scrollRate = 0;
        }

//        backgroundScrollSea += scrollRate;
//        backgroundScrollCloud += scrollRate*2;
//
//        if (backgroundScrollSea >= maxScroll) {
//            backgroundScrollSea = 0;
//        }

//        if (backgroundScrollCloud >= 1024) {
//            backgroundScrollCloud = 0;
//        }

//        timesRenderedOut++;

//            if (!Engine.stateIs(GameState.Game)) {
//        if ((timesRenderedOut % 20) == 0) {
//            currentImage = (currentImage + 1) % (maxImage + 1);
//                    currentImage = (currentImage - 1);
//                    if (currentImage < 0) {
//                        currentImage = maxImage + currentImage;
//                    }
//        }
    }

//    public void render(Graphics g, int screenShake) {
//        renderScrollingBackground(g, screenShake);
////        backgroundObjects.render(g);
//    }
//
//    // todo: speed up 'animation'
//    public void renderScrollingBackground(Graphics g, int screenShake) {
//
//        if (!Engine.stateIs(GameState.Test)) {
//            renderSea(g, screenShake);
//            renderClouds(g, screenShake);
//        }
//    }

    public void renderSea(Graphics g, int screenShake) {
//        g.setColor(RED);
//        g.fillRect(100, 200, 32, 32);
        for (Tile tile : tiles) {
            tile.render(g, screenShake);
        }

//        for (int i = minX; i < Engine.WIDTH; i += imageScrollinginterval) {
//            for (int j = minY; j < Engine.HEIGHT; j += imageScrollinginterval) {
//
//                g.drawImage(
//                        imageSea,
//                        i + screenShake,
//                        j + (int) (backgroundScrollSea) + screenShake,
//                        imageScrollinginterval + 1,
//                        imageScrollinginterval + 1,
//                        null);
//
//                imageSea = GameController.getTexture().sea[currentImage];
//            }
//        }
    }

    public void renderClouds(Graphics g, int screenShake) {
        int yMin = -128;

        for (int i = 0; i < 2; i += 1) {
            for (int j = yMin; j < 3; j += 1) {

                g.drawImage(
                        imageCloud,
                        i * 1024 + screenShake,
                        j * 1024 + (int) (backgroundScrollCloud) + screenShake,
                        1024,
                        1024,
                        null);

                g.setColor(Color.BLACK);
                g.drawRect(i * 1024, (int) (j * 1024 + backgroundScrollCloud), 1024, 1024);
            }
        }
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

//        cloudScale = Utility.randomRangeInclusive(1, 10);
//
//        cloud.setWidth(48*cloudScale);
//        cloud.setHeight(54*cloudScale);
//        cloud.setY(-54*cloudScale);
        backgroundObjects.add(cloud);
    }

    public static double getScrollRate() {
        return scrollRate;
    }
}
