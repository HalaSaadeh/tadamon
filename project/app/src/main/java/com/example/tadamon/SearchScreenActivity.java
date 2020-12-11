package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchScreenActivity extends AppCompatActivity {

    private TextView searchTextView;
    private ImageButton catHealth, catFinancial, catWildfires, catNatural, catTech, catSocial, catWinter;
    private ImageView searchButton;

    private ChipGroup tagsGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        catHealth = findViewById(R.id.cat_health);
        catFinancial = findViewById(R.id.cat_financial);
        catWildfires = findViewById(R.id.cat_wildfires);
        catNatural = findViewById(R.id.cat_natural);
        catTech = findViewById(R.id.cat_technological);
        catSocial = findViewById(R.id.cat_social);
        catWinter = findViewById(R.id.cat_winter);

        searchTextView = findViewById(R.id.searchTextView);
        searchButton = findViewById(R.id.searchButton);

        tagsGroup = findViewById(R.id.tagsGroup);

        bottomNavigationView.setSelectedItemId(R.id.search);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.search:
                    return true;
                case R.id.discover:
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileScreenActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });

        tagsGroup.addView(createChip("tag1", "Search Criteria 1"));
        tagsGroup.addView(createChip("tag2", "Search Criteria 2"));
        tagsGroup.addView(createChip("tag3", "Search Criteria 3"));
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public Chip createChip(String tag, String msg) {
        Chip chip = new Chip(this);
        chip.setTag(tag);
        chip.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Entry);

        chip.setChipDrawable(chipDrawable);
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        chip.setChipCornerRadius(dpToPx(8));
        chip.setCheckable(false);
        chip.setElevation(dpToPx(5));
        chip.setText(msg);
        chip.setTextAppearance(R.style.ChipAppearance);
        chip.setCheckedIconVisible(false);
        chip.setCloseIcon(getDrawable(R.drawable.ic_delete));
        chip.setCloseIconEnabled(true);
        chip.setCloseIconSize(dpToPx(15));
        chip.setCloseIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.redPrime)));
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(e -> {
            Log.d("TAG", "This search has been deleted");
        });

        chip.setOnClickListener(e -> {
            Log.d("TAG", "This search has been opened");
        });

        return chip;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}