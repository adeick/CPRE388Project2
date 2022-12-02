package com.example.cpre388project2.autoclicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AutoClickerModel extends ViewModel {
    private MutableLiveData<Integer> numAutoClickers;

    public int getNumAutoClickers() {
        if (numAutoClickers == null) {
            numAutoClickers = new MutableLiveData<>(0);
        }
        return numAutoClickers.getValue();
    }

    public void incrementNumAutoClickers() {
        if (numAutoClickers == null) {
            numAutoClickers = new MutableLiveData<>(0);
        }
        numAutoClickers.setValue(numAutoClickers.getValue() + 1);
    }
}
