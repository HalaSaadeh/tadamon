package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity extends AppCompatActivity implements PopUpMessage.LoggingOut  {

    private Button signout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mAuth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(e-> {openDialog();});
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