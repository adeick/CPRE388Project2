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

import com.example.cpre388project2.towers.TowerTypes;

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
            @Override public void onPositionClicked(int pos, String name) {
                if(name.equals("upgradeButton")){
                    upgrade(pos);
                }else if(name.equals("buyButton")){
                    buy(pos);
                }else if(name.equals("customizeButton")){
                    customize(pos);
                }

            }
        });

        recyclerView.setAdapter(adapter);
        Button butt = (Button)findViewById(R.id.button2);
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


    private void initDataset(){
        dataset = new String[]{"Server", "Microprocessor", "GPU", "QuantumComputer"};
    }

    private void upgrade(int pos){
        MainActivity.getInstance().buyAutoClickerOnClick(null); // Example how to buy/upgrade stuff from main activity methods
        MainActivity.getInstance().saveUser(MainActivity.getInstance().getUserId()); // Make sure to save immediately

//        switch(pos) {
//            case 0:
//                server;
//                break;
//            case 1:
//                microprocessor;
//                break;
//            case 2:
//                GPU;
//                break;
//            case 3:
//                quantumComputer;
//                break;
//        }
    }

    private void buy(int pos){}

    private void customize(int pos){}


}