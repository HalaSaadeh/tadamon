package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

public class VolunteerScreenActivity extends AppCompatActivity {

    private ImageView crisisImage, backButton;
    private TextView crisisTitle, crisisDaysLeft, crisisDesc, crisisHowToVolunteer;

    private ImageView repPicture;
    private TextView repName, repBio;

    private Button volunteerButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

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
        populateCrisis(getIntent().getStringExtra("id"));
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
                        setPic(cover_photo_url, crisisImage);
                        crisisDesc.setText((String) data.get("description"));
                        crisisDaysLeft.setText(""+  data.get("enddate") + " days left");
                        crisisHowToVolunteer.setText((String) data.get("howtovolunteer"));
                        repName.setText((String) data.get("repname"));
                        repBio.setText((String) data.get("repoccupation"));
                        setPic((String) data.get("repphoto_url"), repPicture);


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