package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.cpre388project2.storage.BitcoinStorageModel;
import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerModel;
import com.example.cpre388project2.towers.TowerTypes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BitcoinStorageModel bitcoinStorageModel;
    private TextView bitcoinCountTextView;
    private TowerModel towerModel;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);

        bitcoinStorageModel.initialize();

        bitcoinCountTextView = findViewById(R.id.bitcoinCountTextView);

        bitcoinStorageModel.getAmountStored().observe(this, amount -> {
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, bitcoinStorageModel.getAmountStored().getValue(), "Bit"));
        });

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            return true;
        }
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
}