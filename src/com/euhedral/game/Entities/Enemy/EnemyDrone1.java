package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.Entities.Enemy.Component.Tracker;
import com.euhedral.Game.Pool.ProjectilePool;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyDrone1 extends Enemy{

    Tracker tracker;
//    double destinationX, destinationY;
    int updateSkip = 0;

    public EnemyDrone1(int x, int y, ProjectilePool projectiles, int levelHeight) {
        super(x, y, projectiles, levelHeight);
//        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyDrone[0]);

        score = 10;

        width = Utility.intAtWidth640(16);
        height = width;

        tracker = new Tracker();

        health_MAX = 1;
        damage = 20;
        commonInit();
    }

//    public EnemyDrone1(int x, int y, Color color, int levelHeight) {
//        this(x, y, levelHeight);
//        this.color = color;
//    }

    @Override
    protected void shoot2() {
        // do nothing
    }

    @Override
    public void update() {
        super.update();
            if (updateSkip == 10) {
                tracker.updateDestination();
                calculateVelocities(tracker.destinationX, tracker.destinationY);
                updateSkip = 0;
            } else {
                updateSkip++;
            }
    }

    @Override
    protected void updateBounds() {
        collisionBox.setBounds( pos.x, pos.y, width, height);
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
        tracker.updateDestination();
        calibrateShootTimerFirst();
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }


    @Override
    protected void renderDamageImage() {
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int) pos.x, (int) pos.y, width, height);
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

        g2d = (Graphics2D) g;

        g2d.draw(getBounds());
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_DRONE1;
    }
}
