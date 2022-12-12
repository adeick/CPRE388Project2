package com.example.cpre388project2.hacker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerTypes;

import java.util.ArrayList;

/**
 * View Model that represents current hackers.
 */
public class HackerModel extends ViewModel {
    private MutableLiveData<Hacker[]> hackers;
    private int maxHackers = 8;

    /**
     * Gets hacker at specified position.
     *
     * @param position Position to get hacker from.
     * @return Hacker at position.
     */
    public Hacker getHacker(int position) {
        Hacker[] hackers = getHackers();
        return hackers[position];
    }

    /**
     * Current amount of damage done by all hackers.
     *
     * @return Amount of damage.
     */
    public int getTotalAttackAmount() {
        int total = 0;
        Hacker[] hackers = getHackers();
        for (Hacker hacker : hackers) {
            if (hacker != null) {
                total += hacker.getAttackAmount();
            }
        }
        return total;
    }

    /**
     * Gets array of hackers.
     *
     * @return Hackers.
     */
    public Hacker[] getHackers() {
        if (hackers == null) {
            hackers = new MutableLiveData<>(new Hacker[maxHackers]);
        }
        return hackers.getValue();
    }

    /**
     * Set hacker at position to instance of hacker.
     *
     * @param position Position in array of hackers.
     * @param hacker   Hacker to be set.
     */
    public void setHacker(int position, Hacker hacker) {
        setHacker(position, hacker, false);
    }

    /**
     * Set hacker at position to instance of hacker but only overrides existing hacker if specified.
     *
     * @param position Position in array of hackers.
     * @param hacker   Hacker to be set.
     * @param override Override if already set.
     */
    public void setHacker(int position, Hacker hacker, boolean override) {
        if (hackers == null) {
            hackers = new MutableLiveData<>(new Hacker[maxHackers]);
        }
        if (hackers.getValue()[position] == null || override) {
            hackers.getValue()[position] = hacker;
        }
    }

    /**
     * Find maximum allowed amount of hackers.
     *
     * @return Amount of hackers.
     */
    public int getMaxHackers() {
        return maxHackers;
    }
}
