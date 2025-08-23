package com.euhedral.game.Entities;

import com.euhedral.game.Attribute;
import com.euhedral.game.VariableHandler;

public class Shop {

    public void buyHealth() {
        buyAbstract(VariableHandler.health, 25, "Health is full");
    }

    public void buyPower() {
        buyAbstract(VariableHandler.firepower, 1, "Max firepower has been reached");
    }

    public void buyShield() {
        buyAbstract(VariableHandler.shield, 50, "Shield is full");
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
