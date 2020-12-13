package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileScreenActivity extends AppCompatActivity implements PopUpMessage.LoggingOut {

    TextView userName, userBio, userDonations, userVolunteerings;
    ImageView userProfilePicture;

    LinearLayout listOfVolunteerings, listOfDonations;
    Spinner logoutButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database
    private FirebaseAuth mAuth;

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
        listOfDonations = findViewById(R.id.userDonatedInLinearLayout);

        mAuth = FirebaseAuth.getInstance();


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Log Out");
        spinnerArray.add("Send Feedback");
        spinnerArray.add("About");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        logoutButton = findViewById(R.id.settingsButton);
        logoutButton.setAdapter(adapter);
        logoutButton.setSelection(2);

        Intent webViewIntent = new Intent(this, ExternalWebView.class);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_light);

        logoutButton.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int itemPosition, long itemID) {
                ((TextView) selectedItemView).setText(null);
                ((TextView) selectedItemView).setTypeface(typeface);
                switch (itemPosition){
                    case 0:
                        openDialog();
                        logoutButton.setSelection(2);
                        break;
                    case 1:
                        startActivity(webViewIntent);
                        logoutButton.setSelection(2);
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        populateProfile(); // When the profile screen is opened, get the current user's data from the database.

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.profile:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchScreenActivity.class));
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    finishAffinity();
                    return true;
                case R.id.discover:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    finishAffinity();
                    return true;
            }
            return false;
        });

    }

    public void openDialog() {
        PopUpMessage dialog = new PopUpMessage("Are you sure you want to logout? " +
                "unless you are logging in on another device.", "I'm sure", "Cancel");
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    public void onConfirm() {
        mAuth.signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID", null);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
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
                        String donations, volunteers;
                        if (data.get("donated_to") == null){
                            donations = "0" +" Donations";}
                        else{
                            donations = ((List<String>) data.get("donated_to")).size() +" Donations";
                            for (String donation : (List<String>) data.get("donated_to"))
                                loadEventData(donation, listOfDonations);
                            }
                        userDonations.setText(donations);
                        if (data.get("volunteered_in") == null){
                            volunteers = "0" +" Volunteering Work";}
                        else{
                            volunteers =  ((List<String>) data.get("volunteered_in")).size()+" Volunteering Work";
                            for (String donation : (List<String>) data.get("volunteered_in"))
                                loadEventData(donation, listOfVolunteerings);
                        }
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
    private void loadEventData(String vol_key, LinearLayout listOfCards) {
        DocumentReference eventRef = db.collection("crisis_events").document(vol_key);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String eventname = (String) data.get("eventname");
                        String cover_photo_url = (String) data.get("photo_url");
                        listOfCards.addView(createEventCard(vol_key, eventname, cover_photo_url));
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

    public MaterialCardView createEventCard(String id, String title, String imgSrc) {
        int margin = dpToPx(8);
        int padding = dpToPx(16);

        MaterialCardView card = new MaterialCardView(this);
        //card.setId(id);
        card.setTag(id);

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
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(200)
        ));

        //imageView.setImageDrawable(getResources().getDrawable(imgSrc, getApplicationContext().getTheme()));
        setPic(imgSrc, imageView);
        setPic(imgSrc, imageView);
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
            Intent intent = new Intent(this, CrisisActivity.class);
            intent.putExtra("id", card.getTag().toString());
            startActivity(intent);
        });

        return card;
    }
}