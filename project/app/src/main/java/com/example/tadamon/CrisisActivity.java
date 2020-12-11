package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CrisisActivity extends AppCompatActivity {

    private Button goToDonateScreenButton, goToVolunteerScreenButton;

    private TextView crisisTitle, crisisDaysLeft, crisisDesc;
    private TextView donationCount, totalDonated, neededDonations;
    private ImageView donor1, donor2, donor3, donor4, donor5;
    private ImageView volunteer1, volunteer2, volunteer3, volunteer4, volunteer5;
    private ImageView coverPhoto;
    private TextView totalDonators, totalVolunteers;
    private ProgressBar progressBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crisis);

        goToDonateScreenButton = findViewById(R.id.donateButtonCrisisActivity);
        goToVolunteerScreenButton = findViewById(R.id.volunteerButtonCrisisActivity);

        goToDonateScreenButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, DonationScreenActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });
        goToVolunteerScreenButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, VolunteerScreenActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });

        crisisTitle = findViewById(R.id.crisisTitleCrisisActivity);
        crisisDaysLeft = findViewById(R.id.daysLeftCrisisActivity);
        crisisDesc = findViewById(R.id.crisisDescriptionCrisisActivity);
        coverPhoto = findViewById(R.id.bgImageCrisis);
        String name = getIntent().getStringExtra("id");

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

        populateCrisis(name);

    }
    private void populateCrisis(String id){
        DocumentReference eventRef = db.collection("crisis_events").document(id);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String eventname = (String) data.get("eventname");
                        crisisTitle.setText(eventname);
                        String cover_photo_url = (String) data.get("photo_url");
                        setPic(cover_photo_url, coverPhoto);
                        crisisDesc.setText((String) data.get("description"));
                        Double amountneeded = Double.valueOf(""+data.get("amountneeded"));
                        neededDonations.setText( amountneeded + "M LBP");
                        Double amountraised = Double.valueOf(""+data.get("amountraised"));
                        totalDonated.setText( amountraised + "M LBP");
                        progressBar.setProgress((int) Math.floor(amountraised/amountneeded * 100));
                        List<String> donors = (List<String>) data.get("donorphotos");
                        donationCount.setText(""+donors.size());
                        if (donors.size()>5)
                            totalDonators.setText("+"+(donors.size()-5)); // else hide the circle
                        List<String> volunteers = (List<String>) data.get("volunteerphotos");
                        if (volunteers.size()>5)
                            totalVolunteers.setText("+"+(volunteers.size()-5)); // else hide the circle

                        // Setting donor images
                        ImageView[] donorImages = {donor1, donor2, donor3, donor4, donor5};
                        for (int i=0; i<5; i++){
                            if (i>=donors.size())
                                break;
                            setPic(donors.get(i), donorImages[i]);
                        }
                        // Setting volunteer images
                        ImageView[] volunteerImages = {volunteer1, volunteer2, volunteer3, volunteer4, volunteer5};
                        for (int i=0; i<5; i++){
                            if (i>=volunteers.size())
                                break;
                            setPic(volunteers.get(i), volunteerImages[i]);
                        }
                    }
                }}});
    }
    private static void setPic(String urlImage, ImageView imageView){
        new AsyncTask<String, Integer, Drawable>(){
            @Override
            protected Drawable doInBackground(String... strings) {
                Bitmap pic = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(urlImage).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    pic = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BitmapDrawable(Resources.getSystem(), pic);
            }
            protected void onPostExecute(Drawable result) {

                //Add image to ImageView
                imageView.setImageDrawable(result);
            }
        }.execute();
    }
}