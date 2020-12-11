package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchScreenActivity extends AppCompatActivity {

    private TextView searchTextView;
    private ImageButton catHealth, catFinancial, catWildfires, catNatural, catTech, catSocial, catWinter;
    private ImageView searchButton;

    private ChipGroup tagsGroup;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // get Instance of the Cloud Firestore database
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);


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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        populateTagGroup();

        catHealth.setOnClickListener(e->{searchByCat("Health Crisis");});
        catFinancial.setOnClickListener(e->{searchByCat("Financial Crisis");});
        catNatural.setOnClickListener(e->{searchByCat("Natural Crisis");});
        catSocial.setOnClickListener(e->{searchByCat("Social Crisis");});
        catTech.setOnClickListener(e->{searchByCat("Tech Crisis");});
        catWildfires.setOnClickListener(e->{searchByCat("Wildfires");});
        catWinter.setOnClickListener(e->{searchByCat("Winter Crisis");});


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
            ((ViewManager) chip.getParent()).removeView(chip);
            DocumentReference userRef = db.collection("volunteers").document(userid);
            userRef.update("searched", FieldValue.arrayRemove(msg));
        });

        chip.setOnClickListener(e -> {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("criteria", msg);
            intent.putExtra("keywords", true);
            startActivity(intent);
        });

        return chip;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private void populateTagGroup(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("ID", null); // get UID currently stored in SharedPreferences
        // this UID is the key of the document that references this user in the database
        DocumentReference docRef = db.collection("volunteers").document(userid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        List<String> searches = (List<String>) data.get("searched");
                        if (searches!=null){
                            for (String search : searches){
                                tagsGroup.addView(createChip(search, search));
                            }
                        }
                    }}}});
    }
    private void searchByCat(String cat){
        DocumentReference userRef = db.collection("volunteers").document(userid);
        userRef.update("searched", FieldValue.arrayUnion(cat));
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("criteria", cat);
        intent.putExtra("keywords", false);
        startActivity(intent);
    }
}