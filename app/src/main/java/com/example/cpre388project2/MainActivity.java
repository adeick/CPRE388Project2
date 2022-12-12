package com.example.cpre388project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Main screen where most of the logic in the app happens.
 */
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
    public int[] storageCustomImageList = new int[6];
    public int[] serverCustomImageList = new int[3];
    public int[] mpCustomImageList = new int[3];
    public int[] gpuCustomImageList = new int[3];
    public int[] qcCustomImageList = new int[3];
    public int[] fwCustomImageList = new int[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageCustomImageList[0] = R.drawable.punchcard;
        storageCustomImageList[1] = R.drawable.punchcardv2;
        storageCustomImageList[2] = R.drawable.punchcardv3;
        storageCustomImageList[3] = R.drawable.diskdrivev1;
        storageCustomImageList[4] = R.drawable.diskdrivev2;
        storageCustomImageList[5] = R.drawable.diskdrivev3;
        serverCustomImageList[0] = R.drawable.server;
        serverCustomImageList[1] = R.drawable.serverv2;
        serverCustomImageList[2] = R.drawable.serverv3;
        mpCustomImageList[0] = R.drawable.microprocessor;
        mpCustomImageList[1] = R.drawable.microprocessorv2;
        mpCustomImageList[2] = R.drawable.microprocessorv3;
        gpuCustomImageList[0] = R.drawable.gpu;
        gpuCustomImageList[1] = R.drawable.gpuv2;
        gpuCustomImageList[2] = R.drawable.gpuv3;
        qcCustomImageList[0] = R.drawable.quantum;
        qcCustomImageList[1] = R.drawable.quantumv2;
        qcCustomImageList[2] = R.drawable.quantumv3;


        instance = this;

        TextView prestigeLevelTextView = (TextView) findViewById(R.id.prestigeLevelTextView);
        TextView firewallInfoTextView = (TextView) findViewById(R.id.firewallInfoTextView);
        CircularProgressIndicator firewallHealthBar = (CircularProgressIndicator) findViewById(R.id.firewallHealth);

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

    /**
     * Returns the only instance of this class.
     *
     * @return MainActivity instance.
     */
    public static MainActivity getInstance() {
        return instance;
    }

    /**
     * Gets the numerical user id as a string for the current user.
     *
     * @return String of user id.
     */
    public String getUserId() {
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
        int initialDelay = 15000;

        hackerSpawnerRunnable = new Runnable() {
            @Override
            public void run() {
                int delay = 5000;
                int randomPos = ((int) (Math.random() * hackerModel.getMaxHackers()));
                int randomLevel = ((int) (Math.random() * 20));

                hackerModel.setHacker(randomPos, new Hacker(randomLevel));
                showAttacker(randomPos);
                handler.postAtTime(this, SystemClock.uptimeMillis() + delay);
            }
        };
        handler.postAtTime(hackerSpawnerRunnable, SystemClock.uptimeMillis() + initialDelay);
    }

    /**
     * Upgrades the firewall.
     *
     * @param view
     */
    public void firewallOnClick(View view) {
        long cost = firewallModel.firewallUpgradeCost();
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            firewallModel.upgradeFirewall();
        }
    }

    /**
     * Buys an auto clicker.
     *
     * @param view
     */
    public void buyAutoClickerOnClick(View view) {
        long cost = 20;
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            autoClickerModel.incrementNumAutoClickers();
        }
    }

    /**
     * Buys an increase in prestige.
     *
     * @param view
     */
    public void prestigeOnClick(View view) {
        if (prestigeModel.canAffordNextPrestige(bitcoinStorageModel.getAmountStored().getValue())) {
            bitcoinStorageModel.retrieveAmount(prestigeModel.nextPrestigeCost());
            prestigeModel.upgradeToNextPrestige();
        }
    }

//    public void towerServerOnClick(View view) { // I think we can delete these safely
//        buyTower(TowerTypes.SERVER);
//    }
//
//    public void towerMicroProcessorOnClick(View view) {
//        buyTower(TowerTypes.MICROPROCESSOR);
//    }
//
//    public void towerGPUOnClick(View view) {
//        buyTower(TowerTypes.GPU);
//    }
//
//    public void towerQuantumComputerOnClick(View view) {
//        buyTower(TowerTypes.QUANTUMCOMPUTER);
//    }

    /**
     * Buys the tower of specified type.
     *
     * @param towerType TowerTypes enum for type of tower.
     */
    public void buyTower(TowerTypes towerType) {
        long cost = Tower.getPurchaseCost(towerType);
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            towerModel.addTower(towerType);
        }
    }

