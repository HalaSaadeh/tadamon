package com.example.tadamon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PopUpMessage extends DialogFragment {
    private LoggingOut listener;

    private String title, posButtonMessage, negButtonMessage;

    public PopUpMessage(String title, String posButtonMessage, String negButtonMessage) {
        this.title = title;
        this.posButtonMessage = posButtonMessage;
        this.negButtonMessage = negButtonMessage;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(title)
                .setPositiveButton(posButtonMessage, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onConfirm();
                    }
                })
                .setNegativeButton(negButtonMessage, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    public interface LoggingOut {
        void onConfirm();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (LoggingOut) context;
        } catch (ClassCastException e) {

        }
    }
}
