package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cpre388project2.towers.TowerTypes;

/**
 * Handles buttons being pressed on the buy screen.
 */
public class BuyScreen extends AppCompatActivity {

    //    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
//    private enum LayoutManagerType{
//        GRID_LAYOUT_MANAGER,
//        LINEAR_LAYOUT_MANAGER
//    }
//
//    private LayoutManagerType currLayoutManagerType;
//
    private RecyclerView recyclerView;
    private BuyAdapter adapter;
    //    private RecyclerView.LayoutManager layoutManager;
    private String[] dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_screen);

        initDataset();
        recyclerView = (RecyclerView) findViewById(R.id.buyRecycler);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BuyAdapter(dataset, new BuyListener() {
            @Override
            public void onPositionClicked(int pos, String name) {
                if (name.equals("upgradeButton")) {
                    upgrade(pos);
                } else if (name.equals("buyButton")) {
                    buy(pos);
                } else if (name.equals("customizeButton")) {
                    customize(pos);
                }

            }
        });

        recyclerView.setAdapter(adapter);
        Button butt = (Button) findViewById(R.id.button2);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyScreen.this, MainActivity.class));
            }
        });

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        recyclerView = (RecyclerView) findViewById(R.id.buyRecycler);
//        layoutManager = new LinearLayoutManager(getActivity());
//    }


    private void initDataset() {
        dataset = new String[]{"Storage", "Server", "Microprocessor", "GPU", "Quantum Computer", "Firewall", "Auto Clicker"};
    }

    private void upgrade(int pos) {

        switch (pos) {
            case 0:
                MainActivity.getInstance().upgradeStorageOnClick(null);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 1:
                MainActivity.getInstance().upgradeTower(TowerTypes.SERVER);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 2:
                MainActivity.getInstance().upgradeTower(TowerTypes.MICROPROCESSOR);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 3:
                MainActivity.getInstance().upgradeTower(TowerTypes.GPU);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediatel
                break;
            case 4:
                MainActivity.getInstance().upgradeTower(TowerTypes.QUANTUMCOMPUTER);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediatel
                break;
            case 5:
                MainActivity.getInstance().firewallOnClick(null);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
            default:
                break;
        }
    }

    private void buy(int pos) {
        switch (pos) {
            case 0:
                MainActivity.getInstance().buyStorageOnClick(null);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 1:
                MainActivity.getInstance().buyTower(TowerTypes.SERVER); // buy a server
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 2:
                MainActivity.getInstance().buyTower(TowerTypes.MICROPROCESSOR);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            case 3:
                MainActivity.getInstance().buyTower(TowerTypes.GPU);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediatel
                break;
            case 4:
                MainActivity.getInstance().buyTower(TowerTypes.QUANTUMCOMPUTER);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediatel
            case 6:
                MainActivity.getInstance().buyAutoClickerOnClick(null); //buy a autoclicker
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately
                break;
            default:
                break;
        }
    }

    private void customize(int pos) {
        switch (pos){
            case 0: //customize storage
                MainActivity.getInstance().cycleStorageCustomImage();
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
                break;
            case 1: // customize server
                MainActivity.getInstance().cycleTowerCustomImage(TowerTypes.SERVER);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
                break;
            case 2:
                MainActivity.getInstance().cycleTowerCustomImage(TowerTypes.MICROPROCESSOR);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
                break;
            case 3:
                MainActivity.getInstance().cycleTowerCustomImage(TowerTypes.GPU);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
                break;
            case 4:
                MainActivity.getInstance().cycleTowerCustomImage(TowerTypes.QUANTUMCOMPUTER);
                MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId());
                break;
            default :
                break;
        }

    }


}