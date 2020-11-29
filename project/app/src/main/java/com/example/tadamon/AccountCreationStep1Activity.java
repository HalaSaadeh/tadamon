package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AccountCreationStep1Activity extends AppCompatActivity {
    ImageButton backBtn;
    EditText namefield;
    Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation_step1);

        backBtn = (ImageButton) findViewById(R.id.accountCreationBackButton1);


        //Which activity to go back to ?
        backBtn.setOnClickListener(e->
                {
                    startActivity(new Intent(
                            getApplicationContext(), SignInActivity.class));
                }
                );

        nextBtn = (Button) findViewById(R.id.question1NextButton);
        namefield = (EditText) findViewById(R.id.nameField);

        nextBtn.setOnClickListener(e->{

            if(validateName())
                startActivity(new Intent(this,AccountCreationStep2Activity.class));
        });
    }

    private boolean validateName() {
        String name = namefield.getText().toString();

        if (name.length() >=0 && name.length()<= 30) {
            System.out.println("HELLO");
            String[] words = name.split("\\s+");
            if (words.length <=5)
                return true;
        }
        return false;
    }
}