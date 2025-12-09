package com.example.food_app;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.food_app.screens.activities.Sign_In;
import com.example.food_app.screens.fragments.CartFragment;
import com.example.food_app.screens.fragments.HomeFragment;
import com.example.food_app.screens.fragments.NotificationFragment;
import com.example.food_app.screens.fragments.OrderFragment;
import com.example.food_app.screens.fragments.ProfileFragment;
import com.example.food_app.utils.NetworkChangeReceiver;
import com.example.food_app.utils.NetworkUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    FirebaseAuth auth;
    private NetworkChangeReceiver networkChangeReceiver;
    private AlertDialog noInternetDialog;
    private long backPressedTime;

    private static boolean hasShownWelcome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        networkChangeReceiver = new NetworkChangeReceiver(this::onNetworkChange);
        checkInternet();

        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, Sign_In.class);
            startActivity(intent);
            finish();
        } else {

            if (!hasShownWelcome) {
                hasShownWelcome = true;
                showWelcomeDialog();
            }
        }


        bnView = findViewById(R.id.bnView);

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }

        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Fragment selectedfragment = null;

                if (id == R.id.home) {
                    selectedfragment = new HomeFragment();
                } else if (id == R.id.cart) {
                    selectedfragment = new CartFragment();
                } else if (id == R.id.notification) {
                    selectedfragment = new NotificationFragment();
                } else if (id == R.id.order) {
                    selectedfragment = new OrderFragment();
                } else if (id == R.id.profile) {
                    selectedfragment = new ProfileFragment();
                }

                if (selectedfragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, selectedfragment)
                            .commit();
                }
                return true;
            }
        });




    }



    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        if (currentFragment instanceof HomeFragment) {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                backPressedTime = System.currentTimeMillis();
            }

        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
        }
    }



    private void checkInternet() {
        if (!NetworkUtil.isInternetAvailable(this)) {
            showInternetDialog();
        }
    }

    private void showInternetDialog() {
        if (noInternetDialog != null && noInternetDialog.isShowing()) {
            return; // Prevent multiple dialogs
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please turn on Wi-Fi or Mobile Data.");

        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); // Open Wi-Fi settings
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        noInternetDialog = builder.create();
        noInternetDialog.show();
    }

    private void dismissInternetDialog() {
        if (noInternetDialog != null && noInternetDialog.isShowing()) {
            Toast.makeText(this, "Internet is back!", Toast.LENGTH_SHORT).show();
            noInternetDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }


    public void onNetworkChange(boolean isConnected) {
        if (isConnected) {
            dismissInternetDialog();

        } else {
            showInternetDialog();
        }
    }


    public void showWelcomeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_welcome);
        dialog.show();

        new Handler().postDelayed(() -> {
            if (!isFinishing() && !isDestroyed() && dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 4000);
    }



}