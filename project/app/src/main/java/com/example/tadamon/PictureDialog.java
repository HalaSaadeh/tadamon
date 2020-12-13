package com.example.tadamon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


public class PictureDialog extends DialogFragment {

    //Booleans indicating which button was pressed
    private boolean fromCamera =false, fromGallery=false;
    //Listener that will send the values of the booleans back to the original activity
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Choose how you want to add a new picture")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNeutralButton("From Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fromCamera = true;
                        listener.applyBools(fromCamera, fromGallery);
                    }})
                .setPositiveButton("From Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fromGallery = true;
                        listener.applyBools(fromCamera, fromGallery);
                    }});
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
        }
    }
    public interface ExampleDialogListener {
        void applyBools(boolean cam, boolean gal);
    }
}