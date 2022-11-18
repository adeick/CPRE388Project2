package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.cpre388project2.storage.BitcoinStorageModel;
import com.example.cpre388project2.towers.TowerModel;

public class MainActivity extends AppCompatActivity {

    private BitcoinStorageModel bitcoinStorageModel;
    private TowerModel towerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);

        bitcoinStorageModel.initialize();
    }
}