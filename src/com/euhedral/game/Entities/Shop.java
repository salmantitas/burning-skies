package com.euhedral.game.Entities;

import com.euhedral.game.VariableManager;

public class Shop {

    public void buyHealth() {
        int cost = VariableManager.health.getCost();
        int healthDefault = VariableManager.health.getDefaultValue();

        if (VariableManager.getScore() >= cost) {
            if (VariableManager.health.getValue() < healthDefault) {
                VariableManager.health.increase(25);
                VariableManager.decreaseScore(cost);
                if (VariableManager.health.getValue() > healthDefault)
                    VariableManager.health.reset();
            } else {
                System.out.println("Health is full");
            }
        } else {
            System.out.println("Not enough score");
        }
    }

    public void buyPower(int power) {
        int cost = VariableManager.costPower;

        if (VariableManager.getScore() >= cost) {
            if (power < VariableManager.getMaxPower()) {
                VariableManager.increasePower(1);
                VariableManager.decreaseScore(cost);
                if (VariableManager.getPower() > VariableManager.getMaxPower())
                    VariableManager.decreasePower(1);
            } else {
                System.out.println("Max power is reached");
            }
        } else {
            System.out.println("Not enough score");
        }
    }

    public void buyGround() {
        int cost = VariableManager.costGround;
        if (VariableManager.getScore() >= cost) {
            if (!VariableManager.gotGround()) {
                VariableManager.decreaseScore(cost);
                VariableManager.setGround(true);
            }
        } else {
            System.out.println("Not enough score");
        }
    }
}
