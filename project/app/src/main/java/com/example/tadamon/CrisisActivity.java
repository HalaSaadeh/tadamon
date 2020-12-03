package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class CrisisActivity extends AppCompatActivity {

    private Button goToDonateScreenButton, goToVolunteerScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);

        goToDonateScreenButton = findViewById(R.id.donateButtonCrisisActivity);
        goToVolunteerScreenButton = findViewById(R.id.volunteerButtonCrisisActivity);

        goToDonateScreenButton.setOnClickListener(e -> startActivity(new Intent(this, DonationScreenActivity.class)));
        goToVolunteerScreenButton.setOnClickListener(e -> startActivity(new Intent(this, VolunteerScreenActivity.class)));

    }
}