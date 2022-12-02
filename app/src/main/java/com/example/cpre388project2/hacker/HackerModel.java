package com.example.cpre388project2.hacker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerTypes;

import java.util.ArrayList;

public class HackerModel extends ViewModel {
    private MutableLiveData<Hacker[]> hackers;
    private int maxHackers = 8;

    public Hacker getHacker(int position) {
        Hacker[] hackers = getHackers();
        return hackers[position];
    }

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

    public Hacker[] getHackers() {
        if (hackers == null) {
            hackers = new MutableLiveData<>(new Hacker[maxHackers]);
        }
        return hackers.getValue();
    }

    public void setHacker(int position, Hacker hacker) {
        setHacker(position, hacker, false);
    }

    public void setHacker(int position, Hacker hacker, boolean override) {
        if (hackers == null) {
            hackers = new MutableLiveData<>(new Hacker[maxHackers]);
        }
        if (hackers.getValue()[position] == null || override) {
            hackers.getValue()[position] = hacker;
        }
    }

    public int getMaxHackers() {
        return maxHackers;
    }
}
