package com.example.cpre388project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerTypes;
import com.example.cpre388project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


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

    private void goToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public void onMainButtonClick(View view) {
        goToMain();
    }

    public void getUserList(String alliance) {
        mFirestore.collection("users")
                .whereEqualTo("alliance", alliance)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        TextView participant = findViewById(R.id.allianceMember1);
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) { // Members in alliance
                                String text = "";
                                for (DocumentSnapshot userInfo : task.getResult().getDocuments()) {
                                    text += userInfo.get("username") + " - " + userInfo.getLong("bitcoins") + "\n";
                                }

                                participant.setText(text);
                                return;
                            }
                        }
                        participant.setText("No members");
                    }
                });
    }

    public void onSoftwareEngineerClick(View view) {
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Software Engineers");

        getUserList("se");
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onBackButtonClicked(View view) {
        setContentView(R.layout.alliance_search);
    }

    public void onComputerEngineerClick(View view) {
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Computer Engineers");

        getUserList("cpre");
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onElectricalEngineerClick(View view) {
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("Electrical Engineers");

        getUserList("ee");
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }

    public void onCSClick(View view) {
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);
        allianceName.setText("CS Majors (lol)");

        getUserList("cs");
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
    }
}
