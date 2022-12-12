package com.example.cpre388project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cpre388project2.firewall.FirewallModel;
import com.example.cpre388project2.hacker.Hacker;
import com.example.cpre388project2.hacker.HackerModel;
import com.example.cpre388project2.prestige.PrestigeModel;
import com.example.cpre388project2.storage.BitcoinStorageModel;
import com.example.cpre388project2.autoclicker.AutoClickerModel;
import com.example.cpre388project2.towers.Tower;
import com.example.cpre388project2.towers.TowerModel;
import com.example.cpre388project2.towers.TowerTypes;
import com.example.cpre388project2.util.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // View Models
    private BitcoinStorageModel bitcoinStorageModel;
    private TowerModel towerModel;
    private AutoClickerModel autoClickerModel;
    private HackerModel hackerModel;
    private PrestigeModel prestigeModel;
    private FirewallModel firewallModel;
    private MainActivityViewModel mViewModel;

    // Misc.
    private FirebaseFirestore mFirestore;
    private DocumentReference userDocRef;

    private static final int RC_SIGN_IN = 9001;

    // Runnables
    private Handler handler;
    private Runnable autoClickerRunnable;
    private Runnable hackerSpawnerRunnable;

    // Views
    private TextView bitcoinCountTextView;
    private Button allianceButton;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        TextView prestigeLevelTextView = (TextView) findViewById(R.id.prestigeLevelTextView);
        TextView firewallInfoTextView = (TextView) findViewById(R.id.firewallInfoTextView);
        CircularProgressIndicator firewallHealthBar = (CircularProgressIndicator) findViewById(R.id.firewallHealth);
//        ProgressBar firewallBar = (ProgressBar) findViewById(R.id.firewallHealthBar);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mFirestore = FirebaseUtil.getFirestore();

        bitcoinStorageModel = new ViewModelProvider(this).get(BitcoinStorageModel.class);
        towerModel = new ViewModelProvider(this).get(TowerModel.class);
        autoClickerModel = new ViewModelProvider(this).get(AutoClickerModel.class);
        hackerModel = new ViewModelProvider(this).get(HackerModel.class);
        prestigeModel = new ViewModelProvider(this).get(PrestigeModel.class);
        firewallModel = new ViewModelProvider(this).get(FirewallModel.class);

        prestigeModel.getPrestigeLevel().observe(this, level -> {
            prestigeLevelTextView.setText(getString(R.string.prestigeLevelText, level));
        });
        firewallModel.getFirewallLevel().observe(this, level -> {
            if (level > 0) {
                int health = firewallModel.getCurrentHealth().getValue();
                int maxHealth = firewallModel.getMaxHealth();
                firewallInfoTextView.setText(getString(R.string.firewallInfoText, level, health, maxHealth));
                firewallInfoTextView.setVisibility(View.VISIBLE);
                // TODO: set textview to visible and display firewall level, image, and health
            } else {
                firewallInfoTextView.setVisibility(View.GONE);
            }
        });
        firewallModel.getCurrentHealth().observe(this, health -> {
            int level = firewallModel.getFirewallLevel().getValue();
            int maxHealth = firewallModel.getMaxHealth();
            int healthRatio = (int) ((float) health / maxHealth * 100);
            firewallInfoTextView.setText(getString(R.string.firewallInfoText, level, health, maxHealth));
            firewallHealthBar.setProgress(healthRatio);
//            firewallBar.setProgress(healthRatio);
        });

