package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logoLabel);

        logo.setOnClickListener(e -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userid = preferences.getString("ID", null); // get what is currently stored in SharedPreferences
            if (userid != null){ // If there is a signed-in user
                Intent startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class); // directly open HomeScreen
                startActivity(startIntent);
            }
            else{ // If no one is signed-in
                Intent startIntent = new Intent(getApplicationContext(), SignInActivity.class); // open the SignIn page
                startActivity(startIntent);}
    });
    }
}