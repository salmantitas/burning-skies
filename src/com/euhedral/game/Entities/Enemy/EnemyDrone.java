package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EnemyDrone extends Enemy{

    double destinationX, destinationY;
    int updateSkip = 0;

    public EnemyDrone(int x, int y, int levelHeight) {
        super(x, y, levelHeight);
//        enemyType = EntityHandler.TYPE_DRONE;
        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyDrone[0]);
        width = 32;
        height = 32;
    }

    public EnemyDrone(int x, int y, Color color, int levelHeight) {
        this(x, y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

//        enemyType = EntityHandler.TYPE_DRONE;

//        power = 1;
//        shootTimerDefault = 250;
//        minVelX = 2f;
        score = 25;
        damage = 15;
        commonInit();
    }

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
        this.setHealth(1);
        forwardVelocity = 4f;
//        velX = minVelX;
        updateDestination();
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }

    private void updateDestination() {
        destinationX = EntityHandler.playerX;
        destinationY = EntityHandler.playerY;
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
        Rectangle2D r = getBounds();

        Graphics2D g2d = (Graphics2D) g;

        g2d.draw(r);
    }

    public boolean checkCollision(Rectangle2D object) {
        Rectangle2D r = getBounds();
        return object.intersects(r);
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_DRONE;
    }

//    @Override
//    public int getTurretX() {
//        return (int) x + width/2 - Utility.intAtWidth640(2);
//    }
}
