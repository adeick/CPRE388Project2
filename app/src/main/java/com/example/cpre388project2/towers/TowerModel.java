package com.example.cpre388project2.towers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel that manages Towers.
 */
public class TowerModel extends ViewModel {
    private MutableLiveData<ArrayList<Tower>> towers;

    /**
     * Grab tower object of specified type from the passed in list of towers,.
     * @param towerType TowerType enum
     * @param towers Tower object
     * @return
     */
    public Tower getTower(TowerTypes towerType, ArrayList<Tower> towers) {
        for (Tower tower : towers) {
            if (tower.getTowerType() == towerType) {
                return tower;
            }
        }
        return null;
    }

    /**
     * Gets Tower object of specified type.
     *
     * @param towerType TowerType enum.
     * @return Tower object.
     */
    public Tower getTower(TowerTypes towerType) {
        ArrayList<Tower> towers = getTowers();
        for (Tower tower : towers) {
            if (tower.getTowerType() == towerType) {
                return tower;
            }
        }
        return null;
    }

    /**
     * Adds a Tower of specified type.
     *
     * @param towerType TowerType enum.
     */
    public void addTower(TowerTypes towerType) {
        ArrayList<Tower> towers = getTowers();
        for (Tower tower : towers) {
            if (tower.getTowerType() == towerType) {
                if (towerType != TowerTypes.MAINFRAME) {
                    tower.addTower();
                }
                return;
            }
        }
        towers.add(new Tower(towerType));
    }

    /**
     * Gets ArrayList of all Towers.
     *
     * @return ArrayList of Towers.
     */
    public ArrayList<Tower> getTowers() {
        if (towers == null) {
            towers = new MutableLiveData<>(new ArrayList<>());
        }
        return towers.getValue();
    }

    /**
     * Get the live data of the arraylist of towers
     * @return MutableLiveData of arraylist of towers
     */
    public MutableLiveData<ArrayList<Tower>> getTowersLiveData() {
        if (towers == null) {
            towers = new MutableLiveData<>(new ArrayList<>());
        }
        return towers;
    }

    /**
     * Sets the element at position i of Towers ArrayList to be passed in Tower.
     *
     * @param i     Position of element to be set.
     * @param tower Tower object to be set as.
     */
    public void setTower(int i, Tower tower) {
        if (towers == null) {
            towers = new MutableLiveData<>(new ArrayList<>());
        }
        if (towers.getValue().size() <= i) {
            towers.getValue().add(tower);
        } else {
            towers.getValue().set(i, tower);
        }
    }
}
