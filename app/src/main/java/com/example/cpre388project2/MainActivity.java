package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.example.cpre388project2.storage.BitcoinStorageModel;
import com.example.cpre388project2.autoclicker.AutoClickerViewModel;
import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerModel;
import com.example.cpre388project2.towers.TowerTypes;
import com.example.cpre388project2.util.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private BitcoinStorageModel bitcoinStorageModel;
    private TextView bitcoinCountTextView;
    private TowerModel towerModel;
    private AutoClickerViewModel autoClickerViewModel;
    private MainActivityViewModel mViewModel;
    private FirebaseFirestore mFirestore;

    private static final int RC_SIGN_IN = 9001;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mFirestore = FirebaseUtil.getFirestore();

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);
        autoClickerViewModel = new ViewModelProvider(this).get(AutoClickerViewModel.class);

        bitcoinStorageModel.initialize();

        bitcoinCountTextView = findViewById(R.id.bitcoinCountTextView);

        towerModel.AddTower(TowerTypes.MAINFRAME);
        bitcoinStorageModel.getAmountStored().observe(this, amount -> {
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, bitcoinStorageModel.getAmountStored().getValue(), "Bit"));
        });

    }
        startAutoClickers();
    }

    private void startAutoClickers() {
        handler = new Handler(Looper.getMainLooper());

        int autoClickers = autoClickerViewModel.getNumAutoClickers();
        int delay = 1000 / Math.max(autoClickers, 1);

        runnable = new Runnable() {
            @Override
            public void run() {
                int autoClickers = autoClickerViewModel.getNumAutoClickers();
                int delay = 1000 / Math.max(autoClickers, 1);

                if (autoClickers > 0) {
                    gainBitcoin();
                }
                handler.postAtTime(this, SystemClock.uptimeMillis() + delay);
            }
        };
        handler.postAtTime(runnable, SystemClock.uptimeMillis() + delay);
    }

    public void buyAutoClickerOnClick(View view) {
        int cost = 20;
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            autoClickerViewModel.incrementNumAutoClickers();
        }
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
        gainBitcoin();
    }

    private void gainBitcoin() {
        int bitcoin = getBitCoinPerTower();
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

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if needed
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseUtil.getAuth().getCurrentUser() == null);
    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = FirebaseUtil.getAuthUI()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }
}