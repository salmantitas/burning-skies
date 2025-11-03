package com.euhedral.Game.UI.Menus;

import com.euhedral.Engine.Engine;
import com.euhedral.Engine.GameState;
import com.euhedral.Engine.UI.Button;
import com.euhedral.Engine.UI.ButtonNav;
import com.euhedral.Engine.UI.ButtonOption;
import com.euhedral.Engine.UI.Menu;
import com.euhedral.Engine.Utility;
import com.euhedral.Game.Difficulty;
import com.euhedral.Game.GameController;
import com.euhedral.Game.SoundHandler;
import com.euhedral.Game.UI.HUD;
import com.euhedral.Game.VariableHandler;

import java.awt.*;

public class MenuDifficulty extends Menu {

    // Navigation

    int difficultyY = y40;
    int difficultyX = x10;
    int damageDealtY;
    int damageTakenY;
    int gameSpeedY;
    int firePowerLossY;
    int enemyFireRateY;
    int enemyBulletSpeedY;
    int backY;

    ButtonOption difficulty;
    ButtonOption damageDealt;
    ButtonOption damageTaken;
    ButtonOption gameSpeed;
    Button firePowerLoss;
    ButtonOption enemyFireRate;
    ButtonOption enemyBulletSpeed;

    ButtonNav back;


    public MenuDifficulty() {
        super(GameState.Difficulty);
        MAXBUTTON = 8;
        options = new Button[MAXBUTTON];

        spacingY = Utility.percHeight(5);

        damageDealtY = difficultyY + spacingY;
        damageTakenY = damageDealtY + spacingY;
        gameSpeedY = damageTakenY + spacingY;
        firePowerLossY = gameSpeedY + spacingY;
        enemyFireRateY = firePowerLossY + spacingY;
        enemyBulletSpeedY = enemyFireRateY + spacingY;
        int startY = enemyBulletSpeedY + spacingY;
        backY = startY  + spacingY;

        difficulty = new ButtonOption(difficultyX, difficultyY, Utility.perc(buttonSize, 60), "Difficulty");
        difficulty.setIncreaseActivate(Difficulty::increaseDifficulty);
        difficulty.setDecreaseActivate(Difficulty::decreaseDifficulty);

        damageDealt = new ButtonOption(difficultyX, damageDealtY, Utility.perc(buttonSize, 60), "Damage Dealt");
        damageDealt.setIncreaseActivate(Difficulty::increaseDamageDealt);
        damageDealt.setDecreaseActivate(Difficulty::decreaseDamageDealt);

        damageTaken = new ButtonOption(difficultyX, damageTakenY, Utility.perc(buttonSize, 60), "Damage Taken");
        damageTaken.setIncreaseActivate(Difficulty::increaseDamageTaken);
        damageTaken.setDecreaseActivate(Difficulty::decreaseTakenDealt);

        gameSpeed = new ButtonOption(difficultyX, gameSpeedY, Utility.perc(buttonSize, 60), "Game Speed");
        gameSpeed.setIncreaseActivate(Difficulty::increaseGameSpeed);
        gameSpeed.setDecreaseActivate(Difficulty::decreaseGameSpeed);

        firePowerLoss = new Button(difficultyX, firePowerLossY, Utility.perc(buttonSize, 60), "Firepower Loss");
        firePowerLoss.setActivate(Difficulty::toggleFirePowerLoss);

        enemyFireRate = new ButtonOption(difficultyX, enemyFireRateY, Utility.perc(buttonSize, 60), "Enemy Fire Rate");
        enemyFireRate.setIncreaseActivate(Difficulty::increaseEnemyFireRate);
        enemyFireRate.setDecreaseActivate(Difficulty::decreaseEnemyFireRate);

        enemyBulletSpeed = new ButtonOption(difficultyX, enemyBulletSpeedY, Utility.perc(buttonSize, 60), "Enemy Bullet Speed");
        enemyBulletSpeed.setIncreaseActivate(Difficulty::increaseEnemyBulletSpeed);
        enemyBulletSpeed.setDecreaseActivate(Difficulty::decreaseEnemyBulletSpeed);

        back = new ButtonNav(difficultyX, backY, Utility.perc(buttonSize, 60), "Back", GameState.Transition);

        options[0] = difficulty;
        options[1] = damageDealt;
        options[2] = damageTaken;
        options[3] = gameSpeed;
        options[4] = firePowerLoss;
        options[5] = enemyFireRate;
        options[6] = enemyBulletSpeed;
        options[7] = back;
    }


    @Override
    protected void reassignSelected(int reassign) {
        super.reassignSelected(reassign);
        Difficulty.setCurrentButton(getIndex());
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        HUD.renderScore(g);
        HUD.renderScoreMult(g);

        // Render Screen Text

        g.setFont(new Font("arial", 1, Utility.percWidth(5)));
        g.setColor(Color.WHITE);

        Difficulty.render(g);
        renderIcon(g, firePowerLoss, Difficulty.isFirePowerLoss());

        super.postRender(g);
    }

}
