package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AccountCreationStep2Activity extends AppCompatActivity {

    private EditText ageField;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step2);
        String name = getIntent().getStringExtra("name");

        ageField = findViewById(R.id.ageField);
        nextButton = findViewById(R.id.question2NextButton);
        nextButton.setOnClickListener(e -> {
            if (validateAge()) {
                Intent intent = new Intent(getApplicationContext(), AccountCreationStep3Activity.class);
                intent.putExtra("name", name);
                intent.putExtra("age", Integer.parseInt(ageField.getText().toString().trim()));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            } else
                ageField.setError("Age is too young.");
        });
    }

    public boolean validateAge() {
        String ageString = ageField.getText().toString().trim();
        int age = 0;
        try {
            age = Integer.parseInt(ageString);
        } catch (NumberFormatException t) {
            ageField.setError("Only numbers allowed");
        }
        return age > 13 && age <= 120;
    }
}