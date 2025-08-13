package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;
import com.euhedral.game.VariableHandler;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyDrone1 extends Enemy{

    double destinationX, destinationY;
    int updateSkip = 0;

    public EnemyDrone1(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        score = 10;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyDrone[0]);
        width = Utility.intAtWidth640(16);
        height = width;

        health_MAX = 1;
        damage = 15;
        shootTimerFirst = 100;
        commonInit();
    }

    public EnemyDrone1(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//    }

    @Override
    protected void shoot() {
        // do nothing
    }

    @Override
    public void update() {
        super.update();
        if (updateSkip == 10) {
            updateDestination();
            calculateVelocities(destinationX, destinationY);
            updateSkip = 0;
        } else {
            updateSkip++;
        }
    }

    @Override
    public void move() {
        if (isActive()) {
            // fix wobbling
//            if (destinationX > x) {
//                velX = minVelX;
//            } else {
//                velX = -minVelX;
//            }
//            if (destinationY > y) {
//                velY = forwardVelocity; // todo: temp solution
//            } else {
//                velY = -forwardVelocity; // todo: find why this is double
//            }
        } else if (isExploding()) {
            velY = explodingVelocity;
            velX = 0;
        }
        moveInScreen();
    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        forwardVelocity = 5;
        shootTimer = shootTimerFirst;
//        velX = minVelX;
        updateDestination();
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    protected void updateDestination() {
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY;
    }

    @Override
    protected void drawDamageImage() {
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int) x, (int) y, width, height);
    }

//    @Override
//    public void render(Graphics g) {
//        super.render(g);
//        renderBounds(g);
//
////        g.drawString("y-cord: " + y, (int) x, (int) y);
//    }

    @Override
    protected void renderBounds(Graphics g) {
        g.setColor(Color.green);
        bounds = getBounds();

        g2d = (Graphics2D) g;

        g2d.draw(bounds);
    }

    public boolean checkCollision(Rectangle2D object) {
        bounds = getBounds();
        return object.intersects(bounds);
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE1;
    }

//    @Override
//    public int getTurretX() {
//        return (int) x + width/2 - Utility.intAtWidth640(2);
//    }
}
