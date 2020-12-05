package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AccountCreationStep4Activity extends AppCompatActivity {

    private Button skip, finish;
    private EditText bio;
    private ImageButton backButton;
    private ImageView imageView;

    public Map<String, Object> account = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get instance of the Cloud Firestore database
    FirebaseStorage storage = FirebaseStorage.getInstance(); // get instance of cloud storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step4);
        skip = (Button) findViewById(R.id.skipButton);
        skip.setOnClickListener(e->{
                addDoc();
            startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));});
        finish = (Button) findViewById(R.id.finishButton);
        finish.setOnClickListener(e->{
            addDoc();
            startActivity(new Intent(getApplicationContext(),HomeScreenActivity.class));});

       // backButton.setOnClickListener(e->
         //       startActivity(new Intent(getApplicationContext(), AccountCreationStep3Activity.class)));
        bio = (EditText) findViewById(R.id.bioField);

        imageView = findViewById(R.id.profilePicture);

        // Displaying image from Google or Facebook in case signed in with them
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean thirdParty = preferences.getBoolean("thirdParty", false);
        if (thirdParty){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Uri photoUrl = user.getPhotoUrl();
            Log.d("PhotoURl", photoUrl.toString());
            setDefaultProfilePic(photoUrl.toString(), imageView);

        }

        // Getting the data passed through intents and pushing the new user profile to the database
        String name = getIntent().getStringExtra("name");
        int age = getIntent().getIntExtra("age", 0);
        String loc = getIntent().getStringExtra("loc");
        account.put("name", name);
        account.put("age", age);
        account.put("area", loc);



    }
    private static void setDefaultProfilePic(String urlImage, ImageView imageView){
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

    private void addDoc() {

        account.put("donated_count", 0);
        account.put("volunteered_count", 0);
        account.put("bio", bio.getText().toString());

        // Get the current signed in user ID
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = preferences.getString("ID", null);

        // Upload the profile photo to cloud storage
        StorageReference storageRef = storage.getReference().child("profile_photos/"+userid+".jpg");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Save the storage url of the profile photo
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("uri", String.valueOf(uri));
                        account.put("profile_photo_url", String.valueOf(uri)); // add url of the profile photo to database hash map

                        // After getting the URL, we can push this user to the database
                        // Use the UID as a key for the document of this volunteer in the database
                        // Insert the account hash map into the database
                        db.collection("volunteers").document(userid)
                                .set(account)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("d", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("d", "Error writing document", e);
                                    }
                                });

                    }
            });}});




    }
}