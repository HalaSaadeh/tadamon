package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView logo;
    View separator;

    Animation lineAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logoLabel);
        separator = findViewById(R.id.separator);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userid = preferences.getString("ID", null); // get what is currently stored in SharedPreferences

        //Animation

        lineAnimation = AnimationUtils.loadAnimation(this, R.anim.line_animation);
        separator.setAnimation(lineAnimation);

        new Handler().postDelayed(() -> {
            Intent startIntent;
            if (userid != null) { // If there is a signed-in user
                startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            } else { // If no one is signed-in
                startIntent = new Intent(getApplicationContext(), SignInActivity.class);
            }
            startActivity(startIntent);
            finish();
        }, 2000);

    }
}