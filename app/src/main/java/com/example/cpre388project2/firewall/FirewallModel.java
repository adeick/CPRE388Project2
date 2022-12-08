package com.example.cpre388project2.firewall;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FirewallModel extends ViewModel {
    private MutableLiveData<Integer> firewallLevel;
    private MutableLiveData<Integer> currentHealth;

    /**
     * Repair the object by a specified amount. The health that the object will be healed by is
     * capped to be positive. The object's health is capped at the max health of the object.
     *
     * @param health The amount to repair the object by.
     */
    public void repair(float health) {
        checkNull();
        health = Math.max(0, health);
        int newHealth = (int) Math.min(currentHealth.getValue() + health, getMaxHealth());
        currentHealth.setValue(newHealth);
    }

    public void upgradeFirewall() {
        upgradeFirewall(1);
    }

    public void upgradeFirewall(int level) {
        checkNull();
        firewallLevel.setValue(firewallLevel.getValue() + level);
        currentHealth.setValue(getMaxHealth());
    }

    public long firewallUpgradeCost() {
        checkNull();
        return (long) (Math.pow(2, firewallLevel.getValue()) * 500);
    }


    /**
     * Damage the object by a specified amount. The damage that is applied to the object is capped
     * to be positive. The object's minimum health is capped at 0.
     *
     * @param damage The amount to repair the object by.
     */
    public void damage(float damage) {
        checkNull();
        damage = Math.max(0, damage);
        int newHealth = (int) Math.max(currentHealth.getValue() - damage, 0);
        currentHealth.setValue(newHealth);
    }

    /**
     * Check whether the object has no health remaining and should be destroyed.
     *
     * @return Whether the object has no health remaining.
     */
    public boolean shouldBeDestroyed() {
        checkNull();
        return currentHealth.getValue() <= 0;
    }

    /**
     * Retrieve the max health that this object can have.
     *
     * @return The max health of the object.
     */

    /**
     * Retrieve the current health of the object.
     *
     * @return The current health of the object.
     */
    public MutableLiveData<Integer> getCurrentHealth() {
        checkNull();
        return currentHealth;
    }

    public MutableLiveData<Integer> getFirewallLevel() {
        checkNull();
        return firewallLevel;
    }

    /**
     * @return Max health of firewall
     */
    public int getMaxHealth() {
        if (firewallLevel == null) {
            firewallLevel = new MutableLiveData<>(0);
        }
        return (int) (Math.pow(firewallLevel.getValue(), 2) * 200);
    }

    public void setCurrentHealth(int health) {
        checkNull();
        currentHealth.setValue(health);
    }

    private void checkNull() {
        if (firewallLevel == null) {
            firewallLevel = new MutableLiveData<>(0);
        }
        if (currentHealth == null) {
            currentHealth = new MutableLiveData<>(getMaxHealth());
        }
    }
}
