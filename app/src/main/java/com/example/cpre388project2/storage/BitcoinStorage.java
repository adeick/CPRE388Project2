package com.example.cpre388project2.storage;

public class BitcoinStorage {
    private int storageLevel;
    private int currentAmountStored;

    public BitcoinStorage() {
        storageLevel = 0;
    }

    public BitcoinStorage(int level) {
        storageLevel = level;
    }

    /**
     * Retrieve the storage's current level.
     *
     * @return The storage's current level.
     */
    public int getStorageLevel() {
        return storageLevel;
    }

    /**
     * Upgrade the storage by one level.
     */
    public void upgradeStorage() {
        upgradeStorage(1);
    }

    /**
     * Upgrade the storage by the specified increase.
     *
     * @param increase The amount the storage's level will increase by.
     */
    public void upgradeStorage(int increase) {
        storageLevel += increase;
    }

    /**
     * Store the specified amount of bitcoin. The value is capped to only allow positive values.
     *
     * @param amount The amount of bitcoin to be stored.
     */
    public void storeAmount(int amount) {
        currentAmountStored += Math.max(1, amount);
    }

    /**
     * Retrieve the amount of bitcoin from storage. The value is capped to only allow positive values.
     * The storage will never go below 0, which means the amount retrieved might be different
     * from the value passed in.
     *
     * @param amount The amount to be retrieved from storage
     * @return How much was actually retrieved from storage.
     */
    public int retrieveAmount(int amount) {
        amount = Math.max(1, amount);
        if (amount > currentAmountStored) {
            amount = currentAmountStored;
            currentAmountStored = 0;
        } else {
            currentAmountStored -= amount;
        }
        return amount;
    }


    public int getAmountStored() {
        return currentAmountStored;
    }

    private int storageCapacity() {
        return storageLevel * 200;
    }
}
