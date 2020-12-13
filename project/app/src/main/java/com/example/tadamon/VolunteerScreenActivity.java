package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VolunteerScreenActivity extends AppCompatActivity  implements OnMapReadyCallback {

    private ImageView crisisImage;
    private TextView crisisTitle, crisisDaysLeft, crisisDesc, crisisHowToVolunteer;

    private ImageView repPicture;
    private TextView repName, repBio;

    private Button volunteerButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

    String userid, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_screen);

        // Get the current signed in user ID
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("ID", null);


        crisisImage = findViewById(R.id.bgImageDonation);

        crisisTitle = findViewById(R.id.crisisTitleVolunteeringActivity);
        crisisDaysLeft = findViewById(R.id.daysLeftVolunteeringActivity);
        crisisDesc = findViewById(R.id.crisisDescriptionVolunteeringActivity);
        crisisHowToVolunteer = findViewById(R.id.howToHelpVolunteeringActivity);

        repPicture = findViewById(R.id.repPicture);
        repName = findViewById(R.id.repName);
        repBio = findViewById(R.id.repDescription);

        volunteerButton = findViewById(R.id.volunteerButtonVolunteeringActivity);
        volunteerButton.setOnClickListener(e -> {
            volunteerForEvent();
            Intent thankYouIntent = new Intent(this, ThankYouActivity.class);
            thankYouIntent.putExtra("TYPE", "VOLUNTEER");
            thankYouIntent.putExtra("id", id);
            startActivity(thankYouIntent);
        });

        id = getIntent().getStringExtra("id");
        populateCrisis(id);

        // Check if volunteered
        checkIfVolunteered();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    private void volunteerForEvent(){
        DocumentReference userRef = db.collection("volunteers").document(userid);
        userRef.update("volunteered_in", FieldValue.arrayUnion(id));
        DocumentReference crisisRef = db.collection("crisis_events").document(id);
        crisisRef.update("volunteers", FieldValue.arrayUnion(userid));
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String profile_url = (String) data.get("profile_photo_url");
                        crisisRef.update("volunteerphotos", FieldValue.arrayUnion(profile_url));
                        // ADD VOL ANIMATION HERE
                    }
                }
            }});


    }
    private void checkIfVolunteered(){
        DocumentReference crisisRef = db.collection("crisis_events").document(id);
        crisisRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        List<String> volunteers = (List<String>) data.get("volunteers");
                        Log.d("hi", "hi");
                        if (volunteers.contains(userid)){
                            Log.d("already", "already voled");
                            // ADD GRAYING HERE
                        }

                    }}}});
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(this);
        DocumentReference crisisRef = db.collection("crisis_events").document(id);
        crisisRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String address = (String) data.get("location");

                        List<Address> addresses;
                        try {
                            addresses = geocoder.getFromLocationName(address, 1);
                            if(addresses.size() > 0) {
                                double latitude= addresses.get(0).getLatitude();
                                double longitude= addresses.get(0).getLongitude();
                                LatLng coords = new LatLng(latitude, longitude);
                                googleMap.addMarker(new MarkerOptions()
                                        .position(coords)
                                        .title("Marker"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }}}});


    }
    }


