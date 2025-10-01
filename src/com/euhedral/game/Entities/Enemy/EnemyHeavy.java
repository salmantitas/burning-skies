package com.euhedral.Game.Entities.Enemy;

import com.euhedral.Engine.Utility;
import com.euhedral.Game.EntityHandler;
import com.euhedral.Game.GameController;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class EnemyHeavy extends Enemy {

    int movementDistance_MAX = 3*64;

    int leftTurret, rightTurret;
    boolean turretLeft = true;

    int bulletAngleMIN = 60;
    int bulletAngleMAX = 90;
    int bulletAngleINC = 30;

    double destinationX, destinationY;
    int offsetLeft = 8, offsetRight = 2;

    boolean secondsTillShotFire;
    double tempAngle;
    boolean playerInRange;
    boolean rangeCheck1, rangeCheck2;

    public EnemyHeavy(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

//        bulletVelocity = Utility.intAtWidth640(5);
        bulletAngle = 60;
        shootTimerDefault = 80;
//        shootTimer = shootTimerDefault;
        score = 50;
        leftTurret = width / 3 - Utility.intAtWidth640(2);
        rightTurret = 2 * width / 3 - Utility.intAtWidth640(2);

        // stub
        velX_MIN = 1;
        movementDistance = movementDistance_MAX;
        setHMove(1);

//        attackEffect = true;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyHeavy[0]);

        velX = 0;
        velY_MIN = 1.7f;
        forwardVelocity = EntityHandler.backgroundScrollingSpeed;
//        distance = 0; // stub ; width * 2;
//        movementDistance = distance;
        health_MAX = 4;
        commonInit();
        damage = 60;
    }

    public EnemyHeavy(int x, int y, Color color, int levelHeight) {
        this(x,y, levelHeight);
        this.color = color;
    }

//    @Override
//    public void initialize() {
//        super.initialize();
//
//
//    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreenY) {
            updateDestination();

            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
//                velX = 0;
                if (hMove.equals(HorizontalMovement.LEFT)) {
                    setHMove(-1);
                } else {
                    setHMove(1);
                }
                movementDistance = movementDistance_MAX;
            }
        }
//
//        if (state == STATE_EXPLODING) {
////            explosion.runAnimation();
//            if (explosion.playedOnce) {
////                disable();
//            }
//        }
    }

    @Override
    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g.setColor(Color.red);

                g2d = (Graphics2D) g;
                g.setColor(Color.RED);


                attackPathX = pos.x - (0.5) * (double) width;
                if (bulletAngle == bulletAngleMIN) {
                    attackPathY = getTurretY() - (double) height;
                } else {
                    attackPathY = getTurretY() - (0.5) * (double) height;
                }

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(getBulletAngle()) - bulletArcAngle / 2, bulletArcAngle);

                if (bulletAngle == bulletAngleMIN)
                    g2d.fillArc((int) attackPathX, (int) attackPathY, 2 * width, 2 * height, (int) -(90 + (90 - getBulletAngle())) - bulletArcAngle / 2, bulletArcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    @Override
    protected void shootDefault() {
        bulletsPerShot += 2;
    }

//    @Override
//    protected void shoot() {
//        super.shoot();
//        shot += 2;
////        shootDownDefault();
////        moveShoot();
//    }


    private void incrementBulletAngle() {
        bulletAngle += bulletAngleINC;
        if (bulletAngle > bulletAngleMAX)
            bulletAngle = bulletAngleMIN;
    }

//    @Override
//    protected void shootDownDefault() {
//        shot += 2;
//        bulletAngle += bulletAngleINC;
//        if (bulletAngle > bulletAngleMAX)
//            bulletAngle = bulletAngleMIN;
//    }

//    @Override
//    public void move() {
//        super.move();
////        moveHorizontally();
//    }

    private void moveShoot() {
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width), y + height / 2, 90));
    }

//    public void moveHorizontally() {
//        if (movementDistance > 0) {
//            movementDistance--;
//        } else {
//            movementDistance = distance;
//        }
//
//        int
//                int0 = 0,
//                int1 = Utility.perc(distance, 30),
//                int2 = Utility.perc(distance, 50),
//                int3 = Utility.perc(distance, 80);
//
//
//        if (movementDistance <= distance && movementDistance > int3) {
//            hMove = HorizontalMovement.LEFT;
//        } else if (movementDistance <= int3 && movementDistance > int2 || movementDistance <= int1 && movementDistance > int0) {
//            hMove = HorizontalMovement.NONE;
//        } else if (movementDistance <= int2 && movementDistance > int1) {
//            hMove = HorizontalMovement.RIGHT;
//        }
////        } else if (movementTimer <= int1 && movementTimer > int0) {
////            hMove = HorizontalMovement.NONE;
////        }
//    }

    @Override
    protected void commonInit() {
        this.setHealth(health_MAX);
        velY = forwardVelocity;
        bulletAngle = bulletAngleMIN;
    }

    private void updateDestination() {
        destinationX = EntityHandler.playerPositon.x;
        destinationY = EntityHandler.playerPositon.y;
    }

    @Override
    public int getTurretX() {
        if (turretLeft) {
            turretLeft = !turretLeft;
            return (int) pos.x + width / 3 - Utility.intAtWidth640(2);
        }
        else {
            turretLeft = !turretLeft;
            return (int) pos.x + 2 * width / 3 - Utility.intAtWidth640(2);
        }
    }

    @Override
    public double getBulletAngle() {

        if (turretLeft) {
            tempAngle = bulletAngle;
        }
        else {
            tempAngle = 90 + (90 - bulletAngle);
        }
//        boolean bothShotsFired = (shot == 1);
//        if (bothShotsFired)
//            incrementBulletAngle();

        rangeCheck1 = (destinationX - offsetLeft > pos.x) && (destinationX - offsetLeft < pos.x + width);
        rangeCheck2 = (destinationX + 32 + offsetRight < pos.x + width) && (destinationX + 32 + offsetRight > pos.x);
        playerInRange = rangeCheck1 || rangeCheck2;

        if (playerInRange)
            tempAngle = 90;
        return tempAngle;
    }

    @Override
    protected void setEnemyType() {
        enemyType = VariableHandler.TYPE_HEAVY;
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }
}
