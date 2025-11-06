package com.euhedral.Game.UI;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.*;

import java.awt.*;

public class HUD {

    private static int scoreX = Utility.percWidth(74);;
    private static int scoreY = Utility.intAtWidth640(20);
    private static int scoreSize = Utility.percWidth(2);
    private static Font scoreFont;
    public static Color scoreColor = Color.YELLOW;

    private static int healthIconX, healthIconY;
    private static int firepowerIconY;
    private static int shieldIconY;
    private static int shootRateBoostIconX, pulseIconX;
    private static int pulseX, pulseY;
    private static Color pulseColor;
    private static String pulseText = "";

    // Level
    private static int levelX = Utility.percWidth(90);
    private static int levelY;
    private static int levelSize = scoreSize;
    private static Font levelFont;

    // Wave
    public static float renderWaveDuration = 1f;
    static int waveLabelY = 160;

    // Timer
    private static int timerX = scoreX;
    private static int timerY;

    // Boss Health
    static int bossBarHeight = 0;


    public HUD() {
        scoreFont = VariableHandler.customFont.deriveFont(0, scoreSize);
        levelFont = VariableHandler.customFont.deriveFont(0, levelSize);

        firepowerIconY = scoreY - Utility.intAtWidth640(16);
        healthIconY = firepowerIconY + Utility.intAtWidth640(18);
        shieldIconY = healthIconY + Utility.intAtWidth640(18);

        healthIconX = Utility.intAtWidth640(8);

        levelY = Utility.percHeight(4);
        timerY = scoreY + Utility.intAtWidth640(25);
    }

    public static void update() {
        if (VariableHandler.pulse) {
            if (VariableHandler.pulseVarialbleToBeNamed/10 % 2 == 0) {
                pulseColor = Color.YELLOW;
            } else
                pulseColor = Color.WHITE;
            pulseText = "CTRL";
            VariableHandler.pulseVarialbleToBeNamed++;
        } else {
            VariableHandler.pulseVarialbleToBeNamed = 0;
            pulseColor = Color.WHITE;
            pulseText = "0";
        }

        if (renderWaveDuration > 0 && (Engine.stateIs(GameState.Game))) {
            renderWaveDuration -= 0.01f;
        }
    }

    public static void render(Graphics g) {
//        g.setColor(Color.RED);
//        g.drawLine(0, deadzoneTop + 8, Engine.WIDTH, deadzoneTop + 8);
        g.drawImage(GameController.getTexture().pickup[2], healthIconX,
                firepowerIconY, null);
        VariableHandler.firepower.renderValue(g);
        g.drawImage(GameController.getTexture().pickup[0], healthIconX,
                healthIconY, null);
        VariableHandler.health.renderBar(g);

        if (VariableHandler.isBossAlive() && !(renderWaveDuration > 0)) {
            renderBossHealth(g);
        }

        if (VariableHandler.shield.getValue() > 0) {
            g.drawImage(GameController.getTexture().pickup[1], healthIconX,
                    shieldIconY, null);
            VariableHandler.shield.renderBar(g);
        }

        renderPulse(g);

        renderScore(g);
        renderScoreMult(g);
        renderWave(g);
        if (Engine.stateIs(GameState.Game)) {
            renderTimer(g);
        }

        renderKillstreak(g);

//            renderFPS(g);
    }

    public static void renderScore(Graphics g) {
        g.setFont(scoreFont);
        g.setColor(scoreColor);
        if (GameController.godMode)
            g.setColor(Color.GRAY);
        g.drawString("Score: " + VariableHandler.getScore(), scoreX, scoreY);
        int offset = 120;

        String scoreMult = "X" + Difficulty.getScoreMultiplier();
        scoreMult = scoreMult.substring(0, 4);

        g.drawString(scoreMult, scoreX - offset, scoreY);
    }

    public static void renderScoreMult(Graphics g) {
        g.setFont(scoreFont);
        g.setColor(scoreColor);
        if (GameController.godMode)
            g.setColor(Color.GRAY);

        int offset = 120;

        String scoreMult = "X" + Difficulty.getScoreMultiplier();
        scoreMult = scoreMult.substring(0, 4);

        g.drawString(scoreMult, scoreX - offset, scoreY);
    }

