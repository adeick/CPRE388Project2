package com.example.cpre388project2.towers;

/**
 * Individual Tower that produces bitcoins.
 */
public class Tower {

    private TowerTypes towerType;
    private int towerLevel;
    private int towerCount;

    /**
     * Constructs a Tower.
     *
     * @param _towerType TowerType enum for type of tower.
     */
    public Tower(TowerTypes _towerType) {
        this(_towerType, 1, 1);
    }

    /**
     * Constructs a Tower.
     *
     * @param _towerType  TowerType enum for type of tower.
     * @param _towerLevel Level of tower.
     * @param _towerCount Number of towers.
     */
    public Tower(TowerTypes _towerType, int _towerLevel, int _towerCount) {
        towerType = _towerType;
        towerLevel = _towerLevel;
        towerCount = _towerCount;
    }

    /**
     * Retrieve the type that this tower belongs to.
     *
     * @return The Tower Type.
     */
    public TowerTypes getTowerType() {
        return towerType;
    }

    /**
     * Returns number of towers.
     *
     * @return Number of towers.
     */
    public int getTowerCount() {
        return towerCount;
    }

    /**
     * Retrieve the total amount of bitcoin being produced by all towers of this type per second.
     * Calculated as (towerLevel * towerProductionRate * numberOfTowers).
     *
     * @return The amount of bitcoin this tower type passively produces each second.
     */
    public long getProductionRate() {
        return towerLevel * towerTypeProductionRate() * towerCount;
    }

    /**
     * Calculates upgrade cost.
     *
     * @return Cost in bitcoins.
     */
    public long getUpgradeCost() {
        return (long) Math.pow(2, towerLevel);
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
     * Add one more tower of this type.
     */
    public void addTower() {
        addTowers(1);
    }

    /**
     * Add more towers of this type.
     *
     * @param count How many towers that should be added.
     */
    public void addTowers(int count) {
        towerCount += count;
    }

    /**
     * Calculates cost to buy another tower of given type.
     *
     * @param towerType TowerType enum.
     * @return Cost in bitcoins.
     */
    public static long getPurchaseCost(TowerTypes towerType) {
        switch (towerType) {
            case SERVER:
                return 10;
            case MICROPROCESSOR:
                return 200;
            case GPU:
                return 300;
            case QUANTUMCOMPUTER:
                return 800;
            default:
                return Integer.MAX_VALUE;
        }
    }

    private long towerTypeProductionRate() {
        switch (towerType) {
            case MAINFRAME:
                return 1;
            case SERVER:
                return 10;
            case MICROPROCESSOR:
                return 50;
            case GPU:
                return 100;
            case QUANTUMCOMPUTER:
                return 500;
            default:
                return 0;
        }
    }
}
