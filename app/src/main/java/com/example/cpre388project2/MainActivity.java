package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cpre388project2.storage.BitcoinStorageModel;
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
    private TowerModel towerModel;
    private MainActivityViewModel mViewModel;
    private FirebaseFirestore mFirestore;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mFirestore = FirebaseUtil.getFirestore();

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);

        bitcoinStorageModel.initialize();

        TextView bitcoinCountTextView = findViewById(R.id.bitcoinCountTextView);

        bitcoinStorageModel.getAmountStored().observe(this, amount -> {
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, bitcoinStorageModel.getAmountStored().getValue(), "Bit"));
        });
    }

    public void MainframeOnClick(View view) {
        towerModel.AddTower(TowerTypes.MAINFRAME);
    }

    public void towerOnClick(View view) {
        int bitcoin = getBitCoinPerTower();
        bitcoin = Math.max(1, bitcoin); // Get at least 1 bitcoin per click.
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