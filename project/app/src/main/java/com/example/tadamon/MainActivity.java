package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enter = findViewById(R.id.button);

        enter.setOnClickListener(e -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String userid = preferences.getString("ID", null);
            if (userid != null){
                Intent startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(startIntent);
            }
            else{
                Intent startIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(startIntent);}
    });
    }
}