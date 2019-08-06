package com.ozeh.apps.superwaiter.dialogs;

import com.ozeh.apps.superwaiter.R;
import com.ozeh.apps.superwaiter.contracts.AttendanceContract;
import com.ozeh.apps.superwaiter.entities.Attendance;
import com.ozeh.apps.superwaiter.interfaces.IDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DeleteAttendanceDialog extends DialogFragment{

	IDialogListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			
			mListener = (IDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final String date = getArguments().getString(AttendanceContract.SHIFT_DATE);
		
		LayoutInflater factory = LayoutInflater.from(getActivity());
	    final View v = factory.inflate(R.layout.dialog_delete, null);
	    
	    //final TextView tvDate = (TextView) v.findViewById(R.id.dialog_date);
		//tvDate.setText(date);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_delete_message)
				.setView(v)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								
								Attendance att = new Attendance(null, null
										,date, 0, 0, 0,0, 0);
								
								mListener.onDialogPositiveClick(DeleteAttendanceDialog.this,att);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener.onDialogNegativeClick(DeleteAttendanceDialog.this);
							}
						});
		
		
		return builder.create();

	}
}
