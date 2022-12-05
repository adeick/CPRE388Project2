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
        adapter = new BuyAdapter(dataset);
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

}