package com.msn.julio_net.desafiobtg.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.msn.julio_net.desafiobtg.R;

public class ProgressDialog {

    private AlertDialog dialog;
    private Activity _activity;

    public ProgressDialog(Activity activity) {
        _activity = activity;
    }

    public void show(String text) {
        LayoutInflater inflater = _activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_progress, null);
        TextView textView = dialogView.findViewById(R.id.loading_message);
        textView.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        builder.setView(dialogView);

        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void hide() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
}
