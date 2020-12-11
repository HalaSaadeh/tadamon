package com.example.tadamon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.tadamon.R;


public class PictureDialog extends AppCompatDialogFragment {

    private boolean fromCamera, fromGallery;
    private ExampleDialogListener listener;

    @NonNull


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fromCamera = false;
        fromGallery = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.picture_dialog, null);
        builder.setView(view)
                .setTitle("Choose how you want to add a new picture")
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                })
                .setNeutralButton("From Camera", (dialog, which) -> {
                    fromCamera = true;
                    listener.applyBools(fromCamera, fromGallery);

                })
                .setPositiveButton("From Gallery", (dialogInterface, i) -> {
                    fromGallery = true;
                    listener.applyBools(fromCamera, fromGallery);
                    Toast.makeText(getContext(), "Pic Dialog from cam: " + fromCamera + " fromgal: " + fromGallery, Toast.LENGTH_SHORT).show();
                });
        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement example dialog listener");
        }
    }

    public interface ExampleDialogListener {
        void applyBools(boolean cam, boolean gal);
    }
}