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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.text.DecimalFormat;
import java.util.Map;

public class DonationScreenActivity extends AppCompatActivity {

    private Button selectorOne, selectorTwo, selectorThree, selectorFour;
    private Button numpad1, numpad2, numpad3, numpad4, numpad5,
                    numpad6, numpad7, numpad8, numpad9, numpadtriplezeroes, numpad0;

    private ImageButton numpadBackspace;
    private ImageView enterButton, bgImage;

    private TextView amount;

    private long donated = 0l;

    boolean selectorMode = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_screen);

        bgImage = (ImageView) findViewById(R.id.bgImageDonation);
        String id = getIntent().getStringExtra("id");
        Log.d("id", id);

        selectorOne = findViewById(R.id.selectedOneButton);
        selectorTwo = findViewById(R.id.selectedTwoButton);
        selectorThree = findViewById(R.id.selectedThreeButton);
        selectorFour = findViewById(R.id.selectedFourButton);

        numpad0 = findViewById(R.id.numpad0);
        numpad1 = findViewById(R.id.numpad1);
        numpad2 = findViewById(R.id.numpad2);
        numpad3 = findViewById(R.id.numpad3);
        numpad4 = findViewById(R.id.numpad4);
        numpad5 = findViewById(R.id.numpad5);
        numpad6 = findViewById(R.id.numpad6);
        numpad7 = findViewById(R.id.numpad7);
        numpad8 = findViewById(R.id.numpad8);
        numpad9 = findViewById(R.id.numpad9);
        numpadtriplezeroes = findViewById(R.id.numpadtriplezeroes);
        numpadBackspace = findViewById(R.id.numpadBackspace);

        enterButton = findViewById(R.id.enterButtonDonation);

        amount = findViewById(R.id.amountLabel);
        amount.setText("");

        numpad0.setOnClickListener(this::handleNumpadClick);
        numpad1.setOnClickListener(this::handleNumpadClick);
        numpad2.setOnClickListener(this::handleNumpadClick);
        numpad3.setOnClickListener(this::handleNumpadClick);
        numpad4.setOnClickListener(this::handleNumpadClick);
        numpad5.setOnClickListener(this::handleNumpadClick);
        numpad6.setOnClickListener(this::handleNumpadClick);
        numpad7.setOnClickListener(this::handleNumpadClick);
        numpad8.setOnClickListener(this::handleNumpadClick);
        numpad9.setOnClickListener(this::handleNumpadClick);
        numpadtriplezeroes.setOnClickListener(this::handleNumpadClick);
        numpadBackspace.setOnClickListener(this::handleNumpadClick);

        selectorOne.setOnClickListener(this::handleSelectorClick);
        selectorTwo.setOnClickListener(this::handleSelectorClick);
        selectorThree.setOnClickListener(this::handleSelectorClick);
        selectorFour.setOnClickListener(this::handleSelectorClick);

        enterButton.setOnClickListener(e -> {
            String amountStr = amount.getText().toString();
            amountStr = amountStr.replaceAll(",", "");
            donated = Long.parseLong(amountStr);
            Log.d("TAG", "" + donated);
        });

        DocumentReference eventRef = db.collection("crisis_events").document(id);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String cover_photo_url = (String) data.get("photo_url");
                        setPic(cover_photo_url, bgImage);}}}});


    }

    public void handleNumpadClick(View src){
        if(selectorMode) {
            selectorMode = false;
            if(src.getId() != R.id.numpadBackspace)
                amount.setText("");
        }

        String prevAmount = amount.getText().toString();

        prevAmount = prevAmount.replaceAll(",", "");
        switch(src.getId()){
            case R.id.numpad0:
                if(!prevAmount.equals("0"))
                    prevAmount += '0';
                break;
            case R.id.numpadtriplezeroes:
                if(!prevAmount.equals("0"))
                    prevAmount += "000";
                break;
            case R.id.numpadBackspace:
                prevAmount = prevAmount.substring(0, prevAmount.length()-1);
                break;
            default:
                prevAmount += ((Button) src).getText().toString();
                break;
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        if(prevAmount.length() > 0)
            prevAmount = formatter.format(Long.parseLong(prevAmount));
        else
            prevAmount = "0";
        amount.setText("" + prevAmount);
    }

    public void handleSelectorClick(View src){
        selectorMode = true;
        amount.setText(((Button) src).getText());
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