package com.example.cpre388project2.storage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BitcoinStorageModel extends ViewModel {
    private MutableLiveData<Integer> storageLevel;
    private MutableLiveData<Integer> currentAmountStored;

    public void initialize() {
        initialize(1, 0);
    }

    public void initialize(int level, int amount) {
        if (storageLevel == null) {
            storageLevel = new MutableLiveData<>(level);
        }
        if (currentAmountStored == null) {
            currentAmountStored = new MutableLiveData<>(amount);
        }
    }

    /**
     * Retrieve the storage's current level.
     *
     * @return The storage's current level.
     */
    public int getStorageLevel() {
        return storageLevel.getValue();
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
        storageLevel.setValue(storageLevel.getValue() + increase);
    }

    /**
     * Store the specified amount of bitcoin. The value is capped to only allow positive values.
     * Storage is capped to only storage as much as the storage capacity.
     *
     * @param amount The amount of bitcoin to be stored.
     * @return How much bitcoin was actually stored.
     */
    public int storeAmount(int amount) {
        if (currentAmountStored.getValue() < storageCapacity()) {
            amount = Math.max(1, amount);
            if (amount + currentAmountStored.getValue() > storageCapacity()) {
                amount = currentAmountStored.getValue();
                currentAmountStored.setValue(storageCapacity());
            } else {
                currentAmountStored.setValue(currentAmountStored.getValue() + amount);
            }
            return amount;
        } else {
            return 0;
        }
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
        if (currentAmountStored.getValue() > 0) {
            amount = Math.max(0, amount);
            if (amount > currentAmountStored.getValue()) {
                amount = currentAmountStored.getValue();
                currentAmountStored.setValue(0);
            } else {
                currentAmountStored.setValue(currentAmountStored.getValue() - amount);
            }
            return amount;
        } else {
            return 0;
        }
    }

    /**
     * Get the amount of bitcoin stored in storage.
     *
     * @return The amount of bitcoin stored.
     */
    public MutableLiveData<Integer> getAmountStored() {
        return currentAmountStored;
    }

    public int getUpgradeCost() {
        return (int) (Math.pow(2, storageLevel.getValue() * 2) * 100);
    }

    private int storageCapacity() {
        return (int) (Math.pow(2, storageLevel.getValue() * 3) * 100);
    }
}
