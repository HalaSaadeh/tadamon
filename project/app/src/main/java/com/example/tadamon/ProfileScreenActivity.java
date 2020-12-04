package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ProfileScreenActivity extends AppCompatActivity {

    TextView userName, userBio, userDonations, userVolunteerings;
    ImageView userProfilePicture;

    LinearLayout listOfVolunteerings;
    ImageView settingsButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        userName = findViewById(R.id.userName);
        userBio = findViewById(R.id.userBio);
        userDonations = findViewById(R.id.userDonations);
        userVolunteerings = findViewById(R.id.userVolunteering);

        userProfilePicture = findViewById(R.id.userProfilePicture);

        listOfVolunteerings = findViewById(R.id.userVolunteeredInLinearLayout);
        settingsButton = findViewById(R.id.settingsButton);

        populateProfile(); // When the profile screen is opened, get the current user's data from the database.

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.profile:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.discover:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

    }

    private void populateProfile(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = preferences.getString("ID", null); // get UID currently stored in SharedPreferences
        // this UID is the key of the document that references this user in the database
        DocumentReference docRef = db.collection("volunteers").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        // map of key value pairs where
                        // the string is the key and object can be any Firestore type
                        String name = (String) data.get("name");
                        userName.setText(name);
                        String bio = (String) data.get("bio");
                        userBio.setText(bio);
                        String donations = data.get("donated_count") +" Donations";
                        userDonations.setText(donations);
                        String volunteers = data.get("volunteered_count") +" Volunteering Work";
                        userVolunteerings.setText(volunteers);
                        String photoUrl = (String) data.get("profile_photo_url");
                        setProfilePic(photoUrl, userProfilePicture);
                    }
                } else {
                    Toast.makeText(ProfileScreenActivity.this, "An error occurred. Please try again.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public MaterialCardView createEventCard(int id, String title, int imgSrc) {
        int margin = dpToPx(8);
        int padding = dpToPx(16);

        MaterialCardView card = new MaterialCardView(this);
        card.setId(id);

        card.setLayoutParams(new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.largeButtonWidth),
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        marginLayoutParams.setMargins(margin, margin, margin, margin);
        card.setElevation(8);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dpToPx(200)
        ));
        imageView.setImageDrawable(getResources().getDrawable(imgSrc, getApplicationContext().getTheme()));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout innerLinearLayout = new LinearLayout(this);
        innerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(60)
        ));
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setPadding(padding, padding, padding, padding);


        TextView text = new TextView(this);
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        text.setText(title);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.largeTextSize));
        text.setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_light);
        text.setTypeface(typeface);

        innerLinearLayout.addView(text);
        linearLayout.addView(imageView);
        linearLayout.addView(innerLinearLayout);
        card.addView(linearLayout);

        // TODO -> NAVIGATE TO APPROPRIATE SCREEN WITH THE RIGHT INFO PULLED FROM THE DB
        card.setOnClickListener(e -> {
            startActivity(new Intent(this, CrisisActivity.class));
        });

        return card;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private static void setProfilePic(String urlImage, ImageView imageView){
        new AsyncTask<String, Integer, Drawable>(){
            @Override
            protected Drawable doInBackground(String... strings) {
                Bitmap profilePic = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(urlImage).openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    profilePic = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BitmapDrawable(Resources.getSystem(), profilePic);
            }
            protected void onPostExecute(Drawable result) {

                //Add image to ImageView
                imageView.setImageDrawable(result);
            }
        }.execute();
    }
}