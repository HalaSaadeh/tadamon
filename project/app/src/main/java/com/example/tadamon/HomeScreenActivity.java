package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements PopUpMessage.LoggingOut {

    private Button signout;
    private FirebaseAuth mAuth;

    public ArrayList<MaterialCardView> list1, list2, list3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.discover);

        bottomNavigationView.setOnNavigationItemReselectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.discover:
                    return;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchScreenActivity.class));
                    overridePendingTransition(0, 0);
                    return;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileScreenActivity.class));
                    overridePendingTransition(0, 0);
                    return;
            }
        });

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(e -> {
           openDialog();
        });
    }

    public MaterialCardView getEventCard(){
        MaterialCardView card = new MaterialCardView(getApplicationContext());
        return null;
    }

    public void openDialog() {
        PopUpMessage dialog = new PopUpMessage("Are you sure you want to logout? " +
                "unless you are logging in on another device.", "I'm sure", "Cancel");
        dialog.show(getSupportFragmentManager(), "Dialog");

    }

    @Override
    public void onConfirm() {
        mAuth.signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID", null);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }
}