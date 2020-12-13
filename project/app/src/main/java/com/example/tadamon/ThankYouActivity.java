package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {

    private TextView thankYouTitle, thankYouBody;
    private Animation thankYouAnimation;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        thankYouTitle = findViewById(R.id.thank_you_title);
        thankYouBody = findViewById(R.id.thank_you_body);
        thankYouAnimation = AnimationUtils.loadAnimation(this, R.anim.thankyou_animation);

        String type = getIntent().getExtras().getString("TYPE");
        id = getIntent().getStringExtra("id");

        if(type.equals("DONATION")) {
            thankYouTitle.setText("Donation Received!");
            thankYouBody.setText("Thank you for your donation! Because of you, this national crisis is one step closer to being overcome!");
        } else if (type.equals("VOLUNTEER")) {
            thankYouTitle.setText("Volunteering Applied To!");
            thankYouBody.setText("Thank you for volunteering! When going on site, please be careful and stay safe. We are thankful for all your hardwork!");
        }

        thankYouTitle.setAnimation(thankYouAnimation);
        thankYouBody.setAnimation(thankYouAnimation);

        new Handler().postDelayed(() -> {
            if(type.equals("DONATION")) {
                Intent goBackIntent = new Intent(this, DonationScreenActivity.class);
                goBackIntent.putExtra("id", id);

                finish();
            } else if (type.equals("VOLUNTEER")) {
                Intent goBackIntent = new Intent(this, VolunteerScreenActivity.class);
                goBackIntent.putExtra("id", id);
                startActivity(goBackIntent);
                finish();
            }
        }, 10000);

    }
}