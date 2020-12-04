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

    private EditText location;
    private ImageButton backButton;
    private Button nextButton;
    CustomAutoCompleteView autoCompleteCities;
    ArrayAdapter<String> myAdapter; /// adapter for auto-complete
    DatabaseHandler databaseH; // to query SQLite as the user types
    String[] cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step3);

        backButton = (ImageButton) findViewById(R.id.accountCreationBackButton3);
        backButton.setOnClickListener(e->
                startActivity(new Intent(getApplicationContext(), AccountCreationStep2Activity.class)));
        nextButton = (Button) findViewById(R.id.question3NextButton);
        location = (EditText) findViewById(R.id.locationField);

        // For the auto-complete and SQLite DB
        databaseH = new DatabaseHandler(AccountCreationStep3Activity.this);
        autoCompleteCities = (CustomAutoCompleteView) findViewById(R.id.autoCompleteCities);
        autoCompleteCities.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this)); //
        cities = new String[] {"Enter the name of a city or region"}; // the cities shown in the dropdown. Adding an initial prompt
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cities); // setting the adapter
        autoCompleteCities.setAdapter(myAdapter);
        nextButton.setOnClickListener(e->{
            checkLocation();
        });
    }

    private void checkLocation (){
        if (location.getText().toString().length() == 0)
            location.setError("Please enter a location");
        if (location.getText().toString().length()>40)
            location.setError("Please stick to 40 characters for the location");
        else
            startActivity(new Intent(this,AccountCreationStep4Activity.class));
    }

    public String[] getCitiesFromDB(String searchTerm){
        databaseH = new DatabaseHandler(this);
        try {
            databaseH.createDataBase();
            databaseH.openDataBase();
        }
        catch (Exception e) {
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

