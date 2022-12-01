package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.example.cpre388project2.storage.BitcoinStorageModel;
import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerModel;
import com.example.cpre388project2.towers.TowerTypes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BitcoinStorageModel bitcoinStorageModel;
    private TowerModel towerModel;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);

        bitcoinStorageModel.initialize();

        TextView bitcoinCountTextView = findViewById(R.id.bitcoinCountTextView);

        towerModel.AddTower(TowerTypes.MAINFRAME);
        bitcoinStorageModel.getAmountStored().observe(this, amount -> {
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, bitcoinStorageModel.getAmountStored().getValue(), "Bit"));
        });

        startAutoClickers();
    }

    private void startAutoClickers() {
        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                gainBitcoin(1);
                handler.postAtTime(this, SystemClock.uptimeMillis() + 1000);
            }
        };
        handler.postAtTime(runnable, SystemClock.uptimeMillis() + 1000);
    }

    public void towerServerOnClick(View view) {
        buyTower(TowerTypes.SERVER);
    }

    public void towerMicroProcessorOnClick(View view) {
        buyTower(TowerTypes.MICROPROCESSOR);
    }

    public void towerGPUOnClick(View view) {
        buyTower(TowerTypes.GPU);
    }

    public void towerQuantumComputerOnClick(View view) {
        buyTower(TowerTypes.QUANTUMCOMPUTER);
    }

    private void buyTower(TowerTypes towerType) {
        int cost = Tower.getPurchaseCost(towerType);
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            towerModel.AddTower(towerType);
        }
    }

    public void upgradeServerOnClick(View view) {
        upgradeTower(TowerTypes.SERVER);
    }

    public void upgradeMicroprocessorOnClick(View view) {
        upgradeTower(TowerTypes.MICROPROCESSOR);
    }

    public void upgradeGPUOnClick(View view) {
        upgradeTower(TowerTypes.GPU);
    }

    public void upgradeQuantumComputerOnClick(View view) {
        upgradeTower(TowerTypes.QUANTUMCOMPUTER);
    }

    private void upgradeTower(TowerTypes towerType) {
        Tower tower = towerModel.getTower(towerType);
        if (tower != null) {
            int cost = tower.getUpgradeCost();
            if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
                bitcoinStorageModel.retrieveAmount(cost);
                tower.upgradeTower();
            }
        }
    }

    public void upgradeStorageOnClick(View view) {
        int cost = bitcoinStorageModel.getUpgradeCost();
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            bitcoinStorageModel.upgradeStorage();
        }
    }

    public void mainframeOnClick(View view) {
        gainBitcoin(1);
    }

    private void gainBitcoin(int times) {
        int bitcoin = getBitCoinPerTower() * times;
        bitcoinStorageModel.storeAmount(bitcoin);
    }

    private int getBitCoinPerTower() {
        int bitcoin = 0;
        ArrayList<Tower> towers = towerModel.getTowers();
        for (Tower tower : towers) {
            bitcoin += tower.getProductionRate();
        }
        return bitcoin;
    }
}