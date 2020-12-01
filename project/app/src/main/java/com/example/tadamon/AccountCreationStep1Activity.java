package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AccountCreationStep1Activity extends AppCompatActivity {

    private ImageButton backBtn;
    private TextView namefield;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step1);

        nextBtn = (Button) findViewById(R.id.question1NextButton);
        namefield = (TextView) findViewById(R.id.nameField);

        nextBtn.setOnClickListener(e->{
            if(validateName())
                startActivity(new Intent(this,AccountCreationStep2Activity.class));
            else
                namefield.setError("Please enter a name.");
        });
    }

    private boolean validateName() {
        String name = namefield.getText().toString();
        return name.length() > 0 && name.length() <= 30;
    }
    }
