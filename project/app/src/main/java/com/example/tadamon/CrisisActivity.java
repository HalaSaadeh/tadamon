package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CrisisActivity extends AppCompatActivity {

    private Button goToDonateScreenButton, goToVolunteerScreenButton;

    private TextView crisisTitle, crisisDaysLeft, crisisDesc;
    private TextView donationCount, totalDonated, neededDonations;
    private ImageView donor1, donor2, donor3, donor4, donor5;
    private ImageView volunteer1, volunteer2, volunteer3, volunteer4, volunteer5;
    private TextView totalDonators, totalVolunteers;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);

        goToDonateScreenButton = findViewById(R.id.donateButtonCrisisActivity);
        goToVolunteerScreenButton = findViewById(R.id.volunteerButtonCrisisActivity);

        goToDonateScreenButton.setOnClickListener(e -> startActivity(new Intent(this, DonationScreenActivity.class)));
        goToVolunteerScreenButton.setOnClickListener(e -> startActivity(new Intent(this, VolunteerScreenActivity.class)));

        crisisTitle = findViewById(R.id.crisisTitleCrisisActivity);
        crisisDaysLeft = findViewById(R.id.daysLeftCrisisActivity);
        crisisDesc = findViewById(R.id.crisisDescriptionCrisisActivity);

        donationCount = findViewById(R.id.donorNumberLabelCrisisActivity);
        totalDonated = findViewById(R.id.donationsCurrentLabelCrisisActivity);
        neededDonations = findViewById(R.id.donationsTotalLabelCrisisActivity);

        donor1 = findViewById(R.id.donor1);
        donor2 = findViewById(R.id.donor2);
        donor3 = findViewById(R.id.donor3);
        donor4 = findViewById(R.id.donor4);
        donor5 = findViewById(R.id.donor5);

        volunteer1 = findViewById(R.id.volunteer1);
        volunteer2 = findViewById(R.id.volunteer2);
        volunteer3 = findViewById(R.id.volunteer3);
        volunteer4 = findViewById(R.id.volunteer4);
        volunteer5 = findViewById(R.id.volunteer5);

        totalDonators = findViewById(R.id.totalDonorsNumber);
        totalVolunteers = findViewById(R.id.totalVolunteersNumber);

        progressBar = findViewById(R.id.donationsProgressBar);

    }
}