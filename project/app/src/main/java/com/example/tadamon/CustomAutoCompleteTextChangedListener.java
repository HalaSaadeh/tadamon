package com.example.tadamon;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

public class CustomAutoCompleteTextChangedListener implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        AccountCreationStep3Activity accStep3Activity = ((AccountCreationStep3Activity) context);

        // Query the database based on the user input
        accStep3Activity.cities = accStep3Activity.getCitiesFromDB(userInput.toString());

        // Update the adapater
        accStep3Activity.myAdapter.notifyDataSetChanged();
        accStep3Activity.myAdapter = new ArrayAdapter<String>(accStep3Activity, android.R.layout.simple_dropdown_item_1line, accStep3Activity.cities);
        accStep3Activity.autoCompleteCities.setAdapter(accStep3Activity.myAdapter);

    }

}