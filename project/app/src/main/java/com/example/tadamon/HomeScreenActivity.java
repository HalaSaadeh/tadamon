package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements PopUpMessage.LoggingOut {

    private Button signout;
    private FirebaseAuth mAuth;

    private HorizontalScrollView scrollView;
    private LinearLayout listOfCards;

    public ArrayList<MaterialCardView> list1, list2, list3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.discover);

        listOfCards = findViewById(R.id.upToDateList);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.discover:
                    return true;
                case R.id.search:
                    startActivity(new Intent(getApplicationContext(), SearchScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

        mAuth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(e -> {
            openDialog();
        });

        MaterialCardView card1 = createEventCard(1, "Event 1", R.drawable.bg_img);
        listOfCards.addView(card1);
        MaterialCardView card2 = createEventCard(2, "Event 2", R.drawable.bg_img);
        listOfCards.addView(card2);
        MaterialCardView card3 = createEventCard(3, "Event 3", R.drawable.bg_img);
        listOfCards.addView(card3);

        scrollView = findViewById(R.id.upToDateScollView);

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

    public void openDialog() {
        PopUpMessage dialog = new PopUpMessage("Are you sure you want to logout? " +
                "unless you are logging in on another device.", "I'm sure", "Cancel");
        dialog.show(getSupportFragmentManager(), "Dialog");

    }

    @Override
    public void onConfirm() {
        mAuth.signOut();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID", null);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}