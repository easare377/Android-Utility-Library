package com.emma.general_backend_library.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.emma.general_backend_library.interfaces.ICustomDialog;


public abstract class CustomDialog implements ICustomDialog {
    private final Context context;
    protected AlertDialog dialog;
    private final LayoutInflater inflater;

    public CustomDialog(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    protected abstract View getLayout();

    @Override
    public void show() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        dialog.setView(getLayout());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
