package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cpre388project2.util.FirebaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;


public class AllianceActivity extends AppCompatActivity {

    private Button mainButton;
    private FirebaseFirestore mFirestore;

    private TextView allianceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alliance_search);

        mFirestore = FirebaseUtil.getFirestore();

        mainButton = findViewById(R.id.mainButton);
//        mainButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                goToMain();
//            }
//        });
    }
    private void goToMain(){
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public void onMainButtonClick(View view) {
        goToMain();
    }

    public void onSoftwareEngineerClick(View view){
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Software Engineers");

        TextView participant = findViewById(R.id.allianceMember1);
        participant.setText(FirebaseUtil.getAuth().getCurrentUser().getDisplayName());
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onBackButtonClicked(View view){
        setContentView(R.layout.alliance_search);
    }

    public void onComputerEngineerClick(View view){
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Computer Engineers");

//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onElectricalEngineerClick(View view){
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Electrical Engineers");

//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onCSClick(View view){
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("CS Majors (lol)");

//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }
}
