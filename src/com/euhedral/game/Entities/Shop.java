package com.euhedral.game.Entities;

import com.euhedral.game.Attribute;
import com.euhedral.game.VariableManager;

public class Shop {

    public void buyHealth() {
        buyAbstract(VariableManager.health, 25, "Health is full");
    }

    public void buyPower() {
        buyAbstract(VariableManager.power, 1, "Max power has been reached");
    }

    public void buyShield() {
        buyAbstract(VariableManager.shield, 50, "Shield is full");
    }

    public void buyGround() {
        int cost = VariableManager.ground.getCost();
        if (VariableManager.getScore() >= cost) {
            if (!VariableManager.gotGround()) {
                VariableManager.decreaseScore(cost);
                VariableManager.setGround(true);
            }
        } else {
            System.out.println("Not enough score");
        }
    }

    public void buyAbstract(Attribute attribute, int value, String message) {
        int cost = attribute.getCost();
        int valueMax = attribute.getMAX();

        if (VariableManager.getScore() >= cost) {
            if (attribute.getValue() < valueMax) {
                attribute.increase(value);
                VariableManager.decreaseScore(cost);
            } else {
                System.out.println(message);
            }
        } else {
            System.out.println("Not enough score");
        }
    }
}
