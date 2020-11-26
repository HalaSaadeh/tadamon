package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signup = findViewById(R.id.button);

        signup.setOnClickListener(e -> {
            Intent startIntent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(startIntent);
    });
    }
}