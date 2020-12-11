package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class SearchResultsActivity extends AppCompatActivity {

    private TextView criteriaLabel;
    private String searchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

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

        criteriaLabel = findViewById(R.id.criteria);
        searchCriteria = intent.getStringExtra("criteria");
        criteriaLabel.setText(criteriaLabel.getText() + searchCriteria);

    }
}