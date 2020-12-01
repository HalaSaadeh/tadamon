package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AccountCreationStep3Activity extends AppCompatActivity {

    private EditText location;
    private ImageButton backButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step3);

        backButton = (ImageButton) findViewById(R.id.accountCreationBackButton3);
        backButton.setOnClickListener(e->
                startActivity(new Intent(getApplicationContext(), AccountCreationStep2Activity.class)));
        nextButton = (Button) findViewById(R.id.question3NextButton);
        location = (EditText) findViewById(R.id.locationField);
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
}

