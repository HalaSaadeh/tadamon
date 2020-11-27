package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private Button emailPassSignin;
    private TextView newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailPassSignin = findViewById(R.id.signInWithEmailAndPassButton);
        emailPassSignin.setOnClickListener(e->{
            Intent startIntent = new Intent(getApplicationContext(), LoginWithEmailAndPassActivity.class);
            startActivity(startIntent);});

        newUser = findViewById(R.id.newUserTextView);
        newUser.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
    }
}