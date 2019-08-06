package com.ozeh.apps.superwaiter.interfaces;

import com.ozeh.apps.superwaiter.entities.Attendance;

import android.app.DialogFragment;

public interface IDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog,Attendance attendance);
    public void onDialogNegativeClick(DialogFragment dialog);
}
