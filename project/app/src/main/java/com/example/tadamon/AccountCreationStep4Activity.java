package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
    // For camera and gallery
    private ImageButton getPicture;
    private boolean fromCam, fromGal;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1;
    Uri image_uri;

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

        // For camera or gallery
        getPicture = (ImageButton) findViewById(R.id.changePictureButton);
        getPicture.setOnClickListener(e->openChoosePicture());

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

    private void openChoosePicture() {
        PictureDialog pictureDialog = new PictureDialog();
        pictureDialog.show(getSupportFragmentManager(), "Dialog");
    }

    private void takePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                //Request permission that was previously denied
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //Show popup to req permission
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                //permission was granted
                openCamera();
            }
        } else {
            //system os < marshmallow
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Cam");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //granted
                    openCamera();
                } else {
                    //denied
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(AccountCreationStep4Activity.this, "In on activity result", Toast.LENGTH_LONG).show();
        if (resultCode == RESULT_OK) {
            //PUT PICTURECODEHERE
            //picture.setImageURI(image_uri);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK)
            image_uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
            imageView.setImageBitmap(bitmap);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public void applyBools ( boolean cam, boolean gal){
        fromCam = cam;
        fromGal = gal;

        if (fromCam) {
            takePicture();
        }
        if (fromGal) {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(gallery,"Select Picture"), PICK_IMAGE);
        }
}}