package com.example.cpre388project2.towers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TowerModel extends ViewModel {
    private MutableLiveData<ArrayList<Tower>> towers;

    public Tower getTower(int i) {
        return towers.getValue().get(i);
    }

    public void setTower(int i, Tower tower) {
        if (towers == null) {
            towers = new MutableLiveData<>(new ArrayList<>());
        }
        towers.getValue().set(i, tower);
    }
}
