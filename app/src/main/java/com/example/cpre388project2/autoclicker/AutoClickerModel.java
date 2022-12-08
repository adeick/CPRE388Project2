package com.example.cpre388project2.autoclicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel to handle auto clickers.
 */
public class AutoClickerModel extends ViewModel {
    private MutableLiveData<Integer> numAutoClickers;

    /**
     * Get player's current number of auto clickers.
     *
     * @return Number of auto clickers.
     */
    public int getNumAutoClickers() {
        if (numAutoClickers == null) {
            numAutoClickers = new MutableLiveData<>(0);
        }
        return numAutoClickers.getValue();
    }

    /**
     * Increases the number of auto clickers by one.
     */
    public void incrementNumAutoClickers() {
        if (numAutoClickers == null) {
            numAutoClickers = new MutableLiveData<>(0);
        }
        numAutoClickers.setValue(numAutoClickers.getValue() + 1);
    }

    /**
     * Sets the number of auto clickers to the specified amount.
     *
     * @param num New number of auto clickers.
     */
    public void setNumAutoClickers(int num) {
        if (numAutoClickers == null) {
            numAutoClickers = new MutableLiveData<>(num);
        } else {
            numAutoClickers.setValue(num);
        }
    }
}
