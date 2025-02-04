package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class AccountCreationStep3Activity extends AppCompatActivity {

    private Button nextButton;
    CustomAutoCompleteView autoCompleteCities;
    ArrayAdapter<String> myAdapter; /// adapter for auto-complete
    DatabaseHandler databaseH; // to query SQLite as the user types
    String[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step3);

        String name = getIntent().getStringExtra("name");
        int age = getIntent().getIntExtra("age", 0);

        nextButton = (Button) findViewById(R.id.question3NextButton);

        // For the auto-complete and SQLite DB
        databaseH = new DatabaseHandler(AccountCreationStep3Activity.this);
        autoCompleteCities = (CustomAutoCompleteView) findViewById(R.id.autoCompleteCities);
        autoCompleteCities.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this)); //
        cities = new String[]{"Enter the name of a city or region"}; // the cities shown in the dropdown. Adding an initial prompt
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cities); // setting the adapter
        autoCompleteCities.setAdapter(myAdapter);
        nextButton.setOnClickListener(e -> {
            if (checkLocation()) {
                Intent intent = new Intent(this, AccountCreationStep4Activity.class);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("loc", autoCompleteCities.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }

    private boolean checkLocation() {
        if (autoCompleteCities.getText().toString().length() == 0) {
            autoCompleteCities.setError("Please enter a location");
            return false;
        }
        return true;
    }

    public String[] getCitiesFromDB(String searchTerm) {
        databaseH = new DatabaseHandler(this);
        try {
            databaseH.createDataBase();
            databaseH.openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // add items on the array dynamically
        List<City> cities = databaseH.read(searchTerm);
        int rowCount = cities.size();
        String[] result = new String[rowCount];
        int x = 0;
        for (City record : cities) {

            result[x] = record.toString();
            x++;
        }
        return result;
    }

}