//    public void upgradeServerOnClick(View view) { // I think these are safe delete
//        upgradeTower(TowerTypes.SERVER);
//    }
//
//    public void upgradeMicroprocessorOnClick(View view) {
//        upgradeTower(TowerTypes.MICROPROCESSOR);
//    }
//
//    public void upgradeGPUOnClick(View view) {
//        upgradeTower(TowerTypes.GPU);
//    }
//
//    public void upgradeQuantumComputerOnClick(View view) {
//        upgradeTower(TowerTypes.QUANTUMCOMPUTER);
//    }

    /**
     * Upgrades the tower of the specified type.
     *
     * @param towerType TowerTypes enum for type of tower.
     */
    public void upgradeTower(TowerTypes towerType) {
        Tower tower = towerModel.getTower(towerType);
        if (tower != null) {
            long cost = tower.getUpgradeCost();
            if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
                bitcoinStorageModel.retrieveAmount(cost);
                tower.upgradeTower();
            }
        }
    }

    public void cycleTowerCustomImage(TowerTypes towerType) {
        Tower tower = towerModel.getTower(towerType);
        if (tower != null) {
            switch (towerType) {
                case SERVER:
                    tower.cycleImageId(serverCustomImageList);
                    break;
                case MICROPROCESSOR:
                    tower.cycleImageId(mpCustomImageList);
                    break;
                case GPU:
                    tower.cycleImageId(gpuCustomImageList);
                    break;
                case QUANTUMCOMPUTER:
                    tower.cycleImageId(qcCustomImageList);
                    break;
            }
        }
    }

    /**
     * Buys another storage unit.
     *
     * @param view
     */
    public void buyStorageOnClick(View view) {
        long cost = 2000;
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            bitcoinStorageModel.addStorageContainer();
        }
    }

    /**
     * Upgrades level of storage.
     *
     * @param view
     */
    public void upgradeStorageOnClick(View view) {
        long cost = bitcoinStorageModel.getUpgradeCost();
        if (bitcoinStorageModel.getAmountStored().getValue() >= cost) {
            bitcoinStorageModel.retrieveAmount(cost);
            bitcoinStorageModel.upgradeStorage();
        }
    }

    public void cycleStorageCustomImage() {
        bitcoinStorageModel.cycleImageId(storageCustomImageList);
    }

    /**
     * Repairs firewall.
     *
     * @param view
     */
    public void repairFirewallOnClick(View view) {
        firewallModel.repair(firewallModel.getMaxHealth());
    }

    /**
     * OnClick to gain bitcoin when clicking on screen.
     *
     * @param view
     */
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

    /**
     * Loads previous user if existing or else begins creating new user.
     *
     * @param userId String for user's firebase id.
     */
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
                                bitcoinStorageModel.initialize((Integer) userInfo.getDouble("storagelevel").intValue(), userInfo.getLong("bitcoins"), userInfo.getDouble("storagecount").intValue());

                                ArrayList<HashMap> tMaps = (ArrayList<HashMap>) userInfo.get("towers");
                                for (int i = 0; i < tMaps.size(); i++) {
                                    int customId = 0;
                                    if (tMaps.get(i).get("customImageId") != null) {
                                        customId = ((Long) tMaps.get(i).get("customImageId")).intValue();
                                    }
                                    towerModel.setTower(i, new Tower(TowerTypes.valueOf((String) tMaps.get(i).get("towerType")),
                                            ((Long) tMaps.get(i).get("towerLevel")).intValue(),
                                            ((Long) tMaps.get(i).get("towerCount")).intValue(),
                                            customId
                                    ));
                                }

                                autoClickerModel.setNumAutoClickers((int) userInfo.getDouble("autoclickers").intValue());
                                gainBitcoin((int) (new Timestamp(new Date()).getSeconds() - userInfo.getTimestamp("lastlogin").getSeconds())
                                        * autoClickerModel.getNumAutoClickers());

                                prestigeModel.setPrestigeLevel(userInfo.getDouble("prestigelevel").intValue());
                                firewallModel.upgradeFirewall(userInfo.getDouble("firewalllevel").intValue());

                                finishSetup(owner);
                                updateTowerInfoDisplay(towerModel.getTowers());
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

    private void updateTowerInfoDisplay(ArrayList<Tower> towers) {
        TextView serverCountTextView = (TextView) findViewById(R.id.serverCountTextView);
        TextView serverLevelTextView = (TextView) findViewById(R.id.serverLevelTextView);
        ImageView serverImageView = (ImageView) findViewById(R.id.serverImageView);

        TextView microprocessorCountTextView = (TextView) findViewById(R.id.microprocessorCountTextView);
        TextView microprocessorLevelTextView = (TextView) findViewById(R.id.microprocessorLevelTextView);
        ImageView microprocessorImageView = (ImageView) findViewById(R.id.microprocessorImageView);

        TextView GPUCountTextView = (TextView) findViewById(R.id.GPUCountTextView);
        TextView GPULevelTextView = (TextView) findViewById(R.id.GPULevelTextView);
        ImageView GPUImageView = (ImageView) findViewById(R.id.GPUImageView);

        TextView quantumComputerCountTextView = (TextView) findViewById(R.id.quantumComputerCountTextView);
        TextView quantumComputerLevelTextView = (TextView) findViewById(R.id.quantumComputerLevelTextView);
        ImageView quantumComputerImageView = (ImageView) findViewById(R.id.quantumComputerImageView);

        TextView storageCountTextView = (TextView) findViewById(R.id.storageCountTextView);
        TextView storageLevelTextView = (TextView) findViewById(R.id.storageLevelTextView);
        ImageView storageImageView = (ImageView) findViewById(R.id.storageImageView);

        Tower tower;

        // Server
        tower = towerModel.getTower(TowerTypes.SERVER, towers);
        if (tower != null) {
            serverCountTextView.setText("" + tower.getTowerCount());
            serverLevelTextView.setText(getString(R.string.levelText, tower.getTowerLevel()));
            serverImageView.setImageResource(serverCustomImageList[tower.getCustomImageId()]);
        } else {
            findViewById(R.id.serverTowerInfoView).setVisibility(View.GONE);
        }

        // Microprocessor
        tower = towerModel.getTower(TowerTypes.MICROPROCESSOR, towers);
        if (tower != null) {
            microprocessorCountTextView.setText("" + tower.getTowerCount());
            microprocessorLevelTextView.setText(getString(R.string.levelText, tower.getTowerLevel()));
            microprocessorImageView.setImageResource(mpCustomImageList[tower.getCustomImageId()]);
        } else {
            findViewById(R.id.microprocessorTowerInfoView).setVisibility(View.GONE);
        }

        // GPU
        tower = towerModel.getTower(TowerTypes.GPU, towers);
        if (tower != null) {
            GPUCountTextView.setText("" + tower.getTowerCount());
            GPULevelTextView.setText(getString(R.string.levelText, tower.getTowerLevel()));
            GPUImageView.setImageResource(gpuCustomImageList[tower.getCustomImageId()]);
        } else {
            findViewById(R.id.GPUTowerInfoView).setVisibility(View.GONE);
        }

        // Quantum Computer
        tower = towerModel.getTower(TowerTypes.QUANTUMCOMPUTER, towers);
        if (tower != null) {
            quantumComputerCountTextView.setText("" + tower.getTowerCount());
            quantumComputerLevelTextView.setText(getString(R.string.levelText, tower.getTowerLevel()));
            quantumComputerImageView.setImageResource(qcCustomImageList[tower.getCustomImageId()]);
        } else {
            findViewById(R.id.quantumComputerTowerInfoView).setVisibility(View.GONE);
        }

        // Storage
        storageCountTextView.setText("" + bitcoinStorageModel.getStorageCount());
        storageLevelTextView.setText(getString(R.string.levelText, bitcoinStorageModel.getStorageLevel()));
        storageImageView.setImageResource(storageCustomImageList[bitcoinStorageModel.getCustomImageId()]);
    }

    private void finishSetup(LifecycleOwner owner) {
        towerModel.addTower(TowerTypes.MAINFRAME);
        bitcoinStorageModel.getAmountStored().observe(owner, amount -> {
            String prefix = "Bit";
            long coins = bitcoinStorageModel.getAmountStored().getValue().longValue();
            if (coins > Math.pow(1000, 10) * 100) { // Quettabit
                prefix = "Quettabit";
                coins /= Math.pow(1000, 10);
            } else if (coins > Math.pow(1000, 9) * 100) { // Ronnabit
                prefix = "Ronnabit";
                coins /= Math.pow(1000, 9);
            } else if (coins > Math.pow(1000, 8) * 100) { // Yottabit
                prefix = "Yottabit";
                coins /= Math.pow(1000, 8);
            } else if (coins > Math.pow(1000, 7) * 100) { // Zettabit
                prefix = "Zettabit";
                coins /= Math.pow(1000, 7);
            } else if (coins > Math.pow(1000, 6) * 100) { // Exabit
                prefix = "Exabit";
                coins /= Math.pow(1000, 6);
            } else if (coins > Math.pow(1000, 5) * 100) { // Petabit
                prefix = "Petabit";
                coins /= Math.pow(1000, 5);
            } else if (coins > Math.pow(1000, 4) * 100) { // Terabit
                prefix = "Terabit";
                coins /= Math.pow(1000, 4);
            } else if (coins > Math.pow(1000, 3) * 100) { // Gigabit
                prefix = "Gigabit";
                coins /= Math.pow(1000, 3);
            } else if (coins > Math.pow(1000, 2) * 100) { // Megabit
                prefix = "Megabit";
                coins /= Math.pow(1000, 2);
            } else if (coins > Math.pow(1000, 1) * 100) { // Kilobit
                prefix = "Kilobit";
                coins /= Math.pow(1000, 1);
            }
            bitcoinCountTextView.setText(getString(R.string.bitcoinCountText, coins, prefix));
        });

        startAutoClickers();
        startHackerSpawner();
    }

    /**
     * Lifecycle hook that handles signing in and setting up user.
     */
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

    /**
     * Lifecycle hook that handles saving user data.
     */
    @Override
    public void onStop() {
        super.onStop();

        if (!shouldStartSignIn()) {
            saveUser(FirebaseUtil.getAuth().getUid());
        }
    }

    /**
     * Saves user data to firebase based on user id.
     *
     * @param userId User's firebase id.
     */
    public void saveUser(String userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userid", userId);
        data.put("bitcoins", bitcoinStorageModel.getAmountStored().getValue());
        data.put("storagelevel", bitcoinStorageModel.getStorageLevel());
        data.put("storagecount", bitcoinStorageModel.getStorageCount());
        data.put("lastlogin", new Timestamp(new Date()));
        data.put("towers", towerModel.getTowers());
        data.put("autoclickers", autoClickerModel.getNumAutoClickers());
        data.put("prestigelevel", prestigeModel.getPrestigeLevel().getValue());
        data.put("firewalllevel", firewallModel.getFirewallLevel().getValue());

        userDocRef.update(data);
    }

    /**
     * Creates new user in firebase.
     *
     * @param userId User's firebase id.
     */
    public void createUser(String userId) {
        if (userId == null) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userid", userId);
        data.put("username", FirebaseUtil.getAuth().getCurrentUser().getDisplayName());
        data.put("bitcoins", 0);
        data.put("storagelevel", 1);
        data.put("storagecount", 1);
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

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker0OnClick(View view) {
        hackerModel.setHacker(0, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker1OnClick(View view) {
        hackerModel.setHacker(1, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker2OnClick(View view) {
        hackerModel.setHacker(2, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker3OnClick(View view) {
        hackerModel.setHacker(3, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker4OnClick(View view) {
        hackerModel.setHacker(4, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker5OnClick(View view) {
        hackerModel.setHacker(5, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker6OnClick(View view) {
        hackerModel.setHacker(6, null, true);
        view.setVisibility(View.GONE);
    }

    /**
     * Make hacker go away when clicked.
     *
     * @param view
     */
    public void hacker7OnClick(View view) {
        hackerModel.setHacker(7, null, true);
        view.setVisibility(View.GONE);
    }
}