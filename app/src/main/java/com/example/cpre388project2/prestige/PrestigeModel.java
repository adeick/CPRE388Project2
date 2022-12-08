package com.example.cpre388project2.prestige;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel to handle player's prestige.
 */
public class PrestigeModel extends ViewModel {
    private MutableLiveData<Integer> prestigeLevel;

    /**
     * Get the current prestige level.
     *
     * @return MutableLiveData of prestigeLevel.
     */
    public MutableLiveData<Integer> getPrestigeLevel() {
        checkNull();
        return prestigeLevel;
    }

    /**
     * Sets the current prestige level.
     *
     * @param level New prestige level.
     */
    public void setPrestigeLevel(int level) {
        checkNull();
        prestigeLevel.setValue(level);
    }

    /**
     * Upgrade the prestige level to the next level.
     */
    public void upgradeToNextPrestige() {
        checkNull();
        prestigeLevel.setValue(prestigeLevel.getValue() + 1);
    }

    /**
     * Check whether the player can afford the next prestige level with their amount of prestige.
     *
     * @param bitcoinAmount The amount of bitcoin the player has.
     * @return Whether the player can level up.
     */
    public boolean canAffordNextPrestige(long bitcoinAmount) {
        return bitcoinAmount > nextPrestigeCost();
    }

    /**
     * Calculate how much bitcoin is required for the next prestige level. The cost for the
     * next prestige is calculated by doubling the cost of the last prestige level. The first
     * prestige upgrade costs 5000.
     *
     * @return How much it costs to level up to the next prestige.
     */
    public long nextPrestigeCost() {
        checkNull();
        return (long) (Math.pow(prestigeLevel.getValue(), 2) * 5000);
    }

    private void checkNull() {
        if (prestigeLevel == null) {
            prestigeLevel = new MutableLiveData<>(1);
        }
    }
}
