package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AccountCreationStep2Activity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText ageField;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step2);

        backButton = (ImageButton) findViewById(R.id.accountCreationBackButton2);

        backButton.setOnClickListener(e -> {
            startActivity(new Intent(getApplicationContext(), AccountCreationStep1Activity.class));
        });

        ageField = (EditText) findViewById(R.id.ageField);
        nextButton = (Button) findViewById(R.id.question2NextButton);
        nextButton.setOnClickListener(e -> {
            if(validateAge()){startActivity(new Intent(getApplicationContext(), AccountCreationStep3Activity.class));
            }
            else
                ageField.setError("Age is too young.");
        });
    }

    public boolean validateAge() {
        String ageString = ageField.getText().toString().trim();
        int age = 0;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException t) {
            ageField.setError("Please enter a number.");
        }
        return age > 13 && age <= 120;
    }
}