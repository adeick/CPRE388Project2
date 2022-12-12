package com.example.cpre388project2.storage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel responsible for storing bitcoins.
 */
public class BitcoinStorageModel extends ViewModel {
    private MutableLiveData<Integer> storageLevel;
    private MutableLiveData<Integer> storageCount;
    private MutableLiveData<Long> currentAmountStored;

    /**
     * Initialize to default values.
     */
    public void initialize() {
        initialize(1, 0, 1);
    }

    /**
     * Initialize to specified values.
     *
     * @param level  Level of bitcoin storage.
     * @param amount Amount of bitcoins in storage.
     */
    public void initialize(int level, long amount, int count) {
        if (storageLevel == null) {
            storageLevel = new MutableLiveData<>(level);
        }
        if (currentAmountStored == null) {
            currentAmountStored = new MutableLiveData<>(amount);
        }
        if (storageCount == null) {
            storageCount = new MutableLiveData<>(count);
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
     * Retrieve the number of storage containers.
     *
     * @return The number of storage containers.
     */
    public int getStorageCount() {
        return storageCount.getValue();
    }

    /**
     * Adds specified amount of storage containers.
     *
     * @param amount
     */
    public void addStorageContainer(int amount) {
        storageCount.setValue(storageCount.getValue() + amount);
    }

    /**
     * Add new storage container.
     */
    public void addStorageContainer() {
        addStorageContainer(1);
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
    public long storeAmount(long amount) {
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
    public long retrieveAmount(long amount) {
        if (currentAmountStored.getValue() > 0) {
            amount = Math.max(0, amount);
            if (amount > currentAmountStored.getValue()) {
                amount = currentAmountStored.getValue();
                currentAmountStored.setValue(0L);
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
    public MutableLiveData<Long> getAmountStored() {
        return currentAmountStored;
    }

    /**
     * Calculates the upgrade cost of storage based on level.
     *
     * @return Upgrade cost in bitcoins.
     */
    public long getUpgradeCost() {
        return (long) (Math.pow(2, storageLevel.getValue() * 2) * 100);
    }

    /**
     * Calculates the storage capacity based on level.
     *
     * @return Number of bitcoins that can be stored.
     */
    private long storageCapacity() {
        return (long) (storageCount.getValue() * (Math.pow(2, storageLevel.getValue() * 3) * 100));
    }
}
