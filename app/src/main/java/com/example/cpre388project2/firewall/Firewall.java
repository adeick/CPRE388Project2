package com.example.cpre388project2.firewall;

import com.example.cpre388project2.Damageable;

public class Firewall extends Damageable {
    private int firewallLevel;

    /**
     * Create a new firewall
     */
    public Firewall() {
        firewallLevel = 1;
    }

    /**
     * @return Max health of firewall
     */
    @Override
    protected int getMaxHealth() {
        return (int) (Math.pow(firewallLevel, 3) * 200);
    }
}
