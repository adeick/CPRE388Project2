package com.example.cpre388project2.prestige;

public class Prestige {
    private int prestigeLevel;

    public Prestige() {
    }

    /**
     * Upgrade the prestige level to the next level.
     */
    public void upgradeToNextPrestige() {
        prestigeLevel++;
    }

    /**
     * Check whether the player can afford the next prestige level with their amount of prestige.
     *
     * @param bitcoinAmount The amount of bitcoin the player has.
     * @return Whether the player can level up.
     */
    public boolean canAffordNextPrestige(int bitcoinAmount) {
        return bitcoinAmount > nextPrestigeCost();
    }

    /**
     * Calculate how much bitcoin is required for the next prestige level. The cost for the
     * next prestige is calculated by doubling the cost of the last prestige level. The first
     * prestige upgrade costs 5000.
     *
     * @return How much it costs to level up to the next prestige.
     */
    public int nextPrestigeCost() {
        return (int) (Math.pow(prestigeLevel, 2) * 5000);
    }
}
