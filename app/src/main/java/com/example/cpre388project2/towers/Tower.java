package com.example.cpre388project2.towers;

import com.example.cpre388project2.Damageable;

public class Tower extends Damageable {
    private int towerLevel;
    private float currentTowerHealth;

    public Tower() {
        towerLevel = 0;
    }

    public Tower(int level) {
        towerLevel = level;
    }

    /**
     * Retrieve the tower's current level.
     *
     * @return The tower's current level.
     */
    public int getTowerLevel() {
        return towerLevel;
    }

    /**
     * Upgrade the tower by one level.
     */
    public void upgradeTower() {
        upgradeTower(1);
    }

    /**
     * Upgrade the tower by the specified increase.
     *
     * @param increase The amount the tower's level will increase by.
     */
    public void upgradeTower(int increase) {
        towerLevel += increase;
    }

    /**
     * Retrieve the amount of bitcoin the tower should produce each second.
     *
     * @return The amount of bitcoin this tower passively produces each second.
     */
    public int getProductionRate() {
        return towerLevel * 2;
    }

    @Override
    protected int getMaxHealth() {
        return 50 + towerLevel * 50;
    }
}