//        bitcoinStorageModel.initialize();
//        setupUser(FirebaseUtil.getAuth().getUid());

        bitcoinCountTextView = findViewById(R.id.bitcoinCountTextView);
        hideAllHackers();

        handler = new Handler(Looper.getMainLooper());


        Button butt = (Button) findViewById(R.id.toBuyScreen);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BuyScreen.class));
            }
        });

        allianceButton = findViewById(R.id.allianceButton);
        allianceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlliances();
            }
        });

    }

    public static MainActivity getInstance() {
        return instance;
    }

    public String getUserId(){
        return FirebaseUtil.getAuth().getUid();
    }

    private void startAutoClickers() {
        int autoClickers = autoClickerModel.getNumAutoClickers();
        int delay = 1000 / Math.min(Math.max(autoClickers, 1), 50);

        autoClickerRunnable = new Runnable() {
            @Override
            public void run() {
                int autoClickers = autoClickerModel.getNumAutoClickers();
                int delay = 1000 / Math.min(Math.max(autoClickers, 1), 50); // Doesn't run more than 50 times a second

                if (autoClickers > 0 && autoClickers <= 50) {
                    gainBitcoin();

                    long attackAmount = hackerModel.getTotalAttackAmount();
                    hackAttack(attackAmount);
                } else if (autoClickers > 50) { // Compensates for not running more than 50 times a second
                    gainBitcoin(autoClickers / 50);

                    long attackAmount = hackerModel.getTotalAttackAmount() * (autoClickers / 50);
                    hackAttack(attackAmount);
                }
                handler.postAtTime(this, SystemClock.uptimeMillis() + delay);
            }
        };
        handler.postAtTime(autoClickerRunnable, SystemClock.uptimeMillis() + delay);
    }

    private void hackAttack(long attackAmount) {
        if (firewallModel.getCurrentHealth().getValue() == 0) {
            bitcoinStorageModel.retrieveAmount(attackAmount);
        } else {
            if (attackAmount > 0) {
                int wallDamage = firewallModel.getFirewallLevel().getValue();
                firewallModel.damage(wallDamage);
            }
        }
    }

    private void goToAlliances() {
        Intent switchActivityIntent = new Intent(this, AllianceActivity.class);
        startActivity(switchActivityIntent);
    }

    private void startHackerSpawner() {
        int initialDelay = 15;

        hackerSpawnerRunnable = new Runnable() {
            @Override
            public void run() {
                int delay = 5;
                int randomPos = ((int) (Math.random() * hackerModel.getMaxHackers()));
                int randomLevel = ((int) (Math.random() * 20));

                hackerModel.setHacker(randomPos, new Hacker(randomLevel));
                showAttacker(randomPos);
                handler.postAtTime(this, SystemClock.uptimeMillis() + delay);
            }
        };
        handler.postAtTime(hackerSpawnerRunnable, SystemClock.uptimeMillis() + initialDelay);
    }

    public void firewallOnClick(View view) {
        long cost = firewallModel.firewallUpgradeCost();
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            firewallModel.upgradeFirewall();
        }
    }

    public void buyAutoClickerOnClick(View view) {
        long cost = 20;
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            autoClickerModel.incrementNumAutoClickers();
        }
    }

    public void prestigeOnClick(View view) {
        if (prestigeModel.canAffordNextPrestige(bitcoinStorageModel.getAmountStored().getValue())) {
            bitcoinStorageModel.retrieveAmount(prestigeModel.nextPrestigeCost());
            prestigeModel.upgradeToNextPrestige();
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

    public void buyTower(TowerTypes towerType) {
        long cost = Tower.getPurchaseCost(towerType);
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            towerModel.addTower(towerType);
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
            long cost = tower.getUpgradeCost();
            if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
                bitcoinStorageModel.retrieveAmount(cost);
                tower.upgradeTower();
            }
        }
    }

    public void upgradeStorageOnClick(View view) {
        long cost = bitcoinStorageModel.getUpgradeCost();
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            bitcoinStorageModel.upgradeStorage();
        }
    }

    public void mainframeOnClick(View view) {
        gainBitcoin();
    }

    private void gainBitcoin(double times) {
        long bitcoin = getBitCoinPerTower(times);
        bitcoinStorageModel.storeAmount(bitcoin);
    }

    private void gainBitcoin() {
        gainBitcoin(1);
    }

    private long getBitCoinPerTower(double times) {
        long bitcoin = 0;
        ArrayList<Tower> towers = towerModel.getTowers();
        for (Tower tower : towers) {
            bitcoin += tower.getProductionRate() * times;
        }
        return bitcoin;
    }

    private long getBitCoinPerTower() {
        return getBitCoinPerTower(1);
    }

    public void setupUser(String userId) {
        LifecycleOwner owner = this;
        mFirestore.collection("users")
                .whereEqualTo("userid", userId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) { // User data exists
                                DocumentSnapshot userInfo = task.getResult().getDocuments().get(0);
                                userDocRef = userInfo.getReference();
                                // Set up user from existing data
                                bitcoinStorageModel.initialize((Integer) userInfo.getDouble("storagelevel").intValue(), userInfo.getLong("bitcoins"));

                                ArrayList<HashMap> tMaps = (ArrayList<HashMap>) userInfo.get("towers");
                                for (int i = 0; i < tMaps.size(); i++) {
                                    towerModel.setTower(i, new Tower(TowerTypes.valueOf((String) tMaps.get(i).get("towerType")),
                                            ((Long) tMaps.get(i).get("towerLevel")).intValue(),
                                            ((Long) tMaps.get(i).get("towerCount")).intValue()));
                                }

                                autoClickerModel.setNumAutoClickers((int) userInfo.getDouble("autoclickers").intValue());
                                gainBitcoin((int) (new Timestamp(new Date()).getSeconds() - userInfo.getTimestamp("lastlogin").getSeconds())
                                        * autoClickerModel.getNumAutoClickers());

                                prestigeModel.setPrestigeLevel(userInfo.getDouble("prestigelevel").intValue());
                                firewallModel.upgradeFirewall(userInfo.getDouble("firewalllevel").intValue());

                                finishSetup(owner);
                                return;
                            }
                        }
                        if (userId == null) { // Stop accounts with null userId from being created
                            return;
                        }
                        createUser(userId);
                        // User data doesn't exist
                        bitcoinStorageModel.initialize();
                        finishSetup(owner);
                    }
                });
    }

    private void finishSetup(LifecycleOwner owner) {
        towerModel.addTower(TowerTypes.MAINFRAME);
        bitcoinStorageModel.getAmountStored().observe(owner, amount -> {
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, bitcoinStorageModel.getAmountStored().getValue(), "Bit"));
        });

        startAutoClickers();
        startHackerSpawner();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if needed
        if (shouldStartSignIn()) {
            startSignIn();
            mViewModel.setIsSigningIn(false);
        }
        setupUser(FirebaseUtil.getAuth().getUid());
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!shouldStartSignIn()) {
            saveUser(FirebaseUtil.getAuth().getUid());
        }
    }

    public void saveUser(String userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userid", userId);
        data.put("bitcoins", bitcoinStorageModel.getAmountStored().getValue());
        data.put("storagelevel", bitcoinStorageModel.getStorageLevel());
        data.put("lastlogin", new Timestamp(new Date()));
        data.put("towers", towerModel.getTowers());
        data.put("autoclickers", autoClickerModel.getNumAutoClickers());
        data.put("prestigelevel", prestigeModel.getPrestigeLevel().getValue());
        data.put("firewalllevel", firewallModel.getFirewallLevel().getValue());

        userDocRef.update(data);
    }

    public void createUser(String userId) {
        if (userId == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userid", userId);
        data.put("username", FirebaseUtil.getAuth().getCurrentUser().getDisplayName());
        data.put("bitcoins", 0);
        data.put("storagelevel", 1);
        data.put("alliance", "");
        data.put("lastlogin", new Timestamp(new Date()));
        data.put("towers", towerModel.getTowers());
        data.put("autoclickers", 0);
        data.put("prestigelevel", 1);
        data.put("firewalllevel", 0);

        mFirestore.collection("users").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        userDocRef = documentReference;
                    }
                });
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

    private void hideAllHackers() {
        findViewById(R.id.hacker0).setVisibility(View.GONE);
        findViewById(R.id.hacker1).setVisibility(View.GONE);
        findViewById(R.id.hacker2).setVisibility(View.GONE);
        findViewById(R.id.hacker3).setVisibility(View.GONE);
        findViewById(R.id.hacker4).setVisibility(View.GONE);
        findViewById(R.id.hacker5).setVisibility(View.GONE);
        findViewById(R.id.hacker6).setVisibility(View.GONE);
        findViewById(R.id.hacker7).setVisibility(View.GONE);
    }

    private void showAttacker(int position) {
        switch (position) {
            case 0:
                findViewById(R.id.hacker0).setVisibility(View.VISIBLE);
                break;
            case 1:
                findViewById(R.id.hacker1).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.hacker2).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.hacker3).setVisibility(View.VISIBLE);
                break;
            case 4:
                findViewById(R.id.hacker4).setVisibility(View.VISIBLE);
                break;
            case 5:
                findViewById(R.id.hacker5).setVisibility(View.VISIBLE);
                break;
            case 6:
                findViewById(R.id.hacker6).setVisibility(View.VISIBLE);
                break;
            case 7:
                findViewById(R.id.hacker7).setVisibility(View.VISIBLE);
                break;
        }
    }

    public void hacker0OnClick(View view) {
        hackerModel.setHacker(0, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker1OnClick(View view) {
        hackerModel.setHacker(1, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker2OnClick(View view) {
        hackerModel.setHacker(2, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker3OnClick(View view) {
        hackerModel.setHacker(3, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker4OnClick(View view) {
        hackerModel.setHacker(4, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker5OnClick(View view) {
        hackerModel.setHacker(5, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker6OnClick(View view) {
        hackerModel.setHacker(6, null, true);
        view.setVisibility(View.GONE);
    }

    public void hacker7OnClick(View view) {
        hackerModel.setHacker(7, null, true);
        view.setVisibility(View.GONE);
    }
}