package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

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

        // Setting name from Google or Facebook in case signed in with them
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean thirdParty = preferences.getBoolean("thirdParty", false);
        if (thirdParty){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            namefield.setText(user.getDisplayName());
        }
        nextBtn.setOnClickListener(e->{
            if(validateName()){
                Intent intent = new Intent(this,AccountCreationStep2Activity.class);
                intent.putExtra("name", namefield.getText().toString());
                startActivity(intent);

            }
            else
                namefield.setError("Please enter a name.");
        });


    }

    private boolean validateName() {
        String name = namefield.getText().toString();
        return name.length() > 0 && name.length() <= 30;
    }
    }
