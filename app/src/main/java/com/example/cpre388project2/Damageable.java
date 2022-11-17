package com.example.cpre388project2;

public abstract class Damageable {
    private float currentHealth;

    /**
     * Retrieve the current health of the object.
     *
     * @return The current health of the object.
     */
    public float getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Repair the object by a specified amount. The health that the object will be healed by is
     * capped to be positive. The object's health is capped at the max health of the object.
     *
     * @param health The amount to repair the object by.
     */
    public void repair(float health) {
        health = Math.max(1, health);
        currentHealth += health;
        currentHealth = Math.min(currentHealth, getMaxHealth());
    }

    /**
     * Damage the object by a specified amount. The damage that is applied to the object is capped
     * to be positive. The object's minimum health is capped at 0.
     *
     * @param damage The amount to repair the object by.
     */
    public void damage(float damage) {
        damage = Math.max(1, damage);
        currentHealth -= damage;
        currentHealth = Math.max(0, currentHealth);
    }

    /**
     * Check whether the object has no health remaining and should be destroyed.
     *
     * @return Whether the object has no health remaining.
     */
    public boolean shouldBeDestroyed() {
        return currentHealth <= 0;
    }

    /**
     * Retrieve the max health that this object can have.
     *
     * @return The max health of the object.
     */
    protected abstract int getMaxHealth();
}
