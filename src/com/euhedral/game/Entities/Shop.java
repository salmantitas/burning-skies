package com.euhedral.game.Entities;

import com.euhedral.game.Attribute;
import com.euhedral.game.VariableHandler;

public class Shop {

    public void buyHealth() {
        buyAbstract(VariableHandler.health, 25, "Health is full");
    }

    public void buyPower() {
        buyAbstract(VariableHandler.power, 1, "Max power has been reached");
    }

    public void buyShield() {
        buyAbstract(VariableHandler.shield, 50, "Shield is full");
    }

    public void buyGround() {
        int cost = VariableHandler.ground.getCost();
        if (VariableHandler.getScore() >= cost) {
            if (!VariableHandler.gotGround()) {
                VariableHandler.decreaseScore(cost);
                VariableHandler.setGround(true);
            }
        } else {
            System.out.println("Not enough score");
        }
    }

    public void buyAbstract(Attribute attribute, int value, String message) {
        int cost = attribute.getCost();
        int valueMax = attribute.getMAX();

        if (VariableHandler.getScore() >= cost) {
            if (attribute.getValue() < valueMax) {
                attribute.increase(value);
                VariableHandler.decreaseScore(cost);
            } else {
                System.out.println(message);
            }
        } else {
            System.out.println("Not enough score");
        }
    }
}
