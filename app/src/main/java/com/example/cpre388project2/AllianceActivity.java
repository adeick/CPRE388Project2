package com.example.cpre388project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cpre388project2.util.FirebaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;


public class AllianceActivity extends AppCompatActivity {

    private Button mainButton;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alliance_search);

        mFirestore = FirebaseUtil.getFirestore();

        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                goToMain();
            }
        });
    }
    private void goToMain(){
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

//    public void onSoftwareEngineerClick(View view){
//        FireBaseUser thisUser = FirebaseUtil.getAuth().getCurrentUser().getUid();
//
//        mFirestore.collection("se").add({
//
//        });
//    }
}
