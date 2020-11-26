package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity {
    private Button emailPassSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailPassSignin = findViewById(R.id.signInWithEmailAndPassButton);
        emailPassSignin.setOnClickListener(e->{
            Intent startIntent = new Intent(getApplicationContext(), LoginWithEmailAndPassActivity.class);
            startActivity(startIntent);});
    }
}