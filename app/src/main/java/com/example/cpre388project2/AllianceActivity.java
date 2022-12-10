package com.example.cpre388project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cpre388project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity that handles viewing alliances, seeing alliances' members, and joining alliances.
 */
public class AllianceActivity extends AppCompatActivity {

    private Button mainButton;
    private FirebaseFirestore mFirestore;
    private Button joinLeaveButton;

    private TextView allianceName;

    private String allianceSelectedCode;
    private TableLayout allianceTable;
    /**
     * Lifecycle hook to initialize activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alliance_search);

        allianceSelectedCode = "";

        mFirestore = FirebaseUtil.getFirestore();

        mainButton = findViewById(R.id.mainButton);

    }

    private void goToMain() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    /**
     * onClick method to return to main activity screen.
     *
     * @param view
     */
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
                        TextView participantList = findViewById(R.id.allianceList);
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) { // Members in alliance
                                String text = "";
//                                allianceTable = findViewById(R.id.allianceTable);
                                allianceTable.removeAllViewsInLayout();
                                TableRow header = new TableRow(allianceTable.getContext());
                                TextView tv0 = new TextView(allianceTable.getContext());
                                tv0.setText(" Username ");
                                tv0.setTextSize(19);

                                header.addView(tv0);
                                TextView tv1 = new TextView(allianceTable.getContext());
                                tv1.setText("  Prestige  ");
                                tv1.setTextSize(19);
                                header.addView(tv1);
                                TextView tv2 = new TextView(allianceTable.getContext());
                                tv2.setText("  Bitcoins  ");
                                tv2.setTextSize(19);
                                header.addView(tv2);
                                allianceTable.addView(header);
                            for (DocumentSnapshot userInfo : task.getResult().getDocuments()) {
                                TableRow dataRow = new TableRow(allianceTable.getContext());
                                TextView username = new TextView(allianceTable.getContext());
                                username.setText(userInfo.get("username").toString());
                                dataRow.addView(username);
                                TextView prestige = new TextView(allianceTable.getContext());
                                prestige.setText(userInfo.getLong("prestigelevel").toString());
                                dataRow.addView(prestige);
                                TextView bitcoins = new TextView(allianceTable.getContext());
                                bitcoins.setText(adjustBitcoinString(userInfo.getLong("bitcoins")));
                                dataRow.addView(bitcoins);
                                if(userInfo.get("userid") != null && userInfo.get("userid").equals(FirebaseUtil.getAuth().getCurrentUser().getUid())){
                                    System.out.print("Set Cyan");
                                    dataRow.setBackgroundColor(Color.CYAN);
                                }
                                allianceTable.addView(dataRow);
//                                    text += userInfo.get("username") + " - Prestige: " + userInfo.getLong("prestigelevel") + ", Bitcoins: " + userInfo.getLong("bitcoins") + "\n";
                                }

                                participantList.setText(text);
                                return;
                            }
                        }
                        participantList.setText("No members");
                    }
                });
    }

    public void getCurrentUserAllianceStatus(String alliance){
        mFirestore.collection("users")
                .whereEqualTo("userid", FirebaseUtil.getAuth().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            String firebaseAlliance = task.getResult().getDocuments().get(0).getString("alliance");
                            if(firebaseAlliance.equals("")){
                                joinLeaveButton.setText("Join");
                                joinLeaveButton.setVisibility(View.VISIBLE);
                            }
                            else if(firebaseAlliance.equals(alliance)){
                                joinLeaveButton.setText("Leave");
                                joinLeaveButton.setVisibility(View.VISIBLE);

                            }
                            else{
                                joinLeaveButton.setVisibility(View.INVISIBLE);
                            }
                        }

                    }
                });
    }

    private String adjustBitcoinString(long bitcoinAmount){
        if(bitcoinAmount / (1000000000) > 0){
            double reduced = ((double) bitcoinAmount / 1000000000);
            String str = String.format("%.1f billion", reduced);
            return str;
        }
        if(bitcoinAmount / (1000000) > 0){
            double reduced = ((double) bitcoinAmount / 1000000);
            String str = String.format("%.1f million", reduced);
            return str;
        }
        if(bitcoinAmount / (1000) > 0){
            double reduced = ((double) bitcoinAmount / 1000);
            String str = String.format("%.1f thousand", reduced);
            return str;
        }
        return (Long.toString(bitcoinAmount));
    }

    private void onAllianceClick(){
        setContentView(R.layout.alliance_join);
        allianceName = findViewById(R.id.allianceName);

        joinLeaveButton = findViewById(R.id.joinLeaveButton);
        allianceTable = findViewById(R.id.allianceTable);
        allianceTable.setColumnStretchable(0, true);

        getUserList(allianceSelectedCode);
        getCurrentUserAllianceStatus(allianceSelectedCode);
    }

    /**
     * onClick method to display "Software Engineers" alliance
     *
     * @param view
     */
    public void onSoftwareEngineerClick(View view) {
        allianceSelectedCode = "se";
        onAllianceClick();
        allianceName.setText("Software Engineers");
    }

     /**
     * onClick method to display "Computer Engineers" alliance
     *
     * @param view
     */
    public void onComputerEngineerClick(View view) {
        allianceSelectedCode = "cpre";
        onAllianceClick();
        allianceName.setText("Computer Engineers");
    }

    /**
     * onClick method to display "Electrical Engineers" alliance
     *
     * @param view
     */
    public void onElectricalEngineerClick(View view) {
        allianceSelectedCode = "ee";
        onAllianceClick();
        allianceName.setText("Electrical Engineers");
    }

    /**
     * onClick method to display "CS Majors" alliance
     *
     * @param view
     */
    public void onCSClick(View view) {
        allianceSelectedCode = "cs";
        onAllianceClick();
        allianceName.setText("CS Majors (lol)");
    }

    /**
     * onClick method to join the currently selected/displayed alliance.
     *
     * @param view
     */
    public void onJoinLeaveClick(View view) {
        String userId = FirebaseUtil.getAuth().getUid();
        System.out.println("> " + joinLeaveButton.getText());
        if(joinLeaveButton.getText().equals("Join")) {
            mFirestore.collection("users")
                    .whereEqualTo("userid", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) { // User data exists
                                    DocumentSnapshot userInfo = task.getResult().getDocuments().get(0);
                                    DocumentReference userDocRef = userInfo.getReference();

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("alliance", allianceSelectedCode);

                                    userDocRef.update(data);
                                    getUserList(allianceSelectedCode);
                                    getCurrentUserAllianceStatus(allianceSelectedCode);
                                    return;
                                }
                            }
                        }
                    });
        }
        else if(joinLeaveButton.getText().equals("Leave")) {
            mFirestore.collection("users")
                    .whereEqualTo("userid", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) { // User data exists
                                    DocumentSnapshot userInfo = task.getResult().getDocuments().get(0);
                                    DocumentReference userDocRef = userInfo.getReference();

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("alliance", "");

                                    userDocRef.update(data);
                                    getUserList(allianceSelectedCode);
                                    getCurrentUserAllianceStatus(allianceSelectedCode);
                                    return;
                                }
                            }
                        }
                    });
        }
    }
    public void onBackButtonClicked(View view) {
        setContentView(R.layout.alliance_search);
    }
}
