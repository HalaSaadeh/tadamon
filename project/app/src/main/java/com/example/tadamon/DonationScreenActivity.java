package com.example.tadamon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DonationScreenActivity extends AppCompatActivity {

    private Button selectorOne, selectorTwo, selectorThree, selectorFour;
    private Button numpad1, numpad2, numpad3, numpad4, numpad5,
                    numpad6, numpad7, numpad8, numpad9, numpaddot, numpad0;

    private ImageButton numpadBackspace;
    private ImageView backButton, enterButton;

    private TextView amount;

    private double donated= 0;

    boolean selectorMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_screen);

        selectorOne = findViewById(R.id.selectedOneButton);
        selectorTwo = findViewById(R.id.selectedTwoButton);
        selectorThree = findViewById(R.id.selectedThreeButton);
        selectorFour = findViewById(R.id.selectedFourButton);

        numpad0 = findViewById(R.id.numpad0);
        numpad1 = findViewById(R.id.numpad1);
        numpad2 = findViewById(R.id.numpad2);
        numpad3 = findViewById(R.id.numpad3);
        numpad4 = findViewById(R.id.numpad4);
        numpad5 = findViewById(R.id.numpad5);
        numpad6 = findViewById(R.id.numpad6);
        numpad7 = findViewById(R.id.numpad7);
        numpad8 = findViewById(R.id.numpad8);
        numpad9 = findViewById(R.id.numpad9);
        numpaddot = findViewById(R.id.numpaddot);
        numpadBackspace = findViewById(R.id.numpadBackspace);

        backButton = findViewById(R.id.backButtonDonation);
        enterButton = findViewById(R.id.enterButtonDonation);

        amount = findViewById(R.id.amountLabel);
        amount.setText("");

        numpad0.setOnClickListener(this::handleNumpadClick);
        numpad1.setOnClickListener(this::handleNumpadClick);
        numpad2.setOnClickListener(this::handleNumpadClick);
        numpad3.setOnClickListener(this::handleNumpadClick);
        numpad4.setOnClickListener(this::handleNumpadClick);
        numpad5.setOnClickListener(this::handleNumpadClick);
        numpad6.setOnClickListener(this::handleNumpadClick);
        numpad7.setOnClickListener(this::handleNumpadClick);
        numpad8.setOnClickListener(this::handleNumpadClick);
        numpad9.setOnClickListener(this::handleNumpadClick);
        numpaddot.setOnClickListener(this::handleNumpadClick);
        numpadBackspace.setOnClickListener(this::handleNumpadClick);

        selectorOne.setOnClickListener(this::handleSelectorClick);
        selectorTwo.setOnClickListener(this::handleSelectorClick);
        selectorThree.setOnClickListener(this::handleSelectorClick);
        selectorFour.setOnClickListener(this::handleSelectorClick);

        enterButton.setOnClickListener(e -> {
            donated = Double.parseDouble(amount.getText().toString());
        });

    }

    public void handleNumpadClick(View src){
        if(selectorMode) {
            selectorMode = false;
            amount.setText("");
        }

        String prevAmount = amount.getText().toString();
        switch(src.getId()){
            case R.id.numpad0:
                if(!prevAmount.equals("0"))
                    prevAmount += '0';
                break;
            case R.id.numpaddot:
                if(prevAmount.indexOf('.') == -1)
                    prevAmount += '.';
                break;
            case R.id.numpadBackspace:
                prevAmount = prevAmount.substring(0, prevAmount.length()-1);
                break;
            default:
                prevAmount += ((Button) src).getText().toString();
                break;
        }
        amount.setText(prevAmount);
    }

    public void handleSelectorClick(View src){
        selectorMode = true;
        amount.setText(((Button) src).getText());
    }
}