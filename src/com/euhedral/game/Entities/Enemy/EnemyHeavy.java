package com.euhedral.game.Entities.Enemy;

import com.euhedral.engine.Utility;
import com.euhedral.game.EntityHandler;
import com.euhedral.game.GameController;

import java.awt.*;

public class EnemyHeavy extends Enemy {

    boolean turretLeft = true;
    int bulletAngleMIN = 60;
    int bulletAngleMAX = 90;
    int bulletAngleINC = 30;

    public EnemyHeavy(int x, int y, int levelHeight) {
        super(x, y, levelHeight);

        bulletVelocity = Utility.intAtWidth640(5);
        bulletAngle = 60;
        shootTimerDefault = 100;
//        attackEffect = true;

        textureHandler = GameController.getTexture();
        setImage(textureHandler.enemyHeavy[0]);
    }

    public EnemyHeavy(int x, int y, Color color, int levelHeight) {
        this(x,y, levelHeight);
        this.color = color;
    }

    @Override
    public void initialize() {
        super.initialize();

//        enemyType = EntityHandler.TYPE_HEAVY;

//        width = width*2; // todo: check, it's being called twice
        shootTimerDefault = 200;
//        shootTimer = shootTimerDefault;
        score = 150;
        velX = 0;
        minVelY = 1.75f;
        distance = 0; // stub ; width * 2;
        movementDistance = distance;
        healthMAX = 6;
        commonInit();
        damage = 60;
    }

    @Override
    public void update() {
        super.update();
        if (state == STATE_ACTIVE && inscreen) {
            if (movementDistance >= 0) {
                movementDistance -= Math.abs(velX);
            } else {
                velX = 0;
            }
        }

//        if (state == STATE_EXPLODING) {
////            explosion.runAnimation();
//            if (explosion.playedOnce) {
////                disable();
//            }
//        }
    }

//    @Override
//    public void render(Graphics g) {
//        if (attackEffect) {
//            boolean secondsTillShotFire = (shootTimer < 20);
//            if (isActive() && secondsTillShotFire) {
//                g.setColor(Color.red);
//
//                Graphics2D g2d = (Graphics2D) g;
//                g.setColor(Color.RED);
//
//
//                double drawX = x - (0.5) * (double) width;
//                double drawY = y - (0.5) * (double) height;
//                int arcAngle = 20;
//
//                g2d.setComposite(Utility.makeTransparent(0.5f));
//                g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(getBulletAngle()) - arcAngle / 2, arcAngle);
//
//                g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(90 + (90 - getBulletAngle())) - arcAngle / 2, arcAngle);
//                g2d.setComposite(Utility.makeTransparent(1f));
//            }
//        }
//
//        g.setColor(color);
//        super.render(g);
//    }

    @Override
    protected void renderAttackPath(Graphics g) {
        if (attackEffect) {
            boolean secondsTillShotFire = (shootTimer < 20);
            if (isActive() && secondsTillShotFire) {
                g.setColor(Color.red);

                Graphics2D g2d = (Graphics2D) g;
                g.setColor(Color.RED);


                double drawX = x - (0.5) * (double) width;
                double drawY;
                if (bulletAngle == bulletAngleMIN) {
                    drawY = getTurretY() - (double) height;
                } else {
                    drawY = getTurretY() - (0.5) * (double) height;
                }
                int arcAngle = 20;

                g2d.setComposite(Utility.makeTransparent(0.5f));
                g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(getBulletAngle()) - arcAngle / 2, arcAngle);

                if (bulletAngle == bulletAngleMIN)
                    g2d.fillArc((int) drawX, (int) drawY, 2 * width, 2 * height, (int) -(90 + (90 - getBulletAngle())) - arcAngle / 2, arcAngle);
                g2d.setComposite(Utility.makeTransparent(1f));
            }
        }
    }

    @Override
    protected void shoot() {
        super.shoot();
        shot += 2;
//        shootDownDefault();
//        moveShoot();
    }

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

    @Override
    public void move() {
        super.move();
        moveHorizontally();
    }

    private void moveShoot() {
//        bullets.add(new BulletEnemy((int) (1.1 * x), y + height / 2, 90));
//        bullets.add(new BulletEnemy(x + (int) (0.8 * width), y + height / 2, 90));
    }

    public void moveHorizontally() {
        if (movementDistance > 0) {
            movementDistance--;
        } else {
            movementDistance = distance;
        }

        int
                int0 = 0,
                int1 = Utility.perc(distance, 30),
                int2 = Utility.perc(distance, 50),
                int3 = Utility.perc(distance, 80);


        if (movementDistance <= distance && movementDistance > int3) {
            hMove = HorizontalMovement.LEFT;
        } else if (movementDistance <= int3 && movementDistance > int2 || movementDistance <= int1 && movementDistance > int0) {
            hMove = HorizontalMovement.NONE;
        } else if (movementDistance <= int2 && movementDistance > int1) {
            hMove = HorizontalMovement.RIGHT;
        }
//        } else if (movementTimer <= int1 && movementTimer > int0) {
//            hMove = HorizontalMovement.NONE;
//        }
    }

    @Override
    protected void commonInit() {
        this.setHealth(healthMAX);
        velY = forwardVelocity;
        bulletAngle = bulletAngleMIN;
    }

    @Override
    public int getTurretX() {
        if (turretLeft) {
            turretLeft = !turretLeft;
            return (int) x + width / 3 - Utility.intAtWidth640(2);
        }
        else {
            turretLeft = !turretLeft;
            return (int) x + 2 * width / 3 - Utility.intAtWidth640(2);
        }
    }

    @Override
    public double getBulletAngle() {
        double tempAngle;
        if (turretLeft) {
            tempAngle = bulletAngle;
        }
        else {
            tempAngle = 90 + (90 - bulletAngle);
        }
        boolean bothShotsFired = (shot == 1);
        if (bothShotsFired)
            incrementBulletAngle();
        return tempAngle;
    }

    @Override
    protected void setEnemyType() {
        enemyType = EntityHandler.TYPE_HEAVY;
    }

//    @Override
//    public void resurrect(int x, int y) {
//        commonInit();
//        explosion.playedOnce = false;
//        super.resurrect(x, y);
//    }
}
