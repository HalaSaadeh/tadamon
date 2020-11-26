package com.example.tadamon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button emailpasssign;
    private Button googlesign;
    private Button facebooksign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mTextView = (TextView) findViewById(R.id.text);
        emailpasssign = findViewById(R.id.emailpasssign);
        googlesign = findViewById(R.id.googlesign);
        facebooksign = findViewById(R.id.facebooksign);

        emailpasssign.setOnClickListener(e->{
            Intent startIntent = new Intent(getApplicationContext(), SignupWithEmailAndPassActivity.class);
            startActivity(startIntent);});
       /* googlesign.setOnClickListener(e->{Intent startIntent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(startIntent);});
        facebooksign.setOnClickListener(e->{Intent startIntent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(startIntent);});*/


    }


}