    public static void renderLevel(Graphics g) {
        g.setFont(levelFont);
        g.setColor(Color.YELLOW);
        g.drawString("Level " + VariableHandler.getLevel(), timerX, levelY);
    }

    public static void renderWave(Graphics g) {
        if (renderWaveDuration > 0 && (Engine.stateIs(GameState.Game))) {
            g.setFont(levelFont);
            g.setColor(Color.YELLOW);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(Utility.makeTransparent(renderWaveDuration));

            String text = "Wave " + VariableHandler.getLevel();

            if (VariableHandler.isBossAlive()) {
                text = "INCOMING!!!";
            }

            g.drawString(text, Engine.WIDTH/2 - Utility.intAtWidth640(40), waveLabelY);

            if (bossBarHeight == 0)
                bossBarHeight = g2d.getFontMetrics().getHeight();

            g2d.setComposite(Utility.makeTransparent(1f));
        }
    }

    public static void renderTimer(Graphics g) {
        g.setFont(levelFont);
        g.setColor(Color.WHITE);
        g.drawString("Timer: " + GameController.getCurrentTime(), timerX, timerY);
    }

    public static void renderPulse(Graphics g) {
        g.setFont(levelFont);

        g.setColor(pulseColor);
        g.drawString(pulseText, pulseX, pulseY);
        g.drawImage(GameController.getTexture().pickup[4], pulseIconX, firepowerIconY, null);
    }

//    public static void renderFPS(Graphics g) {
//        g.setFont(new Font("arial", 1, levelSize));
//        g.setColor(Color.YELLOW);
//        g.drawString("FPS: " + Engine.getFPS(), timerX, shield.getY());
//    }

    protected static void renderBossHealth(Graphics g) {
        int bossBarWidthBack = 400;

        int startX = (Engine.WIDTH - bossBarWidthBack) / 2;

        int y = waveLabelY - bossBarHeight;

        int unitWidth = bossBarWidthBack / VariableHandler.healthBossDefault;
        int bossBarWidthFront = VariableHandler.getHealthBoss() * unitWidth;

        Color backColor = Color.lightGray;
        Color healthColor = Color.RED;
        g.setColor(backColor);
        g.fillRect(startX, y, bossBarWidthBack, bossBarHeight);
        g.setColor(healthColor);
        g.fillRect(startX, y, bossBarWidthFront, bossBarHeight);
    }

    public void setHealthProperties(Attribute health) {
        health.setX(healthIconX + Utility.intAtWidth640(18));
        health.setY(healthIconY + Utility.intAtWidth640(2));
    }

    public void setFirepowerProperties(Attribute firepower) {
        firepower.setMIN(1);
        firepower.setMAX(25);
        firepower.textColor = Color.WHITE;
        firepower.increaseTextColor = Color.RED;
        firepower.setFontSize(scoreSize);
        firepower.setX(healthIconX + Utility.intAtWidth640(18));
        firepower.setY(firepowerIconY + Utility.intAtWidth640(13));
    }

    public void setShieldProperties(Attribute shield) {
        shield.setMIN(0);
        shield.setMAX(100);
        shield.setX(healthIconX + Utility.intAtWidth640(18));
        shield.setY(shieldIconY + Utility.intAtWidth640(2));
        shield.setForegroundColor(Color.blue);
        shield.activateSound = SoundHandler.SHIELD_1;
        shield.deactivateSound = SoundHandler.SHIELD_3;
    }

    public void setPulseProperties(Attribute firepower, int iconSpacingX) {
        pulseY = firepower.getY();
        pulseIconX = shootRateBoostIconX + iconSpacingX;
        pulseX = pulseIconX + Utility.intAtWidth640(20);
    }

    public static void renderKillstreak(Graphics g) {
        g.setColor(Color.RED);

//        Graphics2D g2d = (Graphics2D) g;

//        g2d.setComposite(Utility.makeTransparent(StatePlay.killTimer.getProgress()));

        g.drawString("" + StatePlay.getKillstreak(), 850, timerY);

//        g2d.setComposite(Utility.makeTransparent(1));
    }
}
