package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VolunteerScreenActivity extends AppCompatActivity {

    private ImageView crisisImage, backButton;
    private TextView crisisTitle, crisisDaysLeft, crisisDesc, crisisHowToVolunteer;

    private ImageView repPicture;
    private TextView repName, repBio;

    private Button volunteerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_screen);

        crisisImage = findViewById(R.id.bgImageDonation);
        backButton = findViewById(R.id.backButtonDonation);

        crisisTitle = findViewById(R.id.crisisTitleVolunteeringActivity);
        crisisDaysLeft = findViewById(R.id.daysLeftVolunteeringActivity);
        crisisDesc = findViewById(R.id.crisisDescriptionVolunteeringActivity);
        crisisHowToVolunteer = findViewById(R.id.howToHelpVolunteeringActivity);

        repPicture = findViewById(R.id.repPicture);
        repName = findViewById(R.id.repName);
        repBio = findViewById(R.id.repDescription);

        volunteerButton = findViewById(R.id.volunteerButtonVolunteeringActivity);

    }
}