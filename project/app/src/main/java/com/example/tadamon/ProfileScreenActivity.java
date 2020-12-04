package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

public class ProfileScreenActivity extends AppCompatActivity {

    TextView userName, userBio, userDonations, userVolunteerings;
    ImageView userProfilePicture;

    LinearLayout listOfVolunteerings;
    ImageView settingsButton;

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
        settingsButton = findViewById(R.id.settingsButton);


        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.profile:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.discover:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

    }

    public MaterialCardView createEventCard(int id, String title, int imgSrc) {
        int margin = dpToPx(8);
        int padding = dpToPx(16);

        MaterialCardView card = new MaterialCardView(this);
        card.setId(id);

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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dpToPx(200)
        ));
        imageView.setImageDrawable(getResources().getDrawable(imgSrc, getApplicationContext().getTheme()));
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
            startActivity(new Intent(this, CrisisActivity.class));
        });

        return card;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}