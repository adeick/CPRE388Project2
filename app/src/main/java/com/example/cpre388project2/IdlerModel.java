package com.example.cpre388project2;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdlerModel extends ViewModel {
    private MutableLiveData<Integer> bitcoin;

    /**
     * Get the amount of bitcoin the current player has.
     *
     * @return The player's current bitcoin amount.
     */
    public MutableLiveData<Integer> getBitcoin() {
        if (bitcoin == null) {
            bitcoin = new MutableLiveData<>(0);
        }
        return bitcoin;
    }

    /**
     * Increment/Decrement the players bitcoin by the specified amount.
     *
     * @param amount The amount to increment/decrement the bitcoin amount by.
     */
    public void modifyBitcoinBy(int amount) {
        if (bitcoin == null) {
            bitcoin = new MutableLiveData<>(0);
        }
        bitcoin.setValue(bitcoin.getValue() + amount);
    }

    /**
     * Set the player's bitcoin amount to the specified value.
     *
     * @param value The specified amount of bitcoin the player should have.
     */
    public void setBitcoin(int value) {
        if (bitcoin == null) {
            bitcoin = new MutableLiveData<>(0);
        }
        bitcoin.setValue(value);
    }
}
