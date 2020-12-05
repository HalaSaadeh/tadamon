package com.example.tadamon;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class PictureDialog extends AppCompatDialogFragment {

    private Button gallery, picture;
    private boolean fromCamera,fromGallery;
    private ExampleDialogListener listener;
    @NonNull


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        fromCamera =false;
        fromGallery = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.picture_dialog,null);
        builder.setView(view)
                .setTitle("Choose how you want to add a new picture")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyBools(fromCamera ,fromGallery);
                    }
                });

        gallery = view.findViewById(R.id.galleryButton);
        picture = view.findViewById(R.id.pictureButton);
        gallery.setOnClickListener(
                e->fromGallery = true);
        picture.setOnClickListener(
                e->fromCamera = true);
        return  builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement example dialog listener");
        }
    }

    public interface ExampleDialogListener {
        void applyBools(boolean cam,boolean gal);
    }
}